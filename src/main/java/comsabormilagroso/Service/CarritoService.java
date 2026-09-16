package comsabormilagroso.Service;

import comsabormilagroso.dto.ItemPedidoDTO;
import comsabormilagroso.dto.ProductoDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CarritoService {

    public void agregar(List<ItemPedidoDTO> carrito, ProductoDTO producto, int cantidad) {
        ItemPedidoDTO existente = carrito.stream()
                .filter(i -> Objects.equals(i.getProductoId(), producto.getId()))
                .findFirst().orElse(null);
        if (existente != null) {
            existente.setCantidad(existente.getCantidad() + cantidad);
        } else {
            carrito.add(new ItemPedidoDTO(producto.getId(), producto.getNombre(),
                    producto.getImagenUrl(), cantidad, producto.getPrecio()));
        }
    }

    public void actualizarCantidad(List<ItemPedidoDTO> carrito, Long productoId, int cantidad) {
        carrito.stream().filter(i -> Objects.equals(i.getProductoId(), productoId))
                .findFirst().ifPresent(i -> {
                    if (cantidad <= 0) {
                        carrito.remove(i);
                    } else {
                        i.setCantidad(cantidad);
                    }
                });
    }

    public void eliminar(List<ItemPedidoDTO> carrito, Long productoId) {
        carrito.removeIf(i -> Objects.equals(i.getProductoId(), productoId));
    }

    public double subtotal(List<ItemPedidoDTO> carrito) {
        return carrito.stream().mapToDouble(ItemPedidoDTO::getSubtotal).sum();
    }

    public int cantidadItems(List<ItemPedidoDTO> carrito) {
        return carrito.stream().mapToInt(ItemPedidoDTO::getCantidad).sum();
    }
}