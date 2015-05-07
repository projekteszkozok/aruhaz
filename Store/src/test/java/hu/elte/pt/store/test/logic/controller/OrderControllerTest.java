/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.elte.pt.store.test.logic.controller;


import hu.elte.pt.store.logic.controllers.OrderController;
import java.sql.SQLException;
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
}