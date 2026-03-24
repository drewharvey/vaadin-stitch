package com.example.lims.app.views;

import com.example.lims.persistence.OrderStatus;
import com.example.lims.service.LabOrderService;
import com.example.lims.service.LabTestService;
import com.example.lims.service.PatientService;
import com.example.lims.service.dto.LabOrderDto;
import com.example.lims.service.dto.LabTestDto;
import com.example.lims.service.dto.PatientDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;

@Route(value = "orders", layout = MainLayout.class)
@PageTitle("Orders | Lab Management")
public class OrdersView extends VerticalLayout {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final LabOrderService orderService;
    private final PatientService patientService;
    private final LabTestService labTestService;
    private final Grid<LabOrderDto> grid = new Grid<>(LabOrderDto.class, false);

    public OrdersView(LabOrderService orderService, PatientService patientService, LabTestService labTestService) {
        this.orderService = orderService;
        this.patientService = patientService;
        this.labTestService = labTestService;
        setSizeFull();
        configureGrid();
        add(buildToolbar(), grid);
        refreshGrid();
    }

    private HorizontalLayout buildToolbar() {
        Button addButton = new Button("New Order", e -> openCreateOrderDialog());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout toolbar = new HorizontalLayout(addButton);
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(JustifyContentMode.END);
        return toolbar;
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(LabOrderDto::getId).setHeader("ID").setWidth("80px").setFlexGrow(0).setSortable(true);
        grid.addColumn(LabOrderDto::getPatientFullName).setHeader("Patient").setSortable(true);
        grid.addColumn(o -> o.getLabTestCode() + " - " + o.getLabTestName()).setHeader("Test").setSortable(true);
        grid.addColumn(o -> o.getOrderedAt() != null ? o.getOrderedAt().format(FORMATTER) : "").setHeader("Ordered At").setSortable(true);
        grid.addColumn(o -> o.getStatus() != null ? o.getStatus().name() : "").setHeader("Status").setSortable(true);
        grid.addColumn(LabOrderDto::getNotes).setHeader("Notes");
        grid.addComponentColumn(order -> {
            Button update = new Button("Update", e -> openUpdateDialog(order));
            update.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Button delete = new Button("Delete", e -> confirmDelete(order));
            delete.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
            return new HorizontalLayout(update, delete);
        }).setHeader("Actions").setFlexGrow(0);
    }

    private void refreshGrid() {
        grid.setItems(orderService.findAll());
    }

    private void openCreateOrderDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");

        H2 title = new H2("New Order");

        ComboBox<PatientDto> patientCombo = new ComboBox<>("Patient");
        patientCombo.setItems(patientService.findAll());
        patientCombo.setItemLabelGenerator(PatientDto::getFullName);
        patientCombo.setWidthFull();

        ComboBox<LabTestDto> testCombo = new ComboBox<>("Lab Test");
        testCombo.setItems(labTestService.findAll());
        testCombo.setItemLabelGenerator(t -> t.getCode() + " - " + t.getName());
        testCombo.setWidthFull();

        Button save = new Button("Create Order", e -> {
            PatientDto patient = patientCombo.getValue();
            LabTestDto test = testCombo.getValue();
            if (patient == null || test == null) {
                Notification.show("Please select both patient and test", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            orderService.createOrder(patient.getId(), test.getId());
            dialog.close();
            refreshGrid();
            Notification.show("Order created successfully", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancel = new Button("Cancel", e -> dialog.close());

        FormLayout form = new FormLayout(patientCombo, testCombo);
        dialog.add(new VerticalLayout(title, form, new HorizontalLayout(save, cancel)));
        dialog.open();
    }

    private void openUpdateDialog(LabOrderDto order) {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");

        H2 title = new H2("Update Order #" + order.getId());

        ComboBox<OrderStatus> statusCombo = new ComboBox<>("Status");
        statusCombo.setItems(OrderStatus.values());
        statusCombo.setValue(order.getStatus());
        statusCombo.setWidthFull();

        TextArea notes = new TextArea("Notes");
        notes.setValue(order.getNotes() != null ? order.getNotes() : "");
        notes.setWidthFull();
        notes.setMinHeight("100px");

        Button save = new Button("Save", e -> {
            orderService.updateOrder(order.getId(), statusCombo.getValue(), notes.getValue());
            dialog.close();
            refreshGrid();
            Notification.show("Order updated successfully", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancel = new Button("Cancel", e -> dialog.close());

        FormLayout form = new FormLayout(statusCombo, notes);
        form.setColspan(notes, 2);
        dialog.add(new VerticalLayout(title, form, new HorizontalLayout(save, cancel)));
        dialog.open();
    }

    private void confirmDelete(LabOrderDto order) {
        ConfirmDialog confirm = new ConfirmDialog();
        confirm.setHeader("Delete Order");
        confirm.setText("Are you sure you want to delete order #" + order.getId() + "?");
        confirm.setCancelable(true);
        confirm.setConfirmText("Delete");
        confirm.setConfirmButtonTheme("error primary");
        confirm.addConfirmListener(e -> {
            orderService.deleteById(order.getId());
            refreshGrid();
            Notification.show("Order deleted", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });
        confirm.open();
    }
}
