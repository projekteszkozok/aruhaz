package hu.elte.pt.store.logic.entities;

import java.util.Objects;

/**
 * Gyártó
 *
 * @author Honti Dora
 * @version 1.0
 */
public class Manufacturer implements Entity {

    /**
     * Az adott gyártónak az azonosítóját tároló mező. Az aktuális táblában ez az elsődleges kulcsnak felel meg.
     */
    private Integer manufacturerId;
    /**
     * A gyártó nevét tároló mező.
     */
    private String name;
    /**
     * A kapcsolattartó nevét tároló mező.
     */
    private String contactName;
    /**
     * A telephelyet tároló mező.
     */
    private String city;
    /**
     * A telefonszámot tároló mező.
     */
    private String phone;
    /**
     * A raktár nevét tároló mező.
     */
    private Store store;

    /**
     * Tömb, amely eltárolja a megjelenítéshez a fejlécek nevét.
     */
    public static final String[] fieldNames = new String[]{"Gyártó", "Kapcsolattartó", "Telephely", "Telefonszám", "Raktár"};

    /**
     * Egy gyártó azonosítójának lekérdezését elősegítő getter metódus.
     *
     * @return a gyártó azonosítója
     */
    public Integer getManufacturerId() {
        return manufacturerId;
    }

    /**
     * Egy gyártó beállítását elősegítő eljárás.
     *
     * @param manufacturerId az azonosító, amelyet szeretnénk eltárolni.
     */
    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    /**
     * Egy gyártó nevének lekérdezését elősegítő getter metódus.
     *
     * @return a gyártó neve
     */
    public String getName() {
        return name;
    }

    /**
     * Egy gyártó nevének beállítását elősegítő setter eljárás.
     *
     * @param name a név, amit szeretnénk eltárolni.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Egy gyártó kapcsolattartójának lekérdezését elősegítő getter metódus.
     *
     * @return a gyártó kapcsolattartója
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Egy gyártó kapcsolattartójának beállítását elősegítő setter eljárás.
     *
     * @param contactName a kapcsolattartó neve, amit szeretnénk eltárolni.
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Egy gyártó telephelyének lekérdezését elősegítő getter metódus.
     *
     * @return a gyártó telephelye
     */
    public String getCity() {
        return city;
    }

    /**
     * Egy gyártó telephelyének beállítását elősegítő setter eljárás.
     *
     * @param city a telephely, amit szeretnénk eltárolni.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Egy gyártó telefonszámának lekérdezését elősegítő getter metódus.
     *
     * @return a gyártó telefonszáma
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Egy gyártó telefonszámának beállítását elősegítő setter eljárás.
     *
     * @param phone a telefonszám, amit szeretnénk eltárolni.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Egy raktár lekérdezését elősegítő getter metódus.
     *
     * @return a raktár
     */
    public Store getStore() {
        return store;
    }

    /**
     * Egy raktár beállítását elősegítő setter eljárás.
     *
     * @param store a raktár, amit szeretnénk eltárolni.
     */
    public void setStore(Store store) {
        this.store = store;
    }

    /**
     * @see Object#hashCode()
     */
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

    /**
     * @param obj Az objektum, melyet össze szeretnénk hasonlítani az aktuálissal.
     * @see Object#equals(java.lang.Object)
     */
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

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return name;
    }
}
