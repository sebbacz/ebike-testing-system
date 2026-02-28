package com.example.ebike_testing_system.service;

import com.example.ebike_testing_system.model.*;
import com.example.ebike_testing_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TechnicianRepository technicianRepository;

    @Autowired
    private TestBenchRepository testBenchRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private EbikeRepository ebikeRepository;

    @Autowired
    private TestReportRepository testReportRepository;

    @Autowired
    private AdminRepository adminRepository;

//    private TestService testService;

    public AdminService(CustomerRepository customerRepository, TechnicianRepository technicianRepository,
                        TestBenchRepository testBenchRepository, TestRepository testRepository,
                        EbikeRepository ebikeRepository, TestReportRepository testReportRepository,
                        AdminRepository adminRepository
//                        ,TestService testService
    ) {
        this.customerRepository = customerRepository;
        this.technicianRepository = technicianRepository;
        this.testBenchRepository = testBenchRepository;
        this.testRepository = testRepository;
        this.ebikeRepository = ebikeRepository;
        this.testReportRepository = testReportRepository;
        this.adminRepository = adminRepository;
//        this.testService = testService;
    }

    public Admin getLoggedInAdmin() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return adminRepository.findByEmail(username);
    }


    public List<Customer> getCustomersByAdminCompany() {
        Admin admin = getLoggedInAdmin();
        String company = admin.getCompanyName();
        return customerRepository.findAll().stream()
                .filter(c -> company != null && c.getCompanyName() != null &&
                        company.equalsIgnoreCase(c.getCompanyName()))
                .collect(Collectors.toList());
    }


    public List<Ebike> getBikesByAdminCompany() {
        List<Customer> customers = getCustomersByAdminCompany();
        Set<Integer> customerIds = customers.stream().map(Customer::getId).collect(Collectors.toSet());

        return ebikeRepository.findAll().stream()
                .filter(bike -> bike.getCustomerBike() != null &&
                        bike.getCustomerBike().stream()
                                .anyMatch(cb -> cb.getCustomer() != null &&
                                        customerIds.contains(cb.getCustomer().getId())))
                .collect(Collectors.toList());
    }

    public List<Test> getTestByAdminCompany() {
//        Account admin = getLoggedInAdmin(); // adjust if method differs
//        String adminCompany = admin.getCompanyName();

        List<Customer> customers = getCustomersByAdminCompany();

        Set<Integer> customerIds = customers.stream()
                .map(Customer::getId)
                .collect(Collectors.toSet());

        return testRepository.findAll().stream()
                .filter(test -> test.getCustomer() != null &&
                        customerIds.contains(test.getCustomer().getId()))
                .collect(Collectors.toList());
    }


    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public List<Technician> getAllTechnicians() {
        return technicianRepository.findAll();
    }

    public long getActiveTestBenchCount() {
        return testBenchRepository.countActiveTestBenches();
    }

    public List<TestBench> getTestBenchUsageProfiles() {
        return testBenchRepository.findAll();
    }

//    public Map<TestBench, List<Customer>> getUsageProfilesPerTestBench() {
//        List<TestBench> testBenches = testBenchRepository.findAll();
//        Map<TestBench, List<Customer>> usageProfiles = new HashMap<>();
//
//        for (TestBench testBench : testBenches) {
//            List<Customer> customers = testBench.getTestResults().stream()
//                    .map(TestResult::getCustomer)
//                    .filter(Objects::nonNull)
//                    .distinct()
//                    .toList();
//            usageProfiles.put(testBench, customers);
//        }
//
//        return usageProfiles;
//    }

    public Map<TestBench, Long> getTestBenchUsageFrequency() {
        List<TestBench> testBenches = testBenchRepository.findAll();
        Map<TestBench, Long> usageFrequency = new HashMap<>();

        for (TestBench testBench : testBenches) {
            long count = testBench.getTests().size();
            usageFrequency.put(testBench, count);
        }

        return usageFrequency;
    }

    public Map<Test, Long> getExecutedTestsWithCount() {
        List<Test> tests = testRepository.findAll();
        Map<Test, Long> testCounts = new HashMap<>();

        for (Test test : tests) {
            long count = testReportRepository.countByTest(test);
            testCounts.put(test, count);
        }

        return testCounts;
    }

    public List<TestReport> getAllTestResults() {
        return testReportRepository.findAll();
    }

    public List<String> getErrorCodesByAdminCompany() {
        Admin admin = getLoggedInAdmin();
        String company = admin.getCompanyName();

        List<Customer> customers = customerRepository.findAll().stream()
                .filter(c -> company != null && company.equalsIgnoreCase(c.getCompanyName()))
                .toList();

        Set<Integer> customerIds = customers.stream()
                .map(Customer::getId)
                .collect(Collectors.toSet());

        return testReportRepository.findAllWithTestAndCustomer().stream()
                .filter(report -> {
                    Test test = report.getTest();
                    return test != null && test.getCustomer() != null && customerIds.contains(test.getCustomer().getId());
                })
                .map(TestReport::getErrorCode)
                .map(code -> (code == null || code.trim().isEmpty()) ? "N/A" : code)
                .distinct()
                .toList();
    }



    public List<Test> getExecutedTests() {
        return testRepository.findByStatus(TestStatus.COMPLETED);
    }

    public long getTestCountByAdminCompany() {
        Admin admin = getLoggedInAdmin();
        String company = admin.getCompanyName();

        List<Customer> customers = customerRepository.findAll().stream()
                .filter(c -> company != null && company.equalsIgnoreCase(c.getCompanyName()))
                .toList();

        Set<Integer> customerIds = customers.stream()
                .map(Customer::getId)
                .collect(Collectors.toSet());

        return testRepository.findAll().stream()
                .filter(test -> test.getCustomer() != null &&
                        customerIds.contains(test.getCustomer().getId()))
                .count();
    }




    public Ebike addBike(Ebike bike) {
        return ebikeRepository.save(bike);
    }

    public void removeBike(int bikeId) {
        ebikeRepository.deleteById(bikeId);
    }

    public List<Ebike> getAllBikes() {
        return ebikeRepository.findAll();
    }
}