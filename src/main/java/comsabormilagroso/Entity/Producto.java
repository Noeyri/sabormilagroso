package comsabormilagroso.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "descripcion_larga", columnDefinition = "TEXT")
    private String descripcionLarga;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "precio_anterior", precision = 10, scale = 2)
    private BigDecimal precioAnterior;

    private Boolean disponible = true;

    private Boolean popular = false;

    private Boolean recomendado = false;

    private Integer stock = 0;

    private BigDecimal calificacion = BigDecimal.ZERO;

    @Column(name = "tiempo_preparacion_min")
    private Integer tiempoPreparacionMin;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    public boolean enPromocion() {
        return precioAnterior != null && precioAnterior.compareTo(precio) > 0;
    }
}