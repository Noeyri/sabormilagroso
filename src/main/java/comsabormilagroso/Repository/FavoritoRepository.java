package comsabormilagroso.Repository;

import comsabormilagroso.Entity.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    List<Favorito> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);

    Optional<Favorito> findByUsuarioIdAndProductoId(Long usuarioId, Long productoId);

    @Query("select f.producto.id from Favorito f where f.usuario.id = :usuarioId")
    Set<Long> findProductoIdsByUsuarioId(@Param("usuarioId") Long usuarioId);
}