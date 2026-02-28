package com.example.ebike_testing_system.service;

import com.example.ebike_testing_system.exception.NotFoundException;
import com.example.ebike_testing_system.model.Customer;
import com.example.ebike_testing_system.model.Ebike;
import com.example.ebike_testing_system.model.CustomerBike;
import com.example.ebike_testing_system.repository.CustomerBikeRepository;
import com.example.ebike_testing_system.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

//@SpringBootTest
//@ActiveProfiles("test")
class CustomerServiceTest {

    private CustomerRepository custRepo;
    private CustomerBikeRepository bikeRepo;
    private CustomerService svc;

    @BeforeEach
    void setUp() {
        custRepo = mock(CustomerRepository.class);
        bikeRepo = mock(CustomerBikeRepository.class);
        svc      = new CustomerService(custRepo, bikeRepo);
    }

    @Test
    void findAll_returnsRepoList() {
        List<Customer> sample = List.of(new Customer());
        given(custRepo.findAll()).willReturn(sample);

        assertEquals(sample, svc.findAll());
    }

    @Test
    void getCustomerWithBikes_missing_throws() {
        int missingCustomerId = 999;
        given(custRepo.findById(missingCustomerId)).willReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> svc.getCustomerWithBikes(missingCustomerId),
                "Customer not found");
    }

    @Test
    void findCustomerEbikes_returnsRepoData() {
        List<Ebike> sample = List.of(new Ebike());
        given(custRepo.findEbikesForCustomer(10)).willReturn(sample);

        assertEquals(sample, svc.findCustomerEbikes(10));
    }

    @Test
    void removeLink_missing_throwsNotFound() {
        given(bikeRepo.findByCustomerIdAndEbikeId(2, 3))
                .willReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> svc.removeLink(2, 3));
    }

    @Test
    void removeLink_found_deletesLink() {
        CustomerBike link = new CustomerBike();
        // no need to set fields—stub ignores them
        given(bikeRepo.findByCustomerIdAndEbikeId(2, 3))
                .willReturn(Optional.of(link));

        svc.removeLink(2, 3);

        then(bikeRepo).should().delete(link);
    }
}
