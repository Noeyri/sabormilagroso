package comsabormilagroso.Service;

import comsabormilagroso.Entity.Direccion;
import comsabormilagroso.Entity.Usuario;
import comsabormilagroso.Repository.DireccionRepository;
import comsabormilagroso.dto.DireccionDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DireccionService {

    private final DireccionRepository direccionRepository;

    public DireccionService(DireccionRepository direccionRepository) {
        this.direccionRepository = direccionRepository;
    }

    public List<DireccionDTO> obtenerDeUsuario(Long usuarioId) {
        return direccionRepository.findByUsuarioIdOrderByIdAsc(usuarioId).stream()
                .map(this::toDTO).toList();
    }

    public Direccion guardar(Direccion direccion) {
        return direccionRepository.save(direccion);
    }

    private DireccionDTO toDTO(Direccion d) {
        return new DireccionDTO(d.getId(), d.getEtiqueta(), d.getDireccion(),
                d.getDistrito(), d.getReferencia(), Boolean.TRUE.equals(d.getPredeterminada()));
    }
}