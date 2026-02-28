package com.example.ebike_testing_system.controller;

import com.example.ebike_testing_system.model.Customer;
import com.example.ebike_testing_system.model.EbikeModel;
import com.example.ebike_testing_system.service.CustomerService;
import com.example.ebike_testing_system.service.EbikeModelService;
import com.example.ebike_testing_system.service.EbikeService;
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
class CustomerControllerTest {

    private CustomerService customerSvc;
    private EbikeService ebikeSvc;
    private EbikeModelService modelSvc;
    private CustomerController controller;

    @BeforeEach
    void setUp() {
        customerSvc = mock(CustomerService.class);
        ebikeSvc    = mock(EbikeService.class);
        modelSvc    = mock(EbikeModelService.class);
        controller  = new CustomerController(customerSvc, ebikeSvc, modelSvc);
    }

    @Test
    void showCustomers_addsListAndReturnsView() {
        Customer c1 = new Customer();
        c1.setName("Alice");
        Customer c2 = new Customer();
        c2.setName("Bob");
        List<Customer> list = List.of(c1, c2);
        given(customerSvc.findAll()).willReturn(list);

        Model model = new ExtendedModelMap();
        String view = controller.showCustomers(model);

        assertEquals("tech/customers", view);
        assertEquals(list, model.getAttribute("customers"));
    }

    @Test
    void listCustomerBikes_addsCustomerAndModels() {
        int custId = 5;
        Customer cust = new Customer();
        cust.setName("Carl");
        given(customerSvc.getCustomerWithBikes(custId)).willReturn(cust);

        EbikeModel m = new EbikeModel();
        m.setName("M1");
        List<EbikeModel> models = List.of(m);
        given(modelSvc.findAll()).willReturn(models);

        Model model = new ExtendedModelMap();
        String view = controller.listCustomerBikes(custId, model);

        assertEquals("tech/customer-bikes", view);
        assertEquals(cust,   model.getAttribute("customer"));
        assertEquals(models, model.getAttribute("ebikeModels"));
    }

    @Test
    void showAddBikeForm_returnsFormViewWithModelsAndCustomerId() {
        int custId = 7;
        EbikeModel m = new EbikeModel();
        m.setName("M2");
        List<EbikeModel> models = List.of(m);
        given(modelSvc.findAll()).willReturn(models);

        Model model = new ExtendedModelMap();
        String view = controller.showAddBikeForm(custId, model);

        assertEquals("tech/customer-bikes", view);
        assertEquals(models,   model.getAttribute("ebikeModels"));
        assertEquals(custId,   model.getAttribute("customerId"));
    }


}

