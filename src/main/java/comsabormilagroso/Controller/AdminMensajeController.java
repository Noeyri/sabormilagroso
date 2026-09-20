package comsabormilagroso.Controller;

import comsabormilagroso.Entity.Usuario;
import comsabormilagroso.Service.MensajeContactoService;
import comsabormilagroso.Service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/mensajes")
public class AdminMensajeController {

    private final MensajeContactoService mensajeService;
    private final UsuarioService usuarioService;

    public AdminMensajeController(MensajeContactoService mensajeService, UsuarioService usuarioService) {
        this.mensajeService = mensajeService;
        this.usuarioService = usuarioService;
    }

    // El sidebar del admin necesita "usuarioActual"
    @ModelAttribute("usuarioActual")
    public Object usuarioActual(Authentication auth) {
        if (auth == null) return null;
        Usuario u = usuarioService.obtenerPorEmail(auth.getName()).orElse(null);
        return u == null ? null : usuarioService.obtenerComoAdmin(u);
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("mensajes", mensajeService.obtenerTodos());
        model.addAttribute("sinLeer", mensajeService.contarNoLeidos());
        return "admin/mensajes";
    }

    @PostMapping("/{id}/leido")
    public String alternarLeido(@PathVariable Long id) {
        mensajeService.alternarLeido(id);
        return "redirect:/admin/mensajes";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id) {
        mensajeService.eliminar(id);
        return "redirect:/admin/mensajes";
    }
}