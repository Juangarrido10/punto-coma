
![image](./assets/image.png)







# Papelería “Punto & Coma”

En Punto & Coma, estudiantes e independientes hacen encargos de impresión y anillado para recoger el mismo día. El sistema registra nombre y teléfono del cliente y permite crear un pedido con ítems de este catálogo: Impresión B/N ($200 c/u, o $150 c/u si son 100 o más), Impresión Color ($500 c/u, o $400 c/u si son 50 o más), y Anillado ($3.000 c/u, sin precio por volumen). El cálculo funciona así: primero determinar subtotales por ítem aplicando precio por volumen cuando corresponda; luego sumar el total bruto; después aplicar un solo beneficio: 
-si el pedido incluye al menos un anillado y la suma de impresiones (B/N + Color) es ≥ 30, se aplica 10% de descuento; de lo contrario, si el total bruto > $40.000, aplicar 5% de descuento; si ninguna condición se cumple, no hay descuento. No se aceptan cantidades ≤ 0. Tras confirmar el pedido, queda bloqueado y el sistema debe mostrar un resumen con detalle (precio aplicado por ítem), total bruto, descuento y total final. 
No se gestiona inventario ni pagos: solo el flujo de crear → calcular → confirmar → resumir.


analisis del ejercicio
1- Requerimientos funcionales 

RF1. Registrar cliente (nombre, teléfono).
RF2. Crear pedido en estado EN_CREACION.
RF3. Agregar items al pedido.
RF4 Calcular total bruto.
RF5. Aplicar descuentos
RF6. Confirmar pedido.  cambia a estado CONFIRMADO y ya no puede editarse.
RF8. Validar que las cantidades sean mayores a 0.
RF8. Mostrar resumen :Cliente (nombre y teléfono);Ítems con precios aplicados;Subtotal por ítem;Total bruto;Descuento aplicado;Total final


2-Reglas del Negocio

Impresión B/N = Precio Normal $200 -  Precio Volumen	$150	Desde 100 unidades
Impresión Color = Precio Normal $500 -  Precio Volumen $400	Desde 50 unidades
Anillado	$3.000	—	No aplica 

Descuentos:
10% si el pedido tiene >= 1 anillado y el total de impresiones (B/N + Color) >= 30.
5% si el total bruto > $40.000 (solo si no aplica el 10%).
Sin descuento si no se cumple ninguno.

Estados del pedido:

EN_CREACION → puede agregar ítems.
CONFIRMADO → no editable.

Validaciones:
Cantidades > 0
Totales no negativos
Pedido confirmado no se puede modificar

Restricciones:
No hay inventario, pagos ni base de datos.
Consola o línea de comandos simple.


3-Criterios de Aceptación 

CA1. Precio por volumen (B/N):
Dado una impresión B/N x100,
Cuando calculo el subtotal,
Entonces uso $150 c/u (no $200).

CA2. Precio por volumen (Color):
Dado una impresión Color x50,
Cuando calculo el subtotal,
Entonces uso $400 c/u (no $500).

CA3. Descuento 10% (Anillado + impresiones ≥30):
Dado un pedido con 1 anillado y 30 impresiones B/N,
Cuando calculo total final,
Entonces se aplica 10% de descuento.

CA4. Descuento 5% (total bruto > 40.000 sin cumplir 10%):
Dado un pedido sin anillado de total bruto 42.000,
Cuando calculo total final,
Entonces se aplica 5% de descuento.

CA5. Validación de cantidad:
Dado cantidad 0 o negativa,
Cuando intento agregar ítem,
Entonces el sistema rechaza la operación.

CA6. Bloqueo de edición:
Dado un pedido CONFIRMADO,
Cuando intento agregar o modificar ítems,
Entonces se rechaza la acción.

CA7. Resumen:
Dado un pedido válido,
Cuando pido el resumen,
Entonces muestra cliente, ítems con precio aplicado, subtotal, descuento y total final.



 Diseño 

 ![image](./assets/image.png)
 descripción: imagen del UML con sus respectivas relaciones
 
1-Cliente
Atributos: nombre, telefono.

2-Producto (enum)
Define los tres productos del catálogo con precios normales y por volumen.

3-ItemPedido
Atributos: producto, cantidad.
Método: subtotal() → determina precio aplicado (normal o por volumen).

4-Pedido
Atributos: cliente, items, estado.
Métodos:
agregar_item() (valida estado y cantidad > 0)
calcular_total_bruto()
calcular_descuento() (aplica 10% o 5%)
calcular_total_final()
confirmar()
resumen()

5-PapeleriaService
Coordina los casos de uso: crear pedido, agregar ítems, confirmar, mostrar resumen.



-----------------------------
5-Flujo de Consola
Ingresar datos del cliente (nombre y teléfono).
Crear pedido nuevo (estado EN_CREACION).
Agregar ítems seleccionando producto y cantidad.
Mostrar detalle del pedido con precios aplicados (normal o volumen) y total bruto.
Calcular descuento aplicable (10%, 5% o ninguno).
Mostrar resumen completo: subtotal, descuento, total final.
Confirmar pedido → cambia a CONFIRMADO.


modulacion del proyecto
Intentar editar pedido confirmado → debe fallar.
/punto-coma/
├─ domain/
│  ├─ Cliente.py
│  ├─ Producto.py
│  ├─ ItemPedido.py
│  ├─ Pedido.py
│  └─ EstadoPedido.py
├─ service/
│  └─ PapeleriaService.py
└─ app/
   └─ Main.py   # Flujo principal por consola
