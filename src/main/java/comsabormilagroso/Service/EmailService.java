package comsabormilagroso.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final ObjectProvider<JavaMailSender> mailSenderProvider;
    private final String remitente;

    public EmailService(ObjectProvider<JavaMailSender> mailSenderProvider,
                        @Value("${app.mail.from:no-reply@sabormilagroso.pe}") String remitente) {
        this.mailSenderProvider = mailSenderProvider;
        this.remitente = remitente;
    }

    public void enviarRecuperacion(String destino, String nombre, String enlace, int minutos) {
        JavaMailSender sender = mailSenderProvider.getIfAvailable();

        // Sin SMTP configurado (desarrollo): el enlace se imprime en la consola.
        if (sender == null) {
            log.warn("[SMTP no configurado] Enlace de recuperación para {}: {}", destino, enlace);
            return;
        }

        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom(remitente);
            mensaje.setTo(destino);
            mensaje.setSubject("Recupera tu contraseña - Sabor Milagroso");
            mensaje.setText("Hola " + nombre + ",\n\n"
                    + "Recibimos una solicitud para restablecer tu contraseña. "
                    + "Ingresa al siguiente enlace (válido por " + minutos + " minutos):\n\n"
                    + enlace + "\n\n"
                    + "Si no fuiste tú, ignora este mensaje.\n\n"
                    + "Sabor Milagroso");
            sender.send(mensaje);
        } catch (Exception e) {
            log.error("No se pudo enviar el correo a {}: {}", destino, e.getMessage());
        }
    }
}