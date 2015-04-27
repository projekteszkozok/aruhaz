package hu.elte.pt.store.logic.entities;

import java.util.Objects;

/**
 * A Product tábla Java megfelelője.
 *
 * @author Bojtos Csaba
 */
public class Product implements Entity {

    private Integer productID;
    private String productName;
    private Manufacturer manufacturer;
    private String description;
    private Category category;
    private Integer price;
    private Integer stock;
    private Boolean isActive;

    /**
     * Tömb, amely eltárolja a megjeleníteni kívánt táblázathoz a fejlécek nevét
     */
    public static final String[] fieldNames = new String[]{"Terméknév", "Gyártó", "Leírás",
        "Kategória", "Ár", "Raktárkészlet", "Elérhető"};

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.productID);
        hash = 17 * hash + Objects.hashCode(this.productName);
        hash = 17 * hash + Objects.hashCode(this.manufacturer);
        hash = 17 * hash + Objects.hashCode(this.description);
        hash = 17 * hash + Objects.hashCode(this.category);
        hash = 17 * hash + Objects.hashCode(this.price);
        hash = 17 * hash + Objects.hashCode(this.stock);
        hash = 17 * hash + Objects.hashCode(this.isActive);
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
        final Product other = (Product) obj;
        if (!Objects.equals(this.productID, other.productID)) {
            return false;
        }
        if (!Objects.equals(this.productName, other.productName)) {
            return false;
        }
        if (!Objects.equals(this.manufacturer, other.manufacturer)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.category, other.category)) {
            return false;
        }
        if (!Objects.equals(this.price, other.price)) {
            return false;
        }
        if (!Objects.equals(this.stock, other.stock)) {
            return false;
        }
        return Objects.equals(this.isActive, other.isActive);
    }

    @Override
    public String toString() {
        return productName;
    }
}
