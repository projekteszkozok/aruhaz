package hu.elte.pt.store.gui.tablemodels;

import hu.elte.pt.store.gui.StoreFrame;
import hu.elte.pt.store.logic.DataSource;
import hu.elte.pt.store.logic.entities.Product;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.AbstractTableModel;

/**
 * A Product táblához tartozó tábla modell.
 *
 * @author Bojtos Csaba
 */
public class ProductTableModel extends AbstractTableModel implements EntityHandlerTableModel {

    private final int refreshInterval;
    private final List<Product> products;
    private final Timer refreshTimer;
    private final CellEditorListener cellEditorListener;

    public ProductTableModel() {
        refreshInterval = 5 * 1000;
        products = new CopyOnWriteArrayList<>();
        cellEditorListener = new ProductTableModel.ProductCellEditorListener();
        refreshTimer = new Timer(refreshInterval, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                reloadProducts();
            }
        });
        reloadProducts();
        refreshTimer.start();
    }

    @Override
    public int getRowCount() {
        try {
            return DataSource.getInstance().getProductController().getEntityCount();
        } catch (SQLException ex) {
            StoreFrame.displayError(ex.getMessage());
            return 0;
        }
    }

    @Override
    public int getColumnCount() {
        return Product.fieldNames.length;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public String getColumnName(int column) {
        return Product.fieldNames[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        refreshTimer.stop();
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return getProductAtRow(rowIndex).getProductID();
    }

    private Product getProductAtRow(int rowIndex) {
        return products.get(rowIndex);
    }

    @Override
    public void setValueAt(Object aValue, final int rowIndex, int columnIndex) {
        final Product product = getProductAtRow(rowIndex);
        product.setProductName((String) aValue);

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                DataSource.getInstance().updateProduct(product, rowIndex);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    reloadProducts();
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("Az új termék hozzáadása során kivétel keletkezett, így a művelet sikertelen volt!");
                }
            }
        }.execute();
    }

    @Override
    public void addNewEntity() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                DataSource.getInstance().addProduct();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    reloadProducts();
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("Az új termék hozzáadása során kivétel keletkezett, így a művelet sikertelen volt!");
                }
            }
        }.execute();
    }

    @Override
    public void deleteEntity(final int rowIndex) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                DataSource.getInstance().deleteProduct(rowIndex);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    reloadProducts();
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("A termék törlése során kivétel keletkezett, így a művelet sikertelen volt!");
                }
            }
        }.execute();
    }

    private void reloadProducts() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                List<Product> products = DataSource.getInstance().getProductController().getEntities();
                ProductTableModel.this.products.clear();
                ProductTableModel.this.products.addAll(products);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    fireTableDataChanged();
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("Hiba történt a termékek újratöltése során!");
                }
            }
        }.execute();
    }

    private class ProductCellEditorListener implements CellEditorListener {

        @Override
        public void editingStopped(ChangeEvent e) {
            refreshTimer.start();
        }

        @Override
        public void editingCanceled(ChangeEvent e) {
            refreshTimer.start();
        }
    }

    @Override
    public CellEditorListener getCellEditorListener() {
        return cellEditorListener;
    }

}
