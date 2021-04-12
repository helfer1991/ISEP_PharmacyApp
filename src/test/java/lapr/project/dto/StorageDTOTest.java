package lapr.project.dto;

import lapr.project.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import lapr.project.ui.dto.ProductDTO;
import lapr.project.ui.dto.StorageDTO;

import static org.junit.jupiter.api.Assertions.*;

class StorageDTOTest {
    StorageDTO storage = new StorageDTO();

    @BeforeEach
    void setUp() {
        storage = new StorageDTO();
    }



    @Test
    void addProductToStorage() {
        Map<ProductDTO, Integer> map =  storage.getProductMap();
        assertTrue(map.isEmpty());

        ProductDTO testProd = new ProductDTO(1,"vacina", 55.55f, 100);
        storage.addProductToStorage(testProd, 3);
        map = storage.getProductMap();

        assertFalse(map.isEmpty());
        assertEquals(map.get(testProd), 3);
    }

    @Test
    void getProductMap() {

        Map<ProductDTO, Integer> map =  storage.getProductMap();
        assertTrue(map.isEmpty());

        ProductDTO testProd = new ProductDTO(2,"gelo", 5.33f, 400);
        storage.addProductToStorage(testProd, 5);
        map = storage.getProductMap();

        assertFalse(map.isEmpty());
        assertEquals(map.get(testProd), 5);

    }

    @Test
    void testEquals() {
        Map<ProductDTO, Integer> map1 =  new HashMap<>();
        ProductDTO product1 = new ProductDTO(1,"vacina", 55.55f, 100);
        ProductDTO product2 = new ProductDTO(2,"vacina2", 10.55f, 200);
        ProductDTO product3 = new ProductDTO(3,"vacina3", 25.0f, 150);
        map1.put(product1, 2);
        map1.put(product2, 1);
        map1.put(product3, 5);

        //storages with the same maps are equal
        StorageDTO storage1 = new StorageDTO(map1);
        StorageDTO storage2 = new StorageDTO(map1);
        assertEquals(storage1, storage2);

        //map is empty
        StorageDTO storage4 = new StorageDTO();
        assertNotEquals(storage2, storage4);

        //if one of the products or its attribute is different then the storage is different
        Map<ProductDTO, Integer> map2 =  new HashMap<>(map1);
        product3 = new ProductDTO(3,"vacina333", 55.55f, 100);
        map2.put(product3, 5);
        StorageDTO storage3 = new StorageDTO(map2);
        assertNotEquals(storage1, storage3);

        assertNotEquals(storage1, "testString");
        ArrayList<Product> lst = new ArrayList<>();
        assertNotEquals(storage1, lst);


    }

    @Test
    void testHashCode() {
        Map<ProductDTO, Integer> map1 =  new HashMap<>();
        ProductDTO product1 = new ProductDTO(1,"vacina", 55.55f, 100);
        ProductDTO product2 = new ProductDTO(2,"vacina2", 10.55f, 200);
        ProductDTO product3 = new ProductDTO(3,"vacina3", 25.0f, 150);
        map1.put(product1, 2);
        map1.put(product2, 1);
        map1.put(product3, 5);

        //storages with the same products have the same hashcode
        StorageDTO storage1 = new StorageDTO(map1);
        StorageDTO storage2 = new StorageDTO(map1);
        assertEquals(storage1.hashCode(), storage2.hashCode());
    }


}