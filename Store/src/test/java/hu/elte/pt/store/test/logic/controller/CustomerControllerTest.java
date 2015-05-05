package hu.elte.pt.store.test.logic.controller;


import hu.elte.pt.store.logic.controllers.CustomerController;
import hu.elte.pt.store.logic.entities.Customer;
import java.sql.SQLException;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Holló Eszter
 * Customer Controllerhez tartozó tesztek
 */
public class CustomerControllerTest {
    
    public CustomerController customerController = new CustomerController();
       
  
    @Test
    public void addAndRemoveTest() throws SQLException {
      int initialNumberOfCustomers = customerController.getEntityCount();
      
      customerController.addNewEntity();
      Assert.assertEquals(initialNumberOfCustomers + 1, customerController.getEntityCount());      
    
      int lastIndex = initialNumberOfCustomers;
      customerController.deleteEntity(lastIndex);
      Assert.assertEquals(initialNumberOfCustomers, customerController.getEntityCount());          
         
    }
    
    @Test(expected = Exception.class)
    public void removeWithInvalidIndexTest() throws SQLException {
      customerController.deleteEntity(customerController.getEntityCount() + 1); 
    }
    
    @Test(expected = Exception.class)
    public void removeWithNegativeIndexTest() throws SQLException {
      customerController.deleteEntity(-1); 
    }
    
    @Test
    public void entityPropertiesTest() throws SQLException {
        List<Customer> customers = customerController.getEntities();
        
        for(Customer customer : customers){
            Assert.assertNotNull(customer.getCustomerID());
            Assert.assertNotNull(customer.getAddress());
            Assert.assertNotNull(customer.getEmail());
            Assert.assertNotNull(customer.getName());
            Assert.assertNotNull(customer.getTelephone());
        }     
        
    }
    
    @Test
    public void getEntityTest() throws SQLException {
        int numberOfEntities = customerController.getEntityCount();
        for(int i = 0; i < numberOfEntities; i++){
            Assert.assertNotNull(customerController.getEntityByRowIndex(i));
        }
    }
    
    
   
}   
