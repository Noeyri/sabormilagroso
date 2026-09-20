package comsabormilagroso.Controller;

import comsabormilagroso.Service.MensajeContactoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactoController {

    private final MensajeContactoService mensajeService;

    public ContactoController(MensajeContactoService mensajeService) {
        this.mensajeService = mensajeService;
    }

    @PostMapping("/contacto")
    public String enviar(@RequestParam String nombre,
                         @RequestParam String email,
                         @RequestParam(required = false) String telefono,
                         @RequestParam String mensaje,
                         @RequestParam(required = false) String website) {
        // Campo trampa (honeypot): un usuario real nunca lo llena, un bot sí.
        if (website != null && !website.isBlank()) {
            return "redirect:/contacto?enviado";
        }
        if (!mensajeService.guardar(nombre, email, telefono, mensaje)) {
            return "redirect:/contacto?error";
        }
        return "redirect:/contacto?enviado";
    }
}