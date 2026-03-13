<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Aromas Duo - Registro de Cliente</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; background-color: #f4f4f9; }
        .container { display: flex; flex-direction: column; align-items: center; }
        
        /* Estilo del Formulario */
        form { width: 400px; padding: 20px; border: 1px solid #ccc; border-radius: 10px; background: white; margin-bottom: 30px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
        label { font-weight: bold; display: block; margin-top: 10px; }
        input { width: 95%; margin-bottom: 5px; padding: 8px; border-radius: 5px; border: 1px solid #ddd; }
        button { width: 100%; padding: 12px; background-color: #28a745; color: white; border: none; cursor: pointer; font-size: 16px; margin-top: 15px; border-radius: 5px; }
        button:hover { background-color: #218838; }
       
    </style>
</head>
<body>
    <div class="container">
        <h1>Bienvenidos a Aromas.Duo!</h1>

        <h2>Registrar Nuevo Cliente</h2>

        <% 
    String status = request.getParameter("exito");
    if ("true".equals(status)) { 
%>
    <div style="color: white; background-color: #28a745; padding: 10px; border-radius: 5px; margin-bottom: 15px; text-align: center;">
        ¡Cliente registrado con éxito en Aromas.Duo!
    </div>
<% 
    } else if ("error".equals(status)) { 
%>
    <div style="color: white; background-color: #dc3545; padding: 10px; border-radius: 5px; margin-bottom: 15px; text-align: center;">
        Error al registrar el cliente. Intente de nuevo.
    </div>
<% 
    } 
%>
        
        <form action="RegistroServlet" method="POST">
            <label>Nombre:</label>
            <input type="text" name="nombre" required>
            
            <label>Apellidos:</label>
            <input type="text" name="apellidos" required>
            
            <label>Identificación:</label>
            <input type="text" name="identificacion" required>
            
            <label>Dirección:</label>
            <input type="text" name="direccion" required>
            
            <label>Teléfono:</label>
            <input type="text" name="telefono" required>
            
            <label>Correo Electrónico:</label>
            <input type="email" name="email" required>
            
            <button type="submit">Guardar</button>
        </form>

                
    </div>
</body>
</html>