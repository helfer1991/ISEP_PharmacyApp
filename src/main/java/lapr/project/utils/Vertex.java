package lapr.project.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class Vertex<V, E> {

    private int key;                     //Vertex key number
    private V element;                 //Vertex information
    private Map<V, Edge<V, E>> outVerts = new LinkedHashMap<>(); //adjacent vertices

    /**
     * Vertex constructore without parameters
     */
    public Vertex() {
        key = -1;
        element = null;
        outVerts = new LinkedHashMap<>();
    }

    /**
     *
     * Vertex constructor with parameters
     *
     * @param k Vertex key
     * @param vInf Vertex element
     */
    public Vertex(int k, V vInf) {
        key = k;
        element = vInf;
        outVerts = new LinkedHashMap<>();
    }

    /**
     * Returns the vertex key
     *
     * @return Vertex key
     */
    public int getKey() {
        return key;
    }

    /**
     * Sets the vertex key
     *
     * @param k Vertex key
     */
    public void setKey(int k) {
        key = k;
    }

    /**
     * Returns the vertex element
     *
     * @return Vertex element
     */
    public V getElement() {
        return element;
    }

    /**
     * Sets the vertex element
     *
     * @param vInf Vertex element
     */
    public void setElement(V vInf) {
        element = vInf;
    }

    /**
     * Adds a new adjacent vertex
     *
     * @param vAdj Adjacent vertex
     * @param edge Edge that connects them
     */
    public void addAdjVert(V vAdj, Edge<V, E> edge) {
        outVerts.put(vAdj, edge);
    }

    /**
     * Returns the adjacent vertex given an edge
     *
     * @param edge Edge
     * @return Adjacent vertex connect by the edge
     */
    public V getAdjVert(Edge<V, E> edge) {

        for (V vert : outVerts.keySet()) {
            if (edge.equals(outVerts.get(vert))) {
                return vert;
            }
        }

        return null;
    }

    /**
     * Removes an adjacent vertex
     * @param vAdj Adjacent vertex to me removed
     */
    public void remAdjVert(V vAdj) {
        outVerts.remove(vAdj);
    }

    /**
     * Returns the edge that connects to a certain adjacent vertex
     * @param vAdj Adjacent vertex
     * @return Edge
     */
    public Edge<V, E> getEdge(V vAdj) {
        return outVerts.get(vAdj);
    }

    /**
     * Returns the number of adjacent vertexes
     * @return Number of adjacent vertexes
     */
    public int numAdjVerts() {
        return outVerts.size();
    }

    /**
     * Returns all the adjacent vertexes
     * @return Iterable with all the adjacent vertexes
     */
    public Iterable<V> getAllAdjVerts() {
        return outVerts.keySet();
    }

    /**
     * Returns all the outgoing edges
     * @return Iterable with all the outgoing edges
     */
    public Iterable<Edge<V, E>> getAllOutEdges() {
        return outVerts.values();
    }

	
	
    /**
     * Equals implementation
     * @param otherObj Edge to be compared
     * @return True if both edges are equals. False if not.
     */
//    @Override
//    public boolean equals(Object otherObj) {
//
//        if (this == otherObj) {
//            return true;
//        }
//
//        if (otherObj == null || this.getClass() != otherObj.getClass()) {
//            return false;
//        }
//
//		@SuppressWarnings("unchecked")
//        Vertex<V, E> otherVertex = (Vertex<V, E>) otherObj;
//
//        if (this.key != otherVertex.key) {
//            return false;
//        }
//
//        if (this.element != null && otherVertex.element != null
//                && !this.element.equals(otherVertex.element)) {
//            return false;
//        }
//
//        //adjacency vertices should be equal
//        if (this.numAdjVerts() != otherVertex.numAdjVerts()) {
//            return false;
//        }
//
//        //and edges also
//        Iterator<Edge<V, E>> it1 = this.getAllOutEdges().iterator();
//        while (it1.hasNext()) {
//            Iterator<Edge<V, E>> it2 = otherVertex.getAllOutEdges().iterator();
//            boolean exists = false;
//            while (it2.hasNext()) {
//                if (it1.next().equals(it2.next())) {
//                    exists = true;
//                }
//            }
//            if (!exists) {
//                return false;
//            }
//        }
//        return true;
//    }

	
    /**
     * Clones this instance of vertex and returns its clone.
     * @return Vertex clone.
     */
    @Override
    public Vertex<V, E> clone() {

        Vertex<V, E> newVertex = new Vertex<>();

        newVertex.setKey(key);
        newVertex.setElement(element);

        for (V vert : outVerts.keySet()) {
            newVertex.addAdjVert(vert, this.getEdge(vert));
        }

        return newVertex;
    }




	
	

}
