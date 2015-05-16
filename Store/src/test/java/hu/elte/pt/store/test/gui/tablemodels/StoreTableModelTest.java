package hu.elte.pt.store.test.gui.tablemodels;

import hu.elte.pt.store.gui.tablemodels.StoreTableModel;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Dudás Orsi
 */
public class StoreTableModelTest {

    StoreTableModel storeTableModel = new StoreTableModel();

    @Test
    public void columnTest() {
        int columnCount = storeTableModel.getColumnCount();

        for (int i = 0; i < columnCount - 1; i++) {
            Assert.assertNotNull(storeTableModel.getColumnName(i));
        }
    }

    @Test
    public void rowAndColumnTest() {
        int rowCount = storeTableModel.getRowCount();
        int columnCount = storeTableModel.getColumnCount();

        if (rowCount > 1 && columnCount > 1) {

            for (int j = 1; j < columnCount - 1; j++) {
                Assert.assertNotNull(storeTableModel.getColumnName(j));
            }

        }

    }

}
