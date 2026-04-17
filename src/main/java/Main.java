import database.DatabaseConnection;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

        Connection conexion = DatabaseConnection.conectar();

        if(conexion != null){
            System.out.println("OK conexión");
        } else {
            System.out.println("Error conexión");
        }
    }
}