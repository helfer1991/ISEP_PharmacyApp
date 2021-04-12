package lapr.project.data;

import lapr.project.model.*;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class TransferDBTest {

    private final Product product1 = new Product(1, "prod1", 4.3f, 5);

    private final Pharmacy pharmacy1 = new Pharmacy("nome", 1,
            new Park(11, 50, 50, 30, 30),
            new Address(3, 20, -32, "rua", "2500", 100),
            33.2f, 15.5f);

    private final Pharmacy pharmacy2 = new Pharmacy("nome", 2,
            new Park(11, 50, 50, 30, 30),
            new Address(3, 20, -32, "rua", "2500", 100),
            33.2f, 15.5f);

    private Transfer transfer = new Transfer(pharmacy1, pharmacy2, product1, 4, 1);

    TransferDB transferDB = new TransferDB();


    void insertTransfer() throws SQLException {
        int id =  transferDB.insertTransferDocument("this is a test document");
        transferDB.insertTransfer(transfer,id);
    }


}