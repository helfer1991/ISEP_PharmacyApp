package lapr.project.model.autorization;

import lapr.project.model.UserRole;
import lapr.project.model.User;


/**
 *
 */
public class UserSession {

    /**
     *
     */
    private User oUser = null;


    /**
     * Initialize a UserSession for a given user.
     *
     * @param oUser user who is initializing a new session
     */
    public UserSession(User oUser) {
        if (oUser == null) {
            throw new IllegalArgumentException("Argument cannot be null.");
        }
        this.oUser = oUser;
    }
    
    /**
     * Return User from UserSession
     * @return 
     */
    public User getUser(){
        return oUser;
    }

    /**
     * Logouts off the system
     */
    public void doLogout() {
        this.oUser = null;
    }

    /**
     * Check if there is a UserSession running
     *
     * @return boolean
     */
    public boolean isLoggedIn() {
        return this.oUser != null;
    }

    /**
     * Returns the user's email of a given user that is logged in the system
     *
     * @return email
     */
    public String getUserEmail() {
        if (isLoggedIn()) {
            return this.oUser.getEmail();
        }
        return null;
    }

    /**
     * Returns the user's role of a given user
     *
     * @return role
     */
    public UserRole getUserRole() {
        return this.oUser.getRole();
    }

    /**
     * Checks if the role passed as parameter is the same as the role of the person logged in
     * @param userRole UserRole object to be compared with the role of the person logged in
     * @return true if the user logged in has this role
     *          false if there is no one logged in or the user logged in doesn't have this role
     */
    public boolean hasRole(UserRole userRole) {
        if (!isLoggedIn()){
            return false;
        }else{
            return (userRole.equals(this.oUser.getRole()));
        }
    }
}
