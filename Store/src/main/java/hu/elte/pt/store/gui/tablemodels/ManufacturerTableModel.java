package hu.elte.pt.store.gui.tablemodels;

import hu.elte.pt.store.gui.StoreFrame;
import hu.elte.pt.store.logic.DataSource;
import hu.elte.pt.store.logic.entities.Manufacturer;
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
 * Manufacturer táblához tartozó tábla model
 *
 * @author Honti Dora
 */
public class ManufacturerTableModel extends AbstractTableModel implements EntityHandlerTableModel {

    private final int refreshInterval;
    private final List<Manufacturer> manufacturers;
    private final Timer refreshTimer;
    private final CellEditorListener cellEditorListener;

    public ManufacturerTableModel() {
        refreshInterval = 5 * 1000;
        manufacturers = new CopyOnWriteArrayList<>();
        cellEditorListener = new ManufacturerCellEditorListener();
        refreshTimer = new Timer(refreshInterval, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                reloadManufacturers();
            }
        });
        reloadManufacturers();
        refreshTimer.start();
    }

    @Override
    public int getRowCount() {
        try {
            return DataSource.getInstance().getManufacturerController().getEntityCount();
        } catch (SQLException ex) {
            StoreFrame.displayError(ex.getMessage());
            return 0;
        }
    }

    @Override
    public int getColumnCount() {
        return Manufacturer.fieldNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return getManufacturerAtRow(rowIndex).getName();
            case 1:
                return getManufacturerAtRow(rowIndex).getContactName();
            case 2:
                return getManufacturerAtRow(rowIndex).getCity();
            case 3:
                return getManufacturerAtRow(rowIndex).getPhone();
            case 4:
                return getManufacturerAtRow(rowIndex).getStore();
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            case 4:
                return Store.class;
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return Manufacturer.fieldNames[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        refreshTimer.stop();
        return true;
    }

    private Manufacturer getManufacturerAtRow(int rowIndex) {
        return manufacturers.get(rowIndex);
    }

    @Override
    public void setValueAt(Object aValue, final int rowIndex, int columnIndex) {
        final Manufacturer manufacturer = getManufacturerAtRow(rowIndex);
        manufacturer.setName((String) aValue);
        manufacturer.setContactName((String) aValue);
        manufacturer.setCity((String) aValue);
        manufacturer.setPhone((String) aValue);
        manufacturer.setStore((Store) aValue);

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                DataSource.getInstance().updateManufacturer(manufacturer, rowIndex);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    reloadManufacturers();
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("Az új gyártó hozzáadása során kivétel keletkezett, így a művelet sikertelen volt!");
                }
            }
        }.execute();
    }

    @Override
    public void addNewEntity() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                DataSource.getInstance().addManufacturer();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    reloadManufacturers();
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("Az új gyártó hozzáadása során kivétel keletkezett, így a művelet sikertelen volt!");
                }
            }
        }.execute();
    }

    @Override
    public void deleteEntity(final int rowIndex) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                DataSource.getInstance().deleteManufacturer(rowIndex);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    reloadManufacturers();
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("A gyártó törlése során kivétel keletkezett, így a művelet sikertelen volt!");
                }
            }
        }.execute();
    }

    private void reloadManufacturers() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                List<Manufacturer> manufacturers = DataSource.getInstance().getManufacturerController().getEntities();
                ManufacturerTableModel.this.manufacturers.clear();
                ManufacturerTableModel.this.manufacturers.addAll(manufacturers);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    fireTableDataChanged();
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("Hiba történt a gyártók újratöltése során!");
                }
            }
        }.execute();
    }

    private class ManufacturerCellEditorListener implements CellEditorListener {

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
