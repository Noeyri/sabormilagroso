package comsabormilagroso.Controller;

import comsabormilagroso.dto.PedidoDTO;
import comsabormilagroso.mock.MockDataProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/*
  Controlador del ÁREA CLIENTE (Fase 2).
 
  Al igual que {@link PublicController}, solo renderiza vistas Thymeleaf con
  datos ficticios (mock). NO hay autenticación real todavía: se simula un
  único usuario "logueado" (ver {@link MockDataProvider#obtenerUsuarioActual()}).
  Tampoco hay persistencia real de pedidos, carrito ni direcciones: eso se
  implementará en la Fase 4 junto con Spring Security y la base de datos.
 */
@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final MockDataProvider mockDataProvider;

    public ClienteController(MockDataProvider mockDataProvider) {
        this.mockDataProvider = mockDataProvider;
    }

    /*
      Disponible en todas las vistas del área Cliente para pintar el
      sidebar/topbar con el nombre e iniciales del usuario mock.
     */
    @ModelAttribute("usuarioActual")
    public Object usuarioActual() {
        return mockDataProvider.obtenerUsuarioActual();
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("pedidos", mockDataProvider.obtenerPedidosCliente());
        model.addAttribute("favoritos", mockDataProvider.obtenerFavoritos());
        model.addAttribute("direcciones", mockDataProvider.obtenerDirecciones());
        return "cliente/dashboard";
    }

    @GetMapping("/pedidos")
    public String pedidos(Model model) {
        model.addAttribute("pedidos", mockDataProvider.obtenerPedidosCliente());
        return "cliente/pedidos";
    }

    @GetMapping("/pedidos/{id}")
    public String detallePedido(@PathVariable Long id, Model model) {
        PedidoDTO pedido = mockDataProvider.obtenerPedidoPorId(id)
                .orElseGet(() -> mockDataProvider.obtenerPedidosCliente().get(0));
        model.addAttribute("pedido", pedido);
        return "cliente/pedido-detalle";
    }

    @GetMapping("/carrito")
    public String carrito(Model model) {
        model.addAttribute("items", mockDataProvider.obtenerCarritoMock());
        return "cliente/carrito";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("items", mockDataProvider.obtenerCarritoMock());
        model.addAttribute("direcciones", mockDataProvider.obtenerDirecciones());
        return "cliente/checkout";
    }

    @GetMapping("/pedido-confirmado")
    public String pedidoConfirmado(Model model) {
        model.addAttribute("pedido", mockDataProvider.obtenerPedidosCliente().get(mockDataProvider.obtenerPedidosCliente().size() - 1));
        return "cliente/pedido-confirmado";
    }

    @GetMapping("/favoritos")
    public String favoritos(Model model) {
        model.addAttribute("favoritos", mockDataProvider.obtenerFavoritos());
        return "cliente/favoritos";
    }

    @GetMapping("/perfil")
    public String perfil() {
        return "cliente/perfil";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfil() {
        return "cliente/perfil-editar";
    }

    @GetMapping("/direcciones")
    public String direcciones(Model model) {
        model.addAttribute("direcciones", mockDataProvider.obtenerDirecciones());
        return "cliente/direcciones";
    }
}