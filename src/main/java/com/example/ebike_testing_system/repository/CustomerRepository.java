
package com.example.ebike_testing_system.repository;
import com.example.ebike_testing_system.model.Customer;
import com.example.ebike_testing_system.model.Ebike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Query("""
    SELECT c
      FROM Customer c
      LEFT JOIN FETCH c.customerBikes cb
      LEFT JOIN FETCH cb.ebike
     WHERE c.id = :id
    """)
    Optional<Customer> findByIdWithBikes(@Param("id") int id);
    @Query("SELECT cb.ebike FROM CustomerBike cb WHERE cb.customer.id = :custId")
    List<Ebike> findEbikesForCustomer(int custId);
    Optional<Customer> findByEmail(String email);
    @Query("SELECT c FROM Customer c JOIN FETCH c.customerBikes WHERE c.id = :customerId")
    Customer findCustomerWithBikes(int customerId);
}