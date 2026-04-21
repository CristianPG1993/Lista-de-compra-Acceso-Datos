package menu;

import model.ListaCompra;
import model.Producto;
import service.ListaCompraService;
import service.ProductoService;
import service.UsuarioService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Menu {

    // Scanner global para leer datos introducidos por el usuario por consola
    private final Scanner scanner = new Scanner(System.in);

    // Servicio encargado de la lógica de negocio relacionada con usuarios
    private final UsuarioService usuarioService = new UsuarioService();

    // Servicio encargado de la lógica de negocio relacionada con productos
    private final ProductoService productoService = new ProductoService();

    // Servicio encargado de la lógica de negocio relacionada con listas de compra
    private final ListaCompraService listaCompraService = new ListaCompraService();

    // Mé_todo que inicia la ejecución del menú principal
    // Se llama desde la clase Main al arrancar la aplicación
    public void iniciar() {
        menuPrincipal();
    }

    // Mé_todo que muestra el menú principal de la aplicación
    // Desde aquí el usuario puede acceder a los distintos submenús
    private void menuPrincipal() {
        int opcion;

        // El menú se repite hasta que el usuario selecciona la opción de salir
        do {
            System.out.println("\n===== MENÚ PRINCIPAL =====");
            System.out.println("1. Usuarios");
            System.out.println("2. Listas de compra");
            System.out.println("3. Productos");
            System.out.println("4. Añadir producto a lista");
            System.out.println("5. Mostrar lista de la compra");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");

            // Se lee la opción elegida validando que sea un número entero
            opcion = leerEntero();

            // Según la opción elegida, se llama al Mé_todo correspondiente
            switch (opcion) {
                case 1 -> menuUsuarios();
                case 2 -> menuListasCompra();
                case 3 -> menuProductos();
                case 4 -> anadirProductoALista();
                case 5 -> mostrarListaCompleta();
                case 6 -> System.out.println("Saliendo del programa...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 6);
    }

    // Submenú para gestionar operaciones relacionadas con usuarios
    // Permite crear, actualizar o eliminar usuarios
    private void menuUsuarios() {
        int opcion;

        do {
            System.out.println("\n===== MENÚ USUARIOS =====");
            System.out.println("1. Crear usuario");
            System.out.println("2. Actualizar usuario");
            System.out.println("3. Eliminar usuario");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            // Lectura segura de la opción elegida
            opcion = leerEntero();

            // Se ejecuta la acción correspondiente
            switch (opcion) {
                case 1 -> crearUsuario();
                case 2 -> actualizarUsuario();
                case 3 -> eliminarUsuario();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }

    // Submenú para gestionar operaciones relacionadas con listas de compra
    // Permite crear, actualizar, eliminar y mostrar listas ordenadas por precio
    private void menuListasCompra() {
        int opcion;

        do {
            System.out.println("\n===== MENÚ LISTAS DE COMPRA =====");
            System.out.println("1. Crear lista de compra");
            System.out.println("2. Actualizar lista de compra");
            System.out.println("3. Eliminar lista de compra");
            System.out.println("4. Mostrar lista ordenada por precio");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            // Lectura segura de la opción elegida
            opcion = leerEntero();

            // Se ejecuta la acción correspondiente
            switch (opcion) {
                case 1 -> crearListaCompra();
                case 2 -> actualizarListaCompra();
                case 3 -> eliminarListaCompra();
                case 4 -> mostrarListaOrdenadaPorPrecio();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }

    // Submenú para gestionar operaciones relacionadas con productos
    // Permite crear, actualizar y eliminar productos
    private void menuProductos() {
        int opcion;

        do {
            System.out.println("\n===== MENÚ PRODUCTOS =====");
            System.out.println("1. Crear producto");
            System.out.println("2. Actualizar producto");
            System.out.println("3. Eliminar producto");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            // Lectura segura de la opción elegida
            opcion = leerEntero();

            // Se ejecuta la acción correspondiente
            switch (opcion) {
                case 1 -> crearProducto();
                case 2 -> actualizarProducto();
                case 3 -> eliminarProducto();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }

    // Mé_todo auxiliar para leer un número entero desde consola
    // Si el usuario introduce un valor no numérico, se muestra un mensaje de error
    // y se vuelve a pedir el dato hasta que sea correcto
    private int leerEntero() {
        while (!scanner.hasNextInt()) {
            System.out.println("Error: debe introducir un número.");
            scanner.nextLine();
        }
        int numero = scanner.nextInt();
        scanner.nextLine(); // Limpieza del buffer
        return numero;
    }

    // Mé_todo auxiliar para leer un número decimal con precisión BigDecimal
    // Se utiliza principalmente para precios
    // Si el dato introducido no es válido, se vuelve a pedir
    private BigDecimal leerBigDecimal() {
        while (!scanner.hasNextBigDecimal()) {
            System.out.println("Error: debe introducir un número válido.");
            scanner.nextLine();
        }
        BigDecimal numero = scanner.nextBigDecimal();
        scanner.nextLine(); // Limpieza del buffer
        return numero;
    }

    // Mé_todo que recoge por consola los datos necesarios para crear un usuario
    // Después delega la lógica de validación e inserción en UsuarioService
    private void crearUsuario() {
        System.out.print("DNI: ");
        String dni = scanner.nextLine();

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        // Se delega la lógica al servicio
        usuarioService.crearUsuario(dni, nombre, apellido, email, password);
    }

    // Mé_todo que recoge por consola los nuevos datos de un usuario ya existente
    // Después llama al servicio para actualizarlo en la base de datos
    private void actualizarUsuario() {
        System.out.print("Introduce el DNI del usuario a actualizar: ");
        String dni = scanner.nextLine();

        System.out.print("Nuevo nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Nuevo apellido: ");
        String apellido = scanner.nextLine();

        System.out.print("Nuevo email: ");
        String email = scanner.nextLine();

        System.out.print("Nueva password: ");
        String password = scanner.nextLine();

        // Se delega la lógica al servicio
        usuarioService.actualizarUsuario(dni, nombre, apellido, email, password);
    }

    // Mé_todo que solicita el DNI del usuario a eliminar
    // Antes de llamar al servicio, pide confirmación al usuario
    private void eliminarUsuario() {
        System.out.print("Introduce el DNI del usuario a eliminar: ");
        String dni = scanner.nextLine();

        System.out.print("¿Seguro que desea eliminar el usuario? (s/n): ");
        String confirmacion = scanner.nextLine();

        // Si no confirma con "s", se cancela la operación
        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Operación cancelada.");
            return;
        }

        // Se delega la lógica al servicio
        usuarioService.eliminarUsuario(dni);
    }

    // Mé_todo que solicita los datos necesarios para crear una lista de compra
    // Después llama al servicio correspondiente
    private void crearListaCompra() {
        System.out.print("Introduce el DNI del usuario: ");
        String dni = scanner.nextLine();

        System.out.print("Nombre de la lista: ");
        String nombreLista = scanner.nextLine();

        // Se delega la lógica al servicio
        listaCompraService.crearListaCompra(dni, nombreLista);
    }

    // Mé_todo que permite actualizar el nombre de una lista de compra
    // Primero obtiene las listas del usuario y muestra sus opciones
    private void actualizarListaCompra() {
        System.out.print("Introduce el DNI del usuario: ");
        String dni = scanner.nextLine();

        // Obtener listas del usuario
        List<ListaCompra> listas = listaCompraService.obtenerListasPorDni(dni);

        // Si no hay listas, se finaliza el Mé_todo
        if (listas.isEmpty()) {
            return;
        }

        // Mostrar listas disponibles
        System.out.println("=== LISTAS DE COMPRA ===");
        for (int i = 0; i < listas.size(); i++) {
            System.out.println((i + 1) + ". " + listas.get(i).getNombreCompra());
        }

        // Pedir al usuario el número de la lista que desea actualizar
        System.out.print("Seleccione el número de la lista que desea actualizar: ");
        int opcion = leerEntero();

        // Solicitar el nuevo nombre
        System.out.print("Nuevo nombre de la lista: ");
        String nuevoNombre = scanner.nextLine();

        // Delegar la actualización al servicio
        listaCompraService.actualizarListaCompra(dni, opcion, nuevoNombre);
    }

    // Mé_todo que permite eliminar una lista de compra
    // Muestra primero las listas del usuario y después pide confirmación
    private void eliminarListaCompra() {
        System.out.print("Introduce el DNI del usuario: ");
        String dni = scanner.nextLine();

        // Obtener listas del usuario
        List<ListaCompra> listas = listaCompraService.obtenerListasPorDni(dni);

        // Si no hay listas, se finaliza el Mé_todo
        if (listas.isEmpty()) {
            return;
        }

        // Mostrar listas disponibles
        System.out.println("=== LISTAS DE COMPRA ===");
        for (int i = 0; i < listas.size(); i++) {
            System.out.println((i + 1) + ". " + listas.get(i).getNombreCompra());
        }

        // Pedir el número de la lista a eliminar
        System.out.print("Seleccione el número de la lista que desea eliminar: ");
        int opcion = leerEntero();

        // Solicitar confirmación
        System.out.print("¿Seguro que quieres eliminar esta lista? (s/n): ");
        String confirmacion = scanner.nextLine();

        // Si no confirma, se cancela la operación
        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Operación cancelada.");
            return;
        }

        // Delegar la eliminación al servicio
        listaCompraService.eliminarListaCompra(dni, opcion);
    }

    // Mé_todo que muestra una lista de compra ordenada por precio
    // El usuario selecciona qué lista desea visualizar
    private void mostrarListaOrdenadaPorPrecio() {
        System.out.print("Introduce el DNI del usuario: ");
        String dni = scanner.nextLine();

        // Obtener listas del usuario
        List<ListaCompra> listas = listaCompraService.obtenerListasPorDni(dni);

        // Si no hay listas, se termina el Mé_todo
        if (listas.isEmpty()) {
            return;
        }

        // Mostrar listas disponibles
        System.out.println("=== LISTAS DE COMPRA ===");
        for (int i = 0; i < listas.size(); i++) {
            System.out.println((i + 1) + ". " + listas.get(i).getNombreCompra());
        }

        // Selección de lista
        System.out.print("Seleccione el número de la lista: ");
        int opcion = leerEntero();

        // Delegar la visualización al servicio
        listaCompraService.mostrarListaOrdenadaPorPrecio(dni, opcion);
    }

    // Mé_todo que solicita por consola los datos de un nuevo producto
    // Después llama al servicio para su creación
    private void crearProducto() {
        System.out.print("Nombre del producto: ");
        String nombre = scanner.nextLine();

        System.out.print("Precio: ");
        BigDecimal precio = leerBigDecimal();

        System.out.print("Categoría: ");
        String categoria = scanner.nextLine();

        // Delegar la lógica al servicio
        productoService.crearProducto(nombre, precio, categoria);
    }

    // Mé_todo que permite actualizar un producto existente
    // Primero muestra todos los productos y después solicita los nuevos datos
    private void actualizarProducto() {
        List<Producto> productos = productoService.listarProductos();

        // Si no hay productos, se informa al usuario
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }

        // Mostrar productos disponibles
        System.out.println("=== PRODUCTOS ===");
        for (int i = 0; i < productos.size(); i++) {
            System.out.println((i + 1) + ". " + productos.get(i).getNombre()
                    + " - " + productos.get(i).getPrecio() + "€ - " + productos.get(i).getCategoria());
        }

        // Pedir el número del producto a actualizar
        System.out.print("Seleccione el número del producto que desea actualizar: ");
        int opcion = leerEntero();

        // Solicitar nuevos datos
        System.out.print("Nuevo nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Nuevo precio: ");
        BigDecimal precio = leerBigDecimal();

        System.out.print("Nueva categoría: ");
        String categoria = scanner.nextLine();

        // Delegar la actualización al servicio
        productoService.actualizarProducto(opcion, nombre, precio, categoria);
    }

    // Mé_todo que permite eliminar un producto existente
    // Primero muestra todos los productos y solicita confirmación
    private void eliminarProducto() {
        List<Producto> productos = productoService.listarProductos();

        // Si no hay productos, se informa al usuario
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }

        // Mostrar productos disponibles
        System.out.println("=== PRODUCTOS ===");
        for (int i = 0; i < productos.size(); i++) {
            System.out.println((i + 1) + ". " + productos.get(i).getNombre()
                    + " - " + productos.get(i).getPrecio() + "€ - " + productos.get(i).getCategoria());
        }

        // Pedir el número del producto a eliminar
        System.out.print("Seleccione el número del producto que desea eliminar: ");
        int opcion = leerEntero();

        // Solicitar confirmación
        System.out.print("¿Seguro que desea eliminar el producto? (s/n): ");
        String confirmacion = scanner.nextLine();

        // Si no confirma, se cancela la operación
        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Operación cancelada.");
            return;
        }

        // Delegar la eliminación al servicio
        productoService.eliminarProducto(opcion);
    }

    // Mé_todo que permite añadir uno o varios productos a una lista de compra
    // Primero se selecciona la lista y después uno o varios productos
    private void anadirProductoALista() {
        System.out.print("Introduce el DNI del usuario: ");
        String dni = scanner.nextLine();

        // Obtener listas del usuario
        List<ListaCompra> listas = listaCompraService.obtenerListasPorDni(dni);

        // Si no hay listas, se sale del Mé_todo
        if (listas.isEmpty()) {
            return;
        }

        // Mostrar listas disponibles
        System.out.println("=== LISTAS DE COMPRA DEL USUARIO ===");
        for (int i = 0; i < listas.size(); i++) {
            System.out.println((i + 1) + ". " + listas.get(i).getNombreCompra());
        }

        // Seleccionar la lista a la que se le añadirán productos
        System.out.print("Seleccione el número de la lista: ");
        int opcionLista = leerEntero();

        String continuar;

        // Bucle que permite añadir varios productos seguidos
        do {
            List<Producto> productos = listaCompraService.obtenerProductosDisponibles();

            // Si no hay productos disponibles, se informa y se sale
            if (productos.isEmpty()) {
                System.out.println("No hay productos disponibles.");
                return;
            }

            // Mostrar productos disponibles
            System.out.println("=== PRODUCTOS DISPONIBLES ===");
            for (int i = 0; i < productos.size(); i++) {
                System.out.println((i + 1) + ". " + productos.get(i).getNombre()
                        + " - " + productos.get(i).getPrecio() + "€");
            }

            // Seleccionar producto
            System.out.print("Seleccione el número del producto que desea añadir: ");
            int opcionProducto = leerEntero();

            // Introducir cantidad del producto
            System.out.print("Cantidad: ");
            int cantidad = leerEntero();

            // Delegar la operación al servicio
            listaCompraService.anadirProductoALista(dni, opcionLista, opcionProducto, cantidad);

            // Preguntar si desea seguir añadiendo productos
            System.out.print("¿Deseas añadir otro producto? (s/n): ");
            continuar = scanner.nextLine();

        } while (continuar.equalsIgnoreCase("s"));
    }

    // Mé_todo que muestra el contenido completo de una lista de compra
    // El usuario selecciona la lista a visualizar
    private void mostrarListaCompleta() {
        System.out.print("Introduce el DNI del usuario: ");
        String dni = scanner.nextLine();

        // Obtener listas del usuario
        List<ListaCompra> listas = listaCompraService.obtenerListasPorDni(dni);

        // Si no hay listas, se sale del Mé_todo
        if (listas.isEmpty()) {
            return;
        }

        // Mostrar listas disponibles
        System.out.println("=== LISTAS DE COMPRA DEL USUARIO ===");
        for (int i = 0; i < listas.size(); i++) {
            System.out.println((i + 1) + ". " + listas.get(i).getNombreCompra());
        }

        // Seleccionar la lista que se desea mostrar
        System.out.print("Seleccione el número de la lista: ");
        int opcion = leerEntero();

        // Delegar la visualización al servicio
        listaCompraService.mostrarListaCompleta(dni, opcion);
    }
}