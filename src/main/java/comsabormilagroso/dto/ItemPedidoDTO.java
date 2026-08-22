package comsabormilagroso.dto;

/*
  DTO plano (mock) que representa un ítem dentro de un pedido del cliente.
  No tiene relación con JPA ni base de datos real (eso se implementará en Fase 4).
 */
public class ItemPedidoDTO {

    private Long productoId;
    private String nombre;
    private String imagenUrl;
    private int cantidad;
    private double precioUnitario;

    public ItemPedidoDTO() {
    }

    public ItemPedidoDTO(Long productoId, String nombre, String imagenUrl, int cantidad, double precioUnitario) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.imagenUrl = imagenUrl;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getSubtotal() {
        return cantidad * precioUnitario;
    }
}