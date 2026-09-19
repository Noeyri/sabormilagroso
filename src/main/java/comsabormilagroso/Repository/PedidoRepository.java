package comsabormilagroso.Repository;

import comsabormilagroso.Entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuarioIdOrderByFechaPedidoDesc(Long usuarioId);

    List<Pedido> findAllByOrderByFechaPedidoDesc();

    List<Pedido> findByEstadoNotIn(List<String> estados);

    List<Pedido> findByFechaPedidoAfter(LocalDateTime desde);

    long countByUsuarioId(Long usuarioId);

    Pedido findTopByOrderByIdDesc();

    @Query("SELECT ip.producto.nombre AS nombre, SUM(ip.cantidad) AS total, " +
            "MAX(ip.producto.imagenUrl) AS imagenUrl " +
            "FROM ItemPedido ip GROUP BY ip.producto.nombre " +
            "ORDER BY SUM(ip.cantidad) DESC")
    List<TopProductoVendido> topProductosVendidos();

    @Query("SELECT ip.producto.categoria.nombre AS categoria, SUM(ip.subtotal) AS total " +
            "FROM ItemPedido ip GROUP BY ip.producto.categoria.nombre " +
            "ORDER BY SUM(ip.subtotal) DESC")
    List<VentaPorCategoria> ventasPorCategoria();

    interface TopProductoVendido {
        String getNombre();
        Long getTotal();
        String getImagenUrl();
    }

    interface VentaPorCategoria {
        String getCategoria();
        BigDecimal getTotal();
    }
}