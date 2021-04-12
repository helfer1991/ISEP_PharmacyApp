package lapr.project.utils;

import java.lang.reflect.Array;
import java.util.Objects;

public class Edge<V,E> implements Comparable<Object> {
    
    private E element;           // Edge information
    private double weight;       // Edge weight
    private Vertex<V,E> vOrig;  // vertex origin
    private Vertex<V,E> vDest;  // vertex destination
    
    /**
     * 
     * Edge constructor with no parameters
     * 
     */
    public Edge() {
        element = null; weight= 0.0; vOrig=null; vDest=null; } 
    
    /**
     * Edge constructor with parameters
     * 
     * @param eInf Edge element
     * @param ew Edge weight
     * @param vo Edge origin
     * @param vd Edge destiny
     */
    public Edge(E eInf, double ew, Vertex<V,E> vo, Vertex<V,E> vd) {
        element = eInf; weight= ew; vOrig=vo; vDest=vd;} 
  
    /**
     * Returns edge's element
     * 
     * @return Edge's element
     */
    public E getElement() { return element; }
    /**
     * Sets the edge's element
     * 
     * @param eInf Edge's element
     */
    public void setElement(E eInf) { element = eInf; }
    /**
     * Returns the edge's weight
     * 
     * @return Edge's weight
     */
    public double getWeight() { return weight; }
    /**
     * 
     * Sets the edge's weight
     * 
     * @param ew Edge's weight
     */
    public void setWeight(double ew) { weight= ew; }
    
    /**
     * 
     * Returns the edge's origin
     * 
     * @return Edge's origin
     */
    public V getVOrig() { 
        if (this.vOrig != null) 
            return vOrig.getElement(); 
        return null;
    }
    /**
     * 
     * Sets the edge's origin
     * 
     * @param vo Edge's origin
     */
    public void setVOrig(Vertex<V,E> vo) { vOrig= vo; }
    
    /**
     * 
     * Returns the edge's destiny
     * 
     * @return Edge's origin
     */
    public V getVDest() { 
        if (this.vDest != null) 
            return vDest.getElement(); 
        return null; 
    }
    /**
     * 
     * Returns the edge's destiny
     * 
     * @param vd Edge's destiny
     */
    public void setVDest(Vertex<V,E> vd) { vDest= vd; }
    
    /**
     * 
     * Returns an array with the edge's endpoints
     * 
     * @return Array with the edge's endpoints
     */
    public V[] getEndpoints() { 
        
        V oElem=null, dElem=null, typeElem=null;
        
        if (this.vOrig != null) 
           oElem = vOrig.getElement();      
        
        if (this.vDest != null)
           dElem = vDest.getElement(); 
        
        if (oElem == null && dElem == null)
          return null;

        if (oElem != null)          // To get type
            typeElem = oElem;
        
        if (dElem != null)
            typeElem = dElem;
        
		@SuppressWarnings("unchecked")
        V[] endverts = (V [])Array.newInstance(typeElem.getClass(), 2);  

        endverts[0]= oElem; 
        endverts[1]= dElem;
        
        return endverts; 
    }

    
    /**
     * 
     * Compare if the edge's weight if bigger than other edge's weight
     * 
     * @param otherObject Other edge
     * @return True if it is bigger. False if not.
     */
    @Override
	@SuppressWarnings({"unchecked"})
    public int compareTo(Object otherObject) {
        
		Edge<V,E> other = new Edge<>();
		
		if(otherObject instanceof Edge<?, ?>) {
		    other = (Edge<V,E>) otherObject ;
		}
		
        if (this.weight < other.weight)  return -1;
        if (this.weight == other.weight) return 0;
        return 1;
    }
     
    /**
     * 
     * Clones this instance of edge.
     * 
     * @return Returns the cloned edge.
     */
    @Override
    public Edge<V,E> clone() {
        
        Edge<V,E> newEdge = new Edge<>();
        
        newEdge.element = element;
        newEdge.weight = weight;
        newEdge.vOrig = vOrig;
        newEdge.vDest = vDest;
        
        return newEdge;
    }

}
