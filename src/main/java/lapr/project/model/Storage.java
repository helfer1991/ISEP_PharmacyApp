package lapr.project.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 */
public class Storage {
    
    private Map<Product, Integer> productMap;

    public Storage() {
        productMap = new HashMap<>();
    }
    public Storage(Map<Product, Integer> map){
        productMap = map;
    }
    
    public void addProductToStorage(Product p, int quantity){
            productMap.put(p, quantity);
    }

    /**
     * Adds the 'this' storage with the storage passed as parameter.
     * If the storage in parameter has negative quantities they are treated as zero (doesn't cancel a negative with a positive)
     * @param storage
     */
    public void addStorage(Storage storage){
        if(storage != null) {
            Map<Product, Integer> mapToSum = storage.getProductMap();
            Map<Product, Integer> newProductMap = new HashMap<>(productMap);
            for(Product p : mapToSum.keySet()){
                if(!newProductMap.containsKey(p) && mapToSum.get(p)>0){
                    newProductMap.put(p, mapToSum.get(p));
                }
                else if(newProductMap.containsKey(p) && mapToSum.get(p)>0){
                        newProductMap.put(p, newProductMap.get(p)+mapToSum.get(p));
                }else{
                    continue;
                }
            }
            this.productMap = newProductMap;
        }
    }

    /**
     * Subtracts the storage passed as parameter form 'this' storage.
     * If the storage in the parameter has negative quantities they are treated as zero
     * @param storage
     */
    public void subtractStorage(Storage storage){
        if(storage != null) {
            Map<Product, Integer> mapToSubtract = storage.getProductMap();
            Map<Product, Integer> newProductMap = new HashMap<>(productMap);
            for(Product p : mapToSubtract.keySet()){
                if(!newProductMap.containsKey(p) || mapToSubtract.get(p)<=0){
                    continue;
                }
                else if(newProductMap.containsKey(p) && mapToSubtract.get(p) >= newProductMap.get(p)){
                    newProductMap.remove(p);
                }else{
                    newProductMap.replace(p, newProductMap.get(p)-mapToSubtract.get(p));
                }
            }
            this.productMap = newProductMap;
        }
    }

    public Map<Product, Integer> getProductMap() {
        return productMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Storage)) return false;
        Storage storage = (Storage) o;
        return Objects.equals(getProductMap(), storage.getProductMap());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductMap());
    }
}
