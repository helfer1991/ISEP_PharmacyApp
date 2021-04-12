package lapr.project.ui.dto;

import java.util.Objects;

/**
 *
 * @author 1100241
 */
public class ProductDTO {

    private int idDTO;
    private String descriptionDTO;
    private float priceDTO;
    private float weightDTO;

    public ProductDTO(int id, String description, float price, float weight) {
        this.idDTO = id;
        this.descriptionDTO = description;
        this.priceDTO = price;
        this.weightDTO = weight;
    }
    
    public ProductDTO(){
        
    }
    
    public void setPrice(float price) {
        this.priceDTO = price;
    }

    public void setId(int id) {
        this.idDTO = id;
    }

    public float getPrice() {
        return priceDTO;
    }

    public String getDescription() {
        return descriptionDTO;
    }

    public float getWeight() {
        return weightDTO;
    }

    public void setWeight(float weight) {
        this.weightDTO = weight;
    }

    public void setDescription(String description) {
        this.descriptionDTO = description;
    }

    public int getId() {
        return idDTO;
    }



    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof ProductDTO)) return false;
        ProductDTO that = (ProductDTO) o;
        return getId() == that.getId()
                && Float.compare(that.getPrice(), getPrice()) == 0
                && Float.compare(that.getWeight(), getWeight()) == 0
                && Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), getPrice(), getWeight());
    }
}
