package hu.elte.pt.store.test.logic.controller;

import hu.elte.pt.store.gui.tablemodels.ManufacturerTableModel;
import hu.elte.pt.store.logic.controllers.ManufacturerController;
import hu.elte.pt.store.logic.entities.Manufacturer;
import java.sql.SQLException;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Honti Dóra
 * Manufacturer Controllerhez tartozó tesztek
 */
public class ManufacturerControllerTest {

    ManufacturerTableModel manufacturerTableModel = new ManufacturerTableModel();
    public ManufacturerController manufacturerController = new ManufacturerController();

    @Test
    public void addAndRemoveTest() throws SQLException {
        int initialNumberOfManufacturers = manufacturerController.getEntityCount();

        manufacturerController.addNewEntity();
        Assert.assertEquals(initialNumberOfManufacturers + 1, manufacturerController.getEntityCount());
        Assert.assertEquals(manufacturerController.getEntityCount(), manufacturerTableModel.getRowCount());

        int lastIndex = initialNumberOfManufacturers;
        manufacturerController.deleteEntity(lastIndex);
        Assert.assertEquals(initialNumberOfManufacturers, manufacturerController.getEntityCount());
        Assert.assertEquals(manufacturerController.getEntityCount(), manufacturerTableModel.getRowCount());

    }

    @Test(expected = Exception.class)
    public void removeWithInvalidIndexTest() throws SQLException {
        manufacturerController.deleteEntity(manufacturerController.getEntityCount() + 1);
    }

    @Test(expected = Exception.class)
    public void removeWithNegativeIndexTest() throws SQLException {
        manufacturerController.deleteEntity(-1);
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

    @Test
    public void getEntityTest() throws SQLException {
        int numberOfEntities = manufacturerController.getEntityCount();
        for (int i = 0; i < numberOfEntities; i++) {
            Assert.assertNotNull(manufacturerController.getEntityByRowIndex(i));
        }
    }

    @Test(expected = Exception.class)
    public void getEntityWithInvalidIndexTest() throws SQLException {
        int numberOfEntities = manufacturerController.getEntityCount();
        Assert.assertNotNull(manufacturerController.getEntityByRowIndex(numberOfEntities + 1));

    }
}
