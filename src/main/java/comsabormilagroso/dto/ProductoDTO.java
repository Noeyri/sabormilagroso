package comsabormilagroso.dto;

/*
  DTO plano usado únicamente por el FRONTEND (Fase 1) para representar un
  producto del menú con datos ficticios (mock).
 
  IMPORTANTE: esta clase NO es una entidad JPA ni se conecta a base de datos.
  Cuando se implemente el backend (Fase 4) este DTO podrá mapearse desde la
  entidad real "Producto".
 */
public class ProductoDTO {

    private Long id;
    private String nombre;
    private String categoria;
    private String descripcion;
    private String descripcionLarga;
    private Double precio;
    private Double precioAnterior; // null si no está en promoción
    private String imagenUrl;
    private boolean popular;
    private boolean recomendado;
    private Double calificacion;
    private Integer tiempoPreparacionMin;

    public ProductoDTO() {
    }

    public ProductoDTO(Long id, String nombre, String categoria, String descripcion, String descripcionLarga,
                        Double precio, Double precioAnterior, String imagenUrl, boolean popular, boolean recomendado,
                        Double calificacion, Integer tiempoPreparacionMin) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.descripcionLarga = descripcionLarga;
        this.precio = precio;
        this.precioAnterior = precioAnterior;
        this.imagenUrl = imagenUrl;
        this.popular = popular;
        this.recomendado = recomendado;
        this.calificacion = calificacion;
        this.tiempoPreparacionMin = tiempoPreparacionMin;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDescripcionLarga() { return descripcionLarga; }
    public void setDescripcionLarga(String descripcionLarga) { this.descripcionLarga = descripcionLarga; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Double getPrecioAnterior() { return precioAnterior; }
    public void setPrecioAnterior(Double precioAnterior) { this.precioAnterior = precioAnterior; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public boolean isPopular() { return popular; }
    public void setPopular(boolean popular) { this.popular = popular; }

    public boolean isRecomendado() { return recomendado; }
    public void setRecomendado(boolean recomendado) { this.recomendado = recomendado; }

    public Double getCalificacion() { return calificacion; }
    public void setCalificacion(Double calificacion) { this.calificacion = calificacion; }

    public Integer getTiempoPreparacionMin() { return tiempoPreparacionMin; }
    public void setTiempoPreparacionMin(Integer tiempoPreparacionMin) { this.tiempoPreparacionMin = tiempoPreparacionMin; }

    public boolean isEnPromocion() {
        return precioAnterior != null && precioAnterior > precio;
    }
}