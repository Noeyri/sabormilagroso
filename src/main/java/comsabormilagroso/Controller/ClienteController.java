package comsabormilagroso.Controller;

import comsabormilagroso.Entity.Direccion;
import comsabormilagroso.Entity.Pedido;
import comsabormilagroso.Entity.Usuario;
import comsabormilagroso.Service.CarritoService;
import comsabormilagroso.Service.DireccionService;
import comsabormilagroso.Service.PedidoService;
import comsabormilagroso.Service.ProductoService;
import comsabormilagroso.Service.UsuarioService;
import comsabormilagroso.dto.ItemPedidoDTO;
import comsabormilagroso.dto.PedidoDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final PedidoService pedidoService;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;
    private final DireccionService direccionService;
    private final CarritoService carritoService;

    public ClienteController(PedidoService pedidoService, ProductoService productoService,
                             UsuarioService usuarioService, DireccionService direccionService,
                             CarritoService carritoService) {
        this.pedidoService = pedidoService;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        this.direccionService = direccionService;
        this.carritoService = carritoService;
    }

    private Usuario obtenerUsuario(Authentication auth) {
        if (auth == null) return null;
        return usuarioService.obtenerPorEmail(auth.getName()).orElse(null);
    }

    @ModelAttribute("usuarioActual")
    public Object usuarioActual(Authentication auth) {
        Usuario u = obtenerUsuario(auth);
        return u == null ? null : usuarioService.obtenerComoCliente(u);
    }

    @GetMapping
    public String dashboard(Model model, Authentication auth) {
        Usuario u = obtenerUsuario(auth);
        if (u == null) return "redirect:/login";
        model.addAttribute("pedidos", pedidoService.obtenerPedidosCliente(u.getId()));
        model.addAttribute("favoritos", productoService.obtenerDestacados());
        model.addAttribute("direcciones", direccionService.obtenerDeUsuario(u.getId()));
        return "cliente/dashboard";
    }

    @GetMapping("/pedidos")
    public String pedidos(Model model, Authentication auth) {
        Usuario u = obtenerUsuario(auth);
        if (u == null) return "redirect:/login";
        model.addAttribute("pedidos", pedidoService.obtenerPedidosCliente(u.getId()));
        return "cliente/pedidos";
    }

    @GetMapping("/pedidos/{id}")
    public String detallePedido(@PathVariable Long id, Model model, Authentication auth) {
        Usuario u = obtenerUsuario(auth);
        Pedido pedido = pedidoService.obtenerEntidad(id).orElse(null);
        if (u == null || pedido == null || !u.getId().equals(pedido.getUsuario().getId())) {
            return "redirect:/cliente/pedidos";
        }
        model.addAttribute("pedido", pedidoService.obtenerPorId(id).orElseThrow());
        return "cliente/pedido-detalle";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, Authentication auth, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<ItemPedidoDTO> carrito = (List<ItemPedidoDTO>) session.getAttribute("carrito");
        model.addAttribute("items", carrito == null ? List.of() : carrito);
        Usuario u = obtenerUsuario(auth);
        if (u != null) {
            model.addAttribute("direcciones", direccionService.obtenerDeUsuario(u.getId()));
        }
        return "cliente/checkout";
    }

    @PostMapping("/confirmar-pedido")
    public String confirmarPedido(@RequestParam String metodoPago,
                                  @RequestParam String tipoEnvio,
                                  @RequestParam(required = false) String direccionEnvio,
                                  Authentication auth, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<ItemPedidoDTO> carrito = (List<ItemPedidoDTO>) session.getAttribute("carrito");
        if (carrito == null || carrito.isEmpty()) {
            return "redirect:/cliente/checkout";
        }
        Usuario u = obtenerUsuario(auth);
        var pedido = pedidoService.crearPedido(carrito, u, metodoPago, tipoEnvio, direccionEnvio);
        carrito.clear();
        return "redirect:/cliente/pedido-confirmado?id=" + pedido.getId();
    }

    @GetMapping("/pedido-confirmado")
    public String pedidoConfirmado(@RequestParam(required = false) Long id, Model model, Authentication auth) {
        PedidoDTO pedido = null;
        if (id != null) {
            Usuario u = obtenerUsuario(auth);
            Pedido entidad = pedidoService.obtenerEntidad(id).orElse(null);
            if (u != null && entidad != null && u.getId().equals(entidad.getUsuario().getId())) {
                pedido = pedidoService.obtenerPorId(id).orElse(null);
            }
        }
        if (pedido == null) {
            return "redirect:/cliente/pedidos";
        }
        model.addAttribute("pedido", pedido);
        return "cliente/pedido-confirmado";
    }

    @GetMapping("/favoritos")
    public String favoritos(Model model) {
        model.addAttribute("favoritos", productoService.obtenerDestacados());
        return "cliente/favoritos";
    }

    @GetMapping("/perfil")
    public String perfil(Model model, Authentication auth) {
        Usuario u = obtenerUsuario(auth);
        if (u == null) return "redirect:/login";
        model.addAttribute("usuario", usuarioService.obtenerComoCliente(u));
        return "cliente/perfil";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfil() {
        return "cliente/perfil-editar";
    }

    @GetMapping("/direcciones")
    public String direcciones(Model model, Authentication auth) {
        Usuario u = obtenerUsuario(auth);
        if (u == null) return "redirect:/login";
        model.addAttribute("direcciones", direccionService.obtenerDeUsuario(u.getId()));
        return "cliente/direcciones";
    }

    @PostMapping("/direcciones/guardar")
    public String guardarDireccion(@RequestParam String etiqueta, @RequestParam String direccion,
                                   @RequestParam(required = false) String distrito,
                                   @RequestParam(required = false) String referencia,
                                   @RequestParam(required = false) Double latitud,
                                   @RequestParam(required = false) Double longitud,
                                   Authentication auth) {
        Usuario u = obtenerUsuario(auth);
        if (u == null) return "redirect:/login";
        Direccion d = new Direccion();
        d.setEtiqueta(etiqueta);
        d.setDireccion(direccion);
        d.setDistrito(distrito);
        d.setReferencia(referencia);
        d.setPredeterminada(false);
        d.setLatitud(latitud);
        d.setLongitud(longitud);
        d.setUsuario(u);
        direccionService.guardar(d);
        return "redirect:/cliente/direcciones";
    }
}