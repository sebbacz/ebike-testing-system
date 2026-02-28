package com.example.ebike_testing_system.repository;

import com.example.ebike_testing_system.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSettingRepository extends JpaRepository<Account,Integer> {
    Account findByEmail(String emial);
}
