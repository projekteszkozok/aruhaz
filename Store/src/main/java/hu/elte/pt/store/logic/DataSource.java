package hu.elte.pt.store.logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Singleton pattern szerint elkészített osztály, mely az adatbázis kapcsolat és az entitásokhoz tartozó controllerek lekérésére szolgál.
 * @author Nagy Krisztián
 */
public class DataSource {
    
    private final StoreConfiguration config;
    private static final Logger log = Logger.getLogger(DataSource.class);
    
    private DataSource(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("store-config.xml");
        config = (StoreConfiguration) context.getBean("StoreConfiguration");
        final StringBuilder logMessageBuilder = new StringBuilder()
                .append("Az adatbázis konfiguráció az alábbi értékekkel betöltődött: ")
                .append("[CONNECTION : ").append(config.getConnectionString())
                .append(" , USER: ").append(config.getUserName())
                .append(" , PASSWORD: ").append(config.getPassword()).append("]");
        log.info(logMessageBuilder.toString());
        
        //Kontroller példányok létrehozása 
    }
    
    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(config.getConnectionString(), config.getUserName(), config.getPassword());
    }
    
    public static DataSource getInstance(){
        return DataSourceHolder.INSTANCE;
    }
    
    private static class DataSourceHolder{
        private static final DataSource INSTANCE = new DataSource();
    }
}
