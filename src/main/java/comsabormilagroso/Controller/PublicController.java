package comsabormilagroso.Controller;

import comsabormilagroso.dto.ProductoDTO;
import comsabormilagroso.mock.MockDataProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/*
  Controlador del ÁREA PÚBLICA (Fase 1).
 
  Solo se encarga de renderizar las vistas Thymeleaf del frontend con datos
  ficticios (mock). NO implementa autenticación real, ni acceso a base de
  datos, ni lógica de negocio: eso corresponde a fases posteriores.
 */
@Controller
public class PublicController {

    private final MockDataProvider mockDataProvider;

    public PublicController(MockDataProvider mockDataProvider) {
        this.mockDataProvider = mockDataProvider;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("destacados", mockDataProvider.obtenerDestacados());
        return "public/index";
    }

    @GetMapping("/menu")
    public String menu(Model model) {
        model.addAttribute("productos", mockDataProvider.obtenerTodos());
        model.addAttribute("categorias", mockDataProvider.obtenerCategorias());
        return "public/menu";
    }

    @GetMapping("/producto/{id}")
    public String detalleProducto(@PathVariable Long id, Model model) {
        ProductoDTO producto = mockDataProvider.obtenerPorId(id)
                .orElseGet(() -> mockDataProvider.obtenerTodos().get(0));
        model.addAttribute("producto", producto);
        model.addAttribute("relacionados", mockDataProvider.obtenerTodos().stream()
                .filter(p -> !p.getId().equals(producto.getId()))
                .limit(3)
                .toList());
        return "public/producto-detalle";
    }

    @GetMapping("/promociones")
    public String promociones(Model model) {
        model.addAttribute("promociones", mockDataProvider.obtenerEnPromocion());
        return "public/promociones";
    }

    @GetMapping("/nosotros")
    public String nosotros() {
        return "public/nosotros";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "public/contacto";
    }

    @GetMapping("/login")
    public String login() {
        return "public/login";
    }

    @GetMapping("/registro")
    public String registro() {
        return "public/registro";
    }

    @GetMapping("/recuperar-password")
    public String recuperarPassword() {
        return "public/recuperar-password";
    }
}