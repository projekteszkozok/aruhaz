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
        
        if(rowCount > 1 && columnCount > 1){
            
            for(int j = 1; j < columnCount-1; j++){
                Assert.assertNotNull(customerTableModel.getColumnName(j));
            }
        
        }
        
    }

}
