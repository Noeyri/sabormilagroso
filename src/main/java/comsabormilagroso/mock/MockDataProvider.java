package comsabormilagroso.mock;

import comsabormilagroso.dto.DireccionDTO;
import comsabormilagroso.dto.ItemPedidoDTO;
import comsabormilagroso.dto.PedidoDTO;
import comsabormilagroso.dto.ProductoDTO;
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
    private UsuarioClienteDTO usuarioCliente;

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
                "Trozos de lomo fino salteados al wok con cebolla, tomate y ají amarillo, papas fritas y arroz.",
                "Trozos de lomo fino salteados al wok a fuego alto con cebolla roja, tomate, ají amarillo y sillao. " +
                        "Servido con crujientes papas amarillas fritas y arroz blanco graneado. El plato bandera de la " +
                        "cocina chifa-criolla peruana.",
                48.00, null, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQxMp-JpDg_8eBIAmXrJKx9xYfRAcyd0OOGPY4oatlsmU_pnCKg6RYJ64o&s=10",
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
        return List.of("Criollos", "Ceviches", "Pollo a la brasa", "Parrillas", "Postres", "Bebidas");
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
}