package uk.ac.sheffield.com2008.controller.manager;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.exceptions.BadRequestException;
import uk.ac.sheffield.com2008.exceptions.UserAlreadyHasRoleException;
import uk.ac.sheffield.com2008.exceptions.UserNotExistsException;
import uk.ac.sheffield.com2008.model.dao.UserDAO;
import uk.ac.sheffield.com2008.model.domain.managers.UserManager;
import uk.ac.sheffield.com2008.model.entities.PersonalDetails;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.manager.ManageUserRolesView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManageUserRolesController extends ViewController {
    private final ManageUserRolesView manageUserRolesView;
    private List<User> standardUserList = new ArrayList<>();
    private List<User> staffList = new ArrayList<>();

    public ManageUserRolesController(NavigationManager navigationManager, Navigation id) {
        super(navigationManager, id);
        this.view = new ManageUserRolesView(this);
        this.manageUserRolesView = (ManageUserRolesView) view;
    }

    @Override
    public void onNavigateTo() {
        List<User> allUsers = new ArrayList<>();
        try {
            allUsers = UserDAO.getAllUsers().stream().filter(user -> user.getAddress() != null).toList();
        } catch (SQLException e) {
            navigation.setLayoutMessage(
                    "Manage User Roles Error",
                    "Could not connect to database. Users list was not fetched.", true);
        }

        standardUserList = new ArrayList<>();
        staffList = new ArrayList<>();
        for (User user : allUsers) {
            if (!user.hasRole(User.Role.STAFF)) {
                standardUserList.add(user);
            } else if (!user.hasRole(User.Role.MANAGER)) {
                staffList.add(user);
            }
        }

        manageUserRolesView.populateTable(staffList);
    }

    public void revokeStaffRole(User user) {
        String errorMessage;
        try {
            UserManager.revokeStaff(user);
            PersonalDetails personalDetails = user.getPersonalDetails();
            StringBuilder messageBuilder = new StringBuilder().append("User ").append(personalDetails.getForename()).append(" ")
                    .append(personalDetails.getSurname()).append(" was successfully revoked staff role");
            navigation.setLayoutMessage("Manage User Roles", messageBuilder.toString(), false);

            staffList.remove(user);
            standardUserList.add(user);
            manageUserRolesView.populateTable(staffList);
            return;
        } catch (SQLException e) {
            errorMessage = "Could not connect to database. User was not revoked staff role.";
        } catch (BadRequestException | UserNotExistsException e) {
            errorMessage = e.getMessage();
        }

        navigation.setLayoutMessage(
                "Manage User Roles Error",
                errorMessage, true);
    }

    public void appointStaffRole(String email) {
        String errorMessage;
        try {
            User user = UserManager.appointStaff(email);
            navigation.setLayoutMessage("Manage User Roles", "User of email " + email + " was successfully promoted to staff", false);

            staffList.add(user);
            standardUserList.remove(user);
            manageUserRolesView.populateTable(staffList);
            return;
        } catch (SQLException e) {
            errorMessage = "Could not connect to database. User was not revoked staff role.";
        } catch (UserNotExistsException | UserAlreadyHasRoleException e) {
            errorMessage = e.getMessage();
        }

        navigation.setLayoutMessage(
                "Manage User Roles Error",
                errorMessage, true);
    }

    public boolean isCustomerEmailValid(String email) {
        return standardUserList.stream().anyMatch(customer -> customer.getEmail().equals(email));
    }
}
