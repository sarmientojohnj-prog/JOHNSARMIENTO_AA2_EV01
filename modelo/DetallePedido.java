package modelo;
/**
 * CLASE ENTIDAD: DetallePedido
 * Esta clase sirve para  representar a los detalles de cada pedido creado en Java.
 * Cada variable corresponde a una columna de la tabla 'Detalle_pedido' en MySQL.
 */

public class DetallePedido {
    private int id;
    private int pedidoId;
    private int productoId;
    private String nombreProducto;
    private int cantidad;
    private double subtotal;

    // Getters y Setters
    // Sirven para que otras clases puedan leer (get) o modificar (set) los datos de forma segura.
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getPedidoId() { return pedidoId; }
    public void setPedidoId(int pedidoId) { this.pedidoId = pedidoId; }
    
    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }
    
    public String getnombreProducto() { return nombreProducto; }
    public void setnombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
