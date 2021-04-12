package lapr.project.model;


import java.util.Objects;

/**
 *
 */
public class UserRole
{
    /**
     * Common data for UserRole: the role and it's description
     */
    private String role;
    private String description;
    
     /**
     * Initialize UserRole with the role received as parameter 
     * @param strRole the role of the user
     */
    public UserRole(String strRole)
    {
        if ( (strRole == null) || (strRole.isEmpty()))
            throw new IllegalArgumentException("The argument cannot be null or empty");
        
        this.role = strRole;
        this.description = strRole;
    }
    
    /**
     * Initialize UserRole with the received parameters.
     * @param role the role of the user
     * @param description description
     */
    public UserRole(String role, String description)
    {
        if ( (role == null) || (description == null) || (role.isEmpty())|| (description.isEmpty()))
            throw new IllegalArgumentException("None of the arguments can be null or empty.");
        
        this.role = role;
        this.description = description;
    }
    
    public UserRole(){
        
    }
    
    /**
     * Check if a given Id already exists
     * @param strId Id to be checked
     * @return boolean
     */
    public boolean hasId(String strId)
    {
        return this.role.equals(strId);
    }
    
    /**
     * Returns the role of the user
     * @return  m_srtPapel.
     */
    public String getRole()
    {
        return this.role;
    }
    
    /**
     * Returns the description
     * @return m_srtDescricao
     */
    public String getDescription()
    {
        return this.description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRole)) return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(role, userRole.role)
                && Objects.equals(description, userRole.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, description);
    }

}
