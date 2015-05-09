package hu.elte.pt.store.logic.entities;

import java.util.Objects;

/**
 * A Category tábla Java-s megfelelője.
 *
 * @author Nagy Krisztián
 * @version 0.%I%
 */
public class Category implements Entity {

    /**
     * Az adott kategóriának az azonosítóját tároló mező. Az aktuális táblában
     * ez az elsődleges kulcsnak felel meg.
     */
    private Integer categoryId;
    /**
     * A ketegória nevét tároló mező.
     */
    private String name;

    /**
     * Tömb, amely eltárolja a megjelenítéshez a fejlécek nevét.
     */
    public static final String[] fieldNames = new String[]{"Kategória"};

    /**
     * Egy kategória azonosítójának lekérdezését elősegítő getter metódus.
     *
     * @return a kategória azonosítója
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * Egy kategória beállítását elősegítő eljárás.
     *
     * @param categoryId az azonosító, amelyet szeretnénk eltárolni.
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Egy kategória nevének lekérdezését elősegítő getter metódus.
     *
     * @return a kategória neve
     */
    public String getName() {
        return name;
    }

    /**
     * Egy kategória nevének beállítását elősegítő setter eljárás.
     *
     * @param name a név, amit szeretnénk eltárolni.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash += 71 * hash + Objects.hashCode(this.categoryId);
        hash += 71 * hash + Objects.hashCode(this.name);
        return hash;
    }

    /**
     * @param object Az objektum, melyet össze szeretnénk hasonlítani az
     * aktuálissal.
     * @see Object#equals(java.lang.Object)
     */
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

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return getName();
    }
}
