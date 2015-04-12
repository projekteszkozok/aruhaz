package hu.elte.pt.store.logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import org.apache.log4j.Logger;

/**
 * Az osztály-t felhasználva létrehozhatjuk az adatbázisban a Dummy táblákat és feltölthetjük adatokkal a mellékelt dummy.sql fájlból.
 * @author Nagy Krisztián
 */
public class DatabaseSetup {
    
    private static final Logger log = Logger.getLogger(DatabaseSetup.class);
    
    public static void main(String[] args) throws SQLException{
        try (
                Scanner scanner = new Scanner(new File("src/main/resources/dummy.sql"));
                Connection connection = DataSource.getInstance().getConnection();
                Statement stmt = connection.createStatement()) {
            scanner.useDelimiter(";");
            while (scanner.hasNext()) {
                String sql = scanner.next().trim();
                if (sql.equals("")) {
                    continue;
                }
                if (!sql.toUpperCase().startsWith("SELECT")) {
                    try {
                        stmt.execute(sql);
                        log.info(sql.replaceAll("\\s+", " ") + " : OK");
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                        log.error(sql.replaceAll("\\s+", " ") + " : ERROR", ex);
                    }
                }
            }
        } catch(SQLException ex){
            log.error("Az adatbázis műveletek futtatása során kivétel keletkezett! ",ex);
        } catch(FileNotFoundException ex){
            log.error("A dummy.sql fájl nem található a megadott helyen! ", ex);
        }     
    }
}
