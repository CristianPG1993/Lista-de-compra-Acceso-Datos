import dao.ListaCompraDao;
import dao.ProductoDao;
import model.ListaCompra;
import model.Producto;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Producto p = new Producto("Leche", new BigDecimal("1.50"), "Lácteos");
        ProductoDao.insertarProducto(p);
    }
}