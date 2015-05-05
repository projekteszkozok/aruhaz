package hu.elte.pt.store.test.logic.controller;

import hu.elte.pt.store.logic.controllers.ManufacturerController;
import hu.elte.pt.store.logic.entities.Manufacturer;
import java.sql.SQLException;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Honti Dóra
 * Manufacturer Controllerhez tartozó tesztek
 */
public class ManufacturerControllerTest {

    public ManufacturerController manufacturerController = new ManufacturerController();

    @Test
    public void addAndRemoveTest() throws SQLException {
        int initialNumberOfManufacturers = manufacturerController.getEntityCount();

        manufacturerController.addNewEntity();
        Assert.assertEquals(initialNumberOfManufacturers + 1, manufacturerController.getEntityCount());

        int lastIndex = initialNumberOfManufacturers;
        manufacturerController.deleteEntity(lastIndex);
        Assert.assertEquals(initialNumberOfManufacturers, manufacturerController.getEntityCount());

    }

    @Test(expected = Exception.class)
    public void removeWithInvalidIndexTest() throws SQLException {
        manufacturerController.deleteEntity(manufacturerController.getEntityCount() + 1);
    }

    @Test
    public void entityPropertiesTest() throws SQLException {
        List<Manufacturer> manufacturers = manufacturerController.getEntities();

        for (Manufacturer manufacturer : manufacturers) {
            Assert.assertNotNull(manufacturer.getManufacturerId());
            Assert.assertNotNull(manufacturer.getName());
            Assert.assertNotNull(manufacturer.getContactName());
            Assert.assertNotNull(manufacturer.getCity());
            Assert.assertNotNull(manufacturer.getPhone());
            Assert.assertNotNull(manufacturer.getStore());
        }
    }
}
