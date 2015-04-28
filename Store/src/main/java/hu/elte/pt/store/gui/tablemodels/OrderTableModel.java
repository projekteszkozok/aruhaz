package hu.elte.pt.store.gui.tablemodels;

import hu.elte.pt.store.gui.StoreFrame;
import hu.elte.pt.store.logic.DataSource;
import hu.elte.pt.store.logic.entities.Customer;
import hu.elte.pt.store.logic.entities.Order;
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
 * Order táblához tartozó tábla model
 *
 * @author Cseh Zoltán
 */
public class OrderTableModel extends AbstractTableModel implements EntityHandlerTableModel {

    private final int refreshInterval;
    private final List<Order> orderers;
    private final Timer refreshTimer;
    private final CellEditorListener cellEditorListener;

    public OrderTableModel() {
        refreshInterval = 5 * 1000;
        orderers = new CopyOnWriteArrayList<>();
        cellEditorListener = new OrderCellEditorListener();
        refreshTimer = new Timer(refreshInterval, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                reloadOrders();
            }
        });
        reloadOrders();
        refreshTimer.start();
    }

    @Override
    public int getRowCount() {
        try {
            return DataSource.getInstance().getOrderController().getEntityCount();
        } catch (SQLException ex) {
            StoreFrame.displayError(ex.getMessage());
            return 0;
        }
    }

    @Override
    public int getColumnCount() {
        return Order.fieldNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return getOrderAtRow(rowIndex).getProduct().getProductName();
            case 1:
                return getOrderAtRow(rowIndex).getCustomer().getName();
                
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Product.class;
            case 1:
                return Customer.class;
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return Order.fieldNames[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        refreshTimer.stop();
        return true;
    }

    private Order getOrderAtRow(int rowIndex) {
        return orderers.get(rowIndex);
    }

    @Override
    public void setValueAt(Object aValue, final int rowIndex, int columnIndex) {
        final Order order = getOrderAtRow(rowIndex);
        switch (columnIndex) {
            case 0:
                order.setProduct((Product) aValue);
            case 1:
                order.setCustomer((Customer) aValue);
        }

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                DataSource.getInstance().updateOrder(order, rowIndex);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    reloadOrders();
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("Az új rendelés hozzáadása során kivétel keletkezett, így a művelet sikertelen volt!");
                }
            }
        }.execute();
    }

    @Override
    public void addNewEntity() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                DataSource.getInstance().addOrder();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    reloadOrders();
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("Az új rendelés hozzáadása során kivétel keletkezett, így a művelet sikertelen volt!");
                }
            }
        }.execute();
    }

    @Override
    public void deleteEntity(final int rowIndex) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                DataSource.getInstance().deleteOrder(rowIndex);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    reloadOrders();
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("A rendelés törlése során kivétel keletkezett, így a művelet sikertelen volt!");
                }
            }
        }.execute();
    }

    private void reloadOrders() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                List<Order> orderers = DataSource.getInstance().getOrderController().getEntities();
                OrderTableModel.this.orderers.clear();
                OrderTableModel.this.orderers.addAll(orderers);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    fireTableDataChanged();
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("Hiba történt a rendelések újratöltése során!");
                }
            }
        }.execute();
    }

    private class OrderCellEditorListener implements CellEditorListener {

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
