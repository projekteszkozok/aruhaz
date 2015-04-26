package hu.elte.pt.store.logic.entities;

import java.util.Objects;

/**
 * A Category tábla Java megfelelője.
 * @author Nagy Krisztián
 */
public class Category implements Entity{
    
    private Integer categoryId;
    private String name;
    
    /**
     * Tömb, amely eltárolja a megjeleníteni kívánt táblázathoz a fejlécek nevét
     */
    public static final String[] fieldNames = new String[]{"Kategória"};

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash +=  71 * hash + Objects.hashCode(this.categoryId);
        hash += 71 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        } 
        
        final Category other = (Category) object;
        
        return (Objects.equals(this.categoryId, other.categoryId) && Objects.equals(this.name, other.name));
    }

    @Override
    public String toString() {
        return getName();
    }    
}
