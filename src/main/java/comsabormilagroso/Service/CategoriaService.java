package comsabormilagroso.Service;

import comsabormilagroso.Entity.Categoria;
import comsabormilagroso.Repository.CategoriaRepository;
import comsabormilagroso.Repository.ProductoRepository;
import comsabormilagroso.dto.CategoriaAdminDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    public List<CategoriaAdminDTO> obtenerTodasAdmin() {
        return categoriaRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<Categoria> obtenerPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    public Optional<Categoria> obtenerPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    public Categoria guardar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }

    private CategoriaAdminDTO toDTO(Categoria c) {
        return new CategoriaAdminDTO(c.getId(), c.getNombre(), c.getDescripcion(),
                (int) productoRepository.countByCategoriaId(c.getId()),
                !Boolean.FALSE.equals(c.getActivo()));
    }
}