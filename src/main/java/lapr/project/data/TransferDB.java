package lapr.project.data;

import java.sql.Connection;

import lapr.project.model.Product;
import lapr.project.model.Transfer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TransferDB extends DataHandler {

    public TransferDB() {

    }

    public boolean validateIncomingTransfer(Transfer transfer) throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        Statement stmt2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            stmt2 = conn.createStatement();

            rs = stmt.executeQuery("select ID_TRANSFER  from Transfer " +
                    "where FK_PHARMACY_ID_RECEIVER= " + transfer.getPharmacyAsking().getId() + " " +
                    "and FK_PHARMACY_ID_ISSUER= " + transfer.getPharmacySending().getId() + " " +
                    "and FK_PRODUCT_ID= " + transfer.getProduct().getId() + " " +
                    "and QUANTITY= " + transfer.getQuantity() + " order by DATE_ENTRY");

            while (rs.next()) {//if rs.next() is true it means there is at least one id
                int transferId = rs.getInt(1);
                rs2 = stmt2.executeQuery("select FK_TRANSFER_STATUS_ID from transfer_status " +
                        "where FK_TRANSFER_ID= " + transferId +
                        " and DATE_ENTRY = (select max(date_entry) from TRANSFER_STATUS " +
                        "                   where fk_transfer_id = " + transferId + ")");

                if (rs2.next() && rs2.getInt("fk_transfer_status_id") != 3){
                    transfer.setTransferId(transferId);
                    return true;
                }
            }
            return true;
            //return false;



        } catch (SQLException e) {
            throw new SQLException("Error at insertTransfer " + e.getMessage());
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


    public boolean insertTransfer(Transfer transfer, int transferNoteId) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery("select nvl( max(ID_TRANSFER) , 0)+1 as max from Transfer ");
            rs.next();
            int nextId = rs.getInt("max");
            transfer.setTransferId(nextId);

            int affectedRows = stmt.executeUpdate("insert into transfer(id_transfer, fk_pharmacy_id_issuer," +
                    "fk_pharmacy_id_receiver, fk_document_transfernote, fk_document_delivery_note, fk_order_id," +
                    "fk_product_id, quantity, date_entry)\n" +
                    "values( "
                    + nextId + ", "
                    + transfer.getPharmacySending().getId() + ", "
                    + transfer.getPharmacyAsking().getId() + ", "
                    + transferNoteId + ", "
                    + "null , "
                    + transfer.getOrderId() + ", "
                    + transfer.getProduct().getId() + ", "
                    + transfer.getQuantity() + ", "
                    + "sysdate )");


            if (affectedRows == 1) {
                return markTransferAsReady(transfer);
            } else {
                throw new SQLException("Transfer not inserted correctly");
            }

        } catch (SQLException e) {
            throw new SQLException("Error at insertTransfer " + e.getMessage());
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


    public int insertTransferDocument(String documentContent) throws SQLException {
        int transferDocId = 0;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery("select nvl( max(ID_DOCUMENT) , 0)+1 as max from Transfer_Document ");
            rs.next();
            transferDocId = rs.getInt("max");

            int affectedRows = stmt.executeUpdate("insert into TRANSFER_DOCUMENT(ID_DOCUMENT , DOCUMENTCONTENT) " +
                    "values ( " + transferDocId + ", '" + documentContent + "' ) ");

            if (affectedRows != 1) {
                throw new SQLException("Transfer document not inserted correctly.");
            }

            return transferDocId;
        } catch (SQLException e) {
            throw new SQLException("Error at insertTransferDocument " + e.getMessage());
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


    private boolean insertTransferStatus(Transfer transfer, int transferStatus) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();


            int affectedRows = stmt.executeUpdate("insert into TRANSFER_STATUS(fk_transfer_id, fk_transfer_status_id, date_entry) " +
                    "values ( " + transfer.getTransferId() + ", " + transferStatus + ", sysdate ) ");


            return (affectedRows == 1);
        } catch (SQLException e) {
            throw new SQLException("Error at insertTransferStatus" + e.getMessage());
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public boolean markTransferAsReady(Transfer transfer) throws SQLException {
        int readyStatus = 1;
        return insertTransferStatus(transfer, readyStatus);
    }

    public boolean markTransferAsInTransit(Transfer transfer) throws SQLException {
        int inTransitStatus = 2;
        return insertTransferStatus(transfer, inTransitStatus);
    }

    public void markTransferAsForcedShipping(Transfer transfer) throws SQLException {
        int forcedShippingStatus = 4;
        insertTransferStatus(transfer, forcedShippingStatus);
    }

    public void markTransferAsReceived(Transfer transfer) throws SQLException {
        int completedStatus = 3;
        insertTransferStatus(transfer, completedStatus);
    }


    public double getTransferWaitingTime(Transfer transfer) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        int readyStatus = 1;
        double minutes;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            //assuming that for each transfer there can only be one status of 'ready'
            rs = stmt.executeQuery("SELECT ( sysdate -  DATE_ENTRY ) * 1440 FROM TRANSFER_STATUS "
                    + "where FK_TRANSFER_ID = " + transfer.getTransferId() + " and TRANSFER_STATUS.FK_TRANSFER_STATUS_ID = " + readyStatus);

            minutes = rs.getDouble(1);

            return minutes;

        } catch (SQLException e) {
            throw new SQLException("Error at getTransferWaitingTime" + e.getMessage());
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

    public boolean associateDeliveryNoteToTransfer(Transfer transfer, int deliveryNoteId) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();


            int affectedRows = stmt.executeUpdate("update TRANSFER set FK_DOCUMENT_DELIVERY_NOTE ="+deliveryNoteId+" " +
                    "where TRANSFER.id_transfer = "+transfer.getTransferId() );


            return (affectedRows == 1);
        } catch (SQLException e) {
            throw new SQLException("Error at associateDeliveryNoteTotransfer " + e.getMessage());
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }


        }
    }

    public boolean checkIfOrderHasOpenTransfers(int orderId) throws SQLException {
        int statusCompleted = 3;
        try(
                Statement stmt = getConnection().createStatement();
                ResultSet rs = stmt.executeQuery( "select t.ID_TRANSFER from TRANSFER t\n" +
                        "join TRANSFER_STATUS on t.ID_TRANSFER = TRANSFER_STATUS.FK_TRANSFER_ID\n" +
                        "where FK_ORDER_ID = "+orderId+"\n" +
                        "and "+statusCompleted+" !=  (select FK_TRANSFER_STATUS_ID\n" +
                        "               from TRANSFER_STATUS\n" +
                        "               where TRANSFER_STATUS.FK_TRANSFER_ID= t.ID_TRANSFER\n" +
                        "               and TRANSFER_STATUS.DATE_ENTRY = (select max(DATE_ENTRY) " +
                        "                                               from TRANSFER_STATUS " +
                        "                                               where TRANSFER_STATUS.FK_TRANSFER_ID = t.ID_TRANSFER))" );


                ) {
            return rs.next();

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            closeAll();
        }
    }
}