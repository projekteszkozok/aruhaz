package hu.elte.pt.store.gui.comboboxmodels;

import hu.elte.pt.store.gui.StoreFrame;
import hu.elte.pt.store.logic.DataSource;
import hu.elte.pt.store.logic.entities.Category;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;

/**
 * Egy Category entitás combo box-ba történő megjelenítése érdekében létrehozott model osztály.
 * @author Nagy Krisztián
 */
public class CategoryComboBoxModel extends DefaultComboBoxModel<Category>{
    @Override
    public int getSize() {
        try {
            return DataSource.getInstance().getCategoryController().getEntityCount();
        } catch (SQLException ex) {
            StoreFrame.showError(ex.getMessage());
            return 0;
        }
    }

    @Override
    public Category getElementAt(int index) {
        try {
            return DataSource.getInstance().getCategoryController().getEntityByRowIndex(index);
        } catch (SQLException ex) {
            StoreFrame.showError(ex.getMessage());
            return null;
        }
    }    
}
