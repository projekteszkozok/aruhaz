/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.pt.store.logic.entities;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author Cseh Zoltán
 */
public class Order implements Entity {

    private Integer orderID;
    //private Product productsID;
    private Integer productID;
    private Customer ordererID;


    public Order(){ 
    }
    
    public Order(Integer orderID){
        this.orderID = orderID;
    }
    
    //private List<Products> products;

    public Customer getOrdererID() {
        return ordererID;
    }

    public void setOrdererID(Customer ordererID) {
        this.ordererID = ordererID;
    }
    
    public Integer getOrderID() {
        return orderID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Integer getId() {
        return orderID;
    }

    public void setId(Integer id) {
        this.orderID = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.orderID);
        hash = 79 * hash + Objects.hashCode(this.productID);
        hash = 79 * hash + Objects.hashCode(this.ordererID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (!Objects.equals(this.orderID, other.orderID)) {
            return false;
        }
        if (!Objects.equals(this.productID, other.productID)) {
            return false;
        }
        if (!Objects.equals(this.ordererID, other.ordererID)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Order{"
                + "orderID=" + orderID + ","
                + "productID=" + productID + ","
                + "ordererID=" + ordererID + '}';

    }

}
