package comsabormilagroso.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;

    @Column(name = "fecha_pedido", updatable = false)
    private LocalDateTime fechaPedido = LocalDateTime.now();

    @Column(length = 30)
    private String estado = "PENDIENTE";

    @Column(name = "tipo_envio", length = 30)
    private String tipoEnvio;

    @Column(name = "direccion_envio", columnDefinition = "TEXT")
    private String direccionEnvio;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "metodo_pago", length = 30)
    private String metodoPago;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemPedido> items = new ArrayList<>();

    public void addItem(ItemPedido item) {
        items.add(item);
        item.setPedido(this);
    }
}