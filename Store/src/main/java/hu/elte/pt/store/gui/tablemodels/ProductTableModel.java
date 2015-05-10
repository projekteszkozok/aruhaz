package hu.elte.pt.store.gui.tablemodels;

import hu.elte.pt.store.gui.StoreFrame;
import hu.elte.pt.store.logic.DataSource;
import hu.elte.pt.store.logic.entities.Category;
import hu.elte.pt.store.logic.entities.Manufacturer;
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
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return Manufacturer.class;
            case 2:
                return String.class;
            case 3:
                return Category.class;
            case 4:
                return Integer.class;
            case 5:
                return Integer.class;
            case 6:
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return Product.fieldNames[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        refreshTimer.stop();
        if (columnIndex == 6) {
            return true;
        } else {
            Product product = getProductAtRow(rowIndex);
            return product.isActive();
        }
    }

    @Override
    public void setValueAt(Object aValue, final int rowIndex, int columnIndex) {
        final Product product = getProductAtRow(rowIndex);

        switch (columnIndex) {
            case 0:
                product.setProductName((String) aValue);
                break;
            case 1:
                product.setManufacturer((Manufacturer) aValue);
                break;
            case 2:
                product.setDescription((String) aValue);
                break;
            case 3:
                product.setCategory((Category) aValue);
                break;
            case 4:
                product.setPrice((Integer) aValue);
                break;
            case 5:
                product.setStock((Integer) aValue);
                break;
            case 6:
                product.setIsActive((Boolean) aValue);
                break;
        }

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
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return getProductAtRow(rowIndex).getProductName();
            case 1:
                return getProductAtRow(rowIndex).getManufacturer();
            case 2:
                return getProductAtRow(rowIndex).getDescription();
            case 3:
                return getProductAtRow(rowIndex).getCategory();
            case 4:
                return getProductAtRow(rowIndex).getPrice();
            case 5:
                return getProductAtRow(rowIndex).getStock();
            case 6:
                return getProductAtRow(rowIndex).isActive();
            default:
                return null;
        }
    }

    private Product getProductAtRow(int rowIndex) {
        return products.get(rowIndex);
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
