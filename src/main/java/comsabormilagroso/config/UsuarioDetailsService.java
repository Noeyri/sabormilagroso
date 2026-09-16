package comsabormilagroso.config;

import comsabormilagroso.Entity.Usuario;
import comsabormilagroso.Repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        GrantedAuthority authority = new SimpleGrantedAuthority(usuario.getRol().getNombre());

        return User.withUsername(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities(List.of(authority))
                .disabled(!Boolean.TRUE.equals(usuario.getActivo()))
                .build();
    }
}