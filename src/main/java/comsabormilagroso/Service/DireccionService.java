package comsabormilagroso.Service;

import comsabormilagroso.Entity.Direccion;
import comsabormilagroso.Entity.Usuario;
import comsabormilagroso.Repository.DireccionRepository;
import comsabormilagroso.dto.DireccionDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    // Crea una dirección. La primera del cliente queda como predeterminada.
    @Transactional
    public boolean crear(Usuario usuario, String etiqueta, String direccion, String distrito,
                         String referencia, Double latitud, Double longitud) {
        if (!datosValidos(etiqueta, direccion, distrito, referencia)) return false;

        Direccion d = new Direccion();
        aplicar(d, etiqueta, direccion, distrito, referencia, latitud, longitud);
        d.setUsuario(usuario);
        d.setPredeterminada(direccionRepository.countByUsuarioId(usuario.getId()) == 0);
        direccionRepository.save(d);
        return true;
    }

    @Transactional
    public boolean actualizar(Long id, Long usuarioId, String etiqueta, String direccion,
                              String distrito, String referencia, Double latitud, Double longitud) {
        Optional<Direccion> encontrada = direccionRepository.findByIdAndUsuarioId(id, usuarioId);
        if (encontrada.isEmpty() || !datosValidos(etiqueta, direccion, distrito, referencia)) return false;

        Direccion d = encontrada.get();
        aplicar(d, etiqueta, direccion, distrito, referencia, latitud, longitud);
        direccionRepository.save(d);
        return true;
    }

    // Elimina la dirección. Si era la predeterminada, la más antigua restante pasa a serlo.
    @Transactional
    public boolean eliminar(Long id, Long usuarioId) {
        Optional<Direccion> encontrada = direccionRepository.findByIdAndUsuarioId(id, usuarioId);
        if (encontrada.isEmpty()) return false;

        boolean eraPredeterminada = Boolean.TRUE.equals(encontrada.get().getPredeterminada());
        direccionRepository.delete(encontrada.get());

        if (eraPredeterminada) {
            direccionRepository.findByUsuarioIdOrderByIdAsc(usuarioId).stream().findFirst()
                    .ifPresent(primera -> {
                        primera.setPredeterminada(true);
                        direccionRepository.save(primera);
                    });
        }
        return true;
    }

    @Transactional
    public boolean marcarPredeterminada(Long id, Long usuarioId) {
        if (direccionRepository.findByIdAndUsuarioId(id, usuarioId).isEmpty()) return false;

        List<Direccion> todas = direccionRepository.findByUsuarioIdOrderByIdAsc(usuarioId);
        for (Direccion d : todas) {
            d.setPredeterminada(d.getId().equals(id));
        }
        direccionRepository.saveAll(todas);
        return true;
    }

    private boolean datosValidos(String etiqueta, String direccion, String distrito, String referencia) {
        String e = limpiar(etiqueta);
        String d = limpiar(direccion);
        return !e.isEmpty() && e.length() <= 50
                && !d.isEmpty() && d.length() <= 255
                && limpiar(distrito).length() <= 100
                && limpiar(referencia).length() <= 255;
    }

    private void aplicar(Direccion d, String etiqueta, String direccion, String distrito,
                         String referencia, Double latitud, Double longitud) {
        d.setEtiqueta(limpiar(etiqueta));
        d.setDireccion(limpiar(direccion));
        d.setDistrito(limpiar(distrito).isEmpty() ? null : limpiar(distrito));
        d.setReferencia(limpiar(referencia).isEmpty() ? null : limpiar(referencia));
        if (latitud != null && longitud != null) {
            d.setLatitud(latitud);
            d.setLongitud(longitud);
        }
    }

    private String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private DireccionDTO toDTO(Direccion d) {
        return new DireccionDTO(d.getId(), d.getEtiqueta(), d.getDireccion(),
                d.getDistrito(), d.getReferencia(), Boolean.TRUE.equals(d.getPredeterminada()),
                d.getLatitud(), d.getLongitud());
    }
}