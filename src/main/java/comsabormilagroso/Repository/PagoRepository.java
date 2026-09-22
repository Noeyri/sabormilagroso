package comsabormilagroso.Repository;

import comsabormilagroso.Entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
        java.util.Optional<comsabormilagroso.Entity.Pago> findByPedidoId(Long pedidoId);
}