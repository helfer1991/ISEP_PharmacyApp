package lapr.project.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Graph<V, E> implements GraphInterface<V, E> {

    private int numVert;
    private int numEdge;
    private boolean isDirected;
    private Map<V, Vertex<V, E>> vertices;  //all Vertices of the graph 

    /**
     *
     * Constructs an empty graph (either undirected or directed)
     *
     * @param directed True/false if the graph is directed/undirected
     */
    public Graph(boolean directed) {
        numVert = 0;
        numEdge = 0;
        isDirected = directed;
        vertices = new LinkedHashMap<>();
    }

    /**
     *
     * Returns the number of vertexes
     *
     * @return Number of vertexes
     */
    @Override
    public int numVertices() {
        return numVert;
    }

    /**
     *
     * Returns all the vertexes
     *
     * @return Iterable with all the vertexes
     */
    @Override
    public Iterable<V> vertices() {
        return vertices.keySet();
    }

    /**
     *
     * Checks if a certain vertex is present on the Graph
     *
     * @param vert Vertex
     * @return True if the vertex is present. False if not.
     */
    public boolean validVertex(V vert) {

        if (vertices.get(vert) == null) {
            return false;
        }

        return true;
    }

    /**
     * Returns a certain vertex index number
     *
     * @param vert Vertex
     * @return Vertex index
     */
    public int getKey(V vert) {
        return vertices.get(vert).getKey();
    }

    public <V> V[] allkeyVerts() {

        @SuppressWarnings("unchecked")
        V[] keyverts = (V[]) vertices.keySet().toArray();
        return keyverts;
    }

    /**
     *
     * Returns all the adjacent vertexes of a vertex
     *
     * @param vert Vertex
     * @return Iterable with all the adjacent vertexes
     */
    public Iterable<V> adjVertices(V vert) {

        if (!validVertex(vert)) {
            return null;
        }

        Vertex<V, E> vertex = vertices.get(vert);

        return vertex.getAllAdjVerts();
    }

    /**
     * Returns the number of edges
     *
     * @return Number of edges
     */
    @Override
    public int numEdges() {
        return numEdge;
    }

    /**
     *
     * Returns all the graph's edges
     *
     * @return Iterable with all the graph's edges
     */
    @Override
    public Iterable<Edge<V, E>> edges() {
        List<Edge<V, E>> edges = new ArrayList<>();
        for (Vertex<V, E> vert : vertices.values()) {
            edges.addAll((Collection<? extends Edge<V, E>>) vert.getAllOutEdges());
        }

        return edges;
    }

    /**
     *
     * Returns the edge with a given origin and destiny
     *
     * @param vOrig Origin
     * @param vDest Destiny
     * @return Vertex that connects the two vertexes
     */
    @Override
    public Edge<V, E> getEdge(V vOrig, V vDest) {

        if (!validVertex(vOrig) || !validVertex(vDest)) {
            return null;
        }

        Vertex<V, E> vorig = vertices.get(vOrig);

        return vorig.getEdge(vDest);
    }

    /**
     *
     * Returns the endpoints of a given edge
     *
     * @param edge Edge
     * @return Array with the endpoints
     */
    @Override
    public V[] endVertices(Edge<V, E> edge) {

        if (edge == null) {
            return null;
        }

        if (!validVertex(edge.getVOrig()) || !validVertex(edge.getVDest())) {
            return null;
        }

        Vertex<V, E> vorig = vertices.get(edge.getVOrig());

        if (!edge.equals(vorig.getEdge(edge.getVDest()))) {
            return null;
        }

        return edge.getEndpoints();
    }

    /**
     *
     * Returns the oposite vertex given an edge and one of its endpoints
     *
     * @param vert Vertex
     * @param edge Edge
     * @return Opposite vertex
     */
    @Override
    public V opposite(V vert, Edge<V, E> edge) {

        if (!validVertex(vert)) {
            return null;
        }

        Vertex<V, E> vertex = vertices.get(vert);

        return vertex.getAdjVert(edge);
    }

    /**
     * Returns the outgoing degree of a given vertex
     *
     * @param vert Vertex
     * @return Outgoing degree
     */
    @Override
    public int outDegree(V vert) {

        if (!validVertex(vert)) {
            return -1;
        }

        Vertex<V, E> vertex = vertices.get(vert);

        return vertex.numAdjVerts();
    }

    /**
     * Returns the in degree of a given vertex
     *
     * @param vert Vertex
     * @return In degree
     */
    @Override
    public int inDegree(V vert) {

        if (!validVertex(vert)) {
            return -1;
        }

        int degree = 0;
        for (V otherVert : vertices.keySet()) {
            if (getEdge(otherVert, vert) != null) {
                degree++;
            }
        }

        return degree;
    }

    /**
     *
     * Returns the outgoing edges of a given vertex
     *
     * @param vert Vertex
     * @return Iterable with the outgoing edges
     */
    @Override
    public Iterable<Edge<V, E>> outgoingEdges(V vert) {

        if (!validVertex(vert)) {
            return null;
        }

        Vertex<V, E> vertex = vertices.get(vert);

        return vertex.getAllOutEdges();
    }

    /**
     *
     * Returns the incoming edges of a given vertex
     *
     * @param vert Vertex
     * @return Iterable with the incoming edges
     */
    @Override
    public Iterable<Edge<V, E>> incomingEdges(V vert) {

        List<Edge<V, E>> incEdges = new ArrayList<>();

        for (Vertex<V, E> vertex : vertices.values()) {
            for (Edge<V, E> outE : vertex.getAllOutEdges()) {
                if (outE.getVDest().equals(vert)) {
                    incEdges.add(outE);
                }
            }
        }

        return incEdges;
    }

    /**
     *
     * Inserts a new vertex into the graph
     *
     * @param vert Vertex to be inserted
     * @return Success of the operation
     */
    @Override
    public boolean insertVertex(V vert) {

        if (validVertex(vert)) {
            return false;
        }

        if (vert == null) {
            return false;
        }

        Vertex<V, E> vertex = new Vertex<>(numVert, vert);
        vertices.put(vert, vertex);
        numVert++;

        return true;
    }

    /**
     *
     * Inserts a new edge into the graph
     *
     * @param vOrig Edge's origin
     * @param vDest Edge's destiny
     * @param eInf Edge's element
     * @param eWeight Edge's weight
     * @return Success of the operation
     */
    @Override
    public boolean insertEdge(V vOrig, V vDest, E eInf, double eWeight) {

        if (vOrig == null || vDest == null) {
            return false;
        }

        if (getEdge(vOrig, vDest) != null) {
            return false;
        }

        if (!validVertex(vOrig)) {
            insertVertex(vOrig);
        }

        if (!validVertex(vDest)) {
            insertVertex(vDest);
        }

        Vertex<V, E> vorig = vertices.get(vOrig);
        Vertex<V, E> vdest = vertices.get(vDest);

        Edge<V, E> newEdge = new Edge<>(eInf, eWeight, vorig, vdest);
        vorig.addAdjVert(vDest, newEdge);
        numEdge++;

        //if graph is not direct insert other edge in the opposite direction 
        if (!isDirected) // if vDest different vOrig
        {
            if (getEdge(vDest, vOrig) == null) {
                Edge<V, E> otherEdge = new Edge<>(eInf, eWeight, vdest, vorig);
                vdest.addAdjVert(vOrig, otherEdge);
                numEdge++;
            }
        }

        return true;
    }

    /**
     *
     * Removes a given vertex from the graph
     *
     * @param vert Vertex to be removed
     * @return Success of the operation
     */
    @Override
    public boolean removeVertex(V vert) {

        if (!validVertex(vert)) {
            return false;
        }

        //remove all edges that point to vert
        for (Edge<V, E> edge : incomingEdges(vert)) {
            V vadj = edge.getVOrig();
            removeEdge(vadj, vert);
        }

        Vertex<V, E> vertex = vertices.get(vert);

        //update the keys of subsequent vertices in the map
        for (Vertex<V, E> v : vertices.values()) {
            int keyVert = v.getKey();
            if (keyVert > vertex.getKey()) {
                keyVert = keyVert - 1;
                v.setKey(keyVert);
            }
        }
        //The edges that live from vert are removed with the vertex    
        vertices.remove(vert);

        numVert--;

        return true;
    }

    /**
     *
     * Removes an edge from the graph
     *
     * @param vOrig Edge's origin
     * @param vDest Edge's destiny
     * @return Success of the operation
     */
    @Override
    public boolean removeEdge(V vOrig, V vDest) {

        if (!validVertex(vOrig) || !validVertex(vDest)) {
            return false;
        }

        Edge<V, E> edge = getEdge(vOrig, vDest);

        if (edge == null) {
            return false;
        }

        Vertex<V, E> vorig = vertices.get(vOrig);

        vorig.remAdjVert(vDest);
        numEdge--;

        //if graph is not direct 
        if (!isDirected) {
            edge = getEdge(vDest, vOrig);
            if (edge != null) {
                Vertex<V, E> vdest = vertices.get(vDest);
                vdest.remAdjVert(vOrig);
                numEdge--;
            }
        }
        return true;
    }

    /**
     *
     * Returns a clone of the graph
     *
     * @return Graph clone
     */
    @Override
    public Graph<V, E> clone() {

        Graph<V, E> newObject = new Graph<>(this.isDirected);

        //insert all vertices
        for (V vert : vertices.keySet()) {
            newObject.insertVertex(vert);
        }

        //insert all edges
        for (V vert1 : vertices.keySet()) {
            for (Edge<V, E> e : this.outgoingEdges(vert1)) {
                if (e != null) {
                    V vert2 = this.opposite(vert1, e);
                    newObject.insertEdge(vert1, vert2, e.getElement(), e.getWeight());
                }
            }
        }

        return newObject;
    }

    /**
     * Equals implementation
     *
     * @param otherObj the other graph to test for equality
     * @return true if both objects represent the same graph
     */
    @Override
    public boolean equals(Object otherObj) {

        if (this == otherObj) {
            return true;
        }

        if (otherObj == null || this.getClass() != otherObj.getClass()) {
            return false;
        }

        @SuppressWarnings("unchecked")
        Graph<V, E> otherGraph = (Graph<V, E>) otherObj;

        if (numVert != otherGraph.numVertices() || numEdge != otherGraph.numEdges()) {
            return false;
        }

        //graph must have same vertices
        boolean eqvertex;
        for (V v1 : this.vertices()) {
            eqvertex = false;
            for (V v2 : otherGraph.vertices()) {
                if (v1.equals(v2)) {
                    eqvertex = true;
                }
            }

            if (!eqvertex) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.numVert;
        hash = 97 * hash + this.numEdge;
        hash = 97 * hash + (this.isDirected ? 1 : 0);
        hash = 97 * hash + Objects.hashCode(this.vertices);
        return hash;
    }


}
