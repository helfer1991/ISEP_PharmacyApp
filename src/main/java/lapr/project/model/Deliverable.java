package lapr.project.model;

public interface Deliverable {

    int getIdOrder();

    void setIdOrder(int idOrder);
    
    ShoppingCart getShopCart();

    void setShopCart(ShoppingCart shopCart);
    
    Address getAddress();

    void setAddress(Address address);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

}
