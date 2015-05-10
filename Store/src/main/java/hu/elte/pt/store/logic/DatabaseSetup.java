package hu.elte.pt.store.logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import org.apache.log4j.Logger;

/**
 * Az osztály-t felhasználva létrehozhatjuk az adatbázisban a táblákat és feltölthetjük minta adatokkal a mellékelt dummy.sql fájlból.
 * @author Nagy Krisztián
 * @version 1.0
 */
public class DatabaseSetup {
    /**
     * Naplózást elősegítő mező
     */
    private static final Logger log = Logger.getLogger(DatabaseSetup.class);
    
    /**
     * Főprogram, mely lehetővé teszi egy .sql fájl feldolgozását és a benne található nem select-el kezdődő műveletek végrehajtását.
     * @param args parancssori argumentum tömb
     * @throws SQLException sikertelen adatbázisművelet végrehajtása során keletkező kivétel
     * @see java.sql.SQLException
     */
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
                        log.error(sql.replaceAll("\\s+", " ") + " : FAILED", ex);
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
