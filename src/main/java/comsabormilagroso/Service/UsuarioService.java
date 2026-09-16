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
}