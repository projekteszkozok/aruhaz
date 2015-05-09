package hu.elte.pt.store.logic;

/**
 * Egy olyan osztály, melynek egy példányába a Spring Framework segítségével bekerülnek az adatbázis kapcsolat megfelelő adatai 
 * @author Nagy Krisztián
 * @version 1.%I%
 */
public class StoreConfiguration {
    /**
     * Az adatbáziskapcsolat elérési címét tartalmazó mező
     */
    private String connectionString;
    /**
     * Az adatbázisban szereplő felhasználó
     */
    private String userName;
    /**
     * A felhasználóhoz tartozó jelszó.
     */
    private String password;

    /**
     * Az adatbáziskapcsolat eléréséi címének elkérését elősegítő metódus.
     * @return adatbáziskapcsolat eléréséi címét tartalmazó String
     */
    public String getConnectionString() {
        return connectionString;
    }

    /**
     * Az adatbáziskapcsolat eléréséi címének beállítását elősegítő eljárás.
     * @param connectionString a cím, melyet el szeretnénk tárolni.
     */
    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    /**
     * A felhasználó nevének lekérdezését elősegítő metódus
     * @return a felhasználó neve.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * A felhasználó nevének beállítását elősegítő eljárás.
     * @param userName a név, amit el szeretnénk tárolni.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * A jelszó lekérdezését elősegítő metódus
     * @return a jelszó.
     */
    public String getPassword() {
        return password;
    }

    /**
     * A jelszó beállítását elősegítő eljárás.
     * @param password a jelszó, amit el szeretnénk tárolni.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
