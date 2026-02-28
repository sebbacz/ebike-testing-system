package com.example.ebike_testing_system.webapi;

import com.example.ebike_testing_system.dto.EbikeDto;
import com.example.ebike_testing_system.dto.EbikeModelDto;
import com.example.ebike_testing_system.dto.EbikeModelProjection;
import com.example.ebike_testing_system.exception.NotFoundException;
import com.example.ebike_testing_system.model.Ebike;
import com.example.ebike_testing_system.model.EbikeModel;
import com.example.ebike_testing_system.service.CustomerService;
import com.example.ebike_testing_system.service.EbikeModelService;
import com.example.ebike_testing_system.service.EbikeService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ebikes")
public class EbikeApiController {

    private final EbikeModelService ebikeModelService;
    private final EbikeService ebikeService;
    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    public EbikeApiController(EbikeService ebikeService, CustomerService customerService, EbikeModelService ebikeModelService, ModelMapper modelMapper) {
        this.ebikeModelService = ebikeModelService;
        this.ebikeService = ebikeService;
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

//    @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN') and principal.approved == true")
//    @GetMapping()
//    public ResponseEntity<List<EbikeDto>> getAllEbikes() {
//        List<Ebike> ebikes = ebikeService.findAll();
//        Map<Integer, Integer> customerCounts = ebikeService.getCustomerCountsByEbikeId();
//
//        List<EbikeDto> dtos = ebikes.stream().map(e -> {
//            EbikeDto dto = modelMapper.map(e, EbikeDto.class);
//            dto.setCustomerCount(customerCounts.getOrDefault(e.getId(), 0));
//            if (e.getEbikeModel() != null) {
//                dto.setEbikeModelDto(modelMapper.map(e.getEbikeModel(), EbikeModelDto.class));
//            }
//            return dto;
//        }).toList();
//
//        return ResponseEntity.ok(dtos);
//    }


    @GetMapping()
    public ResponseEntity<List<EbikeDto>> getAllEbikes() {
        List<Ebike> ebikes = ebikeService.findAll();
        Map<Integer, Integer> customerCounts = ebikeService.getCustomerCountsByEbikeId();

        List<EbikeDto> dtos = ebikes.stream().map(e -> {
            EbikeDto dto = modelMapper.map(e, EbikeDto.class);
            dto.setCustomerCount(customerCounts.getOrDefault(e.getId(), 0));

            if (e.getEbikeModel() != null) {
                EbikeModelProjection projection = ebikeModelService.getProjectedModelById(e.getEbikeModel().getId());
                EbikeModelDto modelDto = new EbikeModelDto();
                modelDto.setId(projection.getId());
                modelDto.setName(projection.getName());
                modelDto.setBatteryCapacity(projection.getBatteryCapacity());
                modelDto.setMaxSupport(projection.getMaxSupport());
                modelDto.setEnginePowerMax(projection.getEnginePowerMax());
                modelDto.setEnginePowerNominal(projection.getEnginePowerNominal());
                modelDto.setEngineTorque(projection.getEngineTorque());
                modelDto.setEbikeCount(projection.getEbikeCount());
                dto.setEbikeModelDto(modelDto);
            }

            return dto;
        }).toList();

        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/customers/{custId}")
    public ResponseEntity<List<EbikeDto>> getBikesForCustomer(@PathVariable int custId) {
        List<Ebike> ebikes = customerService.findCustomerEbikes(custId);
//        List<EbikeDto> dtos = ebikes.stream().map(e -> modelMapper.map(e, EbikeDto.class)).toList();
        List<EbikeDto> dtos = ebikes.stream().map(e -> {
            EbikeDto dto = modelMapper.map(e, EbikeDto.class);
            if (e.getEbikeModel() != null) {
                dto.setEbikeModelDto(modelMapper.map(e.getEbikeModel(), EbikeModelDto.class));
            }
            return dto;
        }).toList();
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN') and principal.approved == true")
    @GetMapping("/{bikeId}")
    public ResponseEntity<EbikeDto> getOneBike(@PathVariable("bikeId") int bikeId) {
        final Ebike ebike = ebikeService.findById(bikeId);
        final EbikeDto dto = modelMapper.map(ebike, EbikeDto.class);

        if (ebike.getEbikeModel() != null) {
            EbikeModelDto modelDto = modelMapper.map(ebike.getEbikeModel(), EbikeModelDto.class);
            dto.setEbikeModelDto(modelDto);
        }

        return ResponseEntity.ok(dto);

    }

    @PreAuthorize("hasAnyRole('TECHNICIAN') and principal.approved == true")
    @PostMapping("/customers/{custId}")
    public ResponseEntity<EbikeDto> addEbike(@RequestBody EbikeDto dto, @PathVariable int custId) {
        final Ebike s1 = ebikeService.addBikeForCustomer(custId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(s1, EbikeDto.class));
//        final Ebike saved = ebikeService.addEbike(dto.getBrand(), dto.getModel(), dto.getBattery(), dto.get)
    }


    @PreAuthorize("hasAnyRole('TECHNICIAN') and principal.approved == true")
    @DeleteMapping("/customers/{custId}/{bikeId}")
    public ResponseEntity<Void> removeEbikeFromCustomer(@PathVariable int custId, @PathVariable int bikeId) {
        try {
            customerService.removeLink(custId, bikeId); // This deletes from CustomerBike
            return ResponseEntity.noContent().build();
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasAnyRole('TECHNICIAN') and principal.approved == true")
    @DeleteMapping("/{bikeId}")
    public ResponseEntity<Void> removeEbike(@PathVariable int bikeId) {
        try {
            ebikeService.deleteEbike(bikeId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }


    @GetMapping("/models")
    public List<EbikeModelProjection> getAllModels() {
        return ebikeModelService.findAllProjected();
    }

    @GetMapping("/models/{id}")
    public ResponseEntity<EbikeModelDto> getOneModel(@PathVariable("id") int id) {
        final EbikeModel model = ebikeModelService.findById(id);
        EbikeModelDto dto = modelMapper.map(model, EbikeModelDto.class);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN') and principal.approved == true")
    @PostMapping("/models")
    public ResponseEntity<EbikeModelDto> addModel(@RequestBody EbikeModelDto dto) {

        final EbikeModel saved = ebikeModelService.addModel(dto.getName(), dto.getBatteryCapacity(), dto.getMaxSupport(),
                dto.getEnginePowerMax(), dto.getEnginePowerNominal(), dto.getEngineTorque());
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(saved, EbikeModelDto.class));
    }

    @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN') and principal.approved == true")
    @PutMapping("/models/{id}")
    public ResponseEntity<EbikeModelDto> updateModel(
            @PathVariable int id,
            @RequestBody EbikeModelDto dto) {

        EbikeModel updatedModel = ebikeModelService.updateModel(id, dto);
        EbikeModelDto result = modelMapper.map(updatedModel, EbikeModelDto.class);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN') and principal.approved == true")
    @DeleteMapping("/models/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable int id) {
        try {
            ebikeModelService.deleteModel(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }


}
