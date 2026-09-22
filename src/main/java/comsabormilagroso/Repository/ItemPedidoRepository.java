package comsabormilagroso.Repository;

import comsabormilagroso.Entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    boolean existsByProductoId(Long productoId);
}