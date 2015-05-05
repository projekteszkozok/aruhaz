package hu.elte.pt.store.test.logic.controller;

import hu.elte.pt.store.logic.controllers.StoreController;
import hu.elte.pt.store.logic.entities.Store;
import java.sql.SQLException;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Dudás Orsolya Store Controllerhez tesztek
 */
public class StoreControllerTest {

    public StoreController storeController = new StoreController();

    @Test
    public void addAndRemoveTest() throws SQLException {
        int initialNumberOfStores = storeController.getEntityCount();

        storeController.addNewEntity();
        Assert.assertEquals(initialNumberOfStores + 1, storeController.getEntityCount());

        int lastIndex = initialNumberOfStores;
        storeController.deleteEntity(lastIndex);
        Assert.assertEquals(initialNumberOfStores, storeController.getEntityCount());

    }

    @Test(expected = Exception.class)
    public void removeWithInvalidIndexTest() throws SQLException {
        storeController.deleteEntity(storeController.getEntityCount() + 1);
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

}
