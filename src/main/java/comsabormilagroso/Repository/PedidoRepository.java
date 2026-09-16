package comsabormilagroso.Repository;

import comsabormilagroso.Entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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

    interface TopProductoVendido {
        String getNombre();

        Long getTotal();

        String getImagenUrl();
    }
}