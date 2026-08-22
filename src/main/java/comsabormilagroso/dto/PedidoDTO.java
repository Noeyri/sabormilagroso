package comsabormilagroso.dto;

import java.util.List;

/*
  DTO plano (mock) que representa un pedido del cliente.
  Usado solo por el FRONTEND en la Fase 2. No tiene persistencia real todavía.
 */
public class PedidoDTO {

    private Long id;
    private String codigo;
    private String fecha;
    private String estado; // "En preparación", "En camino", "Entregado", "Cancelado"
    private String direccionEntrega;
    private String metodoPago;
    private List<ItemPedidoDTO> items;

    public PedidoDTO() {
    }

    public PedidoDTO(Long id, String codigo, String fecha, String estado, String direccionEntrega,
                      String metodoPago, List<ItemPedidoDTO> items) {
        this.id = id;
        this.codigo = codigo;
        this.fecha = fecha;
        this.estado = estado;
        this.direccionEntrega = direccionEntrega;
        this.metodoPago = metodoPago;
        this.items = items;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public List<ItemPedidoDTO> getItems() { return items; }
    public void setItems(List<ItemPedidoDTO> items) { this.items = items; }

    public double getSubtotal() {
        return items == null ? 0 : items.stream().mapToDouble(ItemPedidoDTO::getSubtotal).sum();
    }

    public double getEnvio() {
        return 8.00;
    }

    public double getTotal() {
        return getSubtotal() + getEnvio();
    }

    public int getCantidadItems() {
        return items == null ? 0 : items.stream().mapToInt(ItemPedidoDTO::getCantidad).sum();
    }
}