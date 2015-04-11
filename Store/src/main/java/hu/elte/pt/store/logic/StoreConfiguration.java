package hu.elte.pt.store.logic;

/**
 * Egy olyan osztály, melynek egy példányába a Spring Framework segítségével bekerülnek az adatbázis kapcsolat megfelelő adatai 
 * @author Nagy Krisztián
 */
public class StoreConfiguration {
    private String connectionString;
    private String userName;
    private String password;

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
