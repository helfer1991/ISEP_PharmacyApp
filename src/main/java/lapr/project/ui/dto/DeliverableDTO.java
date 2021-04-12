package lapr.project.ui.dto;

public interface DeliverableDTO {

    int getId();

    void setId(int id);
    
    ShoppingCartDTO getShopCart();

    void setShopCart(ShoppingCartDTO shopCart);
    
    AddressDTO getAddress();

    void setAddress(AddressDTO address);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();
}
