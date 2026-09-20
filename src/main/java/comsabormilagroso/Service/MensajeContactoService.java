package comsabormilagroso.Service;

import comsabormilagroso.Entity.MensajeContacto;
import comsabormilagroso.Repository.MensajeContactoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MensajeContactoService {

    private final MensajeContactoRepository repository;

    public MensajeContactoService(MensajeContactoRepository repository) {
        this.repository = repository;
    }

    /** Devuelve false si los datos no son válidos. */
    public boolean guardar(String nombre, String email, String telefono, String mensaje) {
        nombre = nombre == null ? "" : nombre.trim();
        email = email == null ? "" : email.trim();
        telefono = telefono == null ? "" : telefono.trim();
        mensaje = mensaje == null ? "" : mensaje.trim();

        if (nombre.isEmpty() || nombre.length() > 100) return false;
        if (email.isEmpty() || email.length() > 100 || !email.contains("@")) return false;
        if (telefono.length() > 30) return false;
        if (mensaje.isEmpty() || mensaje.length() > 2000) return false;

        MensajeContacto m = new MensajeContacto();
        m.setNombre(nombre);
        m.setEmail(email);
        m.setTelefono(telefono.isEmpty() ? null : telefono);
        m.setMensaje(mensaje);
        repository.save(m);
        return true;
    }

    public List<MensajeContacto> obtenerTodos() {
        return repository.findAllByOrderByFechaEnvioDesc();
    }

    public long contarNoLeidos() {
        return repository.countByLeidoFalse();
    }

    public void alternarLeido(Long id) {
        repository.findById(id).ifPresent(m -> {
            m.setLeido(!Boolean.TRUE.equals(m.getLeido()));
            repository.save(m);
        });
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}