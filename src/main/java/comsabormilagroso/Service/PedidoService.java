package comsabormilagroso.Service;

import comsabormilagroso.Entity.ItemPedido;
import comsabormilagroso.Entity.Pago;
import comsabormilagroso.Entity.Pedido;
import comsabormilagroso.Entity.Producto;
import comsabormilagroso.Entity.Usuario;
import comsabormilagroso.Repository.PagoRepository;
import comsabormilagroso.Repository.PedidoRepository;
import comsabormilagroso.dto.ItemPedidoDTO;
import comsabormilagroso.dto.PedidoDTO;
import comsabormilagroso.dto.ProductoVendidoDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PedidoService {

    // Estado -> estados a los que puede pasar directamente.
    private static final Map<String, Set<String>> TRANSICIONES = Map.of(
            "PENDIENTE", Set.of("EN_PREPARACION", "CANCELADO"),
            "EN_PREPARACION", Set.of("EN_CAMINO", "CANCELADO"),
            "EN_CAMINO", Set.of("ENTREGADO"),
            "ENTREGADO", Set.of(),
            "CANCELADO", Set.of()
    );

    private final PedidoRepository pedidoRepository;
    private final PagoRepository pagoRepository;
    private final ProductoService productoService;
    private final ConfiguracionNegocioService configuracionService;

    public PedidoService(PedidoRepository pedidoRepository, PagoRepository pagoRepository,
                         ProductoService productoService, ConfiguracionNegocioService configuracionService) {
        this.pedidoRepository = pedidoRepository;
        this.pagoRepository = pagoRepository;
        this.productoService = productoService;
        this.configuracionService = configuracionService;
    }

    /** Lanza StockInsuficienteException si algún producto no tiene stock suficiente. */
    @Transactional
    public Pedido crearPedido(List<ItemPedidoDTO> items, Usuario usuario, String metodoPago,
                              String tipoEnvio, String direccionEnvio) {
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setMetodoPago(metodoPago);
        pedido.setTipoEnvio(tipoEnvio);
        pedido.setDireccionEnvio(direccionEnvio);
        pedido.setEstado("PENDIENTE");

        BigDecimal total = BigDecimal.ZERO;
        for (ItemPedidoDTO item : items) {
            Producto producto = productoService.obtenerEntidad(item.getProductoId()).orElseThrow();

            int stockActual = producto.getStock() == null ? 0 : producto.getStock();
            if (stockActual < item.getCantidad()) {
                throw new StockInsuficienteException(
                        "No hay suficiente stock de \"" + producto.getNombre() + "\" (disponible: " + stockActual + ").");
            }
            producto.setStock(stockActual - item.getCantidad());
            productoService.guardar(producto);

            BigDecimal precio = producto.getPrecio();
            BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(item.getCantidad()));
            total = total.add(subtotal);

            ItemPedido ip = new ItemPedido();
            ip.setProducto(producto);
            ip.setCantidad(item.getCantidad());
            ip.setPrecioUnitario(precio);
            ip.setSubtotal(subtotal);
            ip.setObservaciones(null);
            pedido.addItem(ip);
        }
        total = total.add(BigDecimal.valueOf(costoEnvio(tipoEnvio)));
        pedido.setTotal(total);

        Pedido guardado = pedidoRepository.save(pedido);
        guardado.setCodigo("SM-" + (1000 + guardado.getId()));
        pedidoRepository.save(guardado);

        Pago pago = new Pago();
        pago.setPedido(guardado);
        pago.setMetodoPago(metodoPago);
        pago.setMonto(total);
        pago.setEstado("PENDIENTE"); // se confirma al entregar el pedido
        pagoRepository.save(pago);

        return guardado;
    }

    /** Costo de envío configurado por el negocio. Gratis si el cliente recoge en local. */
    public double costoEnvio(String tipoEnvio) {
        if ("LLEVAR".equals(tipoEnvio)) {
            return 0.0;
        }
        BigDecimal costo = configuracionService.obtener().getCostoEnvio();
        return costo == null ? 0.0 : costo.doubleValue();
    }

    /*
       Cambia el estado del pedido validando que la transición sea válida.
       Devuelve el mensaje de error, o null si todo salió bien.
       Al pasar a ENTREGADO, el pago se marca como COMPLETADO.
       Al pasar a CANCELADO, el pago se marca como CANCELADO y se devuelve el stock.
     */
    @Transactional
    public String cambiarEstado(Long pedidoId, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if (pedido == null) {
            return "El pedido no existe.";
        }

        String estadoActual = pedido.getEstado() == null ? "PENDIENTE" : pedido.getEstado();
        Set<String> permitidos = TRANSICIONES.getOrDefault(estadoActual, Set.of());
        if (!permitidos.contains(nuevoEstado)) {
            return "No se puede pasar de \"" + formatearEstado(estadoActual) + "\" a \""
                    + formatearEstado(nuevoEstado) + "\".";
        }

        pedido.setEstado(nuevoEstado);
        pedidoRepository.save(pedido);

        if ("CANCELADO".equals(nuevoEstado)) {
            for (ItemPedido item : pedido.getItems()) {
                Producto producto = item.getProducto();
                int stockActual = producto.getStock() == null ? 0 : producto.getStock();
                producto.setStock(stockActual + item.getCantidad());
                productoService.guardar(producto);
            }
            pagoRepository.findByPedidoId(pedidoId).ifPresent(pago -> {
                pago.setEstado("CANCELADO");
                pagoRepository.save(pago);
            });
        } else if ("ENTREGADO".equals(nuevoEstado)) {
            pagoRepository.findByPedidoId(pedidoId).ifPresent(pago -> {
                pago.setEstado("COMPLETADO");
                pagoRepository.save(pago);
            });
        }
        return null;
    }

    // El cliente solo puede cancelar su propio pedido, y solo si es dueño de él.
    @Transactional
    public String cancelarComoCliente(Long pedidoId, Long usuarioId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if (pedido == null || pedido.getUsuario() == null || !pedido.getUsuario().getId().equals(usuarioId)) {
            return "El pedido no existe.";
        }
        return cambiarEstado(pedidoId, "CANCELADO");
    }

    @Transactional(readOnly = true)
    public List<PedidoDTO> obtenerPedidosCliente(Long usuarioId) {
        return pedidoRepository.findByUsuarioIdOrderByFechaPedidoDesc(usuarioId).stream()
                .map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<PedidoDTO> obtenerTodos() {
        return pedidoRepository.findAllByOrderByFechaPedidoDesc().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public Optional<PedidoDTO> obtenerPorId(Long id) {
        return pedidoRepository.findById(id).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<Pedido> obtenerEntidad(Long id) {
        return pedidoRepository.findById(id);
    }

    public Pedido guardar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public double obtenerVentasTotales() {
        return pedidoRepository.findAll().stream()
                .mapToDouble(p -> p.getTotal() == null ? 0 : p.getTotal().doubleValue()).sum();
    }

    public long obtenerPedidosPendientes() {
        return pedidoRepository.findByEstadoNotIn(List.of("ENTREGADO", "CANCELADO")).size();
    }

    @Transactional(readOnly = true)
    public List<BigDecimal> ventasUltimosNDias(int dias) {
        LocalDate inicio = LocalDate.now().minusDays(dias - 1L);
        List<Pedido> recientes = pedidoRepository.findByFechaPedidoAfter(inicio.atStartOfDay());
        Map<LocalDate, BigDecimal> porDia = new HashMap<>();
        for (Pedido p : recientes) {
            if (p.getFechaPedido() == null || p.getTotal() == null
                    || "CANCELADO".equals(p.getEstado())) {
                continue;
            }
            porDia.merge(p.getFechaPedido().toLocalDate(), p.getTotal(), BigDecimal::add);
        }
        List<BigDecimal> resultado = new ArrayList<>();
        for (int i = 0; i < dias; i++) {
            resultado.add(porDia.getOrDefault(inicio.plusDays(i), BigDecimal.ZERO));
        }
        return resultado;
    }

    @Transactional(readOnly = true)
    public List<String> etiquetasUltimosNDias(int dias) {
        DateTimeFormatter formato = dias <= 14
                ? DateTimeFormatter.ofPattern("EEE d", Locale.forLanguageTag("es-PE"))
                : DateTimeFormatter.ofPattern("d MMM", Locale.forLanguageTag("es-PE"));
        return IntStream.range(0, dias)
                .mapToObj(i -> LocalDate.now().minusDays(dias - 1L - i))
                .map(d -> d.format(formato))
                .toList();
    }

    public List<BigDecimal> ventasUltimos7Dias() {
        return ventasUltimosNDias(7);
    }

    public List<String> etiquetasUltimos7Dias() {
        return etiquetasUltimosNDias(7);
    }

    @Transactional(readOnly = true)
    public List<BigDecimal> ventasPorMesEsteAnio() {
        int anioActual = LocalDate.now().getYear();
        Map<Integer, BigDecimal> porMes = new HashMap<>();
        for (Pedido p : pedidoRepository.findAll()) {
            if (p.getFechaPedido() == null || p.getTotal() == null || "CANCELADO".equals(p.getEstado())) {
                continue;
            }
            if (p.getFechaPedido().getYear() == anioActual) {
                porMes.merge(p.getFechaPedido().getMonthValue(), p.getTotal(), BigDecimal::add);
            }
        }
        List<BigDecimal> resultado = new ArrayList<>();
        for (int mes = 1; mes <= 12; mes++) {
            resultado.add(porMes.getOrDefault(mes, BigDecimal.ZERO));
        }
        return resultado;
    }

    public List<String> etiquetasMeses() {
        return List.of("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic");
    }

    @Transactional(readOnly = true)
    public List<ProductoVendidoDTO> topProductosVendidos(int limite) {
        return pedidoRepository.topProductosVendidos().stream().limit(limite)
                .map(tp -> new ProductoVendidoDTO(tp.getNombre(), tp.getTotal(), tp.getImagenUrl()))
                .toList();
    }

    @Transactional(readOnly = true)
    public Map<String, Long> contarPedidosPorEstado() {
        return pedidoRepository.findAll().stream()
                .collect(Collectors.groupingBy(p -> formatearEstado(p.getEstado()), Collectors.counting()));
    }

    @Transactional(readOnly = true)
    public Map<String, BigDecimal> ventasPorCategoria() {
        Map<String, BigDecimal> resultado = new LinkedHashMap<>();
        for (PedidoRepository.VentaPorCategoria v : pedidoRepository.ventasPorCategoria()) {
            resultado.put(v.getCategoria(), v.getTotal() == null ? BigDecimal.ZERO : v.getTotal());
        }
        return resultado;
    }

    private String formatearEstado(String estado) {
        if (estado == null) return "Pendiente";
        return switch (estado) {
            case "PENDIENTE" -> "Pendiente";
            case "EN_PREPARACION" -> "En preparación";
            case "EN_CAMINO" -> "En camino";
            case "ENTREGADO" -> "Entregado";
            case "CANCELADO" -> "Cancelado";
            default -> estado;
        };
    }

    private String formatearMetodoPago(String metodoPago) {
        if (metodoPago == null) return "No especificado";
        return switch (metodoPago) {
            case "TARJETA" -> "Tarjeta de crédito / débito";
            case "YAPE_PLIN" -> "Yape / Plin";
            case "EFECTIVO" -> "Efectivo al recibir";
            default -> metodoPago;
        };
    }

    private PedidoDTO toDTO(Pedido p) {
        List<ItemPedidoDTO> items = p.getItems() == null ? List.of() :
                p.getItems().stream().map(ip -> new ItemPedidoDTO(
                        ip.getProducto().getId(), ip.getProducto().getNombre(),
                        ip.getProducto().getImagenUrl(), ip.getCantidad(),
                        ip.getPrecioUnitario().doubleValue())).toList();
        String fecha = p.getFechaPedido() == null ? "" :
                p.getFechaPedido().format(DateTimeFormatter.ofPattern("d MMM uuuu, h:mm a"));
        Usuario usuario = p.getUsuario();
        return new PedidoDTO(p.getId(), p.getCodigo(), fecha, formatearEstado(p.getEstado()),
                p.getDireccionEnvio(), formatearMetodoPago(p.getMetodoPago()), items,
                usuario != null ? usuario.getNombre() : "Cliente eliminado",
                usuario != null ? usuario.getEmail() : "—",
                usuario != null ? usuario.getTelefono() : "—");
    }
}