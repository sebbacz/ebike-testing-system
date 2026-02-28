package com.example.ebike_testing_system.service;

import com.example.ebike_testing_system.exception.NotFoundException;
import com.example.ebike_testing_system.model.*;
import com.example.ebike_testing_system.repository.AccountRepository;
import com.example.ebike_testing_system.repository.AdminRepository;
import com.example.ebike_testing_system.repository.TechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TechnicianRepository technicianRepository;

    @Autowired
    private AdminRepository adminRepository;

    public void updateResetPasswordToken(String token, String email) throws NotFoundException {
        Account account = accountRepository.findByEmail(email);
        if (account != null) {
            account.setResetPasswordToken(token);
            accountRepository.save(account);
        } else {
            throw new NotFoundException("Could not find any account with the email " + email);
        }
    }

    public void createSuperAdmin() {
        if (accountRepository.findByEmail("adminsuper@kdg.be") == null) {
            SuperAdmin superAdmin = new SuperAdmin();
            superAdmin.setFullName("Super Admin");
            superAdmin.setEmail("adminsuper@kdg.be");
            superAdmin.setPassword(passwordEncoder.encode("KDG123"));
            superAdmin.setRole(Roles.SUPERADMIN);
            accountRepository.save(superAdmin);
        }
    }

    public void approveAdmin(int adminId) {
        Admin admin = (Admin) accountRepository.findById(adminId).orElseThrow(() -> new IllegalArgumentException("Invalid admin Id:" + adminId));
        admin.setRoleApproved(true);
        accountRepository.save(admin);
    }

    public List<Admin> getAllAdmins() {
        return accountRepository.findAllByRole(Roles.ADMIN).stream()
                .map(account -> (Admin) account)
                .collect(Collectors.toList());
    }

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public List<Admin> getApprovedAdmins() {
        return adminRepository.findByIsRoleApproved(true);
    }

    public List<Technician> getApprovedTechnicians() {
        return technicianRepository.findByIsRoleApproved(true);
    }

    public Account getByResetPasswordToken(String token) {
        return accountRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(Account account, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        account.setPassword(encodedPassword);
        account.setResetPasswordToken(null);
        accountRepository.save(account);
    }

    public void saveAvatar(String email, MultipartFile avatar) {
        Account user = accountRepository.findByEmail(email);
        if (user != null) {
            try {
                String avatarPath = "/uploads/" + avatar.getOriginalFilename();
                Path path = Paths.get("src/main/resources/static" + avatarPath);
                Files.createDirectories(path.getParent());
                Files.write(path, avatar.getBytes());
                user.setAvatarUrl(avatarPath);
                accountRepository.save(user);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save avatar", e);
            }
        }
    }

    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public List<Admin> getUnapprovedAdmins() {
        return accountRepository.findAllByRole(Roles.ADMIN).stream()
                .map(account -> (Admin) account)
                .filter(admin -> !admin.isRoleApproved())
                .collect(Collectors.toList());
    }

    public List<Technician> getUnapprovedTechnicians() {
        return accountRepository.findAllByRole(Roles.TECHNICIAN).stream()
                .map(account -> (Technician) account)
                .filter(technician -> !technician.isRoleApproved())
                .collect(Collectors.toList());
    }

    public void approveTechnician(int technicianId) {
        Technician technician = (Technician) accountRepository.findById(technicianId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid technician Id:" + technicianId));
        technician.setRoleApproved(true);
        accountRepository.save(technician);
    }

    public Admin getAdminById(long id) {
        return adminRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Admin not found"));
    }

    public Technician getTechnicianById(long id) {
        return technicianRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Technician not found"));
    }

    public void updateAdmin(int id, String name, String email) {
        Admin admin = getAdminById(id);
        admin.setFullName(name);
        admin.setEmail(email);
        adminRepository.save(admin);
    }

    public void updateTechnician(int id, String name, String email) {
        Technician technician = getTechnicianById(id);
        technician.setFullName(name);
        technician.setEmail(email);
        technicianRepository.save(technician);
    }

    public void deleteAdmin(long id) {
        adminRepository.deleteById(id);
    }

    public void deleteTechnician(long id) {
        technicianRepository.deleteById(id);
    }
}