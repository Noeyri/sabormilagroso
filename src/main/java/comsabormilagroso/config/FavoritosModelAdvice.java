package comsabormilagroso.config;

import comsabormilagroso.Repository.FavoritoRepository;
import comsabormilagroso.Repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Set;

@ControllerAdvice
public class FavoritosModelAdvice {

    private final UsuarioRepository usuarioRepository;
    private final FavoritoRepository favoritoRepository;

    public FavoritosModelAdvice(UsuarioRepository usuarioRepository,
                                FavoritoRepository favoritoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.favoritoRepository = favoritoRepository;
    }

    @ModelAttribute("favoritosIds")
    public Set<Long> favoritosIds(Authentication auth) {
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return Set.of();
        }
        boolean esCliente = auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_CLIENTE".equals(a.getAuthority()));
        if (!esCliente) {
            return Set.of();
        }
        return usuarioRepository.findByEmail(auth.getName())
                .map(u -> favoritoRepository.findProductoIdsByUsuarioId(u.getId()))
                .orElse(Set.of());
    }
}