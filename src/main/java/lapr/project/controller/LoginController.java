package lapr.project.controller;

import lapr.project.model.PharmaDeliveriesApp;
import lapr.project.model.UserRole;

/**
 * Controller to manage the activity related to the login System and user roles
 */
public class LoginController {

    private final PharmaDeliveriesApp application;

    /**
     *Constructor
     */
    public LoginController() {
        this.application = PharmaDeliveriesApp.getInstance();
    }

    /**
     * Do a login with the received parameters
     *
     * @param strEmail the user email
     * @param strPwd the user password
     * @return boolean
     */
    public boolean doLogin(String strEmail, String strPwd) {
        return this.application.doLogin(strEmail, strPwd);
    }

    /**
     * Returns the role of the user that is currently logged in
     * @return userRole
     */
    public UserRole getUserRole() {
        if (this.application.getCurrentSession().isLoggedIn()) {
            return this.application.getCurrentSession().getUserRole();
        }
        return null;
    }

    /**
     * Returns the email of user that is currently signed in
     * @return return nulls if there isn't any user logged in
     */
    public String getUserEmail() {
        if (this.application.getCurrentSession().isLoggedIn()) {
            return this.application.getCurrentSession().getUserEmail();
        }
        return null;
    }
    /**
     * Logs out of the system
     */
    public void doLogout() {
        this.application.doLogout();
    }
}
