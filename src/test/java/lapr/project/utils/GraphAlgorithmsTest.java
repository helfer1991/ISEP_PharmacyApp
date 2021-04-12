/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.utils;

import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.assertArrayEquals;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author catarinaserrano
 */
public class GraphAlgorithmsTest {
    
    Graph<String, String> completeMap = new Graph<>(false);
    Graph<String, String> incompleteMap = new Graph<>(false);
    
    public GraphAlgorithmsTest() {
        
        completeMap.insertVertex("Porto");
        completeMap.insertVertex("Braga");
        completeMap.insertVertex("Vila Real");
        completeMap.insertVertex("Aveiro");
        completeMap.insertVertex("Coimbra");
        completeMap.insertVertex("Leiria");

        completeMap.insertVertex("Viseu");
        completeMap.insertVertex("Guarda");
        completeMap.insertVertex("Castelo Branco");
        completeMap.insertVertex("Lisboa");
        completeMap.insertVertex("Faro");

        completeMap.insertEdge("Porto", "Aveiro", "A1", 75);
        completeMap.insertEdge("Porto", "Braga", "A3", 60);
        completeMap.insertEdge("Porto", "Vila Real", "A4", 100);
        completeMap.insertEdge("Viseu", "Guarda", "A25", 75);
        completeMap.insertEdge("Guarda", "Castelo Branco", "A23", 100);
        completeMap.insertEdge("Aveiro", "Coimbra", "A1", 60);
        completeMap.insertEdge("Coimbra", "Lisboa", "A1", 200);
        completeMap.insertEdge("Coimbra", "Leiria", "A34", 80);
        completeMap.insertEdge("Aveiro", "Leiria", "A17", 120);
        completeMap.insertEdge("Leiria", "Lisboa", "A8", 150);

        completeMap.insertEdge("Aveiro", "Viseu", "A25", 85);
        completeMap.insertEdge("Leiria", "Castelo Branco", "A23", 170);
        completeMap.insertEdge("Lisboa", "Faro", "A2", 280);

        incompleteMap = completeMap.clone();

        incompleteMap.removeEdge("Aveiro", "Viseu");
        incompleteMap.removeEdge("Leiria", "Castelo Branco");
        incompleteMap.removeEdge("Lisboa", "Faro");
    }
    /**
     * Test of transitivePathsClosure method, of class GraphAlgorithms.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testTransitivePathsClosure() {
        System.out.println("Test of transitivePathsClosure");
        assertTrue("Should be null if graph does not exist", GraphAlgorithms.transitivePathsClosure(new Graph<String, String>(false)) == null);

        List[][] result = GraphAlgorithms.transitivePathsClosure(completeMap);
        @SuppressWarnings("unchecked")
        LinkedList<String>[][] expResult = new LinkedList[completeMap.numVertices()][completeMap.numVertices()];
        for (int i = 0; i < completeMap.numVertices(); i++) {
            for (int j = 0; j < completeMap.numVertices(); j++) {
                if (i != j) {
                    expResult[i][j] = new LinkedList();
                }
            }
        }
        
        expResult[0][1].add("Porto"); //Braga
        expResult[0][2].add("Porto"); //Vila Real
        expResult[0][3].add("Porto"); //Aveiro
        expResult[0][4].add("Porto"); //Coimbra
        expResult[0][4].add("Aveiro");
        expResult[0][5].add("Porto"); //Leiria
        expResult[0][5].add("Aveiro");
        expResult[0][6].add("Porto"); //Viseu
        expResult[0][6].add("Aveiro");
        expResult[0][7].add("Porto"); //Guarda
        expResult[0][7].add("Aveiro");
        expResult[0][7].add("Viseu");
        expResult[0][8].add("Porto"); //C Branco
        expResult[0][8].add("Aveiro");
        expResult[0][8].add("Viseu");
        expResult[0][8].add("Guarda");
        expResult[0][9].add("Porto"); //Lisboa 
        expResult[0][9].add("Aveiro");
        expResult[0][9].add("Coimbra");
        expResult[0][10].add("Porto"); //Faro
        expResult[0][10].add("Aveiro");
        expResult[0][10].add("Coimbra");
        expResult[0][10].add("Lisboa");

        expResult[1][0].add("Braga"); //Braga
        expResult[1][2].add("Braga"); //Vila Real
        expResult[1][2].add("Porto");
        expResult[1][3].add("Braga"); //Aveiro
        expResult[1][3].add("Porto");
        expResult[1][4].add("Braga"); //Coimbra
        expResult[1][4].add("Porto");
        expResult[1][4].add("Aveiro");
        expResult[1][5].add("Braga"); //Leiria
        expResult[1][5].add("Porto");
        expResult[1][5].add("Aveiro");
        expResult[1][6].add("Braga"); //Viseu
        expResult[1][6].add("Porto");
        expResult[1][6].add("Aveiro");
        expResult[1][7].add("Braga"); //Guarda
        expResult[1][7].add("Porto");
        expResult[1][7].add("Aveiro");
        expResult[1][7].add("Viseu");
        expResult[1][8].add("Braga"); //C Branco
        expResult[1][8].add("Porto");
        expResult[1][8].add("Aveiro");
        expResult[1][8].add("Viseu");
        expResult[1][8].add("Guarda");
        expResult[1][9].add("Braga"); //Lisboa 
        expResult[1][9].add("Porto");
        expResult[1][9].add("Aveiro");
        expResult[1][9].add("Coimbra");
        expResult[1][10].add("Braga"); //Faro
        expResult[1][10].add("Porto");
        expResult[1][10].add("Aveiro");
        expResult[1][10].add("Coimbra");
        expResult[1][10].add("Lisboa");

        assertArrayEquals("Lines 1 of Array", expResult[0], result[0]);
        assertArrayEquals("Lines 2 of Array", expResult[1], result[1]);
    }
    
}
