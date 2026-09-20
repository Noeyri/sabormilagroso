package comsabormilagroso.Service;

import comsabormilagroso.Entity.TokenRecuperacion;
import comsabormilagroso.Entity.Usuario;
import comsabormilagroso.Repository.TokenRecuperacionRepository;
import comsabormilagroso.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Optional;

@Service
public class RecuperacionService {

    private static final int MINUTOS_VALIDEZ = 30;

    private final UsuarioRepository usuarioRepository;
    private final TokenRecuperacionRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final String baseUrl;

    public RecuperacionService(UsuarioRepository usuarioRepository,
                               TokenRecuperacionRepository tokenRepository,
                               PasswordEncoder passwordEncoder,
                               EmailService emailService,
                               @Value("${app.base-url:http://localhost:8081}") String baseUrl) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.baseUrl = baseUrl;
    }

    /** Si el correo existe y la cuenta está activa, genera el token y envía el enlace. */
    @Transactional
    public void solicitar(String email) {
        if (email == null || email.isBlank()) return;

        Optional<Usuario> encontrado = usuarioRepository.findByEmail(email.trim())
                .filter(u -> Boolean.TRUE.equals(u.getActivo()));
        if (encontrado.isEmpty()) return;

        Usuario usuario = encontrado.get();
        tokenRepository.deleteByUsuarioId(usuario.getId()); // invalida solicitudes anteriores

        String token = generarToken();
        TokenRecuperacion registro = new TokenRecuperacion();
        registro.setTokenHash(hash(token));
        registro.setExpiracion(LocalDateTime.now().plusMinutes(MINUTOS_VALIDEZ));
        registro.setUsuario(usuario);
        tokenRepository.save(registro);

        String enlace = baseUrl + "/restablecer-password?token=" + token;
        emailService.enviarRecuperacion(usuario.getEmail(), usuario.getNombre(), enlace, MINUTOS_VALIDEZ);
    }

    @Transactional(readOnly = true)
    public boolean tokenValido(String token) {
        return buscarVigente(token).isPresent();
    }

    /** Cambia la contraseña si el token es válido. El token se consume (se borra). */
    @Transactional
    public boolean restablecer(String token, String nuevaPassword) {
        Optional<TokenRecuperacion> vigente = buscarVigente(token);
        if (vigente.isEmpty()) return false;

        Usuario usuario = vigente.get().getUsuario();
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
        tokenRepository.deleteByUsuarioId(usuario.getId());
        return true;
    }

    private Optional<TokenRecuperacion> buscarVigente(String token) {
        if (token == null || token.isBlank()) return Optional.empty();
        return tokenRepository.findByTokenHash(hash(token))
                .filter(t -> t.getExpiracion().isAfter(LocalDateTime.now()));
    }

    private String generarToken() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String token) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(sha.digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }
}