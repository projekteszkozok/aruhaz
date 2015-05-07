/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.elte.pt.store.test.logic.controller;


import hu.elte.pt.store.logic.controllers.OrderController;
import hu.elte.pt.store.logic.entities.Order;
import java.sql.SQLException;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
/**
 *
 * @author Zoltán
 * Order Controllerhez tartozó tesztek
 */

public class OrderControllerTest {
    
    public OrderController orderController = new OrderController();
       
  
    @Test
    public void addAndRemoveTest() throws SQLException {
      int initialNumberOfCustomers = orderController.getEntityCount();
      
      orderController.addNewEntity();
      Assert.assertEquals(initialNumberOfCustomers + 1, orderController.getEntityCount());      
    
      int lastIndex = initialNumberOfCustomers;
      orderController.deleteEntity(lastIndex);
      Assert.assertEquals(initialNumberOfCustomers, orderController.getEntityCount());          
         
    }
    
    
    @Test(expected = Exception.class)
    public void removeWithInvalidIndexTest() throws SQLException {
      orderController.deleteEntity(orderController.getEntityCount() + 1); 
    }
    
    
    
    @Test
    public void entityPropertiesTest() throws SQLException {
        List<Order> orders = orderController.getEntities();
        
        for(Order order : orders){
            Assert.assertNotNull(order.getOrderID());
            Assert.assertNotNull(order.getCustomer());
            Assert.assertNotNull(order.getProduct());
        }     
        
    }
    
}
