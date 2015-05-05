package hu.elte.pt.store.test.gui.tablemodels;

import hu.elte.pt.store.gui.tablemodels.CustomerTableModel;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Holló Eszter Category Controllerhez tartozó tesztek
 */
public class CustomerTableModelTest {

    CustomerTableModel customerTableModel = new CustomerTableModel();

    
    @Test
    public void columnTest(){
        int columnCount = customerTableModel.getColumnCount();
        
        for(int i = 0; i < columnCount-1; i++){
            Assert.assertNotNull(customerTableModel.getColumnName(i));
        }
    }
    
    @Test
    public void rowAndColumnTest(){
        int rowCount = customerTableModel.getRowCount();
        int columnCount = customerTableModel.getColumnCount();
        
        for(int i = 0; i < rowCount-1; i++){
            for(int j = 0; j < columnCount-1; j++){
                Assert.assertNotNull(customerTableModel.getValueAt(i, j));
            }
        }
    }

}
