package comsabormilagroso.dto;

import java.util.List;

public class PedidoDTO {

    private Long id;
    private String codigo;
    private String fecha;
    private String estado;
    private String direccionEntrega;
    private String metodoPago;
    private List<ItemPedidoDTO> items;
    private String clienteNombre;
    private String clienteEmail;
    private String clienteTelefono;

    public PedidoDTO() {
    }

    public PedidoDTO(Long id, String codigo, String fecha, String estado, String direccionEntrega,
                      String metodoPago, List<ItemPedidoDTO> items) {
        this(id, codigo, fecha, estado, direccionEntrega, metodoPago, items,
                "Cliente de prueba", "cliente@demo.com", "—");
    }

    public PedidoDTO(Long id, String codigo, String fecha, String estado, String direccionEntrega,
                      String metodoPago, List<ItemPedidoDTO> items,
                      String clienteNombre, String clienteEmail, String clienteTelefono) {
        this.id = id;
        this.codigo = codigo;
        this.fecha = fecha;
        this.estado = estado;
        this.direccionEntrega = direccionEntrega;
        this.metodoPago = metodoPago;
        this.items = items;
        this.clienteNombre = clienteNombre;
        this.clienteEmail = clienteEmail;
        this.clienteTelefono = clienteTelefono;
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

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }

    public String getClienteTelefono() { return clienteTelefono; }
    public void setClienteTelefono(String clienteTelefono) { this.clienteTelefono = clienteTelefono; }

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