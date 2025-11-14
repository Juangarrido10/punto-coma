package services;

import domain.Cliente;
import domain.Pedido;
import domain.Producto;

public class PapeleriaService {

    public Pedido crearPedido(Cliente cliente) {
        return new Pedido(cliente);
    }

    public void agregarItem(Pedido p, Producto prod, int cant) {
        p.agregarItem(prod, cant);
    }

    public void confirmar(Pedido p) {
        p.confirmar();
    }

    public String resumen(Pedido p) {
        StringBuilder sb = new StringBuilder();

        sb.append("=== RESUMEN DEL PEDIDO ===\n");
        sb.append("Cliente: ").append(p.getCliente().nombre())
                .append(" | Tel: ").append(p.getCliente().telefono()).append("\n");
        sb.append("Estado: ").append(p.getEstado()).append("\n");
        sb.append("---------------------------------------\n");

        for (var it : p.getItems()) {
            sb.append(String.format(
                    "%-18s x%-4d unit=%,6d subtotal=%,8d\n",
                    it.getProducto(),
                    it.getCantidad(),
                    it.precioUnitarioAplicado(),
                    it.subtotal()
            ));
        }

        sb.append("---------------------------------------\n");
        int bruto = p.totalBruto();
        int descuento = p.descuento();
        int total = p.totalFinal();

        sb.append(String.format("Total bruto: %,d\n", bruto));
        sb.append(String.format("Descuento:  %,d\n", descuento));
        sb.append(String.format("Total final: %,d\n", total));

        return sb.toString();
    }
}