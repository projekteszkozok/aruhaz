/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.elte.pt.store.logic.entities;


/**
 *
 * @author Cseh Zoltán
 */

public class Order implements Entity {


    private Integer orderID;
    private Integer productID;
    private Integer ordererID;
    

    public Integer getOrdererID() {
        return ordererID;
    }

    public void setOrdererID(Integer ordererID) {
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
        int hash = 0;
        hash += (orderID != null ? orderID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Order)) {
            return false;
        }
        
        Order other = (Order) object;
        if ((this.orderID == null && other.orderID != null) ||
            (this.orderID != null && !this.orderID.equals(other.orderID))) {
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
