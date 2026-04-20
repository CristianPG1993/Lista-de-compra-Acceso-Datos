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
            System.out.println("7. Eliminar una lista");
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
                case 7 -> eliminarListaCompra();
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

    //Método para añadir uno o varios productos a una lista de compra
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

    //Obtener las listas del usuario
    List<ListaCompra> listas = ListaCompraDao.listarListasPorUsuario(usuario.getId());

    //Comprobar que tenga listas
    if (listas.isEmpty()){
        System.out.println("Este usuario no tiene listas de compra.");
        return;
    }

    //Mostrar listas
    System.out.println("=== LISTAS DE COMPRA DEL USUARIO ===");
    for (int i = 0; i < listas.size(); i++){
        System.out.println((i + 1) + ". " + listas.get(i).getNombreCompra());
    }

    //Seleccionar lista
    System.out.print("Selecciona el número de la lista: ");
    int opcion = scanner.nextInt();
    scanner.nextLine();

    if (opcion < 1 || opcion > listas.size()){
        System.out.println("Opción no válida.");
        return;
    }

    //Lista seleccionada
    ListaCompra listaSeleccionada = listas.get(opcion - 1);

    String continuar = null;

    //BUCLE para añadir múltiples productos
    do {

        //Obtener productos
        List<Producto> productos = ProductoDao.listarProductos();

        if (productos.isEmpty()){
            System.out.println("No hay productos disponibles.");
            return;
        }

        //Mostrar productos
        System.out.println("=== PRODUCTOS DISPONIBLES ===");
        for (int i = 0; i < productos.size(); i++){
            System.out.println((i + 1) + ". " +
                    productos.get(i).getNombre() +
                    " - " + productos.get(i).getPrecio() + "€");
        }

        //Seleccionar producto
        System.out.print("Selecciona el número del producto a añadir: ");
        int opcionProducto = scanner.nextInt();
        scanner.nextLine();

        if (opcionProducto < 1 || opcionProducto > productos.size()){
            System.out.println("Opción no válida.");
            continue; // vuelve a empezar el bucle
        }

        Producto productoSeleccionado = productos.get(opcionProducto - 1);

        //Pedir cantidad
        System.out.print("¿Cuántas unidades deseas añadir? ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();

        if (cantidad <= 0){
            System.out.println("La cantidad debe ser mayor que 0.");
            continue; // vuelve a empezar el bucle
        }

        //Crear item
        ItemLista itemLista = new ItemLista(
                productoSeleccionado,
                listaSeleccionada,
                cantidad,
                productoSeleccionado.getPrecio(),
                false
        );

        //Insertar en BD
        ItemListaDao.insertarItemLista(itemLista);

        System.out.println("Producto añadido a la lista.");

        //Preguntar si quiere continuar
        System.out.print("¿Deseas añadir otro producto? (s/n): ");
        continuar = scanner.nextLine();

    } while (continuar.equalsIgnoreCase("s"));
}

    //Mé_todo para mostrar una lista completa con sus items
    private static void mostrarListaCompleta() {

        //Pedir el DNI del usuario
        System.out.print("Introduce el DNI del usuario: ");
        String dni = scanner.nextLine();

        //Validar que el DNI no esté vacío
        if (dni.isEmpty()) {
            System.out.println("El DNI no puede estar vacío.");
            return;
        }

        //Buscar el usuario en la base de datos a partir del DNI
        Usuario usuario = UsuarioDao.buscarUsuarioPorDni(dni);

        //Comprobar que el usuario exista
        if (usuario == null) {
            System.out.println("No se encontró ningún usuario con ese DNI.");
            return;
        }

        //Obtener las listas de compra del usuario
        List<ListaCompra> listas = ListaCompraDao.listarListasPorUsuario(usuario.getId());

        //Comprobar que el usuario tenga listas
        if (listas.isEmpty()){
            System.out.println("Este usuario no tiene listas de compra.");
            return;
        }

        //Mostrar las listas disponibles para que el usuario elija
        System.out.println("=== LISTAS DE COMPRA DEL USUARIO ===");
        for (int i = 0; i < listas.size(); i++){
            System.out.println((i + 1) + ". " + listas.get(i).getNombreCompra());
        }

        //Pedir al usuario que seleccione una lista
        System.out.print("Selecciona el número de la lista: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();

        //Validar que la opción esté dentro del rango correcto
        if (opcion < 1 || opcion > listas.size()){
            System.out.println("Opción no válida.");
            return;
        }

        //Obtener la lista seleccionada (restando 1 porque el índice empieza en 0)
        ListaCompra listaSeleccionada = listas.get(opcion - 1);

        //Obtener los items asociados a la lista seleccionada
        List<ItemLista> items = ItemListaDao.listarItemsPorLista(listaSeleccionada.getIdLista());

        //Calcular el total de la lista
        BigDecimal total = ItemListaDao.calcularPrecioTotalLista(listaSeleccionada.getIdLista());

        //Mostrar cabecera de la lista
        System.out.println("\n=== LISTA DE LA COMPRA ===");
        System.out.println("Nombre de la lista: " + listaSeleccionada.getNombreCompra());
        System.out.println("Usuario: " + usuario.getNombre() + " " + usuario.getApellido());
        System.out.println();

        //Comprobar si la lista tiene items
        if (items.isEmpty()) {
            System.out.println("La lista no tiene productos.");
        } else {

            //Recorrer todos los items de la lista
            for (ItemLista item : items) {

                //Calcular el subtotal (precioUnitario * cantidad)
                BigDecimal subtotal = item.getPrecioUnitario()
                        .multiply(BigDecimal.valueOf(item.getCantidad()));

                //Mostrar información del producto
                System.out.println("- " + item.getProducto().getNombre() +
                        " x" + item.getCantidad() +
                        " → " + subtotal + "€" +
                        " | Comprado: " + item.isComprado());
            }
        }

        //Mostrar el total final de la lista
        System.out.println();
        System.out.println("Total de la lista: " + total + "€");
    }

    //Mé_todo para marcar un item como comprado
    private static void marcarItemComoComprado() {

        //Pedir el DNI del usuario
        System.out.print("Introduce el DNI del usuario: ");
        String dni = scanner.nextLine();

        //Validar que el DNI no esté vacío
        if (dni.isEmpty()) {
            System.out.println("El DNI no puede estar vacío.");
            return;
        }

        //Buscar el usuario en la base de datos a partir del DNI
        Usuario usuario = UsuarioDao.buscarUsuarioPorDni(dni);

        //Comprobar que el usuario exista
        if (usuario == null) {
            System.out.println("No se encontró ningún usuario con ese DNI.");
            return;
        }

        //Obtener las listas de compra del usuario
        List<ListaCompra> listas = ListaCompraDao.listarListasPorUsuario(usuario.getId());

        //Comprobar que el usuario tenga listas
        if (listas.isEmpty()){
            System.out.println("Este usuario no tiene listas de compra.");
            return;
        }

        //Mostrar las listas disponibles para que el usuario elija
        System.out.println("=== LISTAS DE COMPRA DEL USUARIO ===");
        for (int i = 0; i < listas.size(); i++){
            System.out.println((i + 1) + ". " + listas.get(i).getNombreCompra());
        }

        //Pedir al usuario que seleccione una lista
        System.out.print("Selecciona el número de la lista: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();

        //Validar que la opción esté dentro del rango correcto
        if (opcion < 1 || opcion > listas.size()){
            System.out.println("Opción no válida.");
            return;
        }

        //Obtener la lista seleccionada
        ListaCompra listaSeleccionada = listas.get(opcion - 1);

        //Obtener los items asociados a la lista seleccionada
        List<ItemLista> items = ItemListaDao.listarItemsPorLista(listaSeleccionada.getIdLista());

        //Comprobar si la lista tiene productos
        if (items.isEmpty()) {
            System.out.println("La lista no tiene productos.");
            return;
        }

        //Mostrar los items de la lista
        System.out.println("=== ITEMS DE LA LISTA ===");
        for (int i = 0; i < items.size(); i++){

            ItemLista item = items.get(i);

            //Calcular subtotal del item
            BigDecimal subtotal = item.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(item.getCantidad()));

            //Mostrar información del item
            System.out.println((i + 1) + ". " +
                    item.getProducto().getNombre() +
                    " x" + item.getCantidad() +
                    " → " + subtotal + "€" +
                    " | Comprado: " + item.isComprado());
        }

        //Pedir al usuario que seleccione un item
        System.out.print("Selecciona el número del item a marcar como comprado: ");
        int opcionItem = scanner.nextInt();
        scanner.nextLine();

        //Validar opción seleccionada
        if (opcionItem < 1 || opcionItem > items.size()) {
            System.out.println("Opción no válida.");
            return;
        }

        //Obtener el item seleccionado
        ItemLista itemSeleccionado = items.get(opcionItem - 1);

        //Actualizar el estado del item a comprado en la base de datos
        ItemListaDao.marcarComoComprado(itemSeleccionado.getIdItem());

        //Mensaje de confirmación
        System.out.println("Item marcado como comprado correctamente.");
    }

    private static void eliminarListaCompra() {

        System.out.print("Introduce el DNI del usuario: ");
        String dni = scanner.nextLine();

        if (dni.isEmpty()) {
            System.out.println("El DNI no puede estar vacío.");
            return;
        }

        Usuario usuario = UsuarioDao.buscarUsuarioPorDni(dni);

        if (usuario == null) {
            System.out.println("No se encontró ningún usuario con ese DNI.");
            return;
        }

        //Obtener las listas de compra del usuario
        List<ListaCompra> listas = ListaCompraDao.listarListasPorUsuario(usuario.getId());

        //Comprobar que el usuario tenga listas
        if (listas.isEmpty()){
            System.out.println("Este usuario no tiene listas de compra.");
            return;
        }

        //Mostrar las listas disponibles para que el usuario elija
        System.out.println("=== LISTAS DE COMPRA DEL USUARIO ===");
        for (int i = 0; i < listas.size(); i++){
            System.out.println((i + 1) + ". " + listas.get(i).getNombreCompra());
        }

        //Pedir al usuario que seleccione una lista
        System.out.print("Selecciona el número de la lista que desea eliminar: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();

        //Validar que la opción esté dentro del rango correcto
        if (opcion < 1 || opcion > listas.size()){
            System.out.println("Opción no válida.");
            return;
        }

        //Obtener la lista seleccionada
        ListaCompra listaSeleccionada = listas.get(opcion - 1);

        //Confirmación
        System.out.print("¿Seguro que quieres eliminar esta lista? (s/n): ");
        String confirmacion = scanner.nextLine();

        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Operación cancelada.");
            return;
        }

        //Eliminar la lista
        ListaCompraDao.eliminarListaCompra(listaSeleccionada.getIdLista());
    }
}