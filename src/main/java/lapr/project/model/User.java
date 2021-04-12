package lapr.project.model;


import java.util.Objects;

/**
 *
 */
public class User {

    /**
     * Data in common for a User: it's e-mail, password and the user's role.
     */
    private String email;
    private String password;
    private UserRole userRole;

    /**
     * Initialize an User with the received parameters.
     *
     * @param email the user's email
     * @param password the user's password
     */
    public User(String email, String password) {
        if ( (email == null) || (password == null)  || (email.isEmpty()) || (password.isEmpty())) {
            throw new IllegalArgumentException("User arguments can't be null or empty.");
        }
        this.email = email;
        this.password = password;
    }
    
    public User(){
        userRole = new UserRole();
    }

    /**
     * Returns the email of a given user
     * @return m_strEmail
     */
    public String getEmail() {
        return this.email;
    }
    
    /**
     * Returns the password of a given user
     * @return m_strPassword
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the role of the User
     * @param role the new role to be assigned to the user
     * @return boolean. return true if set correctly or false if the role was not changed
     */
    public boolean setRole(UserRole role) {
        if (role != null) {
            this.userRole = role;
            return true;
        }
        return false;
    }

    /**
     * Returns the Role of the user registered on the system
     * @return list
     */
    public UserRole getRole() {
        return this.userRole;
    }
    
    public boolean hasEmail(String email){
        return this.email.equals(email);
    }

    public boolean hasPassword(String password){
        return this.password.equals(password);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(email, user.email)
                && Objects.equals(password, user.password)
                && Objects.equals(userRole, user.userRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, userRole);
    }
}
