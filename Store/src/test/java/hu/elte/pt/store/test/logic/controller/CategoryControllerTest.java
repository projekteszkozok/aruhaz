package hu.elte.pt.store.test.logic.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import hu.elte.pt.store.logic.controllers.CategoryController;
import hu.elte.pt.store.logic.entities.Category;
import java.sql.SQLException;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Holló Eszter
 * Category Controllerhez tartozó tesztek
 */
public class CategoryControllerTest {
    
    public CategoryController categoryController = new CategoryController();
       
  
    @Test
    public void addAndRemoveTest() throws SQLException {
      int initialNumberOfCategories = categoryController.getEntityCount();
      
      categoryController.addNewEntity();
      Assert.assertEquals(initialNumberOfCategories + 1, categoryController.getEntityCount());      
    
      int lastIndex = initialNumberOfCategories;
      categoryController.deleteEntity(lastIndex);
      Assert.assertEquals(initialNumberOfCategories, categoryController.getEntityCount());          
         
    }
    
    @Test(expected = Exception.class)
    public void removeWithInvalidIndexTest() throws SQLException {
      categoryController.deleteEntity(categoryController.getEntityCount() + 1); 
    }
    
    @Test(expected = Exception.class)
    public void removeWithNegativeIndexTest() throws SQLException {
      categoryController.deleteEntity(-1); 
    }
    
    
    @Test
    public void entityPropertiesTest() throws SQLException {
        List<Category> categories = categoryController.getEntities();
        
        for(Category category : categories){
            Assert.assertNotNull(category.getCategoryId());
            Assert.assertNotNull(category.getName());          
        }     
        
    }
    
    @Test
    public void getEntityTest() throws SQLException {
        int numberOfEntities = categoryController.getEntityCount();
        for(int i = 0; i < numberOfEntities; i++){
            Assert.assertNotNull(categoryController.getEntityByRowIndex(i));
        }
    }
   
}   
