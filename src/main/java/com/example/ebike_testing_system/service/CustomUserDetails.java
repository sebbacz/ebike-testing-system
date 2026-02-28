package com.example.ebike_testing_system.service;

import com.example.ebike_testing_system.model.Account;
import com.example.ebike_testing_system.model.Admin;
import com.example.ebike_testing_system.model.Roles;
import com.example.ebike_testing_system.model.Technician;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private final int customUserId;
    private final boolean isApproved;

    public CustomUserDetails(final int customUserId,
                      final String email,
                      final String password,
                      final boolean isApproved,
                      final Roles role){
        super(email, password, Set.of(
                new SimpleGrantedAuthority("ROLE_" + role.name())
        ));
        this.customUserId=customUserId;
        this.isApproved=isApproved;
    }



    public static UserDetails from(final Account account){

        boolean approved = false;

        if (account instanceof Technician technician) {
            approved = technician.isRoleApproved(); // Use your getter
        } else if (account instanceof Admin admin) {
            approved = admin.isRoleApproved();
        }

        return new CustomUserDetails(
                account.getId(),
                account.getEmail(),
                account.getPassword(),
                approved,
                account.getRole()
        );
    }


    public int getCustomUserId() {
        return customUserId;
    }

    public Boolean getApproved() {
        return isApproved;
    }



//    private Account account;
//
//    public CustomUserDetails(Account account) {
//        this.account = account;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.singleton(new SimpleGrantedAuthority(account.getRole()));
//    }
//
//    @Override
//    public String getPassword() {
//        return account.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return account.getEmail();
//    }



}
