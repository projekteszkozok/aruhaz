package hu.elte.pt.store.test.gui.tablemodels;

import hu.elte.pt.store.test.logic.controller.*;
import hu.elte.pt.store.gui.tablemodels.CategoryTableModel;
import hu.elte.pt.store.logic.controllers.CategoryController;
import hu.elte.pt.store.logic.entities.Category;
import java.sql.SQLException;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Holló Eszter Category Controllerhez tartozó tesztek
 */
public class CategoryTableModelTest {

    CategoryTableModel categoryTableModel = new CategoryTableModel();

    @Test
    public void columnTest() {
        int columnCount = categoryTableModel.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            Assert.assertNotNull(categoryTableModel.getColumnName(i));
        }
    }

    @Test(expected = Exception.class)
    public void invalidColumnIndexTest() {
        int columnCount = categoryTableModel.getColumnCount();
        Assert.assertNull(categoryTableModel.getColumnName(columnCount + 1));
    }

    @Test
    public void rowAndColumnTest() {
        int rowCount = categoryTableModel.getRowCount();
        int columnCount = categoryTableModel.getColumnCount();

        if(rowCount > 1 && columnCount > 1){
            for (int i = 1; i < rowCount-1; i++) {
                for (int j = 1; j < columnCount-1; j++) {
                    Assert.assertNotNull(categoryTableModel.getValueAt(i, j));
                }
            }
        }
    }

    @Test(expected = Exception.class)
    public void invalidrowAndColumnIndexTest() {
        int rowCount = categoryTableModel.getRowCount();
        int columnCount = categoryTableModel.getColumnCount();

        Assert.assertNull(categoryTableModel.getValueAt(rowCount + 1, columnCount + 1));

    }

}
