package lapr.project.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lapr.project.model.User;
import lapr.project.model.UserRole;

public class UserDB extends DataHandler {

    /**
     * Exemplo de invocação de uma "stored function".
     * <p>
     * Devolve o registo do marinheiro especificado existente na tabela
     * "Sailors".
     *
     * @param id o identificador do marinheiro.
     * @return o registo do id especificado ou null, se esse registo não
     * existir.
     */
    public User getUserByEmail(String email) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery("select user_entry.email, "
                    + "user_entry.password, "
                    + "user_role.description from user_entry "
                    + "join user_role on "
                    + "user_role.id_user_role=user_entry.fk_user_role_id "
                    + "where user_entry.email='" + email + "'");

            User user = null;
            UserRole userRole = null;
            rs.next();
            //while (rs.next()) {
            user = new User(rs.getString("email"), rs.getString("password"));
            userRole = new UserRole(rs.getString("description"));
            user.setRole(userRole);
            //userIterable.add(user);
            //}
            return user;
            //callStmt = getConnection().prepareCall("select userrole.description, user.email, user.password from user join userrole on userrole.id_userrole=user.fk_userrole_id");
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            try {
                //
            } catch (Exception e) {
            }
        }

    }

    /**
     * This method insert an user into db with email, passowrd and role)
     *
     * @param user
     * @return true is sucess
     * @throws SQLException
     */
    public boolean insertUser(User user) throws SQLException {
        boolean success = false;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery("INSERT INTO USER_ENTRY (email, password, fk_user_role_id) VALUES('"
                    + user.getEmail() + "', '"
                    + user.getPassword() + "', "
                    + user.getRole().getRole() + ")");
            rs.close();
            success = true;
            return success;
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            try {
                //
            } catch (Exception e) {
            }
        }
    }

    /**
     * This method update an user password in db (new email and new role
     * requeires a new user)
     *
     * @param user
     * @return true if success
     * @throws SQLException
     */
    public boolean updateUser(User user) throws SQLException {
        boolean success = false;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery("UPDATE USER_ENTRY SET "
                    + "PASSWORD = '" + user.getPassword() + "' "
                    + "WHERE EMAIL = '" + user.getEmail() + "'");
            rs.close();
            success = true;
            return success;
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            try {
                //
            } catch (Exception e) {
            }
        }

    }

}
