package hu.elte.pt.store.logic.controllers;

import hu.elte.pt.store.logic.DataSource;
import hu.elte.pt.store.logic.entities.Store;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Dudás Orsolya
 */
public class StoreController implements EntityController<Store> {

    private static final Logger log = Logger.getLogger(StoreController.class);
    private final String TABLE_NAME = "STORE";
    private final String FULL_SELECT_SQL = "SELECT * FROM " + TABLE_NAME;

    @Override
    public Store getEntityById(int entityId) throws SQLException {
        Store store = new Store();
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL + " WHERE STORE_ID = " + entityId);) {
            resultSet.next();
            store.setStoreId(resultSet.getInt(1));
            store.setName(resultSet.getString(2));
            store.setPlace(resultSet.getString(3));
            log.info("A táblában az azonosító alapján történő keresés sikeres volt. "
                    + "ResultSet: "
                    + resultSet.getInt(1) + " "
                    + resultSet.getString(2) + " "
                    + resultSet.getString(3)
            );
        } catch (SQLException ex) {
            log.error("A táblában az [" + entityId + "] azonosító alapján történő keresés során kivétel keletkezett", ex);
            throw new SQLException("A táblában az [" + entityId + "] azonosító alapján történő keresés sikertelen volt");
        }
        return store;
    }

    @Override
    public Store getEntityByRowIndex(int rowIndex) throws SQLException {
        Store store = new Store();
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            resultSet.absolute(rowIndex + 1);
            store.setStoreId(resultSet.getInt(1));
            store.setName(resultSet.getString(2));
            store.setPlace(resultSet.getString(3));

            log.info("A táblában a sorindex alapján történő keresés sikeres volt."
                    + "ResultSet: "
                    + resultSet.getInt(1) + " "
                    + resultSet.getString(2) + " "
                    + resultSet.getString(3)
            );
        } catch (SQLException ex) {
            log.error("A táblában a sorindex alapján történő keresés során kivétel keletkezett! ", ex);
            throw new SQLException("A táblában a sorindex alapján történő keresés sikertelen volt!");
        }
        return store;
    }

    @Override
    public int getEntityCount() throws SQLException {
        int storeCount = 0;
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS CNT FROM STORE");) {
            resultSet.next();
            storeCount = resultSet.getInt("CNT");
            log.debug("A táblában fellelhető sorok számlálása sikeresen lezajlott " + storeCount + " értékkel.");
        } catch (SQLException ex) {
            log.error("A táblában fellelhető sorok számlálásának lekérdezése során kivétel keletkezett! ", ex);
            throw new SQLException("A táblában fellelhető sorok számlálásának lekérdezése során kivétel keletkezett!");
        }
        return storeCount;
    }

    @Override
    public void updateEntity(Store entity, int rowIndex) throws SQLException {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            resultSet.absolute(rowIndex + 1);
            resultSet.updateInt(1, entity.getStoreId());
            resultSet.updateString(2, entity.getName());
            resultSet.updateString(3, entity.getPlace());

            resultSet.updateRow();
            log.info("A(z) (" + entity.getStoreId() + ") azonosítójú sor sikeresen módosult. Az új NAME: " + entity.getName() + " lett.");
        } catch (SQLException ex) {
            log.error("A táblában található" + entity.getStoreId() + " azonosíítóval rendelkező sor módosítása során kivétel keletkezett!", ex);
            throw new SQLException("A táblában található" + entity.getStoreId() + " azonosíítóval rendelkező sor módosítása sikertelen volt!");
        }
    }

    @Override
    public int addNewEntity() throws SQLException {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            resultSet.moveToInsertRow();
            resultSet.updateInt(1, DataSource.getInstance().obtainNewId());
            resultSet.updateString(2, "NAME");
            resultSet.updateString(3, "PLACE");
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
    public List<Store> getEntities() throws SQLException {
        List<Store> stores = new ArrayList<>();
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(FULL_SELECT_SQL)) {
            while (rs.next()) {
                Store store = new Store();
                store.setStoreId(rs.getInt(1));
                store.setName(rs.getString(2));
                store.setPlace(rs.getString(3));
                stores.add(store);
            }
        }
        return stores;
    }

}
