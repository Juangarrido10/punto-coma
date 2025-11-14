package domain;

public enum Producto {
    IMPRESION_BN(200, 150, 100),
    IMPRESION_COLOR(500, 400, 50),
    ANILLADO(3000, 3000, Integer.MAX_VALUE); // No tiene volumen

    private final int precio;
    private final int precioVolumen;
    private final int umbralVolumen;

    Producto(int precio, int precioVolumen, int umbralVolumen) {
        this.precio = precio;
        this.precioVolumen = precioVolumen;
        this.umbralVolumen = umbralVolumen;
    }

    public int getPrecio() {
        return precio;
    }

    public int getPrecioVolumen() {
        return precioVolumen;
    }

    public int getUmbralVolumen() {
        return umbralVolumen;
    }
}