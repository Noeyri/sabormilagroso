package comsabormilagroso.Controller;

import comsabormilagroso.Entity.Categoria;
import comsabormilagroso.Entity.Producto;
import comsabormilagroso.Entity.Usuario;
import comsabormilagroso.Service.CategoriaService;
import comsabormilagroso.Service.PedidoService;
import comsabormilagroso.Service.ProductoService;
import comsabormilagroso.Service.UsuarioService;
import comsabormilagroso.dto.PedidoDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;

    public AdminController(ProductoService productoService, CategoriaService categoriaService,
                           PedidoService pedidoService, UsuarioService usuarioService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
    }

    @ModelAttribute("usuarioActual")
    public Object usuarioActual(Authentication auth) {
        if (auth == null) return null;
        Usuario u = usuarioService.obtenerPorEmail(auth.getName()).orElse(null);
        return u == null ? null : usuarioService.obtenerComoAdmin(u);
    }

    @GetMapping
    public String dashboard(Model model) {
        List<PedidoDTO> pedidos = pedidoService.obtenerTodos();
        double ventasTotales = pedidoService.obtenerVentasTotales();

        model.addAttribute("pedidos", pedidos);
        model.addAttribute("productos", productoService.obtenerTodos());
        model.addAttribute("usuarios", usuarioService.obtenerUsuariosSistema());
        model.addAttribute("ventasTotales", ventasTotales);
        model.addAttribute("pedidosPendientes", pedidoService.obtenerPedidosPendientes());
        model.addAttribute("ventasUltimos7", pedidoService.ventasUltimos7Dias());
        model.addAttribute("etiquetasVentas", pedidoService.etiquetasUltimos7Dias());
        model.addAttribute("topProductos", pedidoService.topProductosVendidos(5));
        model.addAttribute("ticketPromedio", pedidos.isEmpty() ? 0 : ventasTotales / pedidos.size());

        Map<String, Long> pedidosPorEstado = pedidoService.contarPedidosPorEstado();
        model.addAttribute("estadosLabels", new ArrayList<>(pedidosPorEstado.keySet()));
        model.addAttribute("estadosValores", new ArrayList<>(pedidosPorEstado.values()));

        Map<String, BigDecimal> ventasCategoria = pedidoService.ventasPorCategoria();
        model.addAttribute("categoriasLabels", new ArrayList<>(ventasCategoria.keySet()));
        model.addAttribute("categoriasValores", new ArrayList<>(ventasCategoria.values()));

        return "admin/dashboard";
    }

    @GetMapping("/pedidos")
    public String pedidos(@RequestParam(required = false) String estado,
                          @RequestParam(required = false) String buscar,
                          Model model) {
        List<PedidoDTO> pedidos = pedidoService.obtenerTodos();

        if (estado != null && !estado.isBlank() && !"TODOS".equalsIgnoreCase(estado)) {
            pedidos = pedidos.stream().filter(p -> p.getEstado().equalsIgnoreCase(estado)).toList();
        }
        if (buscar != null && !buscar.isBlank()) {
            String termino = buscar.trim().toLowerCase();
            pedidos = pedidos.stream()
                    .filter(p -> p.getCodigo() != null && p.getCodigo().toLowerCase().contains(termino))
                    .toList();
        }

        model.addAttribute("pedidos", pedidos);
        model.addAttribute("filtroEstado", estado == null || estado.isBlank() ? "TODOS" : estado);
        model.addAttribute("filtroBuscar", buscar == null ? "" : buscar);
        return "admin/pedidos";
    }

    @GetMapping("/pedidos/{id}")
    public String detallePedido(@PathVariable Long id, Model model) {
        model.addAttribute("pedido", pedidoService.obtenerPorId(id).orElseThrow());
        return "admin/pedido-detalle";
    }

    @PostMapping("/pedidos/{id}/estado")
    public String cambiarEstadoPedido(@PathVariable Long id, @RequestParam String estado) {
        pedidoService.obtenerEntidad(id).ifPresent(p -> {
            p.setEstado(estado);
            pedidoService.guardar(p);
        });
        return "redirect:/admin/pedidos";
    }

    @GetMapping("/productos")
    public String productos(Model model) {
        model.addAttribute("productos", productoService.obtenerTodos());
        model.addAttribute("categorias", categoriaService.obtenerTodasAdmin());
        return "admin/productos";
    }

    @GetMapping("/productos/nuevo")
    public String nuevoProducto(Model model) {
        model.addAttribute("categorias", categoriaService.obtenerTodas());
        return "admin/producto-form";
    }

    @GetMapping("/productos/{id}/editar")
    public String editarProducto(@PathVariable Long id, Model model) {
        Producto producto = productoService.obtenerEntidad(id).orElseThrow();
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.obtenerTodas());
        return "admin/producto-form";
    }

    @PostMapping("/productos/guardar")
    public String guardarProducto(@RequestParam(required = false) Long id,
                                  @RequestParam String nombre,
                                  @RequestParam Long categoriaId,
                                  @RequestParam BigDecimal precio,
                                  @RequestParam(required = false) BigDecimal precioAnterior,
                                  @RequestParam(required = false) String descripcion,
                                  @RequestParam(required = false) String descripcionLarga,
                                  @RequestParam(required = false) String imagenUrl,
                                  @RequestParam(defaultValue = "false") boolean popular,
                                  @RequestParam(defaultValue = "false") boolean recomendado,
                                  @RequestParam(required = false) Integer stock) {
        boolean esNuevo = (id == null);
        Producto producto = esNuevo ? new Producto() : productoService.obtenerEntidad(id).orElse(new Producto());
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setPrecioAnterior(precioAnterior);
        producto.setDescripcion(descripcion);
        producto.setDescripcionLarga(descripcionLarga);
        producto.setImagenUrl(imagenUrl);
        producto.setPopular(popular);
        producto.setRecomendado(recomendado);
        producto.setStock(stock == null ? 0 : stock);
        if (esNuevo) {
            producto.setDisponible(true);
        }
        Categoria categoria = categoriaService.obtenerPorId(categoriaId).orElseThrow();
        producto.setCategoria(categoria);
        productoService.guardar(producto);
        return "redirect:/admin/productos";
    }

    @PostMapping("/productos/{id}/eliminar")
    public String eliminarProducto(@PathVariable Long id) {
        productoService.eliminar(id);
        return "redirect:/admin/productos";
    }

    @PostMapping("/productos/{id}/toggle")
    public String toggleProducto(@PathVariable Long id) {
        productoService.obtenerEntidad(id).ifPresent(p -> {
            p.setDisponible(!Boolean.TRUE.equals(p.getDisponible()));
            productoService.guardar(p);
        });
        return "redirect:/admin/productos";
    }

    @GetMapping("/categorias")
    public String categorias(Model model) {
        model.addAttribute("categorias", categoriaService.obtenerTodasAdmin());
        return "admin/categorias";
    }

    @PostMapping("/categorias/guardar")
    public String guardarCategoria(@RequestParam(required = false) Long id,
                                   @RequestParam String nombre,
                                   @RequestParam(required = false) String descripcion) {
        Categoria categoria = id != null ? categoriaService.obtenerPorId(id)
                .orElseGet(Categoria::new) : new Categoria();
        boolean esNueva = (id == null);
        categoria.setNombre(nombre);
        if (descripcion != null) {
            categoria.setDescripcion(descripcion);
        }
        if (esNueva) {
            categoria.setActivo(true);
        }
        categoriaService.guardar(categoria);
        return "redirect:/admin/categorias";
    }

    @PostMapping("/categorias/{id}/eliminar")
    public String eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return "redirect:/admin/categorias";
    }

    @PostMapping("/categorias/{id}/toggle")
    public String toggleCategoria(@PathVariable Long id) {
        categoriaService.obtenerPorId(id).ifPresent(c -> {
            c.setActivo(!Boolean.TRUE.equals(c.getActivo()));
            categoriaService.guardar(c);
        });
        return "redirect:/admin/categorias";
    }

    @GetMapping("/promociones")
    public String promociones(Model model) {
        model.addAttribute("promociones", productoService.obtenerEnPromocion());
        model.addAttribute("productos", productoService.obtenerTodos());
        return "admin/promociones";
    }

    @PostMapping("/promociones/aplicar")
    public String aplicarPromocion(@RequestParam Long productoId,
                                   @RequestParam BigDecimal precioAnterior,
                                   @RequestParam(required = false) BigDecimal descuento) {
        productoService.obtenerEntidad(productoId).ifPresent(p -> {
            p.setPrecioAnterior(precioAnterior);
            if (descuento != null && descuento.compareTo(BigDecimal.ZERO) > 0
                    && descuento.compareTo(new BigDecimal(100)) < 0) {
                BigDecimal factor = BigDecimal.ONE.subtract(descuento.divide(new BigDecimal(100)));
                p.setPrecio(precioAnterior.multiply(factor));
            }
            productoService.guardar(p);
        });
        return "redirect:/admin/promociones";
    }

    @PostMapping("/promociones/{id}/finalizar")
    public String finalizarPromocion(@PathVariable Long id) {
        productoService.obtenerEntidad(id).ifPresent(p -> {
            p.setPrecioAnterior(null);
            productoService.guardar(p);
        });
        return "redirect:/admin/promociones";
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.obtenerUsuariosSistema());
        return "admin/usuarios";
    }

    @PostMapping("/usuarios/{id}/toggle")
    public String toggleUsuario(@PathVariable Long id) {
        usuarioService.obtenerPorId(id).ifPresent(u -> {
            u.setActivo(!Boolean.TRUE.equals(u.getActivo()));
            usuarioService.guardar(u);
        });
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/reportes")
    public String reportes(@RequestParam(defaultValue = "7") String rango, Model model) {
        List<PedidoDTO> pedidos = pedidoService.obtenerTodos();
        double ventasTotales = pedidoService.obtenerVentasTotales();

        List<BigDecimal> ventasSerie;
        List<String> etiquetasSerie;
        if ("30".equals(rango)) {
            ventasSerie = pedidoService.ventasUltimosNDias(30);
            etiquetasSerie = pedidoService.etiquetasUltimosNDias(30);
        } else if ("anio".equals(rango)) {
            ventasSerie = pedidoService.ventasPorMesEsteAnio();
            etiquetasSerie = pedidoService.etiquetasMeses();
        } else {
            rango = "7";
            ventasSerie = pedidoService.ventasUltimosNDias(7);
            etiquetasSerie = pedidoService.etiquetasUltimosNDias(7);
        }

        model.addAttribute("pedidos", pedidos);
        model.addAttribute("productos", productoService.obtenerTodos());
        model.addAttribute("clientesActivos", usuarioService.contarClientes());
        model.addAttribute("ventasTotales", ventasTotales);
        model.addAttribute("ticketPromedio", pedidos.isEmpty() ? 0 : ventasTotales / pedidos.size());
        model.addAttribute("ventasSerie", ventasSerie);
        model.addAttribute("etiquetasSerie", etiquetasSerie);
        model.addAttribute("rangoActual", rango);
        model.addAttribute("topProductos", pedidoService.topProductosVendidos(8));

        Map<String, Long> pedidosPorEstado = pedidoService.contarPedidosPorEstado();
        model.addAttribute("estadosLabels", new ArrayList<>(pedidosPorEstado.keySet()));
        model.addAttribute("estadosValores", new ArrayList<>(pedidosPorEstado.values()));

        Map<String, BigDecimal> ventasCategoria = pedidoService.ventasPorCategoria();
        model.addAttribute("categoriasLabels", new ArrayList<>(ventasCategoria.keySet()));
        model.addAttribute("categoriasValores", new ArrayList<>(ventasCategoria.values()));

        return "admin/reportes";
    }

    @GetMapping("/configuracion")
    public String configuracion() {
        return "admin/configuracion";
    }

    @GetMapping("/perfil")
    public String perfil() {
        return "admin/perfil";
    }
}