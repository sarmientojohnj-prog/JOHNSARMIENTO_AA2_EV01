package modelo;
/**
 * CLASE ENTIDAD: Factura
 * Esta clase sirve para  representar las facturas en Java.
 * Cada variable corresponde a una columna de la tabla 'facturas' en MySQL.
 */

public class Factura {
    private int id;
    private int pedidoId;
    private double total;

    // Getters y Setters
     // Sirven para que otras clases puedan leer (get) o modificar (set) los datos de forma segura.
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPedidoId() { return pedidoId; }
    public void setPedidoId(int pedidoId) { this.pedidoId = pedidoId; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
