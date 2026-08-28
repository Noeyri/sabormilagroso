package comsabormilagroso.dto;

/*
  DTO plano (mock) que representa una categoría del menú vista desde el
  panel de Administrador. No tiene persistencia real todavía (Fase 4).
 */
public class CategoriaAdminDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private int cantidadProductos;
    private boolean activa;

    public CategoriaAdminDTO() {
    }

    public CategoriaAdminDTO(Long id, String nombre, String descripcion, int cantidadProductos, boolean activa) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidadProductos = cantidadProductos;
        this.activa = activa;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getCantidadProductos() { return cantidadProductos; }
    public void setCantidadProductos(int cantidadProductos) { this.cantidadProductos = cantidadProductos; }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
}