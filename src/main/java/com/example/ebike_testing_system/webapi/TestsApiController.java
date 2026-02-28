package com.example.ebike_testing_system.webapi;

import com.example.ebike_testing_system.dto.EbikeModelDto;
import com.example.ebike_testing_system.dto.TestDto;
import com.example.ebike_testing_system.model.EbikeModel;
import com.example.ebike_testing_system.model.Test;
import com.example.ebike_testing_system.service.CustomerService;
import com.example.ebike_testing_system.service.EbikeService;
import com.example.ebike_testing_system.service.TestService;
import com.example.ebike_testing_system.service.TestbenchService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tests")
public class TestsApiController {
    private final TestService testService;
    private final CustomerService customerService;
    private final EbikeService ebikeService;
    private final ModelMapper modelMapper;

    public TestsApiController(TestService testService, ModelMapper modelMapper,
                              CustomerService customerService, EbikeService ebikeService) {
        this.testService = testService;
        this.customerService = customerService;
        this.ebikeService = ebikeService;
        this.modelMapper = modelMapper;
    }

    //@PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN') and principal.approved == true")
    @GetMapping("/all")
    public ResponseEntity<List<TestDto>> getAllTests(){
        final List<Test> tests = testService.getTestsForCurrentUser();
        final List<TestDto> testDtos = tests.stream().map(t -> modelMapper.map(t, TestDto.class)).toList();
        return ResponseEntity.ok(testDtos);

    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TestDto>> getAllTestsForCustomer(@PathVariable int userId){
        final List<Test> tests = testService.findByCustomerId(userId);
        final List<TestDto> testDtos = tests.stream().map(t -> modelMapper.map(t, TestDto.class)).toList();
        return ResponseEntity.ok(testDtos);

    }

    @GetMapping("/bike/{bikeId}")
    public ResponseEntity<List<TestDto>> getTestsByBikeId(@PathVariable int bikeId) {
        List<Test> tests = testService.findByBikeId(bikeId);
        List<TestDto> testDtos = tests.stream()
                .map(test -> modelMapper.map(test, TestDto.class))
                .toList();
        return ResponseEntity.ok(testDtos);
    }



    @GetMapping("/{testId}")
    public ResponseEntity<TestDto> getTestById(@PathVariable int testId) {
        final Test test = testService.findById(testId);
        return ResponseEntity.ok(modelMapper.map(test, TestDto.class));
    }

    @PreAuthorize("hasAnyRole('TECHNICIAN') and principal.approved == true")
    @PatchMapping("/{id}")
    public ResponseEntity<TestDto> updateTest(
            @PathVariable int id,
            @RequestBody TestDto dto) {

        Test udpatedTest = testService.updateTestNotes(id, dto);
        TestDto result = modelMapper.map(udpatedTest, TestDto.class);
        return ResponseEntity.ok(result);
    }


}
