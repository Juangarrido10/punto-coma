package domain;

public record Cliente(String nombre, String telefono) {
    public Cliente {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        }
        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("El teléfono del cliente es obligatorio");
        }
    }
}
