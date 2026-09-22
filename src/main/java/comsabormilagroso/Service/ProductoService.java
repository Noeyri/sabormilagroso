package comsabormilagroso.Service;

import comsabormilagroso.Entity.Categoria;
import comsabormilagroso.Entity.Producto;
import comsabormilagroso.Repository.ItemPedidoRepository;
import comsabormilagroso.Repository.ProductoRepository;
import comsabormilagroso.dto.ProductoDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public ProductoService(ProductoRepository productoRepository, ItemPedidoRepository itemPedidoRepository) {
        this.productoRepository = productoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    public List<ProductoDTO> obtenerTodos() {
        return productoRepository.findAll().stream().map(this::toDTO).toList();
    }

    public List<ProductoDTO> obtenerDestacados() {
        return productoRepository.findByRecomendadoTrue().stream().map(this::toDTO).toList();
    }

    public List<ProductoDTO> obtenerDisponibles() {
        return productoRepository.findByDisponibleTrue().stream().map(this::toDTO).toList();
    }

    public List<ProductoDTO> obtenerEnPromocion() {
        return productoRepository.findByPrecioAnteriorIsNotNullAndPrecioAnteriorGreaterThan(BigDecimal.ZERO)
                .stream().map(this::toDTO).toList();
    }

    public Optional<ProductoDTO> obtenerPorId(Long id) {
        return productoRepository.findById(id).map(this::toDTO);
    }

    public Optional<Producto> obtenerEntidad(Long id) {
        return productoRepository.findById(id);
    }

    public List<ProductoDTO> obtenerPorCategoria(Categoria categoria) {
        return productoRepository.findByCategoriaIdOrderByNombreAsc(categoria.getId())
                .stream().map(this::toDTO).toList();
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    /*
       Elimina el producto. Devuelve false (y no elimina nada) si el producto
       ya aparece en algún pedido, para no romper el historial de ventas.
       En ese caso conviene desactivarlo en vez de borrarlo.
     */
    public boolean eliminar(Long id) {
        if (itemPedidoRepository.existsByProductoId(id)) {
            return false;
        }
        productoRepository.deleteById(id);
        return true;
    }

    public boolean hayStockDisponible(Long productoId, int cantidadRequerida) {
        return productoRepository.findById(productoId)
                .map(p -> p.getStock() != null && p.getStock() >= cantidadRequerida)
                .orElse(false);
    }

    public List<Producto> obtenerEntidades() {
        return productoRepository.findAll();
    }

    private ProductoDTO toDTO(Producto p) {
        return new ProductoDTO(
                p.getId(), p.getNombre(), p.getCategoria().getNombre(), p.getDescripcion(),
                p.getDescripcionLarga(), p.getPrecio().doubleValue(),
                p.getPrecioAnterior() == null ? null : p.getPrecioAnterior().doubleValue(),
                p.getImagenUrl(), Boolean.TRUE.equals(p.getPopular()),
                Boolean.TRUE.equals(p.getRecomendado()),
                p.getCalificacion() == null ? null : p.getCalificacion().doubleValue(),
                p.getTiempoPreparacionMin(), p.getStock(), Boolean.TRUE.equals(p.getDisponible()));
    }
}