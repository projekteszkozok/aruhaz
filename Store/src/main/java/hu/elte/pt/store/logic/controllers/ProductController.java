package hu.elte.pt.store.logic.controllers;

import hu.elte.pt.store.logic.DataSource;
import hu.elte.pt.store.logic.entities.Product;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * A Product táblán való műveletek végrehajtásáért felelős controller osztály.
 *
 * @author Bojtos Csaba
 *
 * HIÁNYOS
 *
 */
public class ProductController implements EntityController<Product> {

    private static final Logger log = Logger.getLogger(ProductController.class);
    private final String TABLE_NAME = "PRODUCT";
    private final String FULL_SELECT_SQL = "SELECT * FROM " + TABLE_NAME;

    @Override
    public Product getEntityById(int entityId) throws SQLException {
        Product product = new Product();
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL + " WHERE PRODUCT_ID = " + entityId);) {
            resultSet.next();
            product.setProductID(resultSet.getInt(1));
            //           product.setManufacturer(resultSet.getString(2));
            product.setDescription(resultSet.getString(3));
            //           product.setCategory(resultSet.getString(4));
            product.setPrice(resultSet.getInt(5));
            product.setStock(resultSet.getInt(6));
            product.setIsActive(resultSet.getBoolean(7));
            log.info("A táblában az azonosító alapján történő keresés sikeres volt. "
                    + "ResultSet: " + " "
                    + resultSet.getInt(1) + " "
                    + resultSet.getString(2) + " "
                    + resultSet.getInt(3) + " "
                    + resultSet.getString(4) + " "
                    + resultSet.getString(5) + " "
                    + resultSet.getInt(6) + " "
                    + resultSet.getInt(7) + " "
                    + resultSet.getBoolean(8)
            );
        } catch (SQLException ex) {
            log.error("A táblában az [" + entityId + "] azonosító alapján történő keresés során kivétel keletkezett", ex);
            throw new SQLException("A táblában az [" + entityId + "] azonosító alapján történő keresés sikertelen volt");
        }
        return product;
    }

    @Override
    public Product getEntityByRowIndex(int rowIndex) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getEntityCount() throws SQLException {
        int productCount = 0;
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS CNT FROM PRODUCT");) {
            resultSet.next();
            productCount = resultSet.getInt("CNT");
            log.debug("A táblában fellelhető sorok számlálása sikeresen lezajlott " + productCount + " értékkel.");
        } catch (SQLException ex) {
            log.error("A táblában fellelhető sorok számlálásának lekérdezése során kivétel keletkezett! ", ex);
            throw new SQLException("A táblában fellelhető sorok számlálásának lekérdezése során kivétel keletkezett!");
        }
        return productCount;
    }

    @Override
    public void updateEntity(Product entity, final int rowIndex) throws SQLException {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            resultSet.absolute(rowIndex + 1);
            resultSet.updateInt("CATEGORY_ID", entity.getProductID());
            //       resultSet.updateString("NAME", entity.getName());
            resultSet.updateRow();
            //        log.info("A(z) ("+ entity.getProductID() + ") azonosítójú sor sikeresen módosult. Az új NAME: " + entity.getName() + " lett.");
        } catch (SQLException ex) {
            log.error("A táblában található" + entity.getProductID() + " azonosíítóval rendelkező sor módosítása során kivétel keletkezett!", ex);
            throw new SQLException("A táblában található" + entity.getProductID() + " azonosíítóval rendelkező sor módosítása sikertelen volt!");
        }
    }

    @Override
    public int addNewEntity() throws SQLException {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            resultSet.moveToInsertRow();
            resultSet.updateInt("PRODUCT_ID", DataSource.getInstance().obtainNewId());
            resultSet.updateString("PRODUCT_NAME", "PRODUCT_NAME");
            resultSet.updateString("MANUFACTURER", "MANUFACTURER");
            resultSet.updateString("DESCRIPTION", "DESCRIPTION");
            resultSet.updateString("CATEGORY", "CATEGORY");
            resultSet.updateInt("PRICE", 0);
            resultSet.updateInt("STOCK", 0);
            resultSet.updateBoolean("IS_ACTIVE", true);
            log.info("Az új termék létrehozása sikeres volt.");
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
    public List<Product> getEntities() throws SQLException {
        List<Product> products = new ArrayList<>();
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(FULL_SELECT_SQL)) {
            while (rs.next()) {
                Product product = new Product();
                product.setProductID(rs.getInt(1));
                product.setProductName(rs.getString(2));
                product.setManufacturer(null);
                product.setDescription(rs.getString(4));
                product.setCategory(null);
                product.setPrice(rs.getInt(1));
                product.setStock(Integer.SIZE);
                product.setIsActive(Boolean.FALSE);
                products.add(product);
            }
        }
        return products;
    }

}
