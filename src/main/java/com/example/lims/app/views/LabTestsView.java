package com.example.lims.app.views;

import com.example.lims.service.LabTestService;
import com.example.lims.service.dto.LabTestDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "tests", layout = MainLayout.class)
@PageTitle("Lab Tests | Lab Management")
public class LabTestsView extends VerticalLayout {

    private final LabTestService labTestService;
    private final Grid<LabTestDto> grid = new Grid<>(LabTestDto.class, false);
    private final TextField searchField = new TextField();

    public LabTestsView(LabTestService labTestService) {
        this.labTestService = labTestService;
        setSizeFull();
        configureGrid();
        add(buildToolbar(), grid);
        refreshGrid(null);
    }

    private HorizontalLayout buildToolbar() {
        searchField.setPlaceholder("Search tests...");
        searchField.setClearButtonVisible(true);
        searchField.addValueChangeListener(e -> refreshGrid(e.getValue()));

        Button addButton = new Button("Add Lab Test", e -> openEditDialog(new LabTestDto()));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout toolbar = new HorizontalLayout(searchField, addButton);
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        return toolbar;
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(LabTestDto::getCode).setHeader("Code").setSortable(true).setWidth("100px").setFlexGrow(0);
        grid.addColumn(LabTestDto::getName).setHeader("Name").setSortable(true);
        grid.addColumn(LabTestDto::getDescription).setHeader("Description");
        grid.addColumn(t -> t.getPrice() != null ? "$" + t.getPrice() : "").setHeader("Price").setSortable(true);
        grid.addComponentColumn(test -> {
            Button edit = new Button("Edit", e -> openEditDialog(test));
            edit.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Button delete = new Button("Delete", e -> confirmDelete(test));
            delete.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
            return new HorizontalLayout(edit, delete);
        }).setHeader("Actions").setFlexGrow(0);
    }

    private void refreshGrid(String query) {
        grid.setItems(labTestService.search(query));
    }

    private void openEditDialog(LabTestDto test) {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");

        H2 title = new H2(test.getId() == null ? "Add Lab Test" : "Edit Lab Test");

        Binder<LabTestDto> binder = new Binder<>(LabTestDto.class);
        LabTestDto formBean = test.getId() == null ? new LabTestDto() : copy(test);

        TextField code = new TextField("Code");
        TextField name = new TextField("Name");
        TextArea description = new TextArea("Description");
        BigDecimalField price = new BigDecimalField("Price ($)");

        binder.forField(code).asRequired("Code is required").bind(LabTestDto::getCode, LabTestDto::setCode);
        binder.forField(name).asRequired("Name is required").bind(LabTestDto::getName, LabTestDto::setName);
        binder.forField(description).bind(LabTestDto::getDescription, LabTestDto::setDescription);
        binder.forField(price).bind(LabTestDto::getPrice, LabTestDto::setPrice);
        binder.readBean(formBean);

        FormLayout form = new FormLayout(code, name, description, price);
        form.setColspan(description, 2);

        Button save = new Button("Save", e -> {
            if (binder.writeBeanIfValid(formBean)) {
                labTestService.save(formBean);
                dialog.close();
                refreshGrid(searchField.getValue());
                Notification.show("Lab test saved", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancel = new Button("Cancel", e -> dialog.close());

        dialog.add(new VerticalLayout(title, form, new HorizontalLayout(save, cancel)));
        dialog.open();
    }

    private void confirmDelete(LabTestDto test) {
        ConfirmDialog confirm = new ConfirmDialog();
        confirm.setHeader("Delete Lab Test");
        confirm.setText("Are you sure you want to delete " + test.getName() + "?");
        confirm.setCancelable(true);
        confirm.setConfirmText("Delete");
        confirm.setConfirmButtonTheme("error primary");
        confirm.addConfirmListener(e -> {
            labTestService.deleteById(test.getId());
            refreshGrid(searchField.getValue());
            Notification.show("Lab test deleted", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });
        confirm.open();
    }

    private LabTestDto copy(LabTestDto src) {
        LabTestDto copy = new LabTestDto();
        copy.setId(src.getId());
        copy.setCode(src.getCode());
        copy.setName(src.getName());
        copy.setDescription(src.getDescription());
        copy.setPrice(src.getPrice());
        return copy;
    }
}
