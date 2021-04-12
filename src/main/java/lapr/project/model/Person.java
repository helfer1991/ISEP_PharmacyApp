package lapr.project.model;


import java.util.Objects;

/**
 *
 * @author catarinaserrano
 */
public class Person extends User {
  
    private int nif;
    private String name;

    public Person(int nif, String name) {
        super();
        this.nif = nif;
        this.name = name;
    }

    public Person(){
        super();
    }
    /**
     * Returns NIF
     *
     * @return nif
     */
    public int getNIF() {
        return nif;
    }

    /**
     * Returns Name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set NIf
     *
     * @param nif
     */
    public void setNIF(int nif) {
        this.nif = nif;
    }

    /**
     * Set Name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        if (!super.equals(o)) return false;
        Person person = (Person) o;
        return getNIF() == person.getNIF()
                && Objects.equals(getName(), person.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getNIF(), getName());
    }
}
