package hu.elte.pt.store.gui.tablemodels;

import hu.elte.pt.store.gui.StoreFrame;
import hu.elte.pt.store.logic.DataSource;
import hu.elte.pt.store.logic.entities.Store;
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
 * Store táblához tartozó tábla modell
 *
 * @author Dudás Orsolya
 */
public class StoreTableModel extends AbstractTableModel implements EntityHandlerTableModel {

    private final int refreshInterval;
    private final List<Store> stores;
    private final Timer refreshTimer;
    private final CellEditorListener cellEditorListener;

    public StoreTableModel() {
        refreshInterval = 5 * 1000;
        stores = new CopyOnWriteArrayList<>();
        cellEditorListener = new StoreTableModel.StoreCellEditorListener();
        refreshTimer = new Timer(refreshInterval, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                reloadStores();
            }
        });
        reloadStores();
        refreshTimer.start();
    }

    @Override
    public int getRowCount() {
        try {
            return DataSource.getInstance().getCategoryController().getEntityCount();
        } catch (SQLException ex) {
            StoreFrame.displayError(ex.getMessage());
            return 0;
        }
    }

    @Override
    public int getColumnCount() {
        return Store.fieldNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        switch (columnIndex) {
            case 0:
                return getStoreAtRow(rowIndex).getName();
            case 1:
                return getStoreAtRow(rowIndex).getPlace();
        }

        return 0;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public String getColumnName(int column) {
        return Store.fieldNames[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        refreshTimer.stop();
        return true;
    }

    private Store getStoreAtRow(int rowIndex) {
        return stores.get(rowIndex);
    }

    @Override
    public void setValueAt(Object aValue, final int rowIndex, int columnIndex) {
        final Store store = getStoreAtRow(rowIndex);
        switch (columnIndex) {
            case 0:
                store.setName((String) aValue);
                break;
            case 1:
                store.setPlace((String) aValue);
                break;
        }

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                DataSource.getInstance().updateStore(store, rowIndex);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    reloadStores();
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("Az új kategória hozzáadása során kivétel keletkezett, így a művelet sikertelen volt!");
                }
            }
        }.execute();
    }

    @Override
    public void addNewEntity() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                DataSource.getInstance().addStore();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    reloadStores();
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("Az új kategória hozzáadása során kivétel keletkezett, így a művelet sikertelen volt!");
                }
            }
        }.execute();
    }

    @Override
    public void deleteEntity(final int rowIndex) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                DataSource.getInstance().deleteStore(rowIndex);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    reloadStores();
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("A kategória törlése során kivétel keletkezett, így a művelet sikertelen volt!");
                }
            }
        }.execute();
    }

    private void reloadStores() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                List<Store> stores = DataSource.getInstance().getStoreController().getEntities();
                StoreTableModel.this.stores.clear();
                StoreTableModel.this.stores.addAll(stores);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    fireTableDataChanged();
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("Hiba történt a kategóriák újratöltése során!");
                }
            }
        }.execute();
    }

    private class StoreCellEditorListener implements CellEditorListener {

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
