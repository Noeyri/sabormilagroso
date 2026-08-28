package comsabormilagroso.mock;

import comsabormilagroso.dto.CategoriaAdminDTO;
import comsabormilagroso.dto.DireccionDTO;
import comsabormilagroso.dto.ItemPedidoDTO;
import comsabormilagroso.dto.PedidoDTO;
import comsabormilagroso.dto.ProductoDTO;
import comsabormilagroso.dto.UsuarioAdminDTO;
import comsabormilagroso.dto.UsuarioClienteDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
  Proveedor de datos MOCK/ficticios para el área pública (Fase 1).
 
  Esta clase reemplaza temporalmente al futuro ProductoService + ProductoRepository
  (Fase 4). No realiza ninguna consulta a PostgreSQL ni contiene lógica de negocio real.
  Su único propósito es alimentar las vistas Thymeleaf con datos de ejemplo para
  poder maquetar y navegar el prototipo del área pública.
 */
@Component
public class MockDataProvider {

    private final List<ProductoDTO> productos = new ArrayList<>();
    private final List<PedidoDTO> pedidos = new ArrayList<>();
    private final List<DireccionDTO> direcciones = new ArrayList<>();
    private final List<Long> favoritosIds = new ArrayList<>();
    private final List<UsuarioAdminDTO> usuariosSistema = new ArrayList<>();
    private final List<CategoriaAdminDTO> categoriasAdmin = new ArrayList<>();
    private UsuarioClienteDTO usuarioCliente;
    private UsuarioAdminDTO usuarioAdmin;

    public MockDataProvider() {
        productos.add(new ProductoDTO(1L, "Ceviche Clásico", "Platos criollos",
                "Pesca del día marinada en leche de tigre, ají limo, cebolla roja y camote glaseado.",
                "Pesca del día marinada en nuestra exclusiva leche de tigre, limón sutil, ají limo y cebolla roja. " +
                        "Acompañado de camote glaseado, choclo desgranado y canchita chullpi. Un clásico de la costa peruana " +
                        "preparado al momento para conservar todo su frescor.",
                45.00, null, "https://lh3.googleusercontent.com/aida-public/AB6AXuCb2my87Y3w0nIuaMS40xqOaci24WYZQmJNmYZ3H82UzimUeN1KSmiTnSDnqxTkxoHzlPwEFwwxi9_FfT1IVEH1l6Dphze3WdfuE-AEsgNZRAPXyw1K4KwFaZZlemXN_Icdx2f1e6l_qLkAIIWRljmDFwgTnlYudyucK6rh8tMABdgZcuxIuiitr-L1rIY2RhM8a5uIhhqp8L5hSKpGX2YXWdTp4vhFvARd_PnLNaG-JfPdefpL3A3N",
                true, true, 4.8, 20));

        productos.add(new ProductoDTO(2L, "Pollo a la Brasa Entero", "Pollo a la brasa",
                "Jugoso pollo macerado con finas hierbas, horneado a la leña, papas fritas y ensalada.",
                "Nuestro tradicional pollo asado a la leña, macerado por 24 horas con finas hierbas y especias " +
                        "peruanas. Acompañado de crujientes papas fritas, ensalada fresca de la casa y nuestras salsas " +
                        "criollas: ají y mayonesa casera.",
                65.00, 75.00, "https://lh3.googleusercontent.com/aida-public/AB6AXuC0bHlEablt3DjkTV7bqfbv1_hc5vx3pQsuLjW8yXc0E1jebBhXFJhHSJzqnP_mKQ58HnJ_J3UwIXAYhQYdGSoYwgLyC2T8qlH199lYZhqommV-m4I5jvP98lTZvTIGWtJafGRk9owiQNRnaX03KQw9Jh_BMCM4rtYx3dHSwWdLFUu5xr7-SQ8ZBN95lAT6tCkK1YdkmsFT6CpH5xjGd1rca2gzPOWBPVBsDPZhnY6sjPvoYyf-59nx",
                true, false, 4.9, 35));

        productos.add(new ProductoDTO(3L, "Lomo Saltado Tradicional", "Platos criollos",
                "Lomo fino salteado al wok con cebolla, tomate y ají amarillo, papas fritas y arroz.",
                "Trozos de lomo fino salteados al wok a fuego alto con cebolla roja, tomate, ají amarillo y sillao. " +
                        "Servido con crujientes papas amarillas fritas y arroz blanco graneado. El plato bandera de la " +
                        "cocina chifa-criolla peruana.",
                48.00, null, "https://static.wixstatic.com/media/9755d8_b2d98eade0814b17a67fdf7d95888fdc~mv2.png/v1/fill/w_980,h_551,al_c,q_90,usm_0.66_1.00_0.01,enc_avif,quality_auto/9755d8_b2d98eade0814b17a67fdf7d95888fdc~mv2.png",
                false, true, 4.7, 25));

        productos.add(new ProductoDTO(4L, "Ají de Gallina", "Platos criollos",
                "Cremoso guiso de pollo deshilachado con ají amarillo, nueces y queso parmesano.",
                "Cremoso guiso de pollo deshilachado a base de ají amarillo, nueces, queso parmesano y pan remojado " +
                        "en leche. Acompañado de arroz blanco, medio huevo duro y aceituna de botija.",
                42.00, null, "https://lh3.googleusercontent.com/aida-public/AB6AXuAA3jGMdpYNn6MsRVSXYmToFuw45tvmetx36kDDfY4noZ8M7yG3-1_f4CwpoAOs81WSPazZSo18_oZEKJMyUwIyd4oVvwx98fixZbhULmA9HDwFqhWN8RY7PSD532cTTTSGq7USrcn-f7TGEO1ZobaeXpQfudGXAQtv0zTL228B07uiukLnu80O6fpVm0iBURerO8e_49mfKM8TGDtt9gj3OYY2E42XmegzUiqQ9GVuDdyERQl32IdH",
                false, false, 4.6, 30));

        productos.add(new ProductoDTO(5L, "Pisco Sour Catedral", "Bebidas",
                "Nuestro cóctel bandera: pisco quebranta, jarabe de goma, limón y clara de huevo.",
                "Nuestro cóctel bandera en formato grande. Preparado con Pisco Quebranta de la mejor selección, " +
                        "jarabe de goma artesanal, zumo de limón recién exprimido, clara de huevo y unas gotas de " +
                        "Angostura bitters.",
                28.00, null, "https://lh3.googleusercontent.com/aida-public/AB6AXuDyKuVZBT1zDxXEqxIqCqX6g-R4GUZY_hL0tS2zQh-hgmTsShTQPL_J245BrIjNcJUgfV46_Kox6PdWlgOD1D1NDU1InHfidXwmtmp1KjhBCF79D5oUeD3QF182qQ1tEosExp3WV2BsGwJMtdE9sUCCiEKDvlVKZKcvf_XTaYXsDAQCLwknrOP3X_SmuqEge5QzHRJfnXoNSDL7h6uPhzM5elmKTgucb59vzUMBSjFPEynAByIS0oEZ",
                false, false, 4.5, 8));

        productos.add(new ProductoDTO(6L, "Suspiro a la Limeña", "Postres",
                "Dulce tradicional de manjar blanco coronado con merengue al oporto.",
                "Postre limeño clásico compuesto por una base de manjar blanco elaborado con leche evaporada y " +
                        "yemas de huevo, coronado con un delicado merengue italiano perfumado con oporto.",
                18.00, null, "https://lh3.googleusercontent.com/aida-public/AB6AXuBrVJxyv2HNNPCqUaAHabY4hyYqjsib5kd_1X9qKr-esgvqSbj3h0D7qjBGZeIPwTOScYEx8jXWq2BrtKDMADBKCAG2cHyMXKQf4mc1pPfnbU1OBV7rhw9RQjqqOHc4giq6xl8COF6olUW7i52oZ-0e9sC3dZ7oYZ5MBjSU-BjJ6wNpPD6hzapZTinzQZ77eIsXEl4chk30pfqe2WcX8rTxbB5Dm9-6x--66DvT7MHSC7dIl6Q_iuzZ",
                false, false, 4.4, 10));

        // --- NUEVOS PRODUCTOS AÑADIDOS (Carnes, Alitas, Parrillas, Promociones) ---
        productos.add(new ProductoDTO(7L, "Alitas BBQ (12 unds)", "Alitas",
                "Jugosas alitas bañadas en salsa BBQ artesanal, con papas fritas.",
                "Doce piezas de alitas de pollo fritas hasta quedar súper crujientes, bañadas en nuestra salsa BBQ secreta de la casa. Acompañadas de papas fritas, bastones de apio y salsa blue cheese.",
                25.00, 32.00, "https://images.unsplash.com/photo-1608039829572-78524f79c4c7?auto=format&fit=crop&q=80&w=800",
                true, true, 4.8, 20));

        productos.add(new ProductoDTO(8L, "Parrilla Mixta Sabor Milagroso", "Parrillas",
                "Selección premium de carnes, pollo, chorizos y anticuchos a la parrilla.",
                "Ideal para compartir. Incluye 2 filetes de pechuga, 2 chuletas de cerdo, 2 chorizos finas hierbas, 2 palitos de anticucho, porción de papas doradas, choclo tierno y ensalada parrillera.",
                85.00, null, "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?auto=format&fit=crop&q=80&w=800",
                false, true, 4.9, 45));

        productos.add(new ProductoDTO(9L, "Bife Ancho a la Parrilla (350g)", "Carnes",
                "Corte premium de res a la parrilla con guarnición a elegir.",
                "Corte premium de 350g de bife ancho, extremadamente jugoso y tierno, asado a la parrilla en su punto ideal. Servido con papas fritas rústicas o ensalada fresca y chimichurri casero.",
                55.00, null, "https://images.unsplash.com/photo-1600891964092-4316c288032e?auto=format&fit=crop&q=80&w=800",
                false, false, 4.7, 30));

        productos.add(new ProductoDTO(10L, "Chilcano de Maracuyá", "Tragos",
                "Refrescante cóctel de pisco, zumo de maracuyá y ginger ale.",
                "El clásico chilcano peruano con un toque tropical. Pisco quebranta, zumo fresco de maracuyá, ginger ale, gotas de amargo de angostura y abundante hielo.",
                22.00, 26.00, "https://images.unsplash.com/photo-1551538827-9c037cb4f32a?auto=format&fit=crop&q=80&w=800",
                true, false, 4.6, 5));

        productos.add(new ProductoDTO(11L, "Alitas Buffalo Picantes", "Alitas",
                "Alitas crujientes bañadas en auténtica salsa Buffalo picante.",
                "Diez piezas de alitas fritas, cubiertas con auténtica salsa Buffalo ligeramente picante. Ideal para los amantes de los sabores intensos. Incluye salsa ranch y vegetales.",
                26.00, null, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?auto=format&fit=crop&q=80&w=800",
                false, false, 4.5, 20));

        productos.add(new ProductoDTO(12L, "Limonada Frozen con Menta", "Bebidas",
                "Limonada frozen licuada con hojas de menta fresca.",
                "Bebida súper refrescante preparada con zumo de limón recién exprimido, hielo triturado estilo frozen, jarabe de goma y hojas de menta fresca.",
                12.00, 15.00, "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?auto=format&fit=crop&q=80&w=800",
                false, false, 4.7, 5));

        // --- Usuario cliente (mock, sin autenticación real todavía) ---
        usuarioCliente = new UsuarioClienteDTO("Rosa Mendoza", "rosa.mendoza@example.com",
                "+51 987 111 222", null, "Marzo 2024");

        // --- Direcciones mock ---
        direcciones.add(new DireccionDTO(1L, "Casa", "Jr. Los Álamos 245, Dpto. 302", "San Borja",
                "Frente al parque, puerta blanca", true));
        direcciones.add(new DireccionDTO(2L, "Trabajo", "Av. Javier Prado Este 1450, Piso 8", "San Isidro",
                "Recepción, edificio Torre Azul", false));

        // --- Favoritos mock (referencian productos existentes) ---
        favoritosIds.add(1L);
        favoritosIds.add(3L);
        favoritosIds.add(5L);

        // --- Pedidos mock ---
        pedidos.add(new PedidoDTO(1001L, "SM-1001", "18 ago. 2026, 8:20 p.m.", "Entregado",
                "Jr. Los Álamos 245, Dpto. 302 - San Borja", "Tarjeta de crédito",
                List.of(
                        new ItemPedidoDTO(1L, "Ceviche Clásico", productos.get(0).getImagenUrl(), 1, 45.00),
                        new ItemPedidoDTO(5L, "Pisco Sour Catedral", productos.get(4).getImagenUrl(), 2, 28.00)
                )));

        pedidos.add(new PedidoDTO(1002L, "SM-1002", "20 ago. 2026, 1:05 p.m.", "En camino",
                "Av. Javier Prado Este 1450, Piso 8 - San Isidro", "Yape",
                List.of(
                        new ItemPedidoDTO(2L, "Pollo a la Brasa Entero", productos.get(1).getImagenUrl(), 1, 65.00)
                )));

        pedidos.add(new PedidoDTO(1003L, "SM-1003", "22 ago. 2026, 12:40 p.m.", "En preparación",
                "Jr. Los Álamos 245, Dpto. 302 - San Borja", "Efectivo",
                List.of(
                        new ItemPedidoDTO(3L, "Lomo Saltado Tradicional", productos.get(2).getImagenUrl(), 1, 48.00),
                        new ItemPedidoDTO(6L, "Suspiro a la Limeña", productos.get(5).getImagenUrl(), 2, 18.00)
                )));

        // --- Usuario administrador (mock, sin autenticación real todavía) ---
        usuarioAdmin = new UsuarioAdminDTO(1L, "Carlos Ríos", "carlos.rios@sabormilagroso.com",
                "Administrador", "Activo", "Enero 2024", 0);

        // --- Usuarios del sistema (mock, para la sección Usuarios del Admin) ---
        usuariosSistema.add(new UsuarioAdminDTO(101L, "Rosa Mendoza", "rosa.mendoza@example.com",
                "Cliente", "Activo", "Marzo 2024", 3));
        usuariosSistema.add(new UsuarioAdminDTO(102L, "Jorge Huamán", "jorge.huaman@example.com",
                "Cliente", "Activo", "Mayo 2024", 7));
        usuariosSistema.add(new UsuarioAdminDTO(103L, "Lucía Fernández", "lucia.fernandez@example.com",
                "Cliente", "Inactivo", "Julio 2024", 1));
        usuariosSistema.add(new UsuarioAdminDTO(104L, "Miguel Torres", "miguel.torres@example.com",
                "Cliente", "Activo", "Agosto 2025", 12));
        usuariosSistema.add(new UsuarioAdminDTO(1L, "Carlos Ríos", "carlos.rios@sabormilagroso.com",
                "Administrador", "Activo", "Enero 2024", 0));

        // --- Categorías (mock, para la sección Categorías del Admin) ---
        categoriasAdmin.add(new CategoriaAdminDTO(1L, "Platos criollos", "Platos tradicionales de la costa peruana", 3, true));
        categoriasAdmin.add(new CategoriaAdminDTO(2L, "Pollo a la brasa", "Pollo asado a la leña y acompañamientos", 1, true));
        categoriasAdmin.add(new CategoriaAdminDTO(3L, "Ceviches", "Pescados y mariscos marinados en cítricos", 0, true));
        categoriasAdmin.add(new CategoriaAdminDTO(4L, "Parrillas", "Carnes a la parrilla y anticuchos", 0, true));
        categoriasAdmin.add(new CategoriaAdminDTO(5L, "Postres", "Dulces tradicionales peruanos", 1, true));
        categoriasAdmin.add(new CategoriaAdminDTO(6L, "Bebidas", "Cócteles, jugos y refrescos", 1, false));
    }

    public List<ProductoDTO> obtenerTodos() {
        return productos;
    }

    public List<ProductoDTO> obtenerDestacados() {
        return productos.stream().filter(ProductoDTO::isRecomendado).toList();
    }

    public List<ProductoDTO> obtenerEnPromocion() {
        return productos.stream().filter(ProductoDTO::isEnPromocion).toList();
    }

    public Optional<ProductoDTO> obtenerPorId(Long id) {
        return productos.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public List<String> obtenerCategorias() {
        return List.of("Criollos", "Ceviches", "Pollo a la brasa", "Parrillas", "Carnes", "Alitas", "Postres", "Bebidas", "Tragos");
    }

    // ---------------------------------------------------------------
    // Datos mock del ÁREA CLIENTE (Fase 2)
    // ---------------------------------------------------------------

    public UsuarioClienteDTO obtenerUsuarioActual() {
        return usuarioCliente;
    }

    public List<PedidoDTO> obtenerPedidosCliente() {
        return pedidos;
    }

    public Optional<PedidoDTO> obtenerPedidoPorId(Long id) {
        return pedidos.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public List<DireccionDTO> obtenerDirecciones() {
        return direcciones;
    }

    public List<ProductoDTO> obtenerFavoritos() {
        return productos.stream().filter(p -> favoritosIds.contains(p.getId())).toList();
    }

    /*
      Carrito mock fijo, solo para maquetar Carrito/Checkout/Confirmación.
      En Fase 4 esto se reemplazará por un carrito real basado en sesión/usuario.
     */
    public List<ItemPedidoDTO> obtenerCarritoMock() {
        return new ArrayList<>(List.of(
                new ItemPedidoDTO(1L, "Ceviche Clásico", productos.get(0).getImagenUrl(), 1, 45.00),
                new ItemPedidoDTO(2L, "Pollo a la Brasa Entero", productos.get(1).getImagenUrl(), 1, 65.00),
                new ItemPedidoDTO(6L, "Suspiro a la Limeña", productos.get(5).getImagenUrl(), 2, 18.00)
        ));
    }

    // ---------------------------------------------------------------
    // Datos mock del PANEL ADMINISTRADOR (Fase 3)
    // ---------------------------------------------------------------

    public UsuarioAdminDTO obtenerUsuarioAdmin() {
        return usuarioAdmin;
    }

    /**
     * Reutiliza la misma lista de pedidos del área Cliente: en esta fase mock
     * representan "todos los pedidos del sistema" tal como los vería el Admin.
     */
    public List<PedidoDTO> obtenerPedidosAdmin() {
        return pedidos;
    }

    public List<UsuarioAdminDTO> obtenerUsuariosSistema() {
        return usuariosSistema;
    }

    public List<CategoriaAdminDTO> obtenerCategoriasAdmin() {
        return categoriasAdmin;
    }

    /**
     * Métricas simples calculadas a partir de los datos mock existentes,
     * usadas en el Dashboard y Reportes del Admin. En Fase 4 se calcularán
     * con consultas reales a la base de datos.
     */
    public double obtenerVentasTotales() {
        return pedidos.stream().mapToDouble(PedidoDTO::getTotal).sum();
    }

    public long obtenerPedidosPendientes() {
        return pedidos.stream().filter(p -> !p.getEstado().equals("Entregado") && !p.getEstado().equals("Cancelado")).count();
    }
}