
import modelo.DetallePedido;
import servicio.ClienteService;
import servicio.DetallePedidoService;
import servicio.FacturaService;
import servicio.PedidoService;
import servicio.ProductoService;

/**
 * CLASE PRINCIPAL (MAIN)
 * Aquí es donde se desarrolla todo el sistema de ventas.
 * Se simula el proceso desde que llega un cliente hasta que se cancela un pedido.
 */

public class Main {

    public static void main(String[] args) {

        System.out.println("===== INICIANDO SISTEMA =====");

        // SERVICIOS
        // Se crean los objetos que tienen la lógica para hablar con la base de datos
        ClienteService clienteService = new ClienteService();
        ProductoService productoService = new ProductoService();
        PedidoService pedidoService = new PedidoService();
        DetallePedidoService detalleService = new DetallePedidoService();
        FacturaService facturaService = new FacturaService();


        // =========================
        // 1️⃣ CREAR CLIENTE
        // =========================
        // Registrar un nuevo cliente y guardar su ID para usarlo después

        int idCliente = clienteService.agregarCliente(
                "Juan", 
                "Perez",
                "1233456789",
                "calle 1 #1-01",
                "3001234567",
                "juan@email.com"
        );

        if (idCliente == -1) {
            System.out.println("❌ Error: No se pudo crear el cliente. Limpia la DB y reintenta.");
            return; // freno de seguridad si algo sale mal
        }


        // =========================
        // 2️⃣ CREAR PRODUCTOS
        // =========================
        // Agregar productos al catálogo inicial con su nombre, categoría, precio y stock
        int idProducto1 = productoService.agregarProducto("Hamburguesa", "Comida Rápida", 12000, 10);
        int idProducto2 = productoService.agregarProducto("Pizza", "Italiana", 20000, 5);

        // =========================
        //  ACTUALIZAR PRODUCTOS
        // =========================
        // Actualizar la pizza (idProducto2) de 20000 a 25000 pesos
        productoService.actualizarProducto(idProducto2, "Pizza Familiar", 25000.0, 10);


        // =========================
        // 3️⃣ CREAR PEDIDO
        // =========================
        // Abrir un nuevo pedido vinculándolo al ID del cliente creado arriba
        int pedidoId = pedidoService.crearPedido(idCliente);


        // =========================
        // 4️⃣ AGREGAR DETALLES
        // =========================
        // El cliente elige sus productos: 2 Hamburguesas y 1 Pizza
        // El sistema validará el stock y calculará subtotales automáticamente
        detalleService.agregarDetalle(pedidoId, idProducto1, 2); 
        detalleService.agregarDetalle(pedidoId, idProducto2, 1);


        // =========================
        // 5️⃣ LISTAR DETALLES
        // =========================
        // Consultar la base de datos para mostrar qué productos tiene el pedido actualmente
        System.out.println("----- DETALLES -----");

        for(DetallePedido d : detalleService.listarDetalles()){
            System.out.println(
                    "ID:"+d.getId()+
                    " Pedido:"+d.getPedidoId()+
                    " Producto:"+d.getnombreProducto() +
                    " Cantidad:"+d.getCantidad()+
                    " Subtotal:"+d.getSubtotal()
            );
        }

        // =========================
        //  ELIMINAR PRODUCTOS (parcial)
        // =========================
        // Se simula que el cliente se arrepiente y decide quitar la Pizza del carrito
        detalleService.eliminarDetalle(pedidoId, idProducto2);


        // =========================
        // 6️⃣ GENERAR FACTURA
        // =========================
        // Se calcula el total final del pedido (solo sumará lo que no fue eliminado)
        facturaService.generarFactura(pedidoId);


        // --- PRUEBA DE ELIMINACIÓN TOTAL DEL PEDIDO ---
        // Prueba final: se borra el pedido completo y sus detalles de forma segura
        System.out.println("\n--- 🧨 Intentando borrar el pedido completo... ---");
        pedidoService.eliminarPedido(pedidoId);


        System.out.println("===== FIN DEL SISTEMA =====");
    }
}

