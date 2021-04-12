package lapr.project.model.autorization;

import java.sql.SQLException;
import lapr.project.data.UserDB;
import lapr.project.model.User;
import lapr.project.model.UserRole;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AutorizationFacadeTest {


    @Mock
    UserDB userdb;

    @InjectMocks
    AutorizationFacade facadeInstance = new AutorizationFacade();






    @Test
    void doLogin() throws SQLException {
        System.out.println("doLogin");

        String email = "email@fakeEmail.com";
        String pwd = "fakePwd";
        facadeInstance.doLogout();
        assertNull(facadeInstance.getCurrentSession());

        //use mock as DB access
        when(userdb.getUserByEmail(email)).thenReturn(new User(email, pwd));

        //attempts logIn
        assertNotNull( facadeInstance.doLogin(email,pwd) );

        //validates correct logIn
        assertNotNull(facadeInstance.getCurrentSession());
        assertTrue( facadeInstance.getCurrentSession().isLoggedIn() );

        //validates correct parameters were set
        String resultEmail = facadeInstance.getCurrentSession().getUser().getEmail();
        String resultPwd = facadeInstance.getCurrentSession().getUser().getPassword();
        assertEquals(email, resultEmail);
        assertEquals(pwd, resultPwd);
    }

    @Test
    void getCurrentSession() {

    }

    @Test
    void doLogout() throws SQLException {

        String email = "email@fakeEmail.com";
        String pwd = "fakePwd";

        //use mock as DB access
        when(userdb.getUserByEmail(email)).thenReturn(new User(email, pwd));

        //confirms logIn
        assertNotNull( facadeInstance.doLogin(email,pwd) );
        assertNotNull(facadeInstance.getCurrentSession());

        //logOut
        facadeInstance.doLogout();

        //confirm LogOut
        assertNull(facadeInstance.getCurrentSession());

    }

    @Test
    void loggedInAs() throws SQLException {

        String email = "email@fakeEmail.com";
        String pwd = "fakePwd";

        //use mock as DB access --> mock logIn has no role!
        // Role is only associated in DB
        when(userdb.getUserByEmail(email)).thenReturn(new User(email, pwd));


        //confirms logIn
        assertNotNull( facadeInstance.doLogin(email,pwd) );



        //if parameter is null or empty is always false
        assertFalse( facadeInstance.loggedInAs(null) );
        assertFalse( facadeInstance.loggedInAs("") );
        assertFalse( facadeInstance.loggedInAs("client") );
        assertFalse( facadeInstance.loggedInAs("admin") );

    }
}