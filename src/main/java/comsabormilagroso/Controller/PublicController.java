package comsabormilagroso.Controller;

import comsabormilagroso.Service.CarritoService;
import comsabormilagroso.Service.ProductoService;
import comsabormilagroso.Service.UsuarioService;
import comsabormilagroso.dto.ItemPedidoDTO;
import comsabormilagroso.dto.ProductoDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PublicController {

    private static final String CARRITO_SESSION = "carrito";

    private final ProductoService productoService;
    private final CarritoService carritoService;
    private final UsuarioService usuarioService;

    public PublicController(ProductoService productoService, CarritoService carritoService,
                            UsuarioService usuarioService) {
        this.productoService = productoService;
        this.carritoService = carritoService;
        this.usuarioService = usuarioService;
    }

    @SuppressWarnings("unchecked")
    private List<ItemPedidoDTO> carrito(HttpSession session) {
        List<ItemPedidoDTO> carrito = (List<ItemPedidoDTO>) session.getAttribute(CARRITO_SESSION);
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute(CARRITO_SESSION, carrito);
        }
        return carrito;
    }

    @ModelAttribute("carritoCantidad")
    public int carritoCantidad(HttpSession session) {
        List<ItemPedidoDTO> carrito = (List<ItemPedidoDTO>) session.getAttribute(CARRITO_SESSION);
        return carrito == null ? 0 : carritoService.cantidadItems(carrito);
    }

    @ModelAttribute("usuarioActual")
    public Object usuarioActual(Authentication auth) {
        if (auth == null) return null;
        return usuarioService.obtenerPorEmail(auth.getName())
                .map(usuarioService::obtenerComoCliente)
                .orElse(null);
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("destacados", productoService.obtenerDestacados());
        return "public/index";
    }

    @GetMapping("/menu")
    public String menu(Model model) {
        model.addAttribute("productos", productoService.obtenerTodos());
        model.addAttribute("categorias", productoService.obtenerTodos().stream().map(ProductoDTO::getCategoria).distinct().toList());
        return "public/menu";
    }

    @GetMapping("/producto/{id}")
    public String detalleProducto(@PathVariable Long id, Model model) {
        ProductoDTO producto = productoService.obtenerPorId(id)
                .orElseGet(() -> productoService.obtenerTodos().get(0));
        model.addAttribute("producto", producto);
        model.addAttribute("relacionados", productoService.obtenerTodos().stream()
                .filter(p -> !p.getId().equals(producto.getId()))
                .limit(3)
                .toList());
        return "public/producto-detalle";
    }

    @PostMapping("/carrito/agregar")
    public String agregarCarrito(@RequestParam Long id, @RequestParam(defaultValue = "1") int cantidad,
                                 HttpSession session) {
        List<ItemPedidoDTO> carrito = carrito(session);
        productoService.obtenerPorId(id).ifPresent(p -> carritoService.agregar(carrito, p, cantidad));
        return "redirect:/menu";
    }

    @PostMapping("/carrito/actualizar")
    public String actualizarCarrito(@RequestParam Long id, @RequestParam int cantidad,
                                    HttpSession session) {
        carritoService.actualizarCantidad(carrito(session), id, cantidad);
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/eliminar")
    public String eliminarCarrito(@RequestParam Long id, HttpSession session) {
        carritoService.eliminar(carrito(session), id);
        return "redirect:/carrito";
    }

    @GetMapping("/promociones")
    public String promociones(Model model) {
        model.addAttribute("promociones", productoService.obtenerEnPromocion());
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

    @PostMapping("/registro")
    public String registrar(@RequestParam String nombre, @RequestParam String email,
                            @RequestParam String password, @RequestParam(required = false) String telefono,
                            Model model) {
        float resultado = usuarioService.registrarCliente(nombre, email, password, telefono);
        if (resultado < 0) {
            model.addAttribute("error", "Ya existe una cuenta con ese correo.");
            return "public/registro";
        }
        return "redirect:/login?registrado";
    }

    @GetMapping("/recuperar-password")
    public String recuperarPassword() {
        return "public/recuperar-password";
    }

    @GetMapping("/carrito")
    public String carrito(HttpSession session, Model model) {
        model.addAttribute("items", carrito(session));
        return "cliente/carrito";
    }
}