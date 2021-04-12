
package lapr.project.utils;

import java.util.LinkedList;
import java.util.List;
import lapr.project.model.Address;


public class GraphAlgorithms {
    
    @SuppressWarnings("unchecked")
    public static <V, E> LinkedList<V>[][] transitivePathsClosure(Graph<V, E> graph) {

        if (graph.numVertices() == 0 ) {
            return null;
        }

        //Array que guarda a matriz distancias entre cada um dos vertices
        float[][] pathsLength = new float[graph.numVertices()][graph.numVertices()];
        //Lista que guarda os vertices percorridos com o caminho mais curto
        LinkedList<V>[][] visitedVertices;
        visitedVertices = new LinkedList[graph.numVertices()][graph.numVertices()];

        int i = 0;
        for (V vO : graph.vertices()) {
            int j = 0;
            for (V vD : graph.vertices()) {
                if (!vO.equals(vD) && graph.getEdge(vO, vD) != null) {
                    pathsLength[i][j] = (float) graph.getEdge(vO, vD).getWeight();

                    visitedVertices[i][j] = new LinkedList<>();


                    visitedVertices[i][j].add(vO);
                    //Se nao existir edge
                } else if (!vO.equals(vD)) {
                    pathsLength[i][j] = Float.MAX_VALUE;

                    visitedVertices[i][j] = new LinkedList<>();

                }
                j++;
            }
            i++;
        }

         //Complexidade O(n³)
        //Floyd-Warshall algorithm
        for (int k = 0; k < visitedVertices.length; k++) {
            for (int z = 0; z < visitedVertices.length; z++) {
                if (z != k && pathsLength[z][k] != Integer.MAX_VALUE) {
                    for (int j = 0; j < graph.numVertices(); j++) {
                        if (z != j && k != j && pathsLength[k][j] != Integer.MAX_VALUE) {
                            if (pathsLength[z][j] > pathsLength[z][k] + pathsLength[k][j]) {
                                pathsLength[z][j] = pathsLength[z][k] + pathsLength[k][j];


                                visitedVertices[z][j] = new LinkedList<>();

                                visitedVertices[z][j].addAll(visitedVertices[z][k]);
                                visitedVertices[z][j].addAll(visitedVertices[k][j]);
                                
                            }
                        }
                    }
                }
            }
        }
        
        return visitedVertices;
    }

}
