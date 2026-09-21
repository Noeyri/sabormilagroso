package comsabormilagroso.Service;

import comsabormilagroso.Entity.ConfiguracionNegocio;
import comsabormilagroso.Repository.ConfiguracionNegocioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.regex.Pattern;

@Service
public class ConfiguracionNegocioService {

    private static final Long ID_UNICO = 1L;
    private static final Pattern HORA_VALIDA = Pattern.compile("^([01]\\d|2[0-3]):[0-5]\\d$");

    private final ConfiguracionNegocioRepository repository;

    public ConfiguracionNegocioService(ConfiguracionNegocioRepository repository) {
        this.repository = repository;
    }

    // Devuelve la configuración vigente. Si no existe, crea la fila con los valores por defecto.
    @Transactional
    public ConfiguracionNegocio obtener() {
        return repository.findById(ID_UNICO).orElseGet(() -> repository.save(new ConfiguracionNegocio()));
    }

    // Devuelve el mensaje de error, o null si todo salió bien.
    @Transactional
    public String actualizar(String nombreNegocio, String direccionNegocio, String telefonoNegocio,
                             BigDecimal costoEnvio, String horaApertura, String horaCierre,
                             boolean notificarNuevoPedido, boolean notificarStockBajo,
                             boolean notificarResumenSemanal) {
        nombreNegocio = limpiar(nombreNegocio);
        direccionNegocio = limpiar(direccionNegocio);
        telefonoNegocio = limpiar(telefonoNegocio);

        if (nombreNegocio.isEmpty() || nombreNegocio.length() > 100) {
            return "Ingresa un nombre de restaurante válido.";
        }
        if (direccionNegocio.length() > 255 || telefonoNegocio.length() > 30) {
            return "La dirección o el teléfono son demasiado largos.";
        }
        if (costoEnvio == null || costoEnvio.compareTo(BigDecimal.ZERO) < 0) {
            return "El costo de envío no puede ser negativo.";
        }
        if (!horaValida(horaApertura) || !horaValida(horaCierre)) {
            return "Ingresa horas de apertura y cierre válidas.";
        }

        ConfiguracionNegocio config = obtener();
        config.setNombreNegocio(nombreNegocio);
        config.setDireccionNegocio(direccionNegocio);
        config.setTelefonoNegocio(telefonoNegocio);
        config.setCostoEnvio(costoEnvio);
        config.setHoraApertura(horaApertura);
        config.setHoraCierre(horaCierre);
        config.setNotificarNuevoPedido(notificarNuevoPedido);
        config.setNotificarStockBajo(notificarStockBajo);
        config.setNotificarResumenSemanal(notificarResumenSemanal);
        repository.save(config);
        return null;
    }

    private boolean horaValida(String hora) {
        return hora != null && HORA_VALIDA.matcher(hora).matches();
    }

    private String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}