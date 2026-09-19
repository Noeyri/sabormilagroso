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
        // ==========================================
        // LA CARTA: EL SABOR MILAGROSO (Actualizado con Promociones)
        // ==========================================

        // --- PROMOCIONES (NUEVA SECCIÓN) ---
        productos.add(new ProductoDTO(201L, "Promo Chelas 3x2", "Promociones", "Lleva 3 cervezas Cusqueña y paga 2.", "Aplica para Cusqueña Trigo, Negra o Dorada. Ideal para el fin de semana.", 30.00, 45.00, "https://images.unsplash.com/photo-1608270586620-248524c67de9?auto=format&fit=crop&w=800&q=80", true, true, 4.9, 40));
        productos.add(new ProductoDTO(202L, "Dúo Hamburguesero", "Promociones", "2 Hamburguesas de carne + 2 Papas + 2 Gaseosas.", "El combo ideal para compartir. Hamburguesas clásicas con papas crujientes.", 35.00, 48.00, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=800&q=80", true, true, 4.8, 35));
        productos.add(new ProductoDTO(203L, "Banquete Familiar", "Promociones", "Pollo entero + Chaufa Jumbo + Papas + Gaseosa 1.5L.", "Todo lo que necesitas para tu familia con un gran descuento especial.", 85.00, 105.00, "https://images.unsplash.com/photo-1598514982205-f36b96d1e8d4?auto=format&fit=crop&w=800&q=80", true, true, 4.9, 50));
        productos.add(new ProductoDTO(204L, "Combo Previa", "Promociones", "10 Alitas + Chilcano XXXL.", "La combinación perfecta para arrancar la noche. Elige tus salsas favoritas.", 65.00, 84.00, "https://images.unsplash.com/photo-1527477396000-e27163b481c2?auto=format&fit=crop&w=800&q=80", true, false, 4.8, 20));
        productos.add(new ProductoDTO(205L, "Parrillero Mix", "Promociones", "Anticuchos + Mollejitas + Jarra Chicha 1Lt.", "Nuestras mejores entradas parrilleras juntas a un precio insuperable.", 45.00, 54.00, "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?auto=format&fit=crop&w=800&q=80", true, false, 4.7, 25));

        // --- ALITAS ---
        productos.add(new ProductoDTO(101L, "Alitas 5 Piezas", "Alitas", "Incluye papas + chaufa + ensalada.", "Sabores: BBQ, Búfalo, Honey, Acevichada, Crispy, Anticuchera, Maracuyá, Honey Mustard, Mango habanero.", 18.00, null, "https://images.unsplash.com/photo-1527477396000-e27163b481c2?auto=format&fit=crop&w=800&q=80", false, true, 4.8, 20));
        productos.add(new ProductoDTO(102L, "Alitas 10 Piezas", "Alitas", "Incluye papas + chaufa + ensalada.", "Elige hasta 2 sabores de nuestras exquisitas salsas.", 34.00, 40.00, "https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445?auto=format&fit=crop&w=800&q=80", true, true, 4.9, 25));
        productos.add(new ProductoDTO(103L, "Alitas 20 Piezas", "Alitas", "Incluye papas + chaufa + ensalada (+ ronda de chilcanos).", "Para compartir en grupo, elige hasta 4 sabores.", 65.00, null, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?auto=format&fit=crop&w=800&q=80", false, false, 4.8, 30));

        // --- SUPERCOMBOS ---
        productos.add(new ProductoDTO(104L, "La torre de alitas", "Supercombos", "50 piezas + papas.", "Un reto para compartir. Torre gigante de nuestras mejores alitas.", 120.00, 140.00, "https://images.unsplash.com/photo-1608039829572-78524f79c4c7?auto=format&fit=crop&w=800&q=80", true, false, 4.9, 45));
        productos.add(new ProductoDTO(105L, "El barco de alitas", "Supercombos", "100 piezas + papas + chilcanos.", "Nuestra máxima presentación servida en un barco, ideal para eventos.", 200.00, null, "https://images.unsplash.com/photo-1514362545857-3bc16c4c7d1b?auto=format&fit=crop&w=800&q=80", false, false, 5.0, 60));

        // --- COCTELES ---
        productos.add(new ProductoDTO(106L, "Chilcanos de temporada", "Cocteles", "Fresco y clásico chilcano peruano.", "Pisco quebranta, ginger ale, limón y amargo de angostura.", 15.00, null, "https://images.unsplash.com/photo-1551538827-9c037cb4f32a?auto=format&fit=crop&w=800&q=80", false, true, 4.7, 10));
        productos.add(new ProductoDTO(107L, "Mojitos", "Cocteles", "Mojito clásico con menta y ron.", "Ron blanco, hierbabuena fresca, limón y soda.", 15.00, null, "https://images.unsplash.com/photo-1551538827-9c037cb4f32a?auto=format&fit=crop&w=800&q=80", false, false, 4.6, 10));
        productos.add(new ProductoDTO(108L, "Sours de temporada", "Cocteles", "Pisco sour preparado al momento.", "Pisco, jarabe, limón y clara batida.", 15.00, null, "https://images.unsplash.com/photo-1514362545857-3bc16c4c7d1b?auto=format&fit=crop&w=800&q=80", false, false, 4.8, 10));
        productos.add(new ProductoDTO(109L, "Coctel de algarrobina", "Cocteles", "Dulce y cremoso cóctel de pisco y algarrobina.", "Con leche evaporada y canela espolvoreada.", 15.00, null, "https://images.unsplash.com/photo-1514362545857-3bc16c4c7d1b?auto=format&fit=crop&w=800&q=80", false, false, 4.7, 10));
        productos.add(new ProductoDTO(110L, "Calientito", "Cocteles", "Infusión caliente con pisco.", "Ideal para el frío, preparado con especias andinas.", 15.00, null, "https://images.unsplash.com/photo-1544145945-f90425340c7e?auto=format&fit=crop&w=800&q=80", false, false, 4.5, 10));
        productos.add(new ProductoDTO(111L, "Cuba libre", "Cocteles", "Ron y bebida de cola.", "Clásico y refrescante.", 15.00, null, "https://images.unsplash.com/photo-1514362545857-3bc16c4c7d1b?auto=format&fit=crop&w=800&q=80", false, false, 4.6, 10));
        productos.add(new ProductoDTO(112L, "Piña colada", "Cocteles", "Ron, crema de coco y jugo de piña.", "Sabor tropical garantizado.", 15.00, null, "https://images.unsplash.com/photo-1551538827-9c037cb4f32a?auto=format&fit=crop&w=800&q=80", false, false, 4.7, 10));
        productos.add(new ProductoDTO(113L, "Chilcano XXXL", "Cocteles", "Nuestro chilcano en versión gigante.", "Para aquellos que un vaso normal no es suficiente.", 50.00, 65.00, "https://images.unsplash.com/photo-1551538827-9c037cb4f32a?auto=format&fit=crop&w=800&q=80", true, false, 4.9, 15));

        // --- BEBIDAS ---
        productos.add(new ProductoDTO(27L, "Chicha / Maracuya / Limonada - 1lt", "Bebidas", "Jarra de 1 litro natural.", "Bebida fresca preparada en el día.", 14.00, null, "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?auto=format&fit=crop&w=800&q=80", false, true, 4.8, 5));
        productos.add(new ProductoDTO(114L, "Chicha / Maracuya 1lt Frozen", "Bebidas", "Jarra de 1 litro estilo frozen.", "Granizado y refrescante.", 18.00, null, "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?auto=format&fit=crop&w=800&q=80", false, false, 4.7, 5));
        productos.add(new ProductoDTO(115L, "Agua cielo sin gas x 625 ml", "Bebidas", "Agua mineral.", "Botella personal.", 4.00, null, "https://images.unsplash.com/photo-1544145945-f90425340c7e?auto=format&fit=crop&w=800&q=80", false, false, 4.0, 2));
        productos.add(new ProductoDTO(116L, "Coca cola x 500 ml", "Bebidas", "Gaseosa personal.", "Botella de 500ml.", 5.00, null, "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?auto=format&fit=crop&w=800&q=80", false, false, 4.5, 2));
        productos.add(new ProductoDTO(117L, "Inca cola x 500 ml", "Bebidas", "Gaseosa personal.", "La bebida de sabor nacional.", 5.00, null, "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?auto=format&fit=crop&w=800&q=80", false, false, 4.6, 2));
        productos.add(new ProductoDTO(118L, "Cusqueña trigo", "Bebidas", "Cerveza artesanal de trigo.", "Botella personal.", 15.00, null, "https://images.unsplash.com/photo-1608270586620-248524c67de9?auto=format&fit=crop&w=800&q=80", false, false, 4.8, 5));
        productos.add(new ProductoDTO(119L, "Cusqueña negra", "Bebidas", "Cerveza malta oscura.", "Botella personal.", 15.00, null, "https://images.unsplash.com/photo-1608270586620-248524c67de9?auto=format&fit=crop&w=800&q=80", false, false, 4.8, 5));
        productos.add(new ProductoDTO(120L, "Cusqueña dorada", "Bebidas", "Cerveza lager premium.", "Botella personal.", 15.00, null, "https://images.unsplash.com/photo-1608270586620-248524c67de9?auto=format&fit=crop&w=800&q=80", false, false, 4.7, 5));
        productos.add(new ProductoDTO(121L, "Corona", "Bebidas", "Cerveza importada.", "Botella personal.", 15.00, null, "https://images.unsplash.com/photo-1608270586620-248524c67de9?auto=format&fit=crop&w=800&q=80", false, false, 4.6, 5));
        productos.add(new ProductoDTO(122L, "Pilsen", "Bebidas", "Cerveza tradicional peruana.", "Botella personal.", 12.00, null, "https://images.unsplash.com/photo-1608270586620-248524c67de9?auto=format&fit=crop&w=800&q=80", false, false, 4.5, 5));

        // --- HAMBURGUESAS ---
        productos.add(new ProductoDTO(123L, "Hamburguesa de Carne casera", "Hamburguesas", "Carne artesanal y papas al hilo.", "Medallón 100% vacuno con lechuga, tomate y salsas.", 15.00, null, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=800&q=80", false, false, 4.5, 15));
        productos.add(new ProductoDTO(124L, "Hamburguesa filete de pollo", "Hamburguesas", "Pechuga de pollo a la plancha.", "Con lechuga, tomate y papas.", 15.00, null, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=800&q=80", false, false, 4.4, 15));
        productos.add(new ProductoDTO(125L, "Hamburguesa de pollo crispy", "Hamburguesas", "Pollo empanizado y crujiente.", "Con lechuga, tomate y papas.", 15.00, null, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=800&q=80", false, false, 4.6, 15));
        productos.add(new ProductoDTO(126L, "Hamburguesa pollo deshilachado", "Hamburguesas", "Sabor brasa en formato burger.", "Pollo a la brasa desilachado con nuestro aderezo especial.", 15.00, null, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=800&q=80", false, true, 4.8, 15));
        productos.add(new ProductoDTO(127L, "Hamburguesa royal", "Hamburguesas", "Carne, jamon y queso.", "La clásica royal con papas y cremas.", 17.00, null, "https://images.unsplash.com/photo-1586190848861-99aa4a171e90?auto=format&fit=crop&w=800&q=80", false, false, 4.6, 15));
        productos.add(new ProductoDTO(128L, "Hamburguesa la golosa", "Hamburguesas", "Carne, huevo y chorizo.", "Para los de buen diente, extra sabor ahumado.", 18.00, 23.00, "https://images.unsplash.com/photo-1586190848861-99aa4a171e90?auto=format&fit=crop&w=800&q=80", true, false, 4.8, 15));
        productos.add(new ProductoDTO(129L, "Hamburguesa la santa", "Hamburguesas", "Doble carne, doble queso.", "Sobredosis de carne y queso fundido.", 22.00, 27.00, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=800&q=80", true, false, 4.9, 20));
        productos.add(new ProductoDTO(24L, "Hamburguesa la milagrosa", "Hamburguesas", "Carne, pollo, jamon, queso y huevo.", "Nuestra hamburguesa estrella, lleva de todo.", 23.00, null, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=800&q=80", false, true, 4.9, 20));

        // --- ARMA TU COMBO ---
        productos.add(new ProductoDTO(130L, "Arma tu match", "Arma tu combo", "2 opciones + papas + ensalada chica + chicha 8 Oz.", "Elige 2 carnes de nuestra selección.", 32.00, null, "https://images.unsplash.com/photo-1598514982205-f36b96d1e8d4?auto=format&fit=crop&w=800&q=80", false, true, 4.7, 20));
        productos.add(new ProductoDTO(131L, "Arma tu triki", "Arma tu combo", "3 opciones + papas + ensalada chica + chicha 1/2 Lt.", "Elige 3 carnes de nuestra selección.", 45.00, 52.00, "https://images.unsplash.com/photo-1544025162-87063d810a90?auto=format&fit=crop&w=800&q=80", true, false, 4.8, 25));
        productos.add(new ProductoDTO(132L, "Arma tu combate", "Arma tu combo", "4 opciones + papas + ensalada + chicha 1 Lt.", "El combo definitivo para compartir, 4 carnes a elección.", 56.00, null, "https://images.unsplash.com/photo-1529193591184-b1d58069ecdd?auto=format&fit=crop&w=800&q=80", false, false, 4.9, 30));

        // --- MFC - MILAGROSO FRIED CHICKEN ---
        productos.add(new ProductoDTO(133L, "Chicharron de pollo Milagroso", "MFC", "Nuestra receta secreta gigante.", "Porción extra grande de chicharrón crujiente.", 65.00, null, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?auto=format&fit=crop&w=800&q=80", false, false, 4.8, 30));
        productos.add(new ProductoDTO(134L, "Chicharron de pollo (10 piezas)", "MFC", "10 piezas de pollo + papas + Ensalada.", "Dorados a la perfección.", 50.00, null, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?auto=format&fit=crop&w=800&q=80", false, true, 4.7, 25));
        productos.add(new ProductoDTO(135L, "1/2 chicharron de pollo", "MFC", "5 piezas de pollo + Papas + Ensalada.", "Porción personal generosa.", 28.00, null, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?auto=format&fit=crop&w=800&q=80", false, false, 4.6, 20));
        productos.add(new ProductoDTO(136L, "Chicharron 3 PIEZAS + Chaufa + papas", "MFC", "Combo con arroz chaufa y ensalada.", "Combinación infalible.", 20.00, null, "https://images.unsplash.com/photo-1603133872878-684f208fb84b?auto=format&fit=crop&w=800&q=80", false, false, 4.7, 20));
        productos.add(new ProductoDTO(137L, "Broaster + Papas + Ensalada", "MFC", "Pieza de pollo broaster clásica.", "Crujiente por fuera, jugoso por dentro.", 15.00, null, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?auto=format&fit=crop&w=800&q=80", false, false, 4.5, 15));
        productos.add(new ProductoDTO(138L, "Broaster + Chaufa + papas + Ensalada", "MFC", "Pollo broaster servido con arroz chaufa.", "Para calmar un gran apetito.", 18.00, 22.00, "https://images.unsplash.com/photo-1603133872878-684f208fb84b?auto=format&fit=crop&w=800&q=80", true, false, 4.7, 20));
        productos.add(new ProductoDTO(139L, "Broaster Promo", "MFC", "Promoción especial de broaster.", "Consulta la pieza del día.", 28.00, 35.00, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?auto=format&fit=crop&w=800&q=80", true, true, 4.6, 20));
        productos.add(new ProductoDTO(140L, "Mostrito Milagroso", "MFC", "Broaster + chaufa + papas.", "El clásico mostrito peruano con nuestro toque.", 25.00, null, "https://images.unsplash.com/photo-1603133872878-684f208fb84b?auto=format&fit=crop&w=800&q=80", false, true, 4.8, 20));
        productos.add(new ProductoDTO(16L, "Chaufa Milagroso", "MFC", "Arroz chaufa especial de la casa.", "Preparado a fuego alto con carnes mixtas.", 20.00, null, "https://images.unsplash.com/photo-1603133872878-684f208fb84b?auto=format&fit=crop&w=800&q=80", false, false, 4.8, 15));

        // --- PARRILLAS ---
        productos.add(new ProductoDTO(141L, "Mollejitas", "Parrillas", "Mollejitas al carbón (Precio Personal).", "Acompañadas de papas y sarsa.", 20.00, null, "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?auto=format&fit=crop&w=800&q=80", false, false, 4.5, 20));
        productos.add(new ProductoDTO(142L, "Anticuchos", "Parrillas", "Anticuchos de corazón (Precio Personal).", "Macerados en ají panca, asados al carbón.", 20.00, 25.00, "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?auto=format&fit=crop&w=800&q=80", true, true, 4.9, 20));
        productos.add(new ProductoDTO(143L, "Pechuga de pollo / campesina", "Parrillas", "Pechuga asada a las brasas.", "Marinada con finas hierbas.", 20.00, null, "https://images.unsplash.com/photo-1532550907401-a500c9a57435?auto=format&fit=crop&w=800&q=80", false, false, 4.6, 25));
        productos.add(new ProductoDTO(144L, "Churrasco / campesino", "Parrillas", "Churrasco de res al carbón.", "Corte jugoso y tierno.", 25.00, null, "https://images.unsplash.com/photo-1600891964092-4316c288032e?auto=format&fit=crop&w=800&q=80", false, false, 4.7, 25));
        productos.add(new ProductoDTO(145L, "Chuleta de cerdo", "Parrillas", "Chuleta dorada a las brasas.", "Con aderezo parrillero.", 22.00, null, "https://images.unsplash.com/photo-1432139555190-58524dae6a5a?auto=format&fit=crop&w=800&q=80", false, false, 4.6, 25));
        productos.add(new ProductoDTO(146L, "Parrilla para dos", "Parrillas", "Mix de carnes al carbón.", "Churrasco, pollo, chuleta, anticuchos, chorizos.", 69.00, 80.00, "https://images.unsplash.com/photo-1529193591184-b1d58069ecdd?auto=format&fit=crop&w=800&q=80", true, true, 4.9, 35));

        // --- COMPLEMENTOS ---
        productos.add(new ProductoDTO(147L, "Chaufa Jumbo", "Complementos", "Porción de arroz chaufa gigante.", "Para acompañar tus platillos.", 4.00, null, "https://images.unsplash.com/photo-1603133872878-684f208fb84b?auto=format&fit=crop&w=800&q=80", false, false, 4.5, 5));
        productos.add(new ProductoDTO(148L, "Chaufa clásico", "Complementos", "Porción de arroz chaufa.", "Receta tradicional.", 7.00, null, "https://images.unsplash.com/photo-1603133872878-684f208fb84b?auto=format&fit=crop&w=800&q=80", false, false, 4.5, 5));
        productos.add(new ProductoDTO(149L, "Porción Papas", "Complementos", "Papas fritas crujientes.", "Porción generosa.", 13.00, null, "https://images.unsplash.com/photo-1569691899455-88464f6d3cb1?auto=format&fit=crop&w=800&q=80", false, false, 4.6, 5));
        productos.add(new ProductoDTO(150L, "Ensalada fresca", "Complementos", "Mix de lechuga, tomate y zanahoria.", "Con vinagreta de la casa.", 10.00, null, "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&fit=crop&w=800&q=80", false, false, 4.4, 5));
        productos.add(new ProductoDTO(151L, "Hotdog o Chorizo", "Complementos", "Adicional para tus platos.", "Frito o a la parrilla.", 5.00, null, "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?auto=format&fit=crop&w=800&q=80", false, false, 4.5, 5));
        productos.add(new ProductoDTO(152L, "Huevo adicional", "Complementos", "Huevo frito.", "Perfecto para tu hamburguesa.", 2.00, null, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=800&q=80", false, false, 4.2, 2));
        productos.add(new ProductoDTO(153L, "Queso", "Complementos", "Lámina de queso edam.", "Para derretir.", 2.00, null, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=800&q=80", false, false, 4.2, 2));

        // --- PIQUEOS ---
        productos.add(new ProductoDTO(154L, "Tequeños de jamon y queso", "Piqueos", "6 crujientes tequeños.", "Rellenos de queso fundido y jamón.", 15.00, null, "https://images.unsplash.com/photo-1534422298391-e4f8c172dd36?auto=format&fit=crop&w=800&q=80", false, true, 4.7, 15));
        productos.add(new ProductoDTO(155L, "Tequeños de pollo a la brasa", "Piqueos", "6 tequeños con relleno especial.", "Con nuestro característico pollo deshilachado.", 15.00, null, "https://images.unsplash.com/photo-1534422298391-e4f8c172dd36?auto=format&fit=crop&w=800&q=80", false, false, 4.8, 15));

        // --- CRIOLLOS & BRASAS (PLATOS CLÁSICOS) ---
        productos.add(new ProductoDTO(1L, "Pollo a la brasa entero", "Brasas", "Incluye papas fritas y ensalada fresca.", "Jugoso pollo asado a la leña, macerado por 24 horas con finas hierbas.", 69.00, 79.00, "https://images.unsplash.com/photo-1598514982205-f36b96d1e8d4?auto=format&fit=crop&w=800&q=80", true, true, 4.9, 45));
        productos.add(new ProductoDTO(12L, "Lomo saltado", "Criollos", "Lomo fino salteado al wok con cebolla, tomate y papas.", "Servido con crujientes papas fritas y arroz blanco.", 25.00, null, "https://images.unsplash.com/photo-1601356616077-695728ae17cb?auto=format&fit=crop&w=800&q=80", false, true, 4.9, 20));


        // --- Usuario cliente (mock) ---
        usuarioCliente = new UsuarioClienteDTO("Rosa Mendoza", "rosa.mendoza@example.com",
                "+51 987 111 222", null, "Marzo 2024");

        // --- Direcciones mock ---
        direcciones.add(new DireccionDTO(1L, "Casa", "Jr. Los Álamos 245, Dpto. 302", "San Borja",
                "Frente al parque, puerta blanca", true));
        direcciones.add(new DireccionDTO(2L, "Trabajo", "Av. Javier Prado Este 1450, Piso 8", "San Isidro",
                "Recepción, edificio Torre Azul", false));

        // --- Favoritos mock ---
        favoritosIds.add(1L); // Pollo entero
        favoritosIds.add(12L); // Lomo saltado
        favoritosIds.add(24L); // Hamburguesa milagrosa

        // --- Pedidos mock ---
        pedidos.add(new PedidoDTO(1001L, "SM-1001", "18 ago. 2026, 8:20 p.m.", "Entregado",
                "Jr. Los Álamos 245, Dpto. 302 - San Borja", "Tarjeta de crédito",
                List.of(
                        new ItemPedidoDTO(12L, "Lomo saltado", "https://images.unsplash.com/photo-1601356616077-695728ae17cb?auto=format&fit=crop&w=800&q=80", 1, 25.00),
                        new ItemPedidoDTO(27L, "Chicha / Maracuya - 1lt", "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?auto=format&fit=crop&w=800&q=80", 2, 14.00)
                )));

        pedidos.add(new PedidoDTO(1002L, "SM-1002", "20 ago. 2026, 1:05 p.m.", "En camino",
                "Av. Javier Prado Este 1450, Piso 8 - San Isidro", "Yape",
                List.of(
                        new ItemPedidoDTO(1L, "Pollo a la brasa entero", "https://images.unsplash.com/photo-1598514982205-f36b96d1e8d4?auto=format&fit=crop&w=800&q=80", 1, 69.00)
                )));

        pedidos.add(new PedidoDTO(1003L, "SM-1003", "22 ago. 2026, 12:40 p.m.", "En preparación",
                "Jr. Los Álamos 245, Dpto. 302 - San Borja", "Efectivo",
                List.of(
                        new ItemPedidoDTO(16L, "Chaufa Milagroso", "https://images.unsplash.com/photo-1603133872878-684f208fb84b?auto=format&fit=crop&w=800&q=80", 1, 20.00),
                        new ItemPedidoDTO(24L, "Hamburguesa la milagrosa", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=800&q=80", 2, 23.00)
                )));

        // --- Usuario administrador ---
        usuarioAdmin = new UsuarioAdminDTO(1L, "Carlos Ríos", "carlos.rios@sabormilagroso.com",
                "Administrador", "Activo", "Enero 2024", 0);

        // --- Usuarios del sistema ---
        usuariosSistema.add(new UsuarioAdminDTO(101L, "Rosa Mendoza", "rosa.mendoza@example.com", "Cliente", "Activo", "Marzo 2024", 3));
        usuariosSistema.add(new UsuarioAdminDTO(102L, "Jorge Huamán", "jorge.huaman@example.com", "Cliente", "Activo", "Mayo 2024", 7));
        usuariosSistema.add(new UsuarioAdminDTO(103L, "Lucía Fernández", "lucia.fernandez@example.com", "Cliente", "Inactivo", "Julio 2024", 1));
        usuariosSistema.add(new UsuarioAdminDTO(104L, "Miguel Torres", "miguel.torres@example.com", "Cliente", "Activo", "Agosto 2025", 12));
        usuariosSistema.add(new UsuarioAdminDTO(1L, "Carlos Ríos", "carlos.rios@sabormilagroso.com", "Administrador", "Activo", "Enero 2024", 0));

        // --- Categorías Admin ---
        categoriasAdmin.add(new CategoriaAdminDTO(9L, "Promociones", "Combos y descuentos especiales", 5, true));
        categoriasAdmin.add(new CategoriaAdminDTO(1L, "Brasas", "Pollos asados a la leña", 1, true));
        categoriasAdmin.add(new CategoriaAdminDTO(2L, "Parrillas", "Carnes y anticuchos al carbón", 6, true));
        categoriasAdmin.add(new CategoriaAdminDTO(3L, "Alitas", "Alitas con salsas variadas", 3, true));
        categoriasAdmin.add(new CategoriaAdminDTO(4L, "Supercombos", "Combos grandes y familiares", 2, true));
        categoriasAdmin.add(new CategoriaAdminDTO(5L, "Hamburguesas", "Hamburguesas artesanales", 8, true));
        categoriasAdmin.add(new CategoriaAdminDTO(6L, "MFC", "Milagroso Fried Chicken", 9, true));
        categoriasAdmin.add(new CategoriaAdminDTO(7L, "Cocteles", "Tragos y bebidas con alcohol", 8, true));
        categoriasAdmin.add(new CategoriaAdminDTO(8L, "Bebidas", "Gaseosas, cervezas y chicha", 10, true));
    }

    public List<ProductoDTO> obtenerTodos() {
        return productos;
    }

    public List<ProductoDTO> obtenerDestacados() {
        return productos.stream().filter(ProductoDTO::isRecomendado).toList();
    }

    public List<ProductoDTO> obtenerDisponibles() {
        return productos.stream().filter(ProductoDTO::isDisponible).toList();
    }

    public List<ProductoDTO> obtenerEnPromocion() {
        return productos.stream().filter(ProductoDTO::isEnPromocion).toList();
    }

    public Optional<ProductoDTO> obtenerPorId(Long id) {
        return productos.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public List<String> obtenerCategorias() {
        return List.of("Promociones", "Alitas", "Supercombos", "Cocteles", "Bebidas", "Hamburguesas", "Arma tu combo", "MFC", "Parrillas", "Complementos", "Piqueos", "Brasas", "Criollos");
    }

    // ---------------------------------------------------------------
    // Datos mock del ÁREA CLIENTE
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

    public List<ItemPedidoDTO> obtenerCarritoMock() {
        return new ArrayList<>(List.of(
                new ItemPedidoDTO(12L, "Lomo saltado", "https://images.unsplash.com/photo-1601356616077-695728ae17cb?auto=format&fit=crop&w=800&q=80", 1, 25.00),
                new ItemPedidoDTO(1L, "Pollo a la brasa entero", "https://images.unsplash.com/photo-1598514982205-f36b96d1e8d4?auto=format&fit=crop&w=800&q=80", 1, 69.00),
                new ItemPedidoDTO(24L, "Hamburguesa la milagrosa", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=800&q=80", 2, 23.00)
        ));
    }

    // ---------------------------------------------------------------
    // Datos mock del PANEL ADMINISTRADOR
    // ---------------------------------------------------------------

    public UsuarioAdminDTO obtenerUsuarioAdmin() {
        return usuarioAdmin;
    }

    public List<PedidoDTO> obtenerPedidosAdmin() {
        return pedidos;
    }

    public List<UsuarioAdminDTO> obtenerUsuariosSistema() {
        return usuariosSistema;
    }

    public List<CategoriaAdminDTO> obtenerCategoriasAdmin() {
        return categoriasAdmin;
    }

    public double obtenerVentasTotales() {
        return pedidos.stream().mapToDouble(PedidoDTO::getTotal).sum();
    }

    public long obtenerPedidosPendientes() {
        return pedidos.stream().filter(p -> !p.getEstado().equals("Entregado") && !p.getEstado().equals("Cancelado")).count();
    }
}