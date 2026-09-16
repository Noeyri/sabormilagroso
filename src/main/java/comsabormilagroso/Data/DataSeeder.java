package comsabormilagroso.Data;

import comsabormilagroso.Entity.Categoria;
import comsabormilagroso.Entity.Rol;
import comsabormilagroso.Entity.Producto;
import comsabormilagroso.Entity.Usuario;
import comsabormilagroso.Repository.CategoriaRepository;
import comsabormilagroso.Repository.ProductoRepository;
import comsabormilagroso.Repository.RolRepository;
import comsabormilagroso.Repository.UsuarioRepository;
import comsabormilagroso.dto.ProductoDTO;
import comsabormilagroso.mock.MockDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final RolRepository rolRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final MockDataProvider mockDataProvider;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(RolRepository rolRepository, CategoriaRepository categoriaRepository,
                      ProductoRepository productoRepository, UsuarioRepository usuarioRepository,
                      MockDataProvider mockDataProvider, PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
        this.mockDataProvider = mockDataProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedRoles();
        seedUsuarios();
        seedCatalogo();
        log.info("Seed de datos completado.");
    }

    private void seedRoles() {
        List<String> roles = List.of("ROLE_ADMIN", "ROLE_CLIENTE", "ROLE_MESERO");
        for (String r : roles) {
            rolRepository.findByNombre(r).orElseGet(() -> {
                Rol rol = new Rol();
                rol.setNombre(r);
                return rolRepository.save(rol);
            });
        }
    }

        private void seedUsuarios() {
        Rol adminRol = rolRepository.findByNombre("ROLE_ADMIN").orElseThrow();

        usuarioRepository.findByEmail("admin@gmail.com").orElseGet(() -> {
            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("1234"));
            admin.setTelefono("+51 999 000 111");
            admin.setActivo(true);
            admin.setRol(adminRol);
            log.info("Usuario admin creado: admin@gmail.com / 1234");
            return usuarioRepository.save(admin);
        });

        if (usuarioRepository.count() <= 1) {
            Rol clienteRol = rolRepository.findByNombre("ROLE_CLIENTE").orElseThrow();
            Usuario cliente = new Usuario();
            cliente.setNombre("Rosa Mendoza");
            cliente.setEmail("rosa.mendoza@example.com");
            cliente.setPassword(passwordEncoder.encode("cliente123"));
            cliente.setTelefono("+51 987 111 222");
            cliente.setActivo(true);
            cliente.setRol(clienteRol);
            usuarioRepository.save(cliente);
            log.info("Usuario cliente demo creado: rosa.mendoza@example.com / cliente123");
        }
    }

    private void seedCatalogo() {
        if (productoRepository.count() > 0) {
            return;
        }
        Map<String, Categoria> categorias = new HashMap<>();
        List<ProductoDTO> carta = new ArrayList<>(mockDataProvider.obtenerTodos());

        for (ProductoDTO dto : carta) {
            String nombreCategoria = dto.getCategoria();
            Categoria categoria = categorias.computeIfAbsent(nombreCategoria, n -> {
                Categoria c = categoriaRepository.findByNombre(n).orElseGet(() -> {
                    Categoria nueva = new Categoria();
                    nueva.setNombre(n);
                    nueva.setDescripcion("");
                    nueva.setActivo(true);
                    return categoriaRepository.save(nueva);
                });
                return c;
            });

            Producto p = new Producto();
            p.setNombre(dto.getNombre());
            p.setDescripcion(dto.getDescripcion());
            p.setDescripcionLarga(dto.getDescripcionLarga());
            p.setPrecio(BigDecimal.valueOf(dto.getPrecio()));
            p.setPrecioAnterior(dto.getPrecioAnterior() == null ? null : BigDecimal.valueOf(dto.getPrecioAnterior()));
            p.setDisponible(true);
            p.setPopular(dto.isPopular());
            p.setRecomendado(dto.isRecomendado());
            p.setCalificacion(dto.getCalificacion() == null ? BigDecimal.ZERO : BigDecimal.valueOf(dto.getCalificacion()));
            p.setTiempoPreparacionMin(dto.getTiempoPreparacionMin());
            p.setStock(dto.getStock() == null ? (int) (Math.abs(dto.getId()) % 80) + 20 : dto.getStock());
            p.setImagenUrl(dto.getImagenUrl());
            p.setCategoria(categoria);
            productoRepository.save(p);
        }
        log.info("Carta migrada: {} productos en {} categorías.", productoRepository.count(), categorias.size());
    }
}