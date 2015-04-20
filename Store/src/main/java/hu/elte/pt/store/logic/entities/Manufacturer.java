/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.pt.store.logic.entities;

import java.util.Objects;

/**
 * Gyártó
 * @author Honti Dora
 */
public class Manufacturer implements Entity {

    private Integer manufacturerId;
    private String name;
    private String contactName;
    private String city;
    private String phone;
    private Integer store;

    public static final String[] fieldNames = new String[]{"Gyártó"};

    public Integer getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.manufacturerId);
        hash = 71 * hash + Objects.hashCode(this.name);
        hash = 71 * hash + Objects.hashCode(this.contactName);
        hash = 71 * hash + Objects.hashCode(this.city);
        hash = 71 * hash + Objects.hashCode(this.phone);
        hash = 71 * hash + Objects.hashCode(this.store);
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
        final Manufacturer other = (Manufacturer) obj;
        if (!Objects.equals(this.manufacturerId, other.manufacturerId)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.contactName, other.contactName)) {
            return false;
        }
        if (!Objects.equals(this.city, other.city)) {
            return false;
        }
        if (!Objects.equals(this.phone, other.phone)) {
            return false;
        }
        if (!Objects.equals(this.store, other.store)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Manufacturer{" + "manufacturerId=" + manufacturerId + ", name=" + name + ", contactName=" + contactName + ", city=" + city + ", phone=" + phone + ", store=" + store + '}';
    }

}
