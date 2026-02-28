package com.example.ebike_testing_system.service;

import com.example.ebike_testing_system.dto.EbikeDto;
import com.example.ebike_testing_system.exception.NotFoundException;
import com.example.ebike_testing_system.model.*;
import com.example.ebike_testing_system.repository.CustomerBikeRepository;
import com.example.ebike_testing_system.repository.CustomerRepository;
import com.example.ebike_testing_system.repository.EbikeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EbikeService {

    private final EbikeRepository ebikeRepository;
    private final EbikeModelService ebikeModelService;
    private final CustomerRepository customerRepository;
    private final CustomerBikeRepository customerBikeRepository;

    public EbikeService(EbikeRepository ebikeRepository, EbikeModelService ebikeModelService,
                        CustomerRepository customerRepository, CustomerBikeRepository customerBikeRepository) {
        this.ebikeRepository = ebikeRepository;
        this.ebikeModelService = ebikeModelService;
        this.customerRepository = customerRepository;
        this.customerBikeRepository = customerBikeRepository;
    }

    public List<Ebike> findAll() {
        return ebikeRepository.findAll();
    }

    public Ebike findById(int eBikeId) {
        return ebikeRepository.findById(eBikeId).orElseThrow(() -> new NotFoundException("eBike not found"));
    }

    public List<Ebike> findByBrand(String brand) {
        return this.ebikeRepository.findByBrandIgnoreCase(brand);
    }

    public Map<Integer, Integer> getCustomerCountsByEbikeId() {
        List<Object[]> results = customerBikeRepository.countCustomersPerEbike();
        Map<Integer, Integer> countMap = new HashMap<>();

        for (Object[] row : results) {
            Integer ebikeId = (Integer) row[0];
            Long count = (Long) row[1];
            countMap.put(ebikeId, count.intValue());
        }

        return countMap;
    }

    public Ebike addEbike(Ebike eBike) {
        return ebikeRepository.save(eBike);
    }


    public Ebike addBikeForCustomer(int custId, EbikeDto dto) {
        Customer customer = customerRepository.findById(custId).orElseThrow(() -> new NotFoundException("Customer not found: " + custId));

        EbikeModel model = ebikeModelService.findById(dto.getModelId());

        Ebike ebike = new Ebike();
        ebike.setBrand(dto.getBrand());
        ebike.setModel(dto.getModel());
        ebike.setBattery(dto.getBattery());
        ebike.seteBikeModel(model);
        ebike.setAddedDate(LocalDate.now());

        ebikeRepository.save(ebike);

        CustomerBike link = new CustomerBike(ebike, customer);
        customerBikeRepository.save(link);
        return ebike;
    }

//    public Ebike addEbike(String brand, String model, String battery, EbikeModel eBikeModel,
//                          List<CustomerBike> customerBike, List<TestResult> testResults) {
//
//        final Ebike newBike = new Ebike();
//        newBike.setBrand(brand);
//        newBike.setModel(model);
//        newBike.setBattery(battery);
//        newBike.seteBikeModel(eBikeModel);
//        newBike.setCustomerBike(customerBike);
//        newBike.setTestResults(testResults);
//        ebikeRepository.save(newBike);
//
//        return newBike;
//    }

    public Ebike updateEbike(int id, EbikeDto dto) {
        Ebike ebike = findById(id); // throws if not found
        EbikeModel model = ebikeModelService.findById(dto.getModelId());

        ebike.setBrand(dto.getBrand());
        ebike.setModel(dto.getModel());
        ebike.setBattery(dto.getBattery());
        ebike.seteBikeModel(model);
        return ebikeRepository.save(ebike);
    }


    @Transactional
    public void deleteEbike(int bikeId) {
        final Ebike ebike = ebikeRepository.findById(bikeId)
                .orElseThrow(() -> new NotFoundException("eBike not found!"));
//        eModelRepository.deleteAll(model.getScreenplays());
        ebikeRepository.deleteById(bikeId);
    }


}
