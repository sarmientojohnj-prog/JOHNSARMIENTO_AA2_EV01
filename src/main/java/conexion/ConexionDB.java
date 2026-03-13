package conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * CLASE DE CONEXIÓN MODIFICADA
 */
public class ConexionDB {

    // 1. VERIFICA: ¿Tu base de datos en PHPMyAdmin se llama exactamente 'aromas_duo'?
    private static final String URL = "jdbc:mysql://localhost:3306/aromas_duo";
    private static final String USER = "root";
    
    // 2. CAMBIO: Dejamos esto vacío si usas XAMPP estándar
    private static final String PASS = ""; 

    public static Connection getConnection() {
        try {
            // Intentamos cargar el driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Intentamos establecer la conexión
            Connection conexion = DriverManager.getConnection(URL, USER, PASS);
            
            if (conexion != null) {
                System.out.println("✅ CONEXIÓN EXITOSA A LA BASE DE DATOS");
            }
            return conexion;
            
        } catch (ClassNotFoundException e) {
            System.out.println("❌ ERROR: No se encontró el conector (archivo .jar)");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            // 3. ESTO ES LO MÁS IMPORTANTE: Nos dirá si es la clave o el nombre de la BD
            System.out.println("❌ ERROR DE SQL (DETALLE): " + e.getMessage());
            System.out.println("Causa del error: " + e.getSQLState());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.out.println("❌ ERROR INESPERADO: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}