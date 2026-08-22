package comsabormilagroso.dto;

/*
  DTO plano (mock) que representa al usuario/cliente "logueado".
  En esta fase NO hay autenticación real: se simula un único usuario fijo
  para poder maquetar el área de Cliente. Esto se reemplazará en la Fase 4
  por el usuario autenticado real (Spring Security + entidad Usuario).
 */
public class UsuarioClienteDTO {

    private String nombre;
    private String email;
    private String telefono;
    private String imagenUrl;
    private String miembroDesde;

    public UsuarioClienteDTO() {
    }

    public UsuarioClienteDTO(String nombre, String email, String telefono, String imagenUrl, String miembroDesde) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.imagenUrl = imagenUrl;
        this.miembroDesde = miembroDesde;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getMiembroDesde() { return miembroDesde; }
    public void setMiembroDesde(String miembroDesde) { this.miembroDesde = miembroDesde; }

    public String getIniciales() {
        if (nombre == null || nombre.isBlank()) return "SM";
        String[] partes = nombre.trim().split("\\s+");
        String iniciales = "" + partes[0].charAt(0);
        if (partes.length > 1) {
            iniciales += partes[partes.length - 1].charAt(0);
        }
        return iniciales.toUpperCase();
    }
}