package hu.elte.pt.store.logic.entities;

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
        int hash = 0;
        hash += (categoryId != null ? categoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        return (this.categoryId != null || other.categoryId == null) && (this.categoryId == null || this.categoryId.equals(other.categoryId));
    }

    @Override
    public String toString() {
        return getName();
    }    
}
