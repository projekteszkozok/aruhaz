/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.pt.store.logic.entities;


import java.util.Objects;

/**
 *
 * @author Cseh Zoltán
 */
public class Order implements Entity {

    public final static String[] fieldNames = new String[]{"Termék", "Vásárló"};
    
    private Integer orderID;
    //private Product productsID;
    private Product product;

    private Customer customer;

    public Integer getOrderID() {
        return orderID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.orderID);
        hash = 19 * hash + Objects.hashCode(this.product);
        hash = 19 * hash + Objects.hashCode(this.customer);
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
        if (!Objects.equals(this.product, other.product)) {
            return false;
        }
        if (!Objects.equals(this.customer, other.customer)) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {
        return "Order{"
                + "orderID=" + orderID + ","
                + "productID=" + product + ","
                + "ordererID=" + customer + '}';
        
    }

}
