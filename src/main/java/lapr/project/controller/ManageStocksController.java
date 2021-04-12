package lapr.project.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import lapr.project.ui.dto.PharmacyDTO;
import lapr.project.ui.dto.ProductDTO;
import lapr.project.ui.dto.StorageDTO;

import lapr.project.model.PharmaDeliveriesApp;
import lapr.project.model.Pharmacy;
import lapr.project.model.Product;
import lapr.project.model.Storage;

import static lapr.project.controller.DTOConverter.*;

/**
 *
 */
public class ManageStocksController {

    private PharmaDeliveriesApp pharmaDeliveriesApp;

    /**
     *
     */
    public ManageStocksController() {
        this.pharmaDeliveriesApp = PharmaDeliveriesApp.getInstance();
    }

    public StorageDTO getProductsByPharmacy(PharmacyDTO pharmacyDTO) throws SQLException {
        if (pharmacyDTO == null) {
            return null;
        }

        Pharmacy pharmacy = convertPharmacyDTO(pharmacyDTO);
        Storage tmp = PharmaDeliveriesApp.getInstance().getServiceStorage().getProductsByPharmacy(pharmacy);
        if (tmp == null) {
            return null;
        }
        return convertStorage(tmp);
    }

    public Map.Entry<ProductDTO, Integer> insertProductQuantity(PharmacyDTO pharmacyDTO, Map.Entry<ProductDTO, Integer> entry) throws SQLException {
        if (pharmacyDTO == null || entry.getKey() == null || entry.getValue() == null) {
            return null;
        }
        Pharmacy pharmacy = convertPharmacyDTO(pharmacyDTO);
        Map.Entry<Product, Integer> tmp = PharmaDeliveriesApp.getInstance().getServiceStorage().insertProductQuantity(pharmacy,
                new HashMap.SimpleEntry<>(convertProductDTO(entry.getKey()), entry.getValue()));
        if (tmp == null) {
            return null;
        }
        return new HashMap.SimpleEntry<>(convertProduct(tmp.getKey()), entry.getValue());
    }

    public Map.Entry<ProductDTO, Integer> removeProductQuantity(PharmacyDTO pharmacyDTO, Map.Entry<ProductDTO, Integer> entry) throws SQLException {
        if (pharmacyDTO == null || entry.getKey() == null || entry.getValue() == null) {
            return null;
        }
        Pharmacy pharmacy = convertPharmacyDTO(pharmacyDTO);
        Map.Entry<Product, Integer> tmp = PharmaDeliveriesApp.getInstance().getServiceStorage().removeProductQuantity(pharmacy,
                new HashMap.SimpleEntry<>(convertProductDTO(entry.getKey()), entry.getValue()));
        if (tmp == null) {
            return null;
        }
        return new HashMap.SimpleEntry<>(convertProduct(tmp.getKey()), entry.getValue());
    }

    public ProductDTO insertProduct(ProductDTO product) throws SQLException {
        if (product == null) {
            return null;
        }
        ProductDTO tmp = convertProduct(PharmaDeliveriesApp.getInstance().getServiceStorage().insertProduct(convertProductDTO(product)));
        if (tmp == null) {
            return null;
        }
        return tmp;
    }

    public ProductDTO updateProduct(PharmacyDTO pharmacyDTO, ProductDTO product) throws SQLException {
        if (pharmacyDTO == null || product == null) {
            return null;
        }
        Pharmacy pharmacy = convertPharmacyDTO(pharmacyDTO);
        ProductDTO tmp = convertProduct(PharmaDeliveriesApp.getInstance().getServiceStorage().updateProduct(pharmacy, convertProductDTO(product)));
        if (tmp == null) {
            return null;
        }
        return tmp;
    }

}
