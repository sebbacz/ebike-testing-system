package com.example.ebike_testing_system.repository;

import com.example.ebike_testing_system.model.Account;
import com.example.ebike_testing_system.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account,Integer> {

    @Query("SELECT a FROM Account a WHERE a.email = ?1")
    public Account findByEmail(String email);

    public Account findByResetPasswordToken(String token);

    @Query("SELECT a FROM Account a WHERE a.role = ?1")
    List<Account> findAllByRole(Roles role);
}
