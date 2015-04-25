package hu.elte.pt.store.gui.tablemodels;

import hu.elte.pt.store.gui.StoreFrame;
import hu.elte.pt.store.logic.DataSource;
import hu.elte.pt.store.logic.entities.Customer;
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
 * Customer táblához tartozó tábla modell
 * @author Holló Eszter
 */
public class CustomerTableModel extends AbstractTableModel implements EntityHandlerTableModel{

    private final int refreshInterval;
    private final List<Customer> customers;
    private final Timer refreshTimer;
    private final CellEditorListener cellEditorListener;
    
    public CustomerTableModel(){
        refreshInterval = 5 * 1000;
        customers = new CopyOnWriteArrayList<>();
        cellEditorListener = new CustomerCellEditorListener();
        refreshTimer = new Timer(refreshInterval, new ActionListener() {
   
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadCustomers();
            }
        });
        reloadCustomers();
        refreshTimer.start();        
    }
    
    @Override
    public int getRowCount() {
        try {
            return DataSource.getInstance().getCustomerController().getEntityCount();
        } catch (SQLException ex) {
            StoreFrame.displayError(ex.getMessage());
            return 0;
        }
    }

    @Override
    public int getColumnCount() {
        return Customer.fieldNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return getCustomerAtRow(rowIndex).getName();
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }
    
    @Override
    public String getColumnName(int column) {
        return Customer.fieldNames[column];
    }    
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        refreshTimer.stop();
        return true;
    }
    
    private Customer getCustomerAtRow(int rowIndex) {
        return customers.get(rowIndex);
    }    
    
    @Override
    public void setValueAt(Object aValue, final int rowIndex, int columnIndex) {
        final Customer customer = getCustomerAtRow(rowIndex);
        customer.setName((String) aValue);

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                DataSource.getInstance().updateCustomer(customer, rowIndex);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    reloadCustomers();
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("Az új vevő hozzáadása során kivétel keletkezett, így a művelet sikertelen volt!");
                }
            }
        }.execute();        
    }
    
    @Override
    public void addNewEntity() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                DataSource.getInstance().addCustomer();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    reloadCustomers();
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("Az új vevő hozzáadása során kivétel keletkezett, így a művelet sikertelen volt!");
                }
            }
        }.execute();        
    }

    @Override
    public void deleteEntity(final int rowIndex) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                DataSource.getInstance().deleteCustomer(rowIndex);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    reloadCustomers();
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("A vevő törlése során kivétel keletkezett, így a művelet sikertelen volt!");
                }
            }
        }.execute();         
    }
 
    private void reloadCustomers() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                List<Customer> customers = DataSource.getInstance().getCustomerController().getEntities();
                CustomerTableModel.this.customers.clear();
                CustomerTableModel.this.customers.addAll(customers);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    fireTableDataChanged();
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("Hiba történt a vevők újratöltése során!");
                }
            }
        }.execute();
    }    
    
    private class CustomerCellEditorListener implements CellEditorListener {

        @Override
        public void editingStopped(ChangeEvent e) {
            refreshTimer.start();
        }

        @Override
        public void editingCanceled(ChangeEvent e) {
            refreshTimer.start();
        }
    }    

    public CellEditorListener getCellEditorListener() {
        return cellEditorListener;
    }
}
