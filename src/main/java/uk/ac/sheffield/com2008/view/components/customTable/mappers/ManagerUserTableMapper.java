package uk.ac.sheffield.com2008.view.components.customTable.mappers;

import uk.ac.sheffield.com2008.controller.manager.ManageUserRolesController;
import uk.ac.sheffield.com2008.model.entities.PersonalDetails;
import uk.ac.sheffield.com2008.model.entities.User;

import javax.swing.*;
import java.util.LinkedList;

public class ManagerUserTableMapper implements TableMapper<User> {
    private final ManageUserRolesController controller;

    public ManagerUserTableMapper(ManageUserRolesController controller) {
        this.controller = controller;
    }

    @Override
    public LinkedList<Object> constructColumns(User object) {
        LinkedList<Object> list = new LinkedList<>();
        list.add(object.getEmail());

        PersonalDetails details = object.getPersonalDetails();
        list.add(details.getForename());
        list.add(details.getSurname());

        JButton revokeStaff = new JButton("Revoke staff");
        revokeStaff.addActionListener(e -> controller.revokeStaffRole(object));
        list.add(revokeStaff);

        return list;
    }
}
