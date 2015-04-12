package hu.elte.pt.store.gui.tablemodels;

import hu.elte.pt.store.gui.StoreFrame;
import hu.elte.pt.store.logic.DataSource;
import hu.elte.pt.store.logic.entities.Category;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;

/**
 * A Category táblához tartozó tábla model.
 * @author Nagy Krisztián
 */
public class CategoryTableModel extends AbstractTableModel implements EntityHandlerTableModel{

    @Override
    public int getRowCount() {
        try {
            return DataSource.getInstance().getCategoryController().getEntityCount();
        } catch (SQLException ex) {
            StoreFrame.showError(ex.getMessage());
            return 0;
        }
    }

    @Override
    public int getColumnCount() {
        return Category.fieldNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            return DataSource.getInstance().getCategoryController().getEntityByRowIndex(rowIndex).getName();
        } catch (SQLException ex) {
            StoreFrame.showError(ex.getMessage());
            return "?";
        }
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
        return true;
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {
            Category subject = DataSource.getInstance().getCategoryController().getEntityByRowIndex(rowIndex);
            subject.setName((String) aValue);
            DataSource.getInstance().getCategoryController().updateEntity(subject);
        } catch (SQLException ex) {
            StoreFrame.showError(ex.getMessage());
        }
    }
    
    @Override
    public void addNewEntity() {
        try {
            int newRowIndex = DataSource.getInstance().getCategoryController().addNewEntity();
            fireTableRowsInserted(newRowIndex, newRowIndex);
        } catch (SQLException ex) {
            StoreFrame.showError(ex.getMessage());
        }
    }

    @Override
    public void deleteEntity(int rowIndex) {
        try {
            DataSource.getInstance().getCategoryController().deleteEntity(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        } catch (SQLException ex) {
            StoreFrame.showError(ex.getMessage());
        }
    }
    
}
