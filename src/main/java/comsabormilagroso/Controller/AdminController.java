package comsabormilagroso.Controller;

import comsabormilagroso.dto.ProductoDTO;
import comsabormilagroso.mock.MockDataProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/*
  Controlador del PANEL ADMINISTRADOR (Fase 3).
 
  Al igual que {@link PublicController} y {@link ClienteController}, solo
  renderiza vistas Thymeleaf con datos ficticios (mock). NO hay autenticación
  ni control de roles real todavía: se simula un único usuario "Administrador"
  logueado. Tampoco hay persistencia real de productos, categorías,
  promociones ni usuarios: eso se implementará en la Fase 4 junto con
  Spring Security y la base de datos PostgreSQL.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final MockDataProvider mockDataProvider;

    public AdminController(MockDataProvider mockDataProvider) {
        this.mockDataProvider = mockDataProvider;
    }

    /*
      Disponible en todas las vistas del panel Admin para pintar el
      sidebar/topbar con el nombre e iniciales del administrador mock.
     */
    @ModelAttribute("usuarioActual")
    public Object usuarioActual() {
        return mockDataProvider.obtenerUsuarioAdmin();
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("pedidos", mockDataProvider.obtenerPedidosAdmin());
        model.addAttribute("productos", mockDataProvider.obtenerTodos());
        model.addAttribute("usuarios", mockDataProvider.obtenerUsuariosSistema());
        model.addAttribute("ventasTotales", mockDataProvider.obtenerVentasTotales());
        model.addAttribute("pedidosPendientes", mockDataProvider.obtenerPedidosPendientes());
        return "admin/dashboard";
    }

    @GetMapping("/pedidos")
    public String pedidos(Model model) {
        model.addAttribute("pedidos", mockDataProvider.obtenerPedidosAdmin());
        return "admin/pedidos";
    }

    @GetMapping("/pedidos/{id}")
    public String detallePedido(@PathVariable Long id, Model model) {
        model.addAttribute("pedido", mockDataProvider.obtenerPedidoPorId(id)
                .orElseGet(() -> mockDataProvider.obtenerPedidosAdmin().get(0)));
        return "admin/pedido-detalle";
    }

    @GetMapping("/productos")
    public String productos(Model model) {
        model.addAttribute("productos", mockDataProvider.obtenerTodos());
        model.addAttribute("categorias", mockDataProvider.obtenerCategoriasAdmin());
        return "admin/productos";
    }

    @GetMapping("/productos/nuevo")
    public String nuevoProducto(Model model) {
        model.addAttribute("categorias", mockDataProvider.obtenerCategoriasAdmin());
        return "admin/producto-form";
    }

    @GetMapping("/productos/{id}/editar")
    public String editarProducto(@PathVariable Long id, Model model) {
        ProductoDTO producto = mockDataProvider.obtenerPorId(id)
                .orElseGet(() -> mockDataProvider.obtenerTodos().get(0));
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", mockDataProvider.obtenerCategoriasAdmin());
        return "admin/producto-form";
    }

    @GetMapping("/categorias")
    public String categorias(Model model) {
        model.addAttribute("categorias", mockDataProvider.obtenerCategoriasAdmin());
        return "admin/categorias";
    }

    @GetMapping("/promociones")
    public String promociones(Model model) {
        model.addAttribute("promociones", mockDataProvider.obtenerEnPromocion());
        model.addAttribute("productos", mockDataProvider.obtenerTodos());
        return "admin/promociones";
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", mockDataProvider.obtenerUsuariosSistema());
        return "admin/usuarios";
    }

    @GetMapping("/reportes")
    public String reportes(Model model) {
        model.addAttribute("pedidos", mockDataProvider.obtenerPedidosAdmin());
        model.addAttribute("productos", mockDataProvider.obtenerTodos());
        model.addAttribute("usuarios", mockDataProvider.obtenerUsuariosSistema());
        model.addAttribute("ventasTotales", mockDataProvider.obtenerVentasTotales());
        return "admin/reportes";
    }

    @GetMapping("/configuracion")
    public String configuracion() {
        return "admin/configuracion";
    }

    @GetMapping("/perfil")
    public String perfil() {
        return "admin/perfil";
    }
}