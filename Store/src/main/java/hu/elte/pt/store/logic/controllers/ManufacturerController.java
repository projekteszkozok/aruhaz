package hu.elte.pt.store.logic.controllers;

import hu.elte.pt.store.logic.DataSource;
import hu.elte.pt.store.logic.entities.Manufacturer;
import hu.elte.pt.store.logic.entities.Store;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Manufacturer controller osztálya
 *
 * @author Honti Dora
 */
public class ManufacturerController implements EntityController<Manufacturer> {

    private static final Logger log = Logger.getLogger(ManufacturerController.class);
    private final String TABLE_NAME = "MANUFACTURER";
    private final String FULL_SELECT_SQL = "SELECT * FROM " + TABLE_NAME;

    @Override
    public Manufacturer getEntityById(int entityId) throws SQLException {
        Manufacturer manufacturer = new Manufacturer();
        int storeId;

        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL + " WHERE MANUFACTURER_ID = " + entityId);) {
            resultSet.next();
            manufacturer.setManufacturerId(resultSet.getInt(1));
            manufacturer.setName(resultSet.getString(2));
            manufacturer.setContactName(resultSet.getString(3));
            manufacturer.setCity(resultSet.getString(4));
            manufacturer.setPhone(resultSet.getString(5));
            storeId = resultSet.getInt(6);

            log.debug("A táblában az azonosító alapján történő keresés sikeres volt. "
                    + "ResultSet: "
                    + resultSet.getInt(1) + " "
                    + resultSet.getString(2) + " "
                    + resultSet.getString(3) + " "
                    + resultSet.getString(4) + " "
                    + resultSet.getString(5) + " "
                    + resultSet.getInt(6)
            );
        } catch (SQLException ex) {
            log.error("A táblában az [" + entityId + "] azonosító alapján történő keresés során kivétel keletkezett", ex);
            throw new SQLException("A táblában az [" + entityId + "] azonosító alapján történő keresés sikertelen volt");
        }

        Store store = DataSource.getInstance().getStoreController().getEntityById(storeId);
        manufacturer.setStore(store);

        return manufacturer;
    }

    @Override
    public Manufacturer getEntityByRowIndex(int rowIndex) throws SQLException {
        Manufacturer manufacturer = new Manufacturer();
        int storeId;
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            resultSet.absolute(rowIndex + 1);
            manufacturer.setManufacturerId(resultSet.getInt(1));
            manufacturer.setName(resultSet.getString(2));
            manufacturer.setContactName(resultSet.getString(3));
            manufacturer.setCity(resultSet.getString(4));
            manufacturer.setPhone(resultSet.getString(5));
            storeId = resultSet.getInt(6);

            log.debug("A táblában a sorindex alapján történő keresés sikeres volt."
                    + "ResultSet: "
                    + resultSet.getInt(1) + " "
                    + resultSet.getString(2) + " "
                    + resultSet.getString(3) + " "
                    + resultSet.getString(4) + " "
                    + resultSet.getString(5) + " "
                    + resultSet.getInt(6)
            );
        } catch (SQLException ex) {
            log.error("A táblában a sorindex alapján történő keresés során kivétel keletkezett! ", ex);
            throw new SQLException("A táblában a sorindex alapján történő keresés sikertelen volt!");
        }

        Store store = DataSource.getInstance().getStoreController().getEntityById(storeId);
        manufacturer.setStore(store);

        return manufacturer;
    }

    @Override
    public int getEntityCount() throws SQLException {
        int manufacturerCount = 0;
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS CNT FROM MANUFACTURER");) {
            resultSet.next();
            manufacturerCount = resultSet.getInt("CNT");
            log.debug("A táblában fellelhető sorok számlálása sikeresen lezajlott " + manufacturerCount + " értékkel.");
        } catch (SQLException ex) {
            log.error("A táblában fellelhető sorok számlálásának lekérdezése során kivétel keletkezett! ", ex);
            throw new SQLException("A táblában fellelhető sorok számlálásának lekérdezése során kivétel keletkezett!");
        }
        return manufacturerCount;
    }

    @Override
    public void updateEntity(Manufacturer entity, final int rowIndex) throws SQLException {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            resultSet.absolute(rowIndex + 1);
            resultSet.updateInt("MANUFACTURER_ID", entity.getManufacturerId());
            resultSet.updateString("NAME", entity.getName());
            resultSet.updateString("CONTACT_NAME", entity.getContactName());
            resultSet.updateString("CITY", entity.getCity());
            resultSet.updateString("TELEPHONE", entity.getPhone());
            resultSet.updateInt("STORE_ID", entity.getStore().getStoreId());

            resultSet.updateRow();
            
            log.info("A(z) (" + entity.getManufacturerId() + ") azonosítójú sor sikeresen módosult. Az új név: " + entity.getName() + ", kapcsolattartó: " + entity.getContactName() + ", telephely: " + entity.getCity() + ", telefonszám " + entity.getPhone()+ ", raktár " + entity.getStore().getName() + " lett.");
        } catch (SQLException ex) {
            log.error("A táblában található" + entity.getManufacturerId() + " azonosíítóval rendelkező sor módosítása során kivétel keletkezett!", ex);
            throw new SQLException("A táblában található" + entity.getManufacturerId() + " azonosíítóval rendelkező sor módosítása sikertelen volt!");
        }
    }

    @Override
    public int addNewEntity() throws SQLException {
        if(DataSource.getInstance().getStoreController().getEntityCount() < 1) throw new SQLException("Nem található raktár!");
        
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            resultSet.moveToInsertRow();
            resultSet.updateInt("MANUFACTURER_ID", DataSource.getInstance().obtainNewId());
            resultSet.updateString("NAME", "<új gyártó neve>");
            resultSet.updateString("CONTACT_NAME", "<kapcsolattartó");
            resultSet.updateString("CITY", "<telephely>");
            resultSet.updateString("TELEPHONE", "<telefonszám>");
            resultSet.updateInt("STORE_ID", DataSource.getInstance().getStores().get(0).getStoreId());
            resultSet.insertRow();
            log.info("Az új gyártó létrehozása sikeres volt.");
        } catch (SQLException ex) {
            log.error("A táblán történő új sor létrehozása során kivétel keletkezett! ", ex);
            throw new SQLException("Nem sikerült új sort létrehozni a táblában!");
        }

        try {
            return getEntityCount() - 1;
        } catch (SQLException ex) {
            log.error("A táblán történő új sor hozzáadása folyamán, az új sor indexének megállapítása közben kivétel váltódott ki!");
            throw new SQLException("A táblán történő új sor hozzáadása folyamán, az új sor indexének megállapítása közben kivétel váltódott ki!");
        }

    }

    @Override
    public void deleteEntity(int rowIndex) throws SQLException {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            resultSet.absolute(rowIndex + 1);
            resultSet.deleteRow();
            log.info("A kiválasztott [ " + rowIndex + " ] sorindex-ű elem törlése sikeres volt");
        } catch (SQLException ex) {
            log.error("A kiválasztott [ " + rowIndex + " ] sorindex-ű elem törlése során kivétel keletkezett, így a törlés nem valósult meg! ", ex);
            throw new SQLException("A kiválasztott [ " + rowIndex + " ] sorindex-ű elem törlése során kivétel keletkezett, így a törlés nem valósult meg!");
        }
    }

    @Override
    public List<Manufacturer> getEntities() throws SQLException {
        List<Manufacturer> manufacturers = new ArrayList<>();
        int storeId;

        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(FULL_SELECT_SQL)) {
            while (rs.next()) {
                Manufacturer manufacturer = new Manufacturer();
                manufacturer.setManufacturerId(rs.getInt(1));
                manufacturer.setName(rs.getString(2));
                manufacturer.setContactName(rs.getString(3));
                manufacturer.setCity(rs.getString(4));
                manufacturer.setPhone(rs.getString(5));
                storeId = rs.getInt(6);
                Store store = DataSource.getInstance().getStoreController().getEntityById(storeId);
                manufacturer.setStore(store);

                manufacturers.add(manufacturer);
            }
        }

        return manufacturers;
    }
}
