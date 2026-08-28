package comsabormilagroso.dto;

/*
  DTO plano (mock) que representa un usuario del sistema tal como lo vería
  el Administrador en la sección "Usuarios". No tiene relación con la
  entidad JPA "Usuario" ni con base de datos real: eso se conectará en la
  Fase 4.
 */
public class UsuarioAdminDTO {

    private Long id;
    private String nombre;
    private String email;
    private String rol; // "Cliente", "Administrador"
    private String estado; // "Activo", "Inactivo"
    private String fechaRegistro;
    private int pedidosRealizados;

    public UsuarioAdminDTO() {
    }

    public UsuarioAdminDTO(Long id, String nombre, String email, String rol, String estado,
                            String fechaRegistro, int pedidosRealizados) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
        this.pedidosRealizados = pedidosRealizados;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public int getPedidosRealizados() { return pedidosRealizados; }
    public void setPedidosRealizados(int pedidosRealizados) { this.pedidosRealizados = pedidosRealizados; }

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