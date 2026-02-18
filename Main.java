public class Main {

    public static void main(String[] args) {

        System.out.println("===== INICIANDO SISTEMA =====");

        // SERVICIOS
        ClienteService clienteService = new ClienteService();
        ProductoService productoService = new ProductoService();
        PedidoService pedidoService = new PedidoService();
        DetallePedidoService detalleService = new DetallePedidoService();
        FacturaService facturaService = new FacturaService();


        // =========================
        // 1️⃣ CREAR CLIENTE
        // =========================
        clienteService.agregarCliente(
                "Juan Perez",
                "juan@email.com",
                "3001234567"
        );


        // =========================
        // 2️⃣ CREAR PRODUCTOS
        // =========================
        productoService.agregarProducto("Hamburguesa", 12000, 10);
        productoService.agregarProducto("Pizza", 20000, 5);


        // =========================
        // 3️⃣ CREAR PEDIDO
        // =========================
        int pedidoId = pedidoService.crearPedido(1);


        // =========================
        // 4️⃣ AGREGAR DETALLES
        // =========================
        detalleService.agregarDetalle(pedidoId,1,2);
        detalleService.agregarDetalle(pedidoId,2,1);


        // =========================
        // 5️⃣ LISTAR DETALLES
        // =========================
        System.out.println("----- DETALLES -----");

        for(DetallePedido d : detalleService.listarDetalles()){
            System.out.println(
                    "ID:"+d.getId()+
                    " Pedido:"+d.getPedidoId()+
                    " Producto:"+d.getProductoId()+
                    " Cantidad:"+d.getCantidad()+
                    " Subtotal:"+d.getSubtotal()
            );
        }


        // =========================
        // 6️⃣ GENERAR FACTURA
        // =========================
        facturaService.generarFactura(pedidoId);


        System.out.println("===== FIN DEL SISTEMA =====");
    }
}

