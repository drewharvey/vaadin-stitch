package com.example.lims.app.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Lab Management");
        logo.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.MEDIUM);

        DrawerToggle toggle = new DrawerToggle();

        HorizontalLayout header = new HorizontalLayout(toggle, logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassNames(LumoUtility.Padding.Vertical.NONE, LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);
    }

    private void createDrawer() {
        SideNav nav = new SideNav();

        SideNavItem patientsItem = new SideNavItem("Patients", PatientsView.class);
        patientsItem.setPrefixComponent(VaadinIcon.USERS.create());

        SideNavItem testsItem = new SideNavItem("Lab Tests", LabTestsView.class);
        testsItem.setPrefixComponent(VaadinIcon.RECORDS.create());

        SideNavItem ordersItem = new SideNavItem("Orders", OrdersView.class);
        ordersItem.setPrefixComponent(VaadinIcon.CLIPBOARD_TEXT.create());

        nav.addItem(patientsItem, testsItem, ordersItem);
        addToDrawer(nav);
    }
}
