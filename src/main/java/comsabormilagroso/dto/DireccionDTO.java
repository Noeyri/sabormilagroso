package comsabormilagroso.dto;

/*
  DTO plano (mock) que representa una dirección guardada del cliente.
 */
public class DireccionDTO {

    private Long id;
    private String etiqueta; // "Casa", "Trabajo", etc.
    private String direccion;
    private String distrito;
    private String referencia;
    private boolean predeterminada;

    public DireccionDTO() {
    }

    public DireccionDTO(Long id, String etiqueta, String direccion, String distrito, String referencia, boolean predeterminada) {
        this.id = id;
        this.etiqueta = etiqueta;
        this.direccion = direccion;
        this.distrito = distrito;
        this.referencia = referencia;
        this.predeterminada = predeterminada;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEtiqueta() { return etiqueta; }
    public void setEtiqueta(String etiqueta) { this.etiqueta = etiqueta; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getDistrito() { return distrito; }
    public void setDistrito(String distrito) { this.distrito = distrito; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public boolean isPredeterminada() { return predeterminada; }
    public void setPredeterminada(boolean predeterminada) { this.predeterminada = predeterminada; }
}