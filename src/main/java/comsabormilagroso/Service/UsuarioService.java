package comsabormilagroso.Service;

import comsabormilagroso.Entity.Rol;
import comsabormilagroso.Entity.Usuario;
import comsabormilagroso.Repository.PedidoRepository;
import comsabormilagroso.Repository.RolRepository;
import comsabormilagroso.Repository.UsuarioRepository;
import comsabormilagroso.dto.UsuarioAdminDTO;
import comsabormilagroso.dto.UsuarioClienteDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PedidoRepository pedidoRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository,
                          PedidoRepository pedidoRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.pedidoRepository = pedidoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public float registrarCliente(String nombre, String email, String password, String telefono) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            return -1;
        }
        Rol rolCliente = rolRepository.findByNombre("ROLE_CLIENTE")
                .orElseThrow(() -> new IllegalStateException("Rol CLIENTE no existe en BD"));

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setTelefono(telefono);
        usuario.setActivo(true);
        usuario.setRol(rolCliente);
        usuarioRepository.save(usuario);
        return 1;
    }

    /*
       Actualiza nombre, correo, teléfono y (opcionalmente) la contraseña.
       Devuelve el mensaje de error o null si todo salió bien.
       Primero valida todo y solo después modifica la entidad.
     */
    public String actualizarPerfil(Usuario usuario, String nombre, String email, String telefono,
                                   String passwordActual, String passwordNueva, String passwordConfirmar) {
        nombre = limpiar(nombre);
        email = limpiar(email);
        telefono = limpiar(telefono);
        passwordActual = passwordActual == null ? "" : passwordActual;
        passwordNueva = passwordNueva == null ? "" : passwordNueva;
        passwordConfirmar = passwordConfirmar == null ? "" : passwordConfirmar;

        if (nombre.isEmpty() || nombre.length() > 100) {
            return "Ingresa un nombre válido (máximo 100 caracteres).";
        }
        if (email.isEmpty() || email.length() > 100 || !email.contains("@")) {
            return "Ingresa un correo electrónico válido.";
        }
        if (telefono.length() > 30) {
            return "El teléfono no puede superar los 30 caracteres.";
        }

        if (!email.equals(usuario.getEmail())) {
            Optional<Usuario> otro = usuarioRepository.findByEmail(email);
            if (otro.isPresent() && !otro.get().getId().equals(usuario.getId())) {
                return "Ese correo ya está registrado en otra cuenta.";
            }
        }

        boolean cambiaPassword = !passwordActual.isEmpty() || !passwordNueva.isEmpty()
                || !passwordConfirmar.isEmpty();
        if (cambiaPassword) {
            if (!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
                return "La contraseña actual no es correcta.";
            }
            if (passwordNueva.length() < 6) {
                return "La nueva contraseña debe tener al menos 6 caracteres.";
            }
            if (!passwordNueva.equals(passwordConfirmar)) {
                return "La nueva contraseña y su confirmación no coinciden.";
            }
        }

        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setTelefono(telefono.isEmpty() ? null : telefono);
        if (cambiaPassword) {
            usuario.setPassword(passwordEncoder.encode(passwordNueva));
        }
        usuarioRepository.save(usuario);
        return null;
    }

    public UsuarioClienteDTO obtenerComoCliente(Usuario usuario) {
        String miembro = usuario.getFechaCreacion() == null ? "" :
                usuario.getFechaCreacion().format(DateTimeFormatter.ofPattern("MMMM yyyy"));
        return new UsuarioClienteDTO(usuario.getNombre(), usuario.getEmail(),
                usuario.getTelefono(), null, miembro);
    }

    public UsuarioAdminDTO obtenerComoAdmin(Usuario usuario) {
        int pedidos = (int) pedidoRepository.countByUsuarioId(usuario.getId());
        String rol = "ROLE_ADMIN".equals(usuario.getRol().getNombre()) ? "Administrador" : "Cliente";
        String estado = Boolean.TRUE.equals(usuario.getActivo()) ? "Activo" : "Inactivo";
        String fecha = usuario.getFechaCreacion() == null ? "" :
                usuario.getFechaCreacion().format(DateTimeFormatter.ofPattern("MMMM yyyy"));
        return new UsuarioAdminDTO(usuario.getId(), usuario.getNombre(), usuario.getEmail(),
                rol, estado, fecha, pedidos);
    }

    public List<UsuarioAdminDTO> obtenerUsuariosSistema() {
        return usuarioRepository.findAll().stream().map(this::obtenerComoAdmin).toList();
    }

    public long contarClientes() {
        return usuarioRepository.countByRolNombre("ROLE_CLIENTE");
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    private String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}