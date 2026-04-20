import dao.ItemListaDao;
import dao.ListaCompraDao;
import dao.ProductoDao;
import dao.UsuarioDao;
import model.ItemLista;
import model.ListaCompra;
import model.Producto;
import model.Usuario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    //Scanner global para leer datos por consola
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        menu();
    }

    //Mé_todo que muestra el menú principal de la aplicación
    private static void menu() {

        int opcion = 0;

        do {
            System.out.println("\n===== MENÚ LISTA DE LA COMPRA =====");
            System.out.println("1. Crear usuario");
            System.out.println("2. Crear lista de compra");
            System.out.println("3. Crear producto");
            System.out.println("4. Añadir producto a una lista");
            System.out.println("5. Mostrar lista completa");
            System.out.println("6. Marcar item como comprado");
            System.out.println("7. Calcular precio total de una lista");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            //Comprobar que lo introducido es un número
            while (!scanner.hasNextInt()) {
                System.out.println("Debe introducir un número.");
                scanner.next();
            }

            //Leer opción
            opcion = scanner.nextInt();
            scanner.nextLine(); //Limpiar buffer

            //Ejecutar la opción elegida
            switch (opcion) {
                case 1 -> crearUsuario();
                case 2 -> crearListaCompra();
                case 3 -> crearProducto();
                case 4 -> anadirProductoALista();
                case 5 -> mostrarListaCompleta();
                case 6 -> marcarItemComoComprado();
                case 7 -> calcularTotalLista();
                case 0 -> System.out.println("Saliendo del programa...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 0);

        scanner.close();
    }

    private static void crearUsuario() {

        System.out.println("DNI: ");
        String dni = scanner.nextLine();

        if (dni.isEmpty()){
            System.out.println("El dni no puede estar vacío.");
            return;
        }

        if(!dni.matches("^[0-9]{8}[a-zA-Z]$")){
            System.out.println("Formato de DNI no válido(8 números y 1 letra).");
            return;
        }

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        //Validación simple
        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacío.");
            return;
        }

        if(!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            System.out.println("El nombre solo puede contener letras.");
            return;
        }

        //Verificar que no exista un usuario duplicado
        if (UsuarioDao.buscarUsuarioPorDni(dni) != null) {
            System.out.println("Ya existe un usuario con ese DNI.");
            return;
        }

        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();

        if (apellido.isEmpty()) {
            System.out.println("El apellido no puede estar vacío.");
            return;
        }

        if (!apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")){
            System.out.println("El apellido solo puede contener letras.");
            return;
        }

        System.out.print("Email: ");
        String email = scanner.nextLine();

        if (email.isEmpty() || !email.contains("@")) {
            System.out.println("Email no válido.");
            return;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            System.out.println("Email no válido.");
            return;
        }

        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (password.length() < 6) {
            System.out.println("La contraseña debe tener al menos 4 caracteres.");
            return;
        }

        if(!password.matches(".*\\d.*")){
            System.out.println("La contraseña debe contener al menos un número.");
        }

        //Crear objeto
        Usuario usuario = new Usuario(dni, nombre, apellido, email, password);

        //Insertar en BD
        UsuarioDao.insertarUsuario(usuario);

        System.out.println("Usuario creado correctamente.");
    }

    //Mé_todo para crear una nueva lista de compra
    private static void crearListaCompra() {

        //Pedir el DNI del usuario que va a crear la lista
        System.out.print("Introduce el DNI del usuario: ");
        String dni = scanner.nextLine();

        //Validar que el DNI no esté vacío
        if (dni.isEmpty()) {
            System.out.println("El DNI no puede estar vacío.");
            return;
        }

        //Buscar usuario en la base de datos a partir del DNI
        Usuario usuario = UsuarioDao.buscarUsuarioPorDni(dni);

        //Comprobar que el usuario exista
        if (usuario == null) {
            System.out.println("No se encontró ningún usuario con ese DNI.");
            return;
        }

        //Pedir nombre de la lista
        System.out.print("Nombre de la lista: ");
        String nombreLista = scanner.nextLine();

        //Validar que el nombre no esté vacío
        if (nombreLista.isEmpty()) {
            System.out.println("El nombre de la lista no puede estar vacío.");
            return;
        }

        //Crear objeto ListaCompra con la fecha actual
        ListaCompra lista = new ListaCompra(usuario, nombreLista, LocalDate.now());

        //Insertar la lista en la base de datos
        ListaCompraDao.insertarListaCompra(lista);

        System.out.println("Lista creada correctamente.");
    }

    //Mé_todo para crear un nuevo producto
    private static void crearProducto() {

        System.out.print("Nombre del producto: ");
        String nombre = scanner.nextLine();

        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacío.");
            return;
        }

        System.out.print("Precio: ");

        while (!scanner.hasNextBigDecimal()) {
            System.out.println("Introduce un número válido.");
            scanner.next();
        }

        BigDecimal precio = scanner.nextBigDecimal();
        scanner.nextLine();

        System.out.print("Categoría: ");
        String categoria = scanner.nextLine();

        if (categoria.isEmpty()) {
            System.out.println("La categoría no puede estar vacía.");
            return;
        }

        Producto producto = new Producto(nombre, precio, categoria);

        ProductoDao.insertarProducto(producto);

        System.out.println("Producto creado correctamente.");
    }

    //Mé_todo para añadir un producto a una lista de compra
    private static void anadirProductoALista() {

        //Pedir el DNI del usuario
        System.out.print("Introduce el DNI del usuario: ");
        String dni = scanner.nextLine();

        //Validar que no esté vacío
        if (dni.isEmpty()) {
            System.out.println("El DNI no puede estar vacío.");
            return;
        }

        //Buscar el usuario en la base de datos
        Usuario usuario = UsuarioDao.buscarUsuarioPorDni(dni);

        //Comprobar que exista
        if (usuario == null) {
            System.out.println("No se encontró ningún usuario con ese DNI.");
            return;
        }

        //Obtener las listas de compra del usuario
        List<ListaCompra> listas = ListaCompraDao.listarListasPorUsuario(usuario.getId());

        //Comprobar que tenga listas
        if (listas.isEmpty()){
            System.out.println("Este usuario no tiene listas de compra.");
            return;
        }

        //Mostrar listas del usuario
        System.out.println("=== LISTAS DE COMPRA DEL USUARIO ===");
        for (int i = 0; i < listas.size(); i++){
            System.out.println((i + 1) + ". " + listas.get(i).getNombreCompra());
        }

        //Pedir al usuario que elija una lista
        System.out.print("Selecciona el número de la lista: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();

        //Validar opción
        if (opcion < 1 || opcion > listas.size()){
            System.out.println("Opción no válida.");
            return;
        }

        //Obtener la lista seleccionada
        ListaCompra listaSeleccionada = listas.get(opcion - 1);

        //Obtener todos los productos
        List<Producto> productos = ProductoDao.listarProductos();

        //Comprobar que existan productos
        if (productos.isEmpty()){
            System.out.println("No hay productos disponibles.");
            return;
        }

        //Mostrar productos
        System.out.println("=== PRODUCTOS PARA AÑADIR EN LA LISTA ===");
        for (int i = 0; i < productos.size(); i++){
            System.out.println((i + 1) + ". " + productos.get(i).getNombre() +
                    " - " + productos.get(i).getPrecio() + "€");
        }

        //Pedir al usuario que elija un producto
        System.out.print("Selecciona el número del producto a añadir: ");
        int opcionProducto = scanner.nextInt();
        scanner.nextLine();

        //Validar opción
        if (opcionProducto < 1 || opcionProducto > productos.size()){
            System.out.println("Opción no válida.");
            return;
        }

        //Obtener el producto seleccionado
        Producto productoSeleccionado = productos.get(opcionProducto - 1);

        //Pedir cantidad
        System.out.print("¿Cuántas unidades deseas añadir? ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();

        //Validar cantidad
        if (cantidad <= 0){
            System.out.println("La cantidad debe ser mayor que 0.");
            return;
        }

        //Crear el item de la lista
        ItemLista itemLista = new ItemLista(
                productoSeleccionado,
                listaSeleccionada,
                cantidad,
                productoSeleccionado.getPrecio(),
                false
        );

        //Insertar el item en la base de datos
        ItemListaDao.insertarItemLista(itemLista);

        System.out.println("Producto añadido a la lista correctamente.");
    }

    //Mé_todo para mostrar una lista completa con sus items
    private static void mostrarListaCompleta() {
        System.out.println("Método mostrarListaCompleta pendiente de implementar.");
    }

    //Mé_todo para marcar un item como comprado
    private static void marcarItemComoComprado() {
        System.out.println("Método marcarItemComoComprado pendiente de implementar.");
    }

    //Mé_todo para calcular el precio total de una lista
    private static void calcularTotalLista() {
        System.out.println("Método calcularTotalLista pendiente de implementar.");
    }
}