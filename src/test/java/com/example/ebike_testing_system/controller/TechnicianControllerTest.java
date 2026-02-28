package com.example.ebike_testing_system.controller;

import com.example.ebike_testing_system.model.Ebike;
import com.example.ebike_testing_system.model.EbikeModel;
import com.example.ebike_testing_system.model.Customer;
import com.example.ebike_testing_system.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

//@SpringBootTest
//@ActiveProfiles("test")
class TechnicianControllerTest {

    private EbikeModelService modelSvc;
    private EbikeService ebikeSvc;
    private CustomerService customerSvc;
    private TechnicianController controller;
    private TestService testsvc;
    private TechnicianService techsvc;

    @BeforeEach
    void setUp() {
        modelSvc    = mock(EbikeModelService.class);
        ebikeSvc    = mock(EbikeService.class);
        customerSvc = mock(CustomerService.class);
        testsvc     = mock(TestService.class);
        techsvc     = mock(TechnicianService.class);
        controller  = new TechnicianController(modelSvc, ebikeSvc, customerSvc, testsvc, techsvc);
    }

    @Test
    void getDashboard_populatesCountsAndLatestNames() {
        // Arrange
        EbikeModel m1 = new EbikeModel();
        m1.setName("M1");
        EbikeModel m2 = new EbikeModel();
        m2.setName("M2");
        List<EbikeModel> models = List.of(m1, m2);
        given(modelSvc.findAll()).willReturn(models);

        Customer c1 = new Customer();
        c1.setName("Alice");
        Customer c2 = new Customer();
        c2.setName("Bob");
        List<Customer> customers = List.of(c1, c2);
        given(customerSvc.findAll()).willReturn(customers);

        Ebike b1 = new Ebike();
        b1.setBrand("BrandX");
        Ebike b2 = new Ebike();
        b2.setBrand("BrandY");
        List<Ebike> bikes = List.of(b1, b2);
        given(ebikeSvc.findAll()).willReturn(bikes);

        Model model = new ExtendedModelMap();

        // Act
        String viewName = controller.getDashboard(model);

        // Assert
        assertEquals("tech/dashboard", viewName);

        assertEquals(models.size(),
                model.getAttribute("modelsCount"),
                "modelsCount should be total models");
        assertEquals(models.get(models.size() - 1).getName(),
                model.getAttribute("latestModelName"),
                "latestModelName should be name of last model");

        assertEquals(customers.size(),
                model.getAttribute("customersCount"),
                "customersCount should be total customers");
        assertEquals(customers.get(customers.size() - 1).getName(),
                model.getAttribute("latestCustomerName"),
                "latestCustomerName should be name of last customer");

        assertEquals(bikes.size(),
                model.getAttribute("bikesCount"),
                "bikesCount should be total ebikes");
        assertEquals(bikes.get(bikes.size() - 1).getBrand(),
                model.getAttribute("popularBikeBrand"),
                "popularBikeBrand should be brand of last ebike");
    }
}
