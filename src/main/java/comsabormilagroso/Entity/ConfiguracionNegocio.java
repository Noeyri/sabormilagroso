package comsabormilagroso.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "configuracion_negocio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionNegocio {

    // Fila única: siempre id = 1.
    @Id
    private Long id = 1L;

    @Column(name = "nombre_negocio", nullable = false, length = 100)
    private String nombreNegocio = "Sabor Milagroso";

    @Column(name = "direccion_negocio", length = 255)
    private String direccionNegocio = "San Joaquín Viejo S-17, Ica";

    @Column(name = "telefono_negocio", length = 30)
    private String telefonoNegocio = "+51 940 667 056";

    @Column(name = "costo_envio", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoEnvio = new BigDecimal("8.00");

    @Column(name = "hora_apertura", length = 5)
    private String horaApertura = "16:00";

    @Column(name = "hora_cierre", length = 5)
    private String horaCierre = "00:00";

    @Column(name = "notificar_nuevo_pedido")
    private Boolean notificarNuevoPedido = true;

    @Column(name = "notificar_stock_bajo")
    private Boolean notificarStockBajo = false;

    @Column(name = "notificar_resumen_semanal")
    private Boolean notificarResumenSemanal = true;
}