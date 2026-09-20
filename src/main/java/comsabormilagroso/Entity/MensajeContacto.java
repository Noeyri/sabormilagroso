package comsabormilagroso.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Entity
@Table(name = "mensajes_contacto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MensajeContacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(length = 30)
    private String telefono;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "fecha_envio", updatable = false)
    private LocalDateTime fechaEnvio = LocalDateTime.now();

    private Boolean leido = false;

    public String getFechaFormateada() {
        return fechaEnvio == null ? "" :
                fechaEnvio.format(DateTimeFormatter.ofPattern("d MMM uuuu, h:mm a",
                        Locale.forLanguageTag("es-PE")));
    }
}