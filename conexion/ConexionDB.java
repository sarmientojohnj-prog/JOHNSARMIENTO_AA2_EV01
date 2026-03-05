package conexion;
import java.sql.Connection; // crea el puente de comunicación entre Java y MySQL.
import java.sql.DriverManager;      // Es el administrador que gestiona las diferentes conexiones.
import java.sql.SQLException;       // Es el traductor de errores de SQL; si algo falla en la base de datos, el avisa.


/**
 * CLASE DE CONEXIÓN
 * Esta clase es la encargada de abrir la puerta hacia nuestra base de datos 'aromas_duo'.
 */
public class ConexionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/aromas_duo";
    private static final String USER = "root";
    private static final String PASS = "#sArA#72727#";

    
    // MÉTODO PARA PEDIR UNA CONEXIÓN
    public static Connection getConnection() {
        
        try {                                                               // busca la comunicacion con el Driver, permite que java se somunique con la base de datos
            Class.forName("com.mysql.cj.jdbc.Driver");

            // el administrador usa la URL, el usuario y la clave para establecer la comunicacion
            return DriverManager.getConnection(URL, USER, PASS);
            
            // prevencion si no se incluye la librería (el archivo .jar) de MySQL.
        } catch (ClassNotFoundException e) {
            System.out.println("❌ No se encontró el driver JDBC de MySQL");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            // mensaje de error sale si la clave está mal, la base de datos no existe o el servidor está apagado.
            System.out.println("❌ Error de SQL: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
