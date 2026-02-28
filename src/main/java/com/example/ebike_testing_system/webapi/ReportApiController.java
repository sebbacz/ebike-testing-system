package com.example.ebike_testing_system.webapi;

import com.example.ebike_testing_system.dto.InspectionReportDto;
import com.example.ebike_testing_system.exception.NotFoundException;
import com.example.ebike_testing_system.model.InspectionReport;
import com.example.ebike_testing_system.model.Test;
import com.example.ebike_testing_system.service.InspectionReportService;
import com.example.ebike_testing_system.service.TestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportApiController {

    private final InspectionReportService reportService;
    private final TestService testService;
    private final ModelMapper modelMapper;

    /**
     * Create a new inspection report from the given DTO.
     */
    @PostMapping
    public ResponseEntity<Void> createReport(@RequestBody @Valid InspectionReportDto dto) {
        reportService.saveFromDto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Retrieve a report DTO by ID.
     */
    @GetMapping("/{testUuid}")
    public ResponseEntity<InspectionReportDto> getReport(@PathVariable UUID testUuid) {
        try {
            InspectionReport dtoUuid = reportService.getReportByTestUuid(testUuid);
            InspectionReportDto dto = reportService.loadDto(dtoUuid.getId());
            return ResponseEntity.ok(dto);
        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Generate a report DTO from test UUID and manual data (metadata enrichment).
     */
    @PostMapping("/generate")
    public ResponseEntity<InspectionReportDto> generateReport(
            @RequestParam UUID testUuid,
            @RequestBody InspectionReportDto manualDto) {
        try {
            InspectionReportDto dto = reportService.generateDto(testUuid, manualDto);
            return ResponseEntity.ok(dto);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Fetch raw report entity and map to DTO.
     */
    @GetMapping("/raw/{reportId}")
    public ResponseEntity<InspectionReportDto> getRawMetadata(@PathVariable Long reportId) {
        try {
            InspectionReport rep = reportService.findById(reportId);
            InspectionReportDto dto = modelMapper.map(rep, InspectionReportDto.class);
            return ResponseEntity.ok(dto);
        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}



//}