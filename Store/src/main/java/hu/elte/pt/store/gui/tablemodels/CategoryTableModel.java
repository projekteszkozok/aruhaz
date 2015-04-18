package hu.elte.pt.store.gui.tablemodels;

import hu.elte.pt.store.gui.StoreFrame;
import hu.elte.pt.store.logic.DataSource;
import hu.elte.pt.store.logic.entities.Category;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.AbstractTableModel;

/**
 * A Category táblához tartozó tábla model.
 * @author Nagy Krisztián
 */
public class CategoryTableModel extends AbstractTableModel implements EntityHandlerTableModel{

    private final int refreshInterval;
    private final List<Category> categories;
    private final Timer refreshTimer;
    private final CellEditorListener cellEditorListener;
    
    public CategoryTableModel(){
        refreshInterval = 5 * 1000;
        categories = new CopyOnWriteArrayList<>();
        cellEditorListener = new CategoryCellEditorListener();
        refreshTimer = new Timer(refreshInterval, new ActionListener() {
   
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadCategories();
            }
        });
        reloadCategories();
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
        return Category.fieldNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return getCategoryAtRow(rowIndex).getName();
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }
    
    @Override
    public String getColumnName(int column) {
        return Category.fieldNames[column];
    }    
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        refreshTimer.stop();
        return true;
    }
    
    private Category getCategoryAtRow(int rowIndex) {
        return categories.get(rowIndex);
    }    
    
    @Override
    public void setValueAt(Object aValue, final int rowIndex, int columnIndex) {
        final Category category = getCategoryAtRow(rowIndex);
        category.setName((String) aValue);

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                DataSource.getInstance().updateCategory(category, rowIndex);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    reloadCategories();
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
                DataSource.getInstance().addCategory();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    reloadCategories();
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
                DataSource.getInstance().deleteCategory(rowIndex);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    reloadCategories();
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("A kategória törlése során kivétel keletkezett, így a művelet sikertelen volt!");
                }
            }
        }.execute();         
    }
 
    private void reloadCategories() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                List<Category> categories = DataSource.getInstance().getCategoryController().getEntities();
                CategoryTableModel.this.categories.clear();
                CategoryTableModel.this.categories.addAll(categories);
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
    
    private class CategoryCellEditorListener implements CellEditorListener {

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
