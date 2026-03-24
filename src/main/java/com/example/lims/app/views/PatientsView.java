package com.example.lims.app.views;

import com.example.lims.service.PatientService;
import com.example.lims.service.dto.PatientDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "patients", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PageTitle("Patients | Lab Management")
public class PatientsView extends VerticalLayout {

    private final PatientService patientService;
    private final Grid<PatientDto> grid = new Grid<>(PatientDto.class, false);
    private final TextField searchField = new TextField();

    public PatientsView(PatientService patientService) {
        this.patientService = patientService;
        setSizeFull();
        configureGrid();
        add(buildToolbar(), grid);
        refreshGrid(null);
    }

    private HorizontalLayout buildToolbar() {
        searchField.setPlaceholder("Search patients...");
        searchField.setClearButtonVisible(true);
        searchField.addValueChangeListener(e -> refreshGrid(e.getValue()));

        Button addButton = new Button("Add Patient", e -> openEditDialog(new PatientDto()));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout toolbar = new HorizontalLayout(searchField, addButton);
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        return toolbar;
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(PatientDto::getFirstName).setHeader("First Name").setSortable(true);
        grid.addColumn(PatientDto::getLastName).setHeader("Last Name").setSortable(true);
        grid.addColumn(PatientDto::getDateOfBirth).setHeader("Date of Birth").setSortable(true);
        grid.addColumn(PatientDto::getEmail).setHeader("Email").setSortable(true);
        grid.addColumn(PatientDto::getPhone).setHeader("Phone");
        grid.addComponentColumn(patient -> {
            Button edit = new Button("Edit", e -> openEditDialog(patient));
            edit.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Button delete = new Button("Delete", e -> confirmDelete(patient));
            delete.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
            return new HorizontalLayout(edit, delete);
        }).setHeader("Actions").setFlexGrow(0);
    }

    private void refreshGrid(String query) {
        grid.setItems(patientService.search(query));
    }

    private void openEditDialog(PatientDto patient) {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");

        H2 title = new H2(patient.getId() == null ? "Add Patient" : "Edit Patient");

        Binder<PatientDto> binder = new Binder<>(PatientDto.class);
        PatientDto formBean = patient.getId() == null ? new PatientDto() : copy(patient);

        TextField firstName = new TextField("First Name");
        TextField lastName = new TextField("Last Name");
        DatePicker dob = new DatePicker("Date of Birth");
        TextField email = new TextField("Email");
        TextField phone = new TextField("Phone");

        binder.forField(firstName).asRequired("First name is required").bind(PatientDto::getFirstName, PatientDto::setFirstName);
        binder.forField(lastName).asRequired("Last name is required").bind(PatientDto::getLastName, PatientDto::setLastName);
        binder.forField(dob).bind(PatientDto::getDateOfBirth, PatientDto::setDateOfBirth);
        binder.forField(email).bind(PatientDto::getEmail, PatientDto::setEmail);
        binder.forField(phone).bind(PatientDto::getPhone, PatientDto::setPhone);
        binder.readBean(formBean);

        FormLayout form = new FormLayout(firstName, lastName, dob, email, phone);

        Button save = new Button("Save", e -> {
            if (binder.writeBeanIfValid(formBean)) {
                patientService.save(formBean);
                dialog.close();
                refreshGrid(searchField.getValue());
                Notification.show("Patient saved successfully", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancel = new Button("Cancel", e -> dialog.close());

        dialog.add(new VerticalLayout(title, form, new HorizontalLayout(save, cancel)));
        dialog.open();
    }

    private void confirmDelete(PatientDto patient) {
        ConfirmDialog confirm = new ConfirmDialog();
        confirm.setHeader("Delete Patient");
        confirm.setText("Are you sure you want to delete " + patient.getFullName() + "?");
        confirm.setCancelable(true);
        confirm.setConfirmText("Delete");
        confirm.setConfirmButtonTheme("error primary");
        confirm.addConfirmListener(e -> {
            patientService.deleteById(patient.getId());
            refreshGrid(searchField.getValue());
            Notification.show("Patient deleted", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });
        confirm.open();
    }

    private PatientDto copy(PatientDto src) {
        PatientDto copy = new PatientDto();
        copy.setId(src.getId());
        copy.setFirstName(src.getFirstName());
        copy.setLastName(src.getLastName());
        copy.setDateOfBirth(src.getDateOfBirth());
        copy.setEmail(src.getEmail());
        copy.setPhone(src.getPhone());
        copy.setCreatedAt(src.getCreatedAt());
        return copy;
    }
}
