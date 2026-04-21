package service;

import dao.UsuarioDao;
import model.Usuario;

// Clase que implementa la lógica de negocio relacionada con usuarios.
// Se encarga de validar los datos introducidos y delegar las operaciones al DAO.
public class UsuarioService {

    // Mé_todo que crea un nuevo usuario en la base de datos
    // Incluye validaciones de formato y duplicidad
    public void crearUsuario(String dni, String nombre, String apellido, String email, String password) {

        // Validar que el DNI no esté vacío
        if (dni.isEmpty()) {
            System.out.println("El DNI no puede estar vacío.");
            return;
        }

        // Validar formato del DNI (8 números + letra)
        if (!dni.matches("^[0-9]{8}[a-zA-Z]$")) {
            System.out.println("Formato de DNI no válido.");
            return;
        }

        // Comprobar que no exista ya un usuario con ese DNI
        if (UsuarioDao.buscarUsuarioPorDni(dni) != null) {
            System.out.println("Ya existe un usuario con ese DNI.");
            return;
        }

        // Validar nombre (solo letras y espacios)
        if (nombre.isEmpty() || !nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            System.out.println("Nombre no válido.");
            return;
        }

        // Validar apellido (solo letras y espacios)
        if (apellido.isEmpty() || !apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            System.out.println("Apellido no válido.");
            return;
        }

        // Validar formato de email
        if (email.isEmpty() || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            System.out.println("Email no válido.");
            return;
        }

        // Validar contraseña (mínimo 6 caracteres y al menos un número)
        if (password.length() < 6 || !password.matches(".*\\d.*")) {
            System.out.println("Password no válida.");
            return;
        }

        // Crear objeto Usuario con los datos validados
        Usuario usuario = new Usuario(dni, nombre, apellido, email, password);

        // Insertar el usuario en la base de datos
        UsuarioDao.insertarUsuario(usuario);

        // Mostrar mensaje de confirmación
        System.out.println("Usuario creado correctamente.");
    }

    // Mé_todo que actualiza los datos de un usuario existente
    // Se identifica al usuario mediante su DNI
    public void actualizarUsuario(String dni, String nombre, String apellido, String email, String password) {

        // Buscar usuario en la base de datos
        Usuario usuario = UsuarioDao.buscarUsuarioPorDni(dni);

        // Si no existe, cancelar operación
        if (usuario == null) {
            System.out.println("No se encontró ningún usuario con ese DNI.");
            return;
        }

        // Actualizar los campos del usuario
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setPassword(password);

        // Guardar cambios en la base de datos
        UsuarioDao.actualizarUsuario(usuario);
    }

    // Mé_todo que elimina un usuario a partir de su DNI
    public void eliminarUsuario(String dni) {

        // Buscar usuario en la base de datos
        Usuario usuario = UsuarioDao.buscarUsuarioPorDni(dni);

        // Si no existe, cancelar operación
        if (usuario == null) {
            System.out.println("No se encontró ningún usuario con ese DNI.");
            return;
        }

        // Eliminar usuario utilizando su ID
        UsuarioDao.eliminarUsuario(usuario.getId());
    }
}