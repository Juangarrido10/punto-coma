import domain.Cliente;
import domain.Producto;
import services.PapeleriaService;

public class Main {
    public static void main(String[] args) {

        var service = new PapeleriaService();
        var cliente = new Cliente("Juan ", "3001234567");

        var pedido = service.crearPedido(cliente);

        service.agregarItem(pedido, Producto.IMPRESION_BN, 120);       // precio volumen
        service.agregarItem(pedido, Producto.IMPRESION_COLOR, 20);
        service.agregarItem(pedido, Producto.ANILLADO, 1);

        System.out.println(service.resumen(pedido));

        service.confirmar(pedido);
        System.out.println("\nPedido confirmado: " + pedido.getEstado());
    }
}
