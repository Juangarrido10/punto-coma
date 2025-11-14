import domain.Cliente;
import domain.Producto;
import services.PapeleriaService;

public class Main {
    public static void main(String[] args) {

         var service = new PapeleriaService();
        var cliente = new Cliente("Karen", "3001234567");

        // -------------------------------
        // Crear pedido
        // -------------------------------
        var pedido = service.crearPedido(cliente);

        // -------------------------------
        // PASO 1: Precio por volumen B/N
        // -------------------------------
        System.out.println("=== PRUEBA 1: Precio volumen B/N ===");
        service.agregarItem(pedido, Producto.IMPRESION_BN, 120); // >100 → debe usar $150
        System.out.println(service.resumen(pedido));
        pedido.getItems().clear(); // Limpiar para siguiente prueba

        // -------------------------------
        // PASO 2: Precio por volumen Color
        // -------------------------------
        System.out.println("\n=== PRUEBA 2: Precio volumen Color ===");
        service.agregarItem(pedido, Producto.IMPRESION_COLOR, 50); // =50 → debe usar $400
        System.out.println(service.resumen(pedido));
        pedido.getItems().clear();

        // -------------------------------
        // PASO 3: Descuento 10% (anillado + impresiones ≥30)
        // -------------------------------
        System.out.println("\n=== PRUEBA 3: Descuento 10% ===");
        service.agregarItem(pedido, Producto.IMPRESION_BN, 30);
        service.agregarItem(pedido, Producto.ANILLADO, 1);
        System.out.println(service.resumen(pedido));
        pedido.getItems().clear();

        // -------------------------------
        // PASO 4: Descuento 5% (total bruto > 40.000 sin anillado)
        // -------------------------------
        System.out.println("\n=== PRUEBA 4: Descuento 5% ===");
        service.agregarItem(pedido, Producto.IMPRESION_COLOR, 100); // 100x500 = 50.000
        System.out.println(service.resumen(pedido));
        pedido.getItems().clear();

        // -------------------------------
        // PASO 5: Cantidad inválida (0 o negativa)
        // -------------------------------
        System.out.println("\n=== PRUEBA 5: Cantidad inválida ===");
        try {
            service.agregarItem(pedido, Producto.ANILLADO, 0);
        } catch (IllegalArgumentException ex) {
            System.out.println("Error esperado: " + ex.getMessage());
        }

        try {
            service.agregarItem(pedido, Producto.IMPRESION_BN, -5);
        } catch (IllegalArgumentException ex) {
            System.out.println("Error esperado: " + ex.getMessage());
        }

        // -------------------------------
        // PASO 6: Bloqueo edición pedido confirmado
        // -------------------------------
        System.out.println("\n=== PRUEBA 6: Bloqueo edición pedido confirmado ===");
        service.agregarItem(pedido, Producto.IMPRESION_BN, 10);
        service.confirmar(pedido);
        System.out.println("Pedido confirmado: " + pedido.getEstado());

        try {
            service.agregarItem(pedido, Producto.ANILLADO, 1);
        } catch (IllegalStateException ex) {
            System.out.println("Error esperado al editar confirmado: " + ex.getMessage());
        }
    }
}