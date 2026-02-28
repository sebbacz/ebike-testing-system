package com.example.ebike_testing_system.webapi;

import com.example.ebike_testing_system.dto.CustomerDto;
import com.example.ebike_testing_system.dto.EbikeDto;
import com.example.ebike_testing_system.model.Customer;
import com.example.ebike_testing_system.model.Ebike;
import com.example.ebike_testing_system.service.CustomerService;
import com.example.ebike_testing_system.service.EbikeService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerApiController {
    private final CustomerService customerService;
    private final EbikeService ebikeService;
    private final ModelMapper modelMapper;

    public CustomerApiController(CustomerService customerService, EbikeService ebikeService,ModelMapper modelMapper) {
        this.customerService = customerService;
        this.ebikeService = ebikeService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN') and principal.approved == true")
    @GetMapping()
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        final List<Customer> custs = customerService.findAll();
        final List<CustomerDto> custDtos = custs.stream().map(c -> modelMapper.map(c, CustomerDto.class)).toList();
        return ResponseEntity.ok(custDtos);
    }

    @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN') and principal.approved == true")
    @GetMapping("/{custid}/bikes")
    public ResponseEntity<List<EbikeDto>> getCustomerWithBikes(@PathVariable("custid") int custid){
        final List<Ebike> ebikes = customerService.findCustomerEbikes(custid);
        if(ebikes.isEmpty()){
            return ResponseEntity.notFound().build();
        }else {
            final List<EbikeDto> ebikeDtos = ebikes
                    .stream()
                    .map(ebike -> modelMapper.map(ebike, EbikeDto.class))
                    .toList();
            return ResponseEntity.ok(ebikeDtos);
        }
    }

    @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN') and principal.approved == true")
    @PostMapping("/{custId}/bikes")
    public ResponseEntity<EbikeDto> addEbikeForCustomer(@RequestBody EbikeDto dto, @PathVariable("custId") int custId){
        final Ebike saved = ebikeService.addBikeForCustomer(custId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(saved, EbikeDto.class));
    }


}
