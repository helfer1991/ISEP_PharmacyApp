package lapr.project.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import lapr.project.model.Pharmacy;
import lapr.project.model.Product;
import lapr.project.model.Storage;

public class StorageDB extends DataHandler {

    public Storage getAllProducts() throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
             rs = stmt.executeQuery("select product.id_product, product.description, product.price, product.weight from product "
                    + "order by product.description");

            Storage storage = new Storage();
            Product product = null;
            while (rs.next()) {
                product = new Product(rs.getInt("id_product"), rs.getString("description"), rs.getFloat("price"), rs.getFloat("weight"));
                storage.addProductToStorage(product, 0);
            }
            rs.close();
            return storage;
        } catch (SQLException e) {
            throw new SQLException("Error at getAllProducts" + e.getMessage());
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            try {
                //
            } catch (Exception e) {
            }
        }

    }

    public Storage getProductsByPharmacy(Pharmacy pharmacy) throws SQLException {


        try (
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from product left join "
                    + "(select fk_pharmacy_id, fk_product_id, sum(quantity) total from pharmacy_product "
                    + "where fk_pharmacy_id = " + pharmacy.getId() + " "
                    + "group by fk_pharmacy_id, fk_product_id order by fk_pharmacy_id, fk_product_id) "
                    + "on id_product = fk_product_id")

            ){

            Storage storage = new Storage();
            Product product = null;
            while (rs.next()) {
                product = new Product(rs.getInt("id_product"), rs.getString("description"),
                        rs.getFloat("price"), rs.getFloat("weight"));

                storage.addProductToStorage(product, rs.getInt("total"));
            }
            return storage;
        } catch (SQLException e) {
            throw new SQLException("Error at getProductsByPharmacy" + e.getMessage());
        }
    }

    public Product getProductById(int productId) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Connection conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from product where PRODUCT.ID_PRODUCT = " + productId);


            rs.next();
            Product product = new Product(
                    rs.getInt("id_product"),
                    rs.getString("description"),
                    rs.getFloat("price"),
                    rs.getFloat("weight")
            );

            return product;
        } catch (SQLException e) {
            throw new SQLException("Error at getProductsByPharmacy" + e.getMessage());
       } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            try {
                //
            } catch (Exception e) {
            }
        }

    }

    public Map.Entry<Product, Integer> insertProductQuantity(Pharmacy pharmacy, Map.Entry<Product, Integer> entry) throws SQLException {
       Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery("select max(id_transaction) id from pharmacy_product");
            int id = 1;
            if (rs.next()) {
                id = rs.getInt("id") + 1;
            };

//            //Valid if it is possible to remove          
//            rs = stmt.executeQuery("select product.id_product, sum(pharmacy_product.quantity) total from product "
//                    + "join pharmacy_product on "
//                    + "product.id_product = pharmacy_product.fk_product_id "
//                    + "where pharmacy_product.fk_pharmacy_id = " + pharmacy.getId() + " "
//                    + "and product.id_product=" + entry.getKey().getId() + " "
//                    + "group by product.id_product");
//
//            int total = 0;
//            if (rs.next()) {
//                total = rs.getInt("total");
//            }
//            if (total < entry.getValue() * -1) {
//                throw new SQLException("Product stock less than " + entry.getValue() * -1);
//            }

            rs = stmt.executeQuery(CommonDB.insertPharmacyProductQuery(pharmacy, entry, id));
            rs.close();
            return entry;
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            try {
                //
            } catch (Exception e) {
            }
        }
    }

    public Product insertProduct(Product product) throws SQLException {
       Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();

             rs = stmt.executeQuery("select max(id_product) id from product");
            int id = 1;
            if (rs.next()) {
                id = rs.getInt("id") + 1;
            }
            product.setId(id);

            rs = stmt.executeQuery(CommonDB.insertProductQuery(product));
            rs.close();
            return product;
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            try {
                //
            } catch (Exception e) {
            }
        }
    }

    public Product updateProduct(Pharmacy pharmacy, Product product) throws SQLException {
       Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();

             rs = stmt.executeQuery(CommonDB.updateProductQuery(pharmacy, product));
            rs = stmt.executeQuery(CommonDB.updatePharmacyQuery(pharmacy));
            rs = stmt.executeQuery(CommonDB.updateParkQuery(pharmacy.getPark()));
            rs.close();
            return product;
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            try {
                //
            } catch (Exception e) {
            }
        }
    }

    public int getProductStock(Pharmacy pharmacy, Product product) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().createStatement();
            rs = stmt.executeQuery("select QUANTITY from PHARMACY_PRODUCT where FK_PHARMACY_ID = "+pharmacy.getId()+" and FK_PRODUCT_ID = "+product.getId() );
            rs.next();
            return rs.getInt("quantity");

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public ArrayList<Integer> ordersWaitingForThisProduct(Pharmacy pharmacy, Product product) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        int statusWaitingTransfer = 2;
        try {

            stmt = getConnection().createStatement();
            rs = stmt.executeQuery( "select id_order from ORDER_ENTRY oe\n" +
                    "join ORDER_STATUS os on oe.ID_ORDER = os.FK_ORDER_ID\n" +
                    "JOIN ORDER_DETAIL ON oe.ID_ORDER = ORDER_DETAIL.FK_ORDER_ID\n" +
                    "where FK_PHARMACY_ID ="+pharmacy.getId()+"\n" +
                    "and FK_PRODUCT_ID = "+product.getId()+"\n" +
                    "and "+statusWaitingTransfer+" =  (select ORDER_STATUS.FK_STATUS_ID\n" +
                    "                           from ORDER_STATUS\n" +
                    "                           where ORDER_STATUS.FK_ORDER_ID = oe.ID_ORDER\n" +
                    "                           and ORDER_STATUS.DATE_ENTRY = (select max(ORDER_STATUS.DATE_ENTRY)" +
                    "                                                               from ORDER_STATUS" +
                    "                                                               where ORDER_STATUS.FK_ORDER_ID = oe.ID_ORDER ) )");

            ArrayList<Integer> ordersList = new ArrayList<>();

            while(rs.next()) {
                ordersList.add( rs.getInt(1) );
            }

            return ordersList;

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public ArrayList<Product> getProductsWithNegativeStockByOrderId(Pharmacy pharmacy, Integer orderId) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {

            stmt = getConnection().createStatement();
            rs = stmt.executeQuery( "select ORDER_DETAIL.FK_PRODUCT_ID\n" +
                    "from ORDER_DETAIL\n" +
                    "join PRODUCT on ORDER_DETAIL.FK_PRODUCT_ID = PRODUCT.ID_PRODUCT\n" +
                    "join PHARMACY_PRODUCT PP on PRODUCT.ID_PRODUCT = PP.FK_PRODUCT_ID\n" +
                    "where ORDER_DETAIL.FK_ORDER_ID = "+orderId+"\n" +
                    "AND PP.FK_PHARMACY_ID ="+pharmacy.getId()+"\n" +
                    "and ( select sum(PHARMACY_PRODUCT.QUANTITY)\n" +
                    "    from PHARMACY_PRODUCT\n" +
                    "    where PHARMACY_PRODUCT.FK_PHARMACY_ID = "+pharmacy.getId()+"\n" +
                    "    and PHARMACY_PRODUCT.FK_PRODUCT_ID = ORDER_DETAIL.fk_product_id) < 0\n" +
                    "group by ORDER_DETAIL.FK_PRODUCT_ID" );

            ArrayList<Product> productsList = new ArrayList<>();

            Product product;
            while(rs.next()) {

                product = getProductById( rs.getInt(1));
                productsList.add(product);
            }

            return productsList;

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }


    }
}
