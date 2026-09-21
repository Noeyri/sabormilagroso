package comsabormilagroso.Controller;

import comsabormilagroso.Entity.Pedido;
import comsabormilagroso.Entity.Usuario;
import comsabormilagroso.Service.CarritoService;
import comsabormilagroso.Service.DireccionService;
import comsabormilagroso.Service.FavoritoService;
import comsabormilagroso.Service.PedidoService;
import comsabormilagroso.Service.UsuarioService;
import comsabormilagroso.dto.ItemPedidoDTO;
import comsabormilagroso.dto.PedidoDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;
    private final DireccionService direccionService;
    private final CarritoService carritoService;
    private final FavoritoService favoritoService;

    public ClienteController(PedidoService pedidoService, UsuarioService usuarioService,
                             DireccionService direccionService, CarritoService carritoService,
                             FavoritoService favoritoService) {
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
        this.direccionService = direccionService;
        this.carritoService = carritoService;
        this.favoritoService = favoritoService;
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
        model.addAttribute("favoritos", favoritoService.obtenerProductos(u.getId()));
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

    // ===================== FAVORITOS =====================

    @GetMapping("/favoritos")
    public String favoritos(Model model, Authentication auth) {
        Usuario u = obtenerUsuario(auth);
        if (u == null) return "redirect:/login";
        model.addAttribute("favoritos", favoritoService.obtenerProductos(u.getId()));
        return "cliente/favoritos";
    }

    /*
       Agrega o quita un favorito. Si la petición viene de fetch (header X-Requested-With)
       responde JSON; si viene de un formulario normal, redirige a la página de origen.
     */
    @PostMapping("/favoritos/{productoId}/toggle")
    public ResponseEntity<?> alternarFavorito(@PathVariable Long productoId, Authentication auth,
                                              @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
                                              @RequestHeader(value = "Referer", required = false) String referer) {
        Usuario u = obtenerUsuario(auth);
        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean esFavorito;
        try {
            esFavorito = favoritoService.alternar(u, productoId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

        if ("fetch".equals(requestedWith)) {
            return ResponseEntity.ok(Map.of("favorito", esFavorito));
        }
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create(rutaSegura(referer)))
                .build();
    }

    // Del Referer solo se usa la ruta (nunca el host), para evitar redirecciones a sitios externos.
    private String rutaSegura(String referer) {
        String porDefecto = "/cliente/favoritos";
        if (referer == null || referer.isBlank()) return porDefecto;
        try {
            URI uri = URI.create(referer);
            String ruta = uri.getRawPath();
            if (ruta == null || !ruta.startsWith("/") || ruta.startsWith("//")) return porDefecto;
            return uri.getRawQuery() == null ? ruta : ruta + "?" + uri.getRawQuery();
        } catch (IllegalArgumentException e) {
            return porDefecto;
        }
    }

    // ===================== PERFIL =====================

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

    @PostMapping("/perfil/editar")
    public String guardarPerfil(@RequestParam String nombre,
                                @RequestParam String email,
                                @RequestParam(required = false) String telefono,
                                @RequestParam(required = false) String passwordActual,
                                @RequestParam(required = false) String passwordNueva,
                                @RequestParam(required = false) String passwordConfirmar,
                                Authentication auth, Model model,
                                HttpServletRequest request, HttpServletResponse response) {
        Usuario u = obtenerUsuario(auth);
        if (u == null) return "redirect:/login";

        String emailAnterior = u.getEmail();
        String error = usuarioService.actualizarPerfil(u, nombre, email, telefono,
                passwordActual, passwordNueva, passwordConfirmar);

        if (error != null) {
            model.addAttribute("error", error);
            model.addAttribute("nombreForm", nombre);
            model.addAttribute("emailForm", email);
            model.addAttribute("telefonoForm", telefono);
            return "cliente/perfil-editar";
        }

        // El correo es el identificador del login: si cambió, se cierra la sesión
        // para que el usuario vuelva a entrar con el correo nuevo.
        if (!emailAnterior.equals(email.trim())) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            return "redirect:/login?emailActualizado";
        }
        return "redirect:/cliente/perfil?actualizado";
    }

    // ===================== DIRECCIONES =====================

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
        boolean ok = direccionService.crear(u, etiqueta, direccion, distrito, referencia, latitud, longitud);
        return "redirect:/cliente/direcciones?" + (ok ? "guardada" : "error");
    }

    @PostMapping("/direcciones/{id}/editar")
    public String editarDireccion(@PathVariable Long id,
                                  @RequestParam String etiqueta, @RequestParam String direccion,
                                  @RequestParam(required = false) String distrito,
                                  @RequestParam(required = false) String referencia,
                                  @RequestParam(required = false) Double latitud,
                                  @RequestParam(required = false) Double longitud,
                                  Authentication auth) {
        Usuario u = obtenerUsuario(auth);
        if (u == null) return "redirect:/login";
        boolean ok = direccionService.actualizar(id, u.getId(), etiqueta, direccion, distrito,
                referencia, latitud, longitud);
        return "redirect:/cliente/direcciones?" + (ok ? "actualizada" : "error");
    }

    @PostMapping("/direcciones/{id}/eliminar")
    public String eliminarDireccion(@PathVariable Long id, Authentication auth) {
        Usuario u = obtenerUsuario(auth);
        if (u == null) return "redirect:/login";
        boolean ok = direccionService.eliminar(id, u.getId());
        return "redirect:/cliente/direcciones?" + (ok ? "eliminada" : "error");
    }

    @PostMapping("/direcciones/{id}/predeterminada")
    public String direccionPredeterminada(@PathVariable Long id, Authentication auth) {
        Usuario u = obtenerUsuario(auth);
        if (u == null) return "redirect:/login";
        boolean ok = direccionService.marcarPredeterminada(id, u.getId());
        return "redirect:/cliente/direcciones?" + (ok ? "predeterminada" : "error");
    }
}