package hu.elte.pt.store.test.gui.tablemodels;

import hu.elte.pt.store.gui.tablemodels.ManufacturerTableModel;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Honti Dóra
 * Manufactuer TableModelhez tartozó tesztek
 */
public class ManufacturerTableModelTest {

    ManufacturerTableModel manufacturerTableModel = new ManufacturerTableModel();

    @Test
    public void columnTest() {
        int columnCount = manufacturerTableModel.getColumnCount();

        for (int i = 0; i < columnCount - 1; i++) {
            Assert.assertNotNull(manufacturerTableModel.getColumnName(i));
        }
    }

    @Test
    public void rowAndColumnTest() {
        int rowCount = manufacturerTableModel.getRowCount();
        int columnCount = manufacturerTableModel.getColumnCount();

        if (rowCount > 1 && columnCount > 1) {
            for (int j = 1; j < columnCount - 1; j++) {
                Assert.assertNotNull(manufacturerTableModel.getColumnName(j));
            }
        }
    }
}
