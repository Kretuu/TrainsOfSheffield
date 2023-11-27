package uk.ac.sheffield.com2008.model.dao;

import uk.ac.sheffield.com2008.database.DatabaseConnectionHandler;
import uk.ac.sheffield.com2008.model.domain.data.AuthUser;
import uk.ac.sheffield.com2008.model.entities.Address;
import uk.ac.sheffield.com2008.model.entities.PersonalDetails;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.model.mappers.AuthUserMapper;
import uk.ac.sheffield.com2008.model.mappers.RowMapper;
import uk.ac.sheffield.com2008.model.mappers.UserMapper;
import uk.ac.sheffield.com2008.util.EncryptionManager;

import java.sql.SQLException;
import java.util.List;

//Mediator pattern - connection between User entity and database. Fetching, updating, deleting etc. all information
//related to User entity.
public class UserDAO {
    public static User getUserByEmail(String email) throws SQLException {
        return getUserByField("email", email);
    }

    private static User getUserByField(String fieldName, Object value) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT uuid, email, forename, surname, roles, Users.houseNumber, Users.postcode, ")
                .append("roadName, cityName FROM Users LEFT OUTER JOIN Address A ON ")
                .append("Users.postcode = A.postcode AND Users.houseNumber = A.houseNumber WHERE ")
                .append(fieldName).append(" = ?");
        String query = queryBuilder.toString();

        UserMapper mapper = new UserMapper();
        List<User> users = DatabaseConnectionHandler.select(mapper, query, value);

        if (users.isEmpty()) return null;

        return users.get(0);
    }

    public static User verifyPassword(String userEmail, char[] password) throws SQLException {
        String query = "SELECT password, salt FROM Users WHERE email = ?";

        AuthUserMapper mapper = new AuthUserMapper();
        List<AuthUser> authUsers = DatabaseConnectionHandler.select(mapper, query, userEmail);
        if (authUsers.isEmpty()) return null;

        AuthUser authUser = authUsers.get(0);
        String passwordHash = EncryptionManager.hashPassword(password, authUser.salt());

        if (authUser.passwordHash().equals(passwordHash)) {
            return getUserByEmail(userEmail);
        }
        return null;
    }

    public static void createUser(User user, AuthUser authUser) throws SQLException {
        String query = "INSERT INTO Users (uuid, email, password, salt, forename, surname) VALUES (?, ?, ?, ?, ?, ?)";
        PersonalDetails details = user.getPersonalDetails();

        DatabaseConnectionHandler.insert(
                query,
                user.getUuid(),
                user.getEmail(),
                authUser.passwordHash(),
                authUser.salt(),
                details.getForename(),
                details.getSurname()
        );
    }

    public static void addOrUpdateAddress(Address address) throws SQLException {
        StringBuilder addressQueryBuilder = new StringBuilder();
        addressQueryBuilder.append("INSERT INTO Address (houseNumber, postcode, roadName, cityName) ");
        addressQueryBuilder.append("VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE roadName = ?, cityName = ?");
        String addressQuery = addressQueryBuilder.toString();

        DatabaseConnectionHandler.insert(
                addressQuery,
                address.getCombinedHouseNo(),
                address.getPostCode(),
                address.getStreet(),
                address.getCity(),
                address.getStreet(),
                address.getCity()
        );
    }

    public static void updateUser(User user) throws SQLException {
        Address address = user.getAddress();
        addOrUpdateAddress(address);

        StringBuilder userQueryBuilder = new StringBuilder().append("UPDATE Users SET ")
                .append("email = ?, forename = ?, surname = ?, houseNumber = ?, postcode = ?, roles = ? ")
                .append("WHERE uuid = ?");
        String userQuery = userQueryBuilder.toString();
        PersonalDetails personalDetails = user.getPersonalDetails();

        DatabaseConnectionHandler.update(
                userQuery,
                user.getEmail(),
                personalDetails.getForename(),
                personalDetails.getSurname(),
                address.getCombinedHouseNo(),
                address.getPostCode(),
                User.Role.parseRolesToString(user.getRoles()),
                user.getUuid()
        );
    }

    public static void removeUnusedAddresses() throws SQLException {
        StringBuilder queryBuilder = new StringBuilder()
                .append("DELETE FROM Address WHERE NOT EXISTS (SELECT 1 FROM Users ")
                .append("WHERE Users.houseNumber = Address.houseNumber AND Users.postcode = Address.postcode)");
        String query = queryBuilder.toString();

        DatabaseConnectionHandler.delete(query);
    }

    public static String fetchUserSalt(User user) throws SQLException {
        String query = "SELECT salt FROM Users WHERE uuid = ?";

        RowMapper<String> mapper = resultSet -> resultSet.getString("salt");
        List<String> salts = DatabaseConnectionHandler.select(mapper, query, user.getUuid());

        if (salts.isEmpty()) return null;
        return salts.get(0);
    }
}
