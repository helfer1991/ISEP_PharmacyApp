package lapr.project.ui.dto;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 */
public class StorageDTO {
    
    private Map<ProductDTO, Integer> productMap;

    public StorageDTO() {
        productMap = new HashMap<>();
    }
    public StorageDTO(Map<ProductDTO, Integer> map){
        productMap = map;
    }
    
    public void addProductToStorage(ProductDTO p, int quantity){
        productMap.put(p, quantity);
    }

    public Map<ProductDTO, Integer> getProductMap() {
        return productMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StorageDTO)) return false;
        StorageDTO that = (StorageDTO) o;
        return Objects.equals(getProductMap(), that.getProductMap());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductMap());
    }


}
