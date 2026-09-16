package comsabormilagroso.Repository;

import comsabormilagroso.Entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.math.BigDecimal;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByRecomendadoTrue();

    List<Producto> findByPrecioAnteriorIsNotNullAndPrecioAnteriorGreaterThan(BigDecimal precio);

    List<Producto> findByDisponibleTrue();

    List<Producto> findByCategoriaIdOrderByNombreAsc(Long categoriaId);

    long countByCategoriaId(Long categoriaId);
}