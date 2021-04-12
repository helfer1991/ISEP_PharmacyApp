package lapr.project.model.autorization;

import lapr.project.model.User;
import lapr.project.data.UserDB;
import lapr.project.model.UserRole;

/**
 *
 */
public class AutorizationFacade {

    private UserSession session = null;
    private UserDB userDB;

    public AutorizationFacade() {
        userDB = new UserDB();
    }

    public UserSession doLogin(String userEmail, String strPwd) {
        try {
            User utlz = userDB.getUserByEmail(userEmail);
            if (utlz != null) {
                if (utlz.hasPassword(strPwd)) {
                    this.session = new UserSession(utlz);
                    return getCurrentSession();
                }
            }
            return null;
        } catch (Exception ex) {
            return null;
        }

    }

    public UserSession getCurrentSession() {
        return this.session;
    }

    public void doLogout() {
        if (this.session != null) {
            this.session.doLogout();
        }
        this.session = null;
    }

    /**
     * Checks if the role passed as parameter is the same as the role of the
     * person logged in
     *
     * @param userRole String with the user role to be compared with the role of
     * the person logged in
     * @return true if the user logged in has this role false if there is no one
     * logged in or the user logged in doesn't have this role
     */
    public boolean loggedInAs(String userRole) {
        if (this.session == null || userRole == null || userRole.isEmpty()) {
            return false;
        } else {
            return session.hasRole(new UserRole(userRole));

        }
    }

}
