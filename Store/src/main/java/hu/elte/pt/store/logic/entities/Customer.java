package hu.elte.pt.store.logic.entities;

import java.util.Objects;

/**
 * A Customer tábla Java megfelelője.
 * @author Holló Eszter
 */

public class Customer implements Entity {
    
    private Integer customerID;    
    private String name;
    private String address;
    private String telephone;
    private String email;  

    public static final String[] fieldNames = new String[]{"Vevő", "Cím", "Telefon", "Email"}; 
    
    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }  

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.customerID);
        hash = 37 * hash + Objects.hashCode(this.name);    
        hash = 37 * hash + Objects.hashCode(this.address);
        hash = 37 * hash + Objects.hashCode(this.telephone);
        hash = 37 * hash + Objects.hashCode(this.email);
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
        final Customer other = (Customer) obj;
        if (!Objects.equals(this.customerID, other.customerID)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }      
        if (!Objects.equals(this.address, other.address)) {
            return false;
        }
        if (!Objects.equals(this.telephone, other.telephone)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Customer{" + "customerID=" + customerID + ", name=" + name + ", address=" + address + ", telephone=" + telephone + ", email=" + email + '}';
    }

  
    
    
    
    
          
    
}
