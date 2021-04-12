package lapr.project.ui.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author 1100241
 */
public class ShoppingCartDTO {

    private Map<ProductDTO, Integer> productMap;
    private float totalWeight;

    public ShoppingCartDTO() {
        productMap = new HashMap<>();
    }

    public ShoppingCartDTO(Map<ProductDTO, Integer> productMap) {
        this.productMap = productMap;
    }
        

    public void addProductToShoppingCart(ProductDTO p, int quantity) {
        int q = 0;
        if(!productMap.containsKey(p)){
            productMap.put(p, quantity);
        }else{
            q = productMap.get(p);
            q = quantity + q;
            productMap.put(p, q);
        }
        totalWeight = quantity * p.getWeight() + totalWeight;
    }

    public Map<ProductDTO, Integer> getProductMap() {
        return productMap;
    }

    public float getShoppingCartWeight() {
        return totalWeight;
    }
    
    /**
     * Method calculates the total cost of the shopping cart bases on the Products
     * in the map, their cost in ProductDTO object and the quantity from the map.
     * @return total cost in double
     */
    public double getTotalCostShoppingCart(){
        Double totalCost = 0d;
        for (Map.Entry<ProductDTO, Integer> entry : productMap.entrySet()) {
            ProductDTO key = entry.getKey();
            Integer value = entry.getValue();
            totalCost = totalCost + (key.getPrice()*value);
        }
        return totalCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShoppingCartDTO)) return false;
        ShoppingCartDTO that = (ShoppingCartDTO) o;
        return Float.compare(that.totalWeight, totalWeight) == 0
                && Objects.equals(getProductMap(), that.getProductMap());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductMap(), totalWeight);
    }
}
