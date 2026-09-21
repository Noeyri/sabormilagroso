package comsabormilagroso.Service;

import comsabormilagroso.Entity.Favorito;
import comsabormilagroso.Entity.Producto;
import comsabormilagroso.Entity.Usuario;
import comsabormilagroso.Repository.FavoritoRepository;
import comsabormilagroso.dto.ProductoDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final ProductoService productoService;

    public FavoritoService(FavoritoRepository favoritoRepository, ProductoService productoService) {
        this.favoritoRepository = favoritoRepository;
        this.productoService = productoService;
    }

    public List<ProductoDTO> obtenerProductos(Long usuarioId) {
        return favoritoRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId).stream()
                .map(f -> productoService.obtenerPorId(f.getProducto().getId()))
                .flatMap(Optional::stream)
                .toList();
    }

    /*
        Agrega el producto a favoritos si no estaba, o lo quita si ya estaba.
        Devuelve true si quedó como favorito.
        Lanza IllegalArgumentException si el producto no existe.
     */
    @Transactional
    public boolean alternar(Usuario usuario, Long productoId) {
        Optional<Favorito> existente =
                favoritoRepository.findByUsuarioIdAndProductoId(usuario.getId(), productoId);
        if (existente.isPresent()) {
            favoritoRepository.delete(existente.get());
            return false;
        }

        Producto producto = productoService.obtenerEntidad(productoId)
                .orElseThrow(() -> new IllegalArgumentException("El producto no existe"));
        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setProducto(producto);
        favoritoRepository.save(favorito);
        return true;
    }
}