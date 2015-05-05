package hu.elte.pt.store.test.logic.controller;

import hu.elte.pt.store.logic.controllers.ProductController;
import hu.elte.pt.store.logic.entities.Product;
import java.sql.SQLException;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Bojtos Csaba Product Controllerhez tartozó tesztek
 */
public class ProductControllerTest {

    public ProductController productController = new ProductController();

    @Test
    public void addAndRemoveTest() throws SQLException {
        int initialNumberOfProducts = productController.getEntityCount();

        productController.addNewEntity();
        Assert.assertEquals(initialNumberOfProducts + 1, productController.getEntityCount());

        int lastIndex = initialNumberOfProducts;
        productController.deleteEntity(lastIndex);
        Assert.assertEquals(initialNumberOfProducts, productController.getEntityCount());

    }

    @Test(expected = Exception.class)
    public void removeWithInvalidIndexTest() throws SQLException {
        productController.deleteEntity(productController.getEntityCount() + 1);
    }

    @Test
    public void entityPropertiesTest() throws SQLException {
        List<Product> products = productController.getEntities();

        for (Product product : products) {
            Assert.assertNotNull(product.getProductID());
            Assert.assertNotNull(product.getProductName());
            Assert.assertNotNull(product.getDescription());
            Assert.assertNotNull(product.getCategory());
            Assert.assertNotNull(product.getManufacturer());
            Assert.assertNotNull(product.getPrice());
            Assert.assertNotNull(product.getStock());

        }

    }

}
