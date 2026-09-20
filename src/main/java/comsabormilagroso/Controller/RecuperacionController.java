package comsabormilagroso.Controller;

import comsabormilagroso.Service.RecuperacionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RecuperacionController {

    private static final int MIN_PASSWORD = 6;

    private final RecuperacionService recuperacionService;

    public RecuperacionController(RecuperacionService recuperacionService) {
        this.recuperacionService = recuperacionService;
    }

    // GET /recuperar-password lo sigue sirviendo PublicController.
    @PostMapping("/recuperar-password")
    public String solicitar(@RequestParam String email) {
        recuperacionService.solicitar(email);
        // Siempre la misma respuesta, exista o no el correo.
        return "redirect:/recuperar-password?enviado";
    }

    @GetMapping("/restablecer-password")
    public String formulario(@RequestParam(required = false) String token, Model model) {
        model.addAttribute("token", token);
        model.addAttribute("tokenInvalido", !recuperacionService.tokenValido(token));
        return "public/restablecer-password";
    }

    @PostMapping("/restablecer-password")
    public String restablecer(@RequestParam String token,
                              @RequestParam String password,
                              @RequestParam String confirmar,
                              Model model) {
        model.addAttribute("token", token);
        model.addAttribute("tokenInvalido", false);

        if (password.length() < MIN_PASSWORD) {
            model.addAttribute("error", "La contraseña debe tener al menos " + MIN_PASSWORD + " caracteres.");
            return "public/restablecer-password";
        }
        if (!password.equals(confirmar)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "public/restablecer-password";
        }
        if (!recuperacionService.restablecer(token, password)) {
            model.addAttribute("tokenInvalido", true);
            return "public/restablecer-password";
        }
        return "redirect:/login?restablecido";
    }
}