package com.example.ebike_testing_system.webapi;

import com.example.ebike_testing_system.dto.BearingThresholdDto;
import com.example.ebike_testing_system.model.BearingThreshold;
import com.example.ebike_testing_system.model.BearingType;
import com.example.ebike_testing_system.repository.BearingThresholdRepository;
import com.example.ebike_testing_system.service.BearingAnalysisService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bearing-threshold")
public class BearingConfigApiController {
    private final BearingThresholdRepository thresholdRepo;
    private final BearingAnalysisService bearingService;
    private final ModelMapper modelMapper;


    public BearingConfigApiController(BearingThresholdRepository thresholdRepo,
                                      BearingAnalysisService bearingService,
                                      ModelMapper modelMapper) {
        this.thresholdRepo = thresholdRepo;
        this.bearingService = bearingService;
        this.modelMapper = modelMapper;
    }

    // 1. Get all configurations
    @GetMapping
    public ResponseEntity<List<BearingThresholdDto>> getAllConfigs() {
        final List<BearingThreshold> bt = bearingService.findAll();
        final List<BearingThresholdDto> btDtos = bt.stream().map(b -> modelMapper.map(b, BearingThresholdDto.class)).toList();
        return ResponseEntity.ok(btDtos);
    }

    // 2. Get single configuration by type
    @GetMapping("/{type}")
    public ResponseEntity<BearingThresholdDto> getConfig(
            @PathVariable BearingType type) {
        final BearingThreshold bt = bearingService.findById(type);
        final BearingThresholdDto btDto = modelMapper.map(bt, BearingThresholdDto.class);
        return ResponseEntity.ok(btDto);
    }

    // 3. Update configuration
    @PutMapping("/{type}")
    public ResponseEntity<BearingThresholdDto> updateConfig(
            @PathVariable BearingType type,
            @RequestBody BearingThresholdDto dto) {

        BearingThreshold updatedBT = bearingService.updateThreshold(type, dto.getMaxHorizontalVibration(), dto.getMaxVerticalVibration());
        BearingThresholdDto result = modelMapper.map(updatedBT, BearingThresholdDto.class);
        return ResponseEntity.ok(result);

    }

}
