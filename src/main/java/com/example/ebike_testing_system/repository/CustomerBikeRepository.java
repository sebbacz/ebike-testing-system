package com.example.ebike_testing_system.repository;

import com.example.ebike_testing_system.model.Customer;
import com.example.ebike_testing_system.model.CustomerBike;
import com.example.ebike_testing_system.model.Ebike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerBikeRepository extends JpaRepository<CustomerBike, Integer> {

    Optional<CustomerBike> findByCustomerIdAndEbikeId(int custId, int bikeId);

    @Query("SELECT cb.ebike.id, COUNT(cb.customer.id) FROM CustomerBike cb GROUP BY cb.ebike.id")
    List<Object[]> countCustomersPerEbike();
    Optional<CustomerBike> findByEbikeAndCustomer(Ebike ebike, Customer customer);

}
