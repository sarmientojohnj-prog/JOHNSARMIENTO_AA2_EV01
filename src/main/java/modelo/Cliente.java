package modelo;
/**
 * CLASE ENTIDAD: Cliente
 * Esta clase sirve para  representar a los clientes en Java.
 * Cada variable corresponde a una columna de la tabla 'clientes' en MySQL.
 */
public class Cliente {
    private int id;
    private String nombre;
    private String apellidos;
    private String identificacion;
    private String direccion;
    private String telefono;
    private String email;
    
    // Getters y Setters
    // Sirven para que otras clases puedan leer (get) o modificar (set) los datos de forma segura.
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    
    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
        
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
        
}
