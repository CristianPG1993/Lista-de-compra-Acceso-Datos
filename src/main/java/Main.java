import dao.ItemListaDao;
import dao.ListaCompraDao;
import model.ItemLista;
import model.ListaCompra;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        int idLista = 1;

        ListaCompra lista = ListaCompraDao.buscarListaCompraPorId(idLista);
        List<ItemLista> items = ItemListaDao.listarItemsPorLista(idLista);
        BigDecimal total = ItemListaDao.calcularPrecioTotalLista(idLista);

        if (lista != null) {
            System.out.println("Lista: " + lista.getNombreCompra());
            System.out.println();

            for (ItemLista item : items) {
                BigDecimal subtotal = item.getPrecioUnitario()
                        .multiply(BigDecimal.valueOf(item.getCantidad()));

                System.out.println("- " + item.getProducto().getNombre() +
                        " x" + item.getCantidad() +
                        " → " + subtotal + "€");
            }

            System.out.println();
            System.out.println("Total: " + total + "€");
        } else {
            System.out.println("No se encontró la lista.");
        }
    }
}