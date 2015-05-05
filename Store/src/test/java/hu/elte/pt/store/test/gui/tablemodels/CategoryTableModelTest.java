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
    public void columnTest(){
        int columnCount = categoryTableModel.getColumnCount();
        
        for(int i = 0; i < columnCount-1; i++){
            Assert.assertNotNull(categoryTableModel.getColumnName(i));
        }
    }
    
    @Test
    public void rowAndColumnTest(){
        int rowCount = categoryTableModel.getRowCount();
        int columnCount = categoryTableModel.getColumnCount();
        
        for(int i = 0; i < rowCount-1; i++){
            for(int j = 0; j < columnCount-1; j++){
                Assert.assertNotNull(categoryTableModel.getValueAt(i, j));
            }
        }
    }

}
