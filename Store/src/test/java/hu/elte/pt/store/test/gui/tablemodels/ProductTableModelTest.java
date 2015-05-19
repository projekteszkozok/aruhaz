package hu.elte.pt.store.test.gui.tablemodels;

import hu.elte.pt.store.gui.tablemodels.ProductTableModel;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Bojtos Csaba
 *
 * Product Table Modelhez tartozó tesztek
 */
public class ProductTableModelTest {

    ProductTableModel productTableModel = new ProductTableModel();

    @Test
    public void columnTest() {
        int columnCount = productTableModel.getColumnCount();
        for (int i = 0; i < columnCount-1; i++) {
            Assert.assertNotNull(productTableModel.getColumnName(i));
        }
    }

    @Test(expected = Exception.class)
    public void invalidColumnIndexTest() {
        int columnCount = productTableModel.getColumnCount();
        Assert.assertNull(productTableModel.getColumnName(columnCount + 1));
    }

    @Test
    public void rowAndColumnTest() {
        int rowCount = productTableModel.getRowCount();
        int columnCount = productTableModel.getColumnCount();

        if (rowCount > 1 && columnCount > 1) {
            for (int i = 1; i < rowCount - 1; i++) {
                Assert.assertNotNull(productTableModel.getColumnName(i));
            }
        }
    }

}
