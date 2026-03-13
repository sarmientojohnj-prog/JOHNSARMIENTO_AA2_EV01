package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import servicio.ClienteService;

// Esta etiqueta mapea el Servlet con el action del formulario en el index.jsp
@WebServlet("/RegistroServlet")
public class RegistroServlet extends HttpServlet {
    
    private ClienteService clienteService = new ClienteService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Recuperar los datos del formulario HTML
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String identificacion = request.getParameter("identificacion");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");
       
        // 2. Usar el servicio que ya se tenia para guardar en MySQL
        int idGenerado = clienteService.agregarCliente(nombre, apellidos, identificacion, direccion, telefono, email);

        // 4. Responder al navegador
        response.setContentType("text/html;charset=UTF-8");
        if (idGenerado > 0) {
        response.sendRedirect("index.jsp?exito=true");
        } else {
        response.sendRedirect("index.jsp?error=true");
        }
    }
}