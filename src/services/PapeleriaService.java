package services;

import domain.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PapeleriaServiceTest {

    private final PapeleriaService service = new PapeleriaService();

    // ----------------------------------------------------------------------
    // CA1 – Precio por volumen B/N (>=100 usa $150)
    // ----------------------------------------------------------------------
    @Test
    void CA1_precioVolumenBN() {
        Pedido p = service.crearPedido(new Cliente("Test", "123"));
        service.agregarItem(p, Producto.IMPRESION_BN, 100);

        ItemPedido item = p.getItems().get(0);

        assertEquals(150, item.precioUnitarioAplicado());
        assertEquals(150 * 100, item.subtotal());
    }

    // ----------------------------------------------------------------------
    // CA2 – Precio por volumen Color (>=50 usa $400)
    // ----------------------------------------------------------------------
    @Test
    void CA2_precioVolumenColor() {
        Pedido p = service.crearPedido(new Cliente("Test", "123"));
        service.agregarItem(p, Producto.IMPRESION_COLOR, 50);

        ItemPedido item = p.getItems().get(0);

        assertEquals(400, item.precioUnitarioAplicado());
        assertEquals(400 * 50, item.subtotal());
    }

    // ----------------------------------------------------------------------
    // CA3 – Descuento 10% por anillado + impresiones >= 30
    // ----------------------------------------------------------------------
    @Test
    void CA3_descuentoEspecial10porciento() {
        Pedido p = service.crearPedido(new Cliente("Test", "123"));
        service.agregarItem(p, Producto.ANILLADO, 1);
        service.agregarItem(p, Producto.IMPRESION_BN, 30); // 30 impresiones

        int bruto = p.totalBruto();     // 3000 + (30 × 200) = 9000
        int descuento = p.descuento();  // 10% = 900
        int total = p.totalFinal();     // 8100

        assertEquals(9000, bruto);
        assertEquals(900, descuento);
        assertEquals(8100, total);
    }

    // ----------------------------------------------------------------------
    // CA4 – Descuento 5% (bruto > 40000 y no aplica el de 10%)
    // ----------------------------------------------------------------------
    @Test
    void CA4_descuento5porciento() {
        Pedido p = service.crearPedido(new Cliente("Test", "123"));
        // 100 impresiones color → volumen → 100 × 400 = 40.000
        // + 5 impresiones B/N (5×200=1000)
        service.agregarItem(p, Producto.IMPRESION_COLOR, 100);
        service.agregarItem(p, Producto.IMPRESION_BN, 5);

        int bruto = p.totalBruto();     // 41.000
        int descuento = p.descuento();  // 5% = 2050
        int total = p.totalFinal();     // 38.950

        assertEquals(41000, bruto);
        assertEquals(2050, descuento);
        assertEquals(38950, total);
    }

    // ----------------------------------------------------------------------
    // CA5 – Validación de cantidad no permite 0 o negativo
    // ----------------------------------------------------------------------
    @Test
    void CA5_validacionCantidad() {
        Pedido p = service.crearPedido(new Cliente("Test", "123"));

        assertThrows(IllegalArgumentException.class,
                () -> service.agregarItem(p, Producto.IMPRESION_BN, 0));

        assertThrows(IllegalArgumentException.class,
                () -> service.agregarItem(p, Producto.IMPRESION_BN, -5));
    }

    // ----------------------------------------------------------------------
    // CA6 – Bloqueo de edición después de confirmar
    // ----------------------------------------------------------------------
    @Test
    void CA6_bloqueoEdicionDespuesConfirmado() {
        Pedido p = service.crearPedido(new Cliente("Test", "123"));
        service.agregarItem(p, Producto.IMPRESION_BN, 10);
        service.confirmar(p);

        assertEquals(EstadoPedido.CONFIRMADO, p.getEstado());

        // Intentar agregar item luego de confirmado
        assertThrows(IllegalStateException.class,
                () -> service.agregarItem(p, Producto.ANILLADO, 1));
    }

    // ----------------------------------------------------------------------
    // CA7 – Resumen contiene todos los datos
    // ----------------------------------------------------------------------
    @Test
    void CA7_resumenCompleto() {
        Pedido p = service.crearPedido(new Cliente("Ana", "555"));
        service.agregarItem(p, Producto.IMPRESION_BN, 10);
        service.agregarItem(p, Producto.ANILLADO, 1);

        String resumen = service.resumen(p);

        assertTrue(resumen.contains("RESUMEN DEL PEDIDO"));
        assertTrue(resumen.contains("Ana"));
        assertTrue(resumen.contains("IMPRESION_BN"));
        assertTrue(resumen.contains("ANILLADO"));
        assertTrue(resumen.contains("subtotal"));
        assertTrue(resumen.contains("Total bruto"));
        assertTrue(resumen.contains("Descuento"));
        assertTrue(resumen.contains("Total final"));
    }
}
