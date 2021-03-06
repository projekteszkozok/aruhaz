package hu.elte.pt.store.test.logic.controller;

import hu.elte.pt.store.gui.tablemodels.StoreTableModel;
import hu.elte.pt.store.logic.controllers.StoreController;
import hu.elte.pt.store.logic.entities.Store;
import java.sql.SQLException;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Dudás Orsolya Store Controllerhez tartozó tesztek
 */
public class StoreControllerTest {

    StoreTableModel storeTableModel = new StoreTableModel();
    public StoreController storeController = new StoreController();

    @Test
    public void addAndRemoveTest() throws SQLException {
        int initialNumberOfStores = storeController.getEntityCount();

        storeController.addNewEntity();
        Assert.assertEquals(initialNumberOfStores + 1, storeController.getEntityCount());
        Assert.assertEquals(storeController.getEntityCount(), storeTableModel.getRowCount());

        int lastIndex = initialNumberOfStores;
        storeController.deleteEntity(lastIndex);
        Assert.assertEquals(initialNumberOfStores, storeController.getEntityCount());
        Assert.assertEquals(storeController.getEntityCount(), storeTableModel.getRowCount());

    }

    @Test(expected = Exception.class)
    public void removeWithInvalidIndexTest() throws SQLException {
        storeController.deleteEntity(storeController.getEntityCount() + 1);
    }

    @Test(expected = Exception.class)
    public void removeWithNegativeIndexTest() throws SQLException {
        storeController.deleteEntity(-1);
    }

    @Test
    public void entityPropertiesTest() throws SQLException {
        List<Store> stores = storeController.getEntities();

        for (Store store : stores) {
            Assert.assertNotNull(store.getStoreId());
            Assert.assertNotNull(store.getName());
            Assert.assertNotNull(store.getPlace());
        }

    }

    @Test
    public void getEntityTest() throws SQLException {
        int numberOfEntities = storeController.getEntityCount();
        for (int i = 0; i < numberOfEntities; i++) {
            Assert.assertNotNull(storeController.getEntityByRowIndex(i));
        }
    }

    @Test(expected = Exception.class)
    public void getEntityWithInvalidIndexTest() throws SQLException {
        int numberOfEntities = storeController.getEntityCount();
        Assert.assertNotNull(storeController.getEntityByRowIndex(numberOfEntities + 1));

    }

}
