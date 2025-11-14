package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pedido {

    private final Cliente cliente;
    private final List<ItemPedido> items = new ArrayList<>();
    private EstadoPedido estado = EstadoPedido.EN_CREACION;

    public Pedido(Cliente cliente) {
        this.cliente = Objects.requireNonNull(cliente, "cliente requerido");
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemPedido> getItems() {
        return items;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void agregarItem(Producto p, int cant) {
        if (estado == EstadoPedido.CONFIRMADO) {
            throw new IllegalStateException("No se puede editar un pedido CONFIRMADO");
        }
        items.add(new ItemPedido(p, cant));
    }

    public void confirmar() {
        if (items.isEmpty()) {
            throw new IllegalStateException("No se puede confirmar un pedido vacío");
        }
        estado = EstadoPedido.CONFIRMADO;
    }

    public int totalBruto() {
        return items.stream().mapToInt(ItemPedido::subtotal).sum();
    }

    // Regla: Descuento por anillado + 30 impresiones (10%)  
    private boolean aplicaDescuentoEspecial() {
        boolean tieneAnillado = items.stream().anyMatch(i -> i.getProducto() == Producto.ANILLADO);
        int totalImpresiones =
                items.stream()
                        .filter(i -> i.getProducto() == Producto.IMPRESION_BN
                                  || i.getProducto() == Producto.IMPRESION_COLOR)
                        .mapToInt(ItemPedido::getCantidad)
                        .sum();
        return tieneAnillado && totalImpresiones >= 30;
    }

    public int descuento() {
        int bruto = totalBruto();

        if (aplicaDescuentoEspecial()) {
            return (int) Math.round(bruto * 0.10);
        }

        if (bruto > 40000) {
            return (int) Math.round(bruto * 0.05);
        }

        return 0;
    }

    public int totalFinal() {
        int total = totalBruto() - descuento();
        if (total < 0)
            throw new IllegalStateException("El total no puede ser negativo");
        return total;
    }
}
