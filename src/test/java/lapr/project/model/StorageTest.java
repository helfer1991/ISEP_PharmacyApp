package lapr.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {
    Storage storage = new Storage();

    Product product1 = new Product(1,"vacina", 55.55f, 100);
    Product product2 = new Product(2,"vacina2", 10.55f, 200);
    Product product3 = new Product(3,"vacina3", 25.0f, 150);

    @BeforeEach
    void setUp() {
        storage = new Storage();
    }



    @Test
    void addProductToStorage() {
        Map<Product, Integer> map =  storage.getProductMap();
        assertTrue(map.isEmpty());

        storage.addProductToStorage(product1, 3);
        map = storage.getProductMap();

        assertFalse(map.isEmpty());
        assertEquals(map.get(product1), 3);
    }

    @Test
    void getProductMap() {

        Map<Product, Integer> map =  storage.getProductMap();
        assertTrue(map.isEmpty());

        Product testProd = new Product(2,"gelo", 5.33f, 400);
        storage.addProductToStorage(testProd, 5);
        map = storage.getProductMap();

        assertFalse(map.isEmpty());
        assertEquals(map.get(testProd), 5);

    }

    @Test
    void testEquals() {
        Map<Product, Integer> map1 =  new HashMap<>();
        map1.put(product1, 2);
        map1.put(product2, 1);
        map1.put(product3, 5);

        //storages with the same maps are equal
        Storage storage1 = new Storage(map1);
        Storage storage2 = new Storage(map1);

        assertEquals(storage1, storage1);
        assertEquals(storage1, storage2);

        //map is empty
        Storage storage4 = new Storage();
        assertNotEquals(storage2, storage4);

        //if one of the products or its attribute is different then the storage is different
        Map<Product, Integer> map2 =  new HashMap<>(map1);
        product3 = new Product(3,"vacina333", 55.55f, 100);
        map2.put(product3, 5);
        Storage storage3 = new Storage(map2);
        assertNotEquals(storage1, storage3);

        assertNotEquals(storage1, "testString");
        ArrayList<Product> lst = new ArrayList<>();
        assertNotEquals(storage1, lst);


    }

    @Test
    void testHashCode() {
        Map<Product, Integer> map1 =  new HashMap<>();
        map1.put(product1, 2);
        map1.put(product2, 1);
        map1.put(product3, 5);

        //storages with the same products have the same hashcode
        Storage storage1 = new Storage(map1);
        Storage storage2 = new Storage(map1);
        assertEquals(storage1.hashCode(), storage2.hashCode());

        Map<Product, Integer> map2 =  new HashMap<>();
        map2.put(product1, 2);
        storage2 = new Storage(map2);

        assertNotEquals(storage1.hashCode(), storage2.hashCode());
    }


    @Test
    void addStorage() {
        Map<Product, Integer> map1 =  new HashMap<>();
        map1.put(product1, 2);
        map1.put(product2, 1);
        map1.put(product3, 5);
        Storage storage1 = new Storage(map1);

        Map<Product, Integer> map2 =  new HashMap<>();
        map2.put(product1, -2);
        map2.put(product2, 3);
        Storage storage2 = new Storage(map2);

        //expected result is a map where the quantities of repeated products were added)
        Map<Product,Integer> expectedResult = new HashMap<>();
        expectedResult.put(product1, 2);
        expectedResult.put(product2, 4);
        expectedResult.put(product3, 5);

        //verify storage has the map has the expected product map
        assertEquals(map1, storage1.getProductMap());
        assertEquals(map2, storage2.getProductMap());

        //if null is  passed the product map is not affected
        storage1.addStorage(null);
        assertEquals(map1, storage1.getProductMap());

        //call method and assert result
        storage1.addStorage(storage2);
        assertNotEquals(map1, storage1.getProductMap());
        assertEquals(expectedResult,storage1.getProductMap());
    }

    @Test
    void subtractStorage_existingProducts(){
        Map<Product, Integer> map1 =  new HashMap<>();
        map1.put(product1, 2);
        map1.put(product2, 1);
        map1.put(product3, 5);
        Storage storage1 = new Storage(map1);

        Map<Product, Integer> map2 =  new HashMap<>();
        map2.put(product1, 2);
        map2.put(product2, 3);
        map2.put(product3, 4);

        Storage storage2 = new Storage(map2);

        //map1 - map2 =  expected result
        Map<Product,Integer> expectedResult = new HashMap<>();
        expectedResult.put(product3, 1);

        //verify storage has the map has the expected product map
        assertEquals(map1, storage1.getProductMap());
        assertEquals(map2, storage2.getProductMap());

        //if null is  passed the product map is not affected
        storage1.subtractStorage(null);
        assertEquals(map1, storage1.getProductMap());

        //call method and assert result
        storage1.subtractStorage(storage2);
        assertNotEquals(map1, storage1.getProductMap());
        assertEquals(expectedResult,storage1.getProductMap());

    }
    @Test
    void subtractStorage_missingProducts(){
        Map<Product, Integer> map1 =  new HashMap<>();
        map1.put(product1, 2);
        map1.put(product2, 1);

        Storage storage1 = new Storage(map1);

        Map<Product, Integer> map2 =  new HashMap<>();
        map2.put(product2, 10);
        map2.put(product3, 15);

        Storage storage2 = new Storage(map2);

        //map1 - map2 =  expected result (product3 does not exist in map1)
        Map<Product,Integer> expectedResult = new HashMap<>();
        expectedResult.put(product1, 2);

        //verify storage has the map has the expected product map
        assertEquals(map1, storage1.getProductMap());
        assertEquals(map2, storage2.getProductMap());

        //if null is  passed the product map is not affected
        storage1.subtractStorage(null);
        assertEquals(map1, storage1.getProductMap());

        //call method and assert result
        storage1.subtractStorage(storage2);
        assertNotEquals(map1, storage1.getProductMap());
        assertEquals(expectedResult,storage1.getProductMap());

    }

    @Test
    void subtractStorage_other(){ //necessary?
        Map<Product, Integer> map1 =  new HashMap<>();
        map1.put(product1, 5);
        map1.put(product2, 5);

        Storage storage1 = new Storage(map1);

        Map<Product, Integer> map2 =  new HashMap<>();
        map2.put(product1, 4);
        map2.put(product2, 15);
        map2.put(product3, 3);

        Storage storage2 = new Storage(map2);

        //map1 - map2 =  expected result (product3 does not exist in map1)
        Map<Product,Integer> expectedResult = new HashMap<>();
        expectedResult.put(product1, 1);

        //verify storage has the map has the expected product map
        assertEquals(map1, storage1.getProductMap());
        assertEquals(map2, storage2.getProductMap());

        //if null is  passed the product map is not affected
        storage1.subtractStorage(null);
        assertEquals(map1, storage1.getProductMap());

        //call method and assert result
        storage1.subtractStorage(storage2);
        assertNotEquals(map1, storage1.getProductMap());
        assertEquals(expectedResult,storage1.getProductMap());

    }
}