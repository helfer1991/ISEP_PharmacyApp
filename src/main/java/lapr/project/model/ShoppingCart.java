package lapr.project.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 */
public class ShoppingCart {

    private final Map<Product, Integer> productMap;
    private float totalWeight;

    public ShoppingCart() {
        productMap = new HashMap<>();
    }
    
    public ShoppingCart(Map<Product, Integer> productMap) {
        this.productMap = productMap;
    }

    public void addProductToShoppingCart(Product p, int quantity) {
        if(p == null || quantity == 0){
            return;
        }
        if(productMap.containsKey(p)){
            productMap.put(p, productMap.get(p)+quantity);
        }else{
            productMap.put(p, quantity);
        }
        totalWeight = quantity * p.getWeight() + totalWeight;

    }

    public Map<Product, Integer> getProductMap() {
        return productMap;
    }

    public float getShoppingCartWeight() {
        return totalWeight;
    }
    
    /**
     * Method calculates the total cost of the shopping cart bases on the Products
     * in the map, their cost in Product object and the quantity from the map.
     * @return total cost in double
     */
    public double getTotalCostShoppingCart(){
        Double totalCost = 0d;
        for (Map.Entry<Product, Integer> entry : productMap.entrySet()) {
            Product key = entry.getKey();
            Integer value = entry.getValue();
            totalCost = totalCost + (key.getPrice()*value);
        }
        return totalCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShoppingCart)) return false;
        ShoppingCart that = (ShoppingCart) o;
        return Float.compare(that.totalWeight, totalWeight) == 0
                && Objects.equals(getProductMap(), that.getProductMap());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductMap(), totalWeight);
    }
}
