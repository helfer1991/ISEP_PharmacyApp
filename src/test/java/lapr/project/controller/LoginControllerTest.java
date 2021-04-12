package lapr.project.controller;


import lapr.project.model.PharmaDeliveriesApp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static lapr.project.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.*;


class LoginControllerTest {


    private PharmaDeliveriesApp appInstance = PharmaDeliveriesApp.getInstance();


    LoginController controllerInstance;


    @BeforeEach
    void setUp() {
        controllerInstance = new LoginController();
    }



    @Test
    void doLogin() {
        System.out.println("doLogin");

        //assertNull( appInstance.getCurrentSession() );
        //logIn
        controllerInstance.doLogin(TEST_ADMIN_EMAIL, TEST_ADMIN_KEY);
        //verify logIn was made
        assertNotNull( appInstance.getCurrentSession() );

        //logOut
        appInstance.doLogout();
        assertNull( appInstance.getCurrentSession() );
    }

    @Test
    void getUserRole() {
        System.out.println("getUserRole");

        //login with valid credentials
        controllerInstance.doLogin(TEST_ADMIN_EMAIL, TEST_ADMIN_KEY);
        assertNotNull( appInstance.getCurrentSession() );

        //get expected role and actual role
        String expectedResult = appInstance.getCurrentSession().getUserRole().getRole();
        String result = controllerInstance.getUserRole().getRole();

        //compare results
        assertEquals(expectedResult,result);
        assertEquals(ROLE_ADMINISTRATOR, result);

        //logOut
        appInstance.doLogout();
        assertNull( appInstance.getCurrentSession() );
    }

    @Test
    void getUserEmail() {
        System.out.println("getUserEmail");
        //login with valid credentials
        controllerInstance.doLogin(TEST_ADMIN_EMAIL, TEST_ADMIN_KEY);
        assertNotNull( appInstance.getCurrentSession() );

        //get expected email and actual email
        String expectedResult = appInstance.getCurrentSession().getUser().getEmail();
        String result = controllerInstance.getUserEmail();

        //compare results
        assertEquals(expectedResult,result);
        assertEquals(TEST_ADMIN_EMAIL,result);

        //logOut
        appInstance.doLogout();
        assertNull( appInstance.getCurrentSession() );
    }

    @Test
    void doLogout() {
        System.out.println("doLogout");

        //logIn
        appInstance.doLogin(TEST_ADMIN_EMAIL, TEST_ADMIN_KEY);

        //verify login was made
        assertNotNull( appInstance.getCurrentSession() );

        //logOut
        controllerInstance.doLogout();
        assertNull( appInstance.getCurrentSession() );
    }

}