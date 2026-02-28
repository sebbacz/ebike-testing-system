package com.example.ebike_testing_system.service;

import com.example.ebike_testing_system.dto.BearingAnalysisDto;
import com.example.ebike_testing_system.dto.InspectionReportDto;
import com.example.ebike_testing_system.dto.InspectionReportDto.ChecklistRow;
import com.example.ebike_testing_system.dto.InspectionReportDto.ProcedureRow;
import com.example.ebike_testing_system.dto.InspectionReportDto.ConditionRow;
import com.example.ebike_testing_system.exception.NotFoundException;
import com.example.ebike_testing_system.model.*;
import com.example.ebike_testing_system.repository.CustomerBikeRepository;
import com.example.ebike_testing_system.repository.InspectionReportRepository;
import com.example.ebike_testing_system.repository.TestReportRepository;
import com.example.ebike_testing_system.repository.TestRepository;
import java.time.ZoneId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Instant;
import java.util.*;

@Service
public class InspectionReportService {

    private final InspectionReportRepository reportRepo;
    private final CustomerBikeRepository bikeRepo;
    private final TestReportRepository testReportRepo;
    private final TestRepository testRepo;
    private final BearingAnalysisService bearingAnalysisService;


    private static final Logger log = LoggerFactory.getLogger(InspectionReportService.class);

    public InspectionReportService(InspectionReportRepository reportRepo,
                                   CustomerBikeRepository bikeRepo,
                                   TestReportRepository testReportRepo,
                                   BearingAnalysisService bearingAnalysisService,
                                   TestRepository testRepo) {
        this.reportRepo = reportRepo;
        this.bikeRepo = bikeRepo;
        this.testReportRepo = testReportRepo;
        this.bearingAnalysisService = bearingAnalysisService;
        this.testRepo = testRepo;
    }

    public InspectionReport findById(Long id) {
        return reportRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Inspection Report not found"));
    }

    @Transactional(readOnly = true)
    public InspectionReport getReportByTestUuid(UUID testUuid) {
        return reportRepo.findByTestUuid(testUuid)
                .orElseThrow(() -> new NotFoundException("Test Not found for UUID: " + testUuid));
    }

    @Transactional(readOnly = true)
    public InspectionReportDto generateDto(UUID testUuid, InspectionReportDto manualDto) {
        List<TestReport> rows = testReportRepo.findByUuid(testUuid);
        if (rows.isEmpty()) throw new NotFoundException("No test data for UUID " + testUuid);

        Test test = testRepo.findByUuid(testUuid)
                .orElseThrow(() -> new NotFoundException("No Test found for UUID " + testUuid));

        var dto = new InspectionReportDto();
//        copyManualInputs(dto, manualDto);
        copyInitialManualInputs(dto, manualDto); // Renamed to clarify purpose
        enrichWithTestData(dto, rows, test);

        BearingAnalysisDto bearingAnalysisResult = bearingAnalysisService.analyze(rows);
        dto.setBearingScore(bearingAnalysisResult.getOverallScore());
        // Map individual conditions from BearingAnalysisDto to InspectionReportDto's List<ConditionRow>
        List<ConditionRow> initialBearingConditions = new ArrayList<>();
        // Assuming ConditionRow 'value' is an Integer where 1=Good, 0=Bad.
        initialBearingConditions.add(new ConditionRow("vooras / achteras", bearingAnalysisResult.getFrontAxelCondition().equalsIgnoreCase("good") ? 1 : 0));
        initialBearingConditions.add(new ConditionRow("achteras", bearingAnalysisResult.getRearAxelCondition().equalsIgnoreCase("good") ? 1 : 0));
        initialBearingConditions.add(new ConditionRow("trapsas / motor", bearingAnalysisResult.getMotorCondition().equalsIgnoreCase("good") ? 1 : 0));
        dto.setBearingConditions(initialBearingConditions); // Populate the list for frontend display/override

//        populateCalculatedBearingConditions(dto, rows); // NEW METHOD
        calculateOverallScore(dto);

        return dto;
    }

    @Transactional
    public void saveFromDto(InspectionReportDto dto) {
        CustomerBike bike = bikeRepo.findById(dto.getCustomerBikeId())
                .orElseThrow(() -> new NotFoundException("No bike " + dto.getCustomerBikeId()));

//        InspectionReport report = buildInspectionReport(dto, bike);
        InspectionReport report;
        if (dto.getReportId() != 0) {
            report = findById(dto.getReportId());
            // Clear existing collections if you're going to re-add them from DTO.
            // This is crucial for orphanRemoval to work correctly for removed items.
            report.getVisualInspection().clear();
            report.getTestProcedures().clear();
            report.getBearingConditionItems().clear(); // <-- Crucial clear
            report.getFunctionalChecks().clear();
        } else {
            report = new InspectionReport();
            report.setCreatedAt(LocalDateTime.now());
        }

        // Map basic fields from DTO to entity (for both new and existing reports)
        report.setCustomerBike(bike);
        report.setComment(dto.getComment());
        report.setBrand(dto.getBrand());
        report.setModel(dto.getModel());
        report.setBatteryCapacityWh(dto.getBatteryCapacityWh());
        report.setMaxSupportPct(dto.getMaxSupportPct());
        report.setEnginePowerMaxW(dto.getEnginePowerMaxW());
        report.setEnginePowerNomW(dto.getEnginePowerNomW());
        report.setEngineTorqueNm(dto.getEngineTorqueNm());
        report.setTestProcScore(dto.getTestProcScore());
        report.setNominalLoadContW(dto.getNominalLoadContW());
        report.setNominalTempRiseCPerWh(dto.getNominalTempRiseCPerWh());
        report.setNominalLoadScore(dto.getNominalLoadScore());
        report.setBatteryTestCapacityWh(dto.getBatteryTestCapacityWh());
        report.setBatteryTestHealthPct(dto.getBatteryTestHealthPct());
        report.setBatteryTestScore(dto.getBatteryTestScore());
        report.setTestUuid(dto.getTestUuid());
//        report.setBearingScore(dto.getBearingScore());

        // Re-add items from DTO to entity collections
        dto.getVisualInspection().forEach(v -> report.getVisualInspection().add(toVisualInspectionItem(v, report)));
        dto.getTestProcedures().forEach(p -> report.getTestProcedures().add(toProcedureCheck(p, report)));

        // The DTO's bearingConditions list contains the *user's final choices*.
//        dto.getBearingConditions().forEach(b -> report.getBearingConditionItems().add(toBearingConditionItem(b, report)));
//        dto.getFunctionalChecks().forEach(f -> report.getFunctionalChecks().add(toFunctionalCheck(f, report)));

        // --- NEW/MODIFIED: Recalculate bearing score based on the technician's input (dto.getBearingConditions()) ---
        // This is where the "override" takes effect for the score.
        // The DTO's bearingConditions list contains the *user's final choices*.
        List<ConditionRow> finalBearingConditions = dto.getBearingConditions(); // Get what the frontend sent
        finalBearingConditions.forEach(b -> report.getBearingConditionItems().add(toBearingConditionItem(b, report)));

        double finalBearingScore = calculateBearingScoreFromConditionRows(finalBearingConditions);
        report.setBearingScore(finalBearingScore); // Set the report's bearing score based on technician's input

        dto.getFunctionalChecks().forEach(f -> report.getFunctionalChecks().add(toFunctionalCheck(f, report)));

        calculateOverallScore(dto);

        double oScore = dto.getOverallScore();
        report.setOverallScore(oScore);
        reportRepo.save(report);
    }

    @Transactional(readOnly = true)
    public InspectionReportDto loadDto(Long reportId) {
        InspectionReport report = reportRepo.findById(reportId)
                .orElseThrow(() -> new NotFoundException("Report not found: " + reportId));

        return mapReportToDto(report);
    }

    private void copyInitialManualInputs(InspectionReportDto dto, InspectionReportDto manualDto) {
        dto.setCustomerBikeId(manualDto.getCustomerBikeId());
        dto.setComment(manualDto.getComment());
        dto.setBrand(manualDto.getBrand());
        dto.setModel(manualDto.getModel());
        dto.setVisualInspection(manualDto.getVisualInspection());
        dto.setFunctionalChecks(manualDto.getFunctionalChecks());
    }

    private void enrichWithTestData(InspectionReportDto dto, List<TestReport> rows, Test test) {
        dto.setBatteryCapacityWh(test.getBatteryCapacity());
        dto.setMaxSupportPct(test.getMaxSupport());
        dto.setEnginePowerMaxW(test.getEnginePowerMax());
        dto.setEnginePowerNomW(test.getEnginePowerNominal());
        dto.setEngineTorqueNm(test.getEngineTorque());

        List<ProcedureRow> proc = calculateProcedures(dto, rows);
        dto.setTestProcedures(proc);

//        BearingAnalysisDto bearingResult = bearingAnalysisService.analyze(rows);
//        dto.setBearingScore(bearingResult.getOverallScore());

        calculateNominalLoad(dto, rows);
        calculateBatteryTest(dto, rows);
    }

    // NEW METHOD to populate initial bearing conditions based on analysis
    private void populateCalculatedBearingConditions(InspectionReportDto dto, List<TestReport> rows) {
        // This method will provide the default bearing conditions based on analysis.
        // The technician can then override these values in the frontend.

        // Get the analytical results (assuming BearingAnalysisService returns detailed per-bearing status)
        BearingAnalysisDto analysisResults = bearingAnalysisService.analyze(rows);

        List<ConditionRow> calculatedConditions = new ArrayList<>();

        // Example: Map calculated statuses to ConditionRow objects
        // For example, if analyzeSingleBearing returns "good" or "bad" string:
        String frontAxisStatus = bearingAnalysisService.analyzeSingleBearing(rows, bearingAnalysisService.findById(BearingType.FRONT_AXIS));
        String rearAxisStatus = bearingAnalysisService.analyzeSingleBearing(rows, bearingAnalysisService.findById(BearingType.REAR_AXIS));
        String motorStatus = bearingAnalysisService.analyzeSingleBearing(rows, bearingAnalysisService.findById(BearingType.MOTOR));

        // Assuming ConditionRow 'value' is an Integer where 1=Good, 0=Bad. Adjust if you use String.
        calculatedConditions.add(new ConditionRow("vooras / achteras", frontAxisStatus.equalsIgnoreCase("good") ? 1 : 0));
        calculatedConditions.add(new ConditionRow("achteras", rearAxisStatus.equalsIgnoreCase("good") ? 1 : 0));
        calculatedConditions.add(new ConditionRow("trapsas / motor", motorStatus.equalsIgnoreCase("good") ? 1 : 0));

        dto.setBearingConditions(calculatedConditions);
    }

    private void calculateOverallScore(InspectionReportDto dto) {
        // Overall Score: Average of section scores
        // Ensure all these scores are correctly calculated before calling this method.
        double totalScore = 0;
        int scoreCount = 0;

        // Test Procedures Score
        if (dto.getTestProcScore() >= 0) { // Check if calculated/valid
            totalScore += dto.getTestProcScore();
            scoreCount++;
        }

        // Nominal Load Score (from requirements: "for now we can not do this")
//        if (dto.getNominalLoadScore() >= 0) {
//            totalScore += dto.getNominalLoadScore();
//            scoreCount++;
//        }

        // Battery Test Score
        if (dto.getBatteryTestScore() >= 0) {
            totalScore += dto.getBatteryTestScore();
            scoreCount++;
        }

        // Bearing Score
        if (dto.getBearingScore() >= 0) {
            totalScore += dto.getBearingScore();
            scoreCount++;
        }

        if (scoreCount > 0) {
            dto.setOverallScore(totalScore / scoreCount);
        } else {
            dto.setOverallScore(0); // Default if no scores are available
        }
        log.info("Calculated Overall Score: {}", dto.getOverallScore());
    }


    private List<ProcedureRow> calculateProcedures(InspectionReportDto dto, List<TestReport> rows) {
        List<ProcedureRow> proc = new ArrayList<>();
        double promisedEngW = dto.getEnginePowerMaxW();
        double maxEngW = rows.stream().mapToDouble(TestReport::getEnginePowerWatt).max().orElse(0);
        double devEng = calculateDeviation(maxEngW, promisedEngW);
        proc.add(new ProcedureRow("Engine power (max)", maxEngW, devEng, "W"));

        double maxTorque = rows.stream().mapToDouble(TestReport::getRollTorque).max().orElse(0);
        proc.add(new ProcedureRow("Engine torque (max)", maxTorque, 0.0, "Nm"));

        double maxWheelW = rows.stream().mapToDouble(TestReport::getWheelPowerWatt).max().orElse(0);
        proc.add(new ProcedureRow("Wheel power (max)", maxWheelW, 0.0, "W"));

        double maxSupport = dto.getMaxSupportPct();
        double derivedSupport = calculateDerivedSupport(maxSupport, promisedEngW, maxEngW);
        double devSup = calculateDeviation(derivedSupport, maxSupport);
        proc.add(new ProcedureRow("Max support", derivedSupport, devSup, "%"));

        dto.setTestProcScore(100 - (devEng + devSup) / 2);
        return proc;
    }

    private double calculateDeviation(double actual, double expected) {
        if (expected == 0) return 0;
        return Math.abs(actual - expected) / expected * 100;
    }

    private double calculateDerivedSupport(double x, double promisedEngW, double actualEngW) {
        double z = promisedEngW / actualEngW * 100 - 100;
        return x / (100 + z) * 100;
    }


    private boolean isFullyDischarged(List<TestReport> testData) {
        if (testData.isEmpty()) return false;

        TestReport first = testData.get(0);
        TestReport last = testData.get(testData.size() - 1);

        // zulul
        // Rule: Battery should discharge from ≥95% to ≤5%
        return first.getBatteryCapacity() >= 95 &&
                last.getBatteryCapacity() <= 5;
    }

    private void calculateNominalLoad(InspectionReportDto dto, List<TestReport> rows) {
        double avgEngW = rows.stream().mapToDouble(TestReport::getEnginePowerWatt).average().orElse(0);
        dto.setNominalLoadContW((int) Math.round(avgEngW));

        double minTemp = rows.stream().mapToDouble(TestReport::getBatteryTemperatureCelsius).min().orElse(0);
        double maxTemp = rows.stream().mapToDouble(TestReport::getBatteryTemperatureCelsius).max().orElse(0);
        dto.setNominalTempRiseCPerWh((maxTemp - minTemp) / dto.getBatteryCapacityWh());
    }

    private void calculateBatteryTest(InspectionReportDto dto, List<TestReport> rows) {
        if (!isFullyDischarged(rows)) {
            dto.setBatteryTestCapacityWh(0);
            dto.setBatteryTestHealthPct(0);
            dto.setComment((dto.getComment() == null ? "" : dto.getComment() + "\n") +
                    "Warning: Battery not fully discharged - capacity results unreliable");
            return;
        }

        // Proceed with existing calculation...
        Instant start = toInstant(rows.get(0));
        Instant end = toInstant(rows.get(rows.size() - 1));
        double hours = Duration.between(start, end).toMillis() / 3600000.0;

        double avgWheelPower = rows.stream()
                .mapToDouble(TestReport::getWheelPowerWatt)
                .average().orElse(0);

        double capacity = avgWheelPower * hours;
        dto.setBatteryTestCapacityWh((int) Math.round(capacity));

        if (dto.getBatteryCapacityWh() > 0) {
            double healthPct = capacity / dto.getBatteryCapacityWh() * 100;
            dto.setBatteryTestHealthPct((int) Math.round(healthPct));
            dto.setBatteryTestScore((double) Math.round(healthPct));
        }
    }

    private Instant toInstant(TestReport report) {
        return report.getTimestamp().atZone(ZoneId.systemDefault()).toInstant();
    }

    private InspectionReport buildInspectionReport(InspectionReportDto dto, CustomerBike bike) {
        var report = new InspectionReport();
        report.setCustomerBike(bike);
        report.setCreatedAt(LocalDateTime.now());
        report.setComment(dto.getComment());
        report.setBrand(dto.getBrand());
        report.setModel(dto.getModel());
        report.setBatteryCapacityWh(dto.getBatteryCapacityWh());
        report.setMaxSupportPct(dto.getMaxSupportPct());
        report.setEnginePowerMaxW(dto.getEnginePowerMaxW());
        report.setEnginePowerNomW(dto.getEnginePowerNomW());
        report.setEngineTorqueNm(dto.getEngineTorqueNm());
        report.setTestProcScore(dto.getTestProcScore());
        report.setNominalLoadContW(dto.getNominalLoadContW());
        report.setNominalTempRiseCPerWh(dto.getNominalTempRiseCPerWh());
        report.setNominalLoadScore(dto.getNominalLoadScore());
        report.setBatteryTestCapacityWh(dto.getBatteryTestCapacityWh());
        report.setBatteryTestHealthPct(dto.getBatteryTestHealthPct());
        report.setBatteryTestScore(dto.getBatteryTestScore());
        report.setBearingScore(dto.getBearingScore());

        dto.getVisualInspection().forEach(v -> report.getVisualInspection().add(toVisualInspectionItem(v, report)));
        dto.getTestProcedures().forEach(p -> report.getTestProcedures().add(toProcedureCheck(p, report)));
        dto.getBearingConditions().forEach(b -> report.getBearingConditionItems().add(toBearingConditionItem(b, report)));
        dto.getFunctionalChecks().forEach(f -> report.getFunctionalChecks().add(toFunctionalCheck(f, report)));

        return report;
    }

    private VisualInspectionItem toVisualInspectionItem(ChecklistRow v, InspectionReport report) {
        var item = new VisualInspectionItem();
        item.setReport(report);
        item.setLabel(v.getLabel());
        item.setState(v.getState());
        return item;
    }

    private ProcedureCheck toProcedureCheck(ProcedureRow p, InspectionReport report) {
        var pc = new ProcedureCheck();
        pc.setReport(report);
        pc.setName(p.getName());
        pc.setResult(p.getResult());
        pc.setDeviationPct(p.getDeviationPct());
        pc.setUnit(p.getUnit());
        return pc;
    }

    private BearingConditionItem toBearingConditionItem(ConditionRow b, InspectionReport report) {
        var bc = new BearingConditionItem();
        bc.setReport(report);
        bc.setName(b.getName());
        bc.setValue(b.getValue());
        return bc;
    }

    private FunctionalCheck toFunctionalCheck(ChecklistRow f, InspectionReport report) {
        var fc = new FunctionalCheck();
        fc.setReport(report);
        fc.setLabel(f.getLabel());
        fc.setState(f.getState());
        return fc;
    }

    private InspectionReportDto mapReportToDto(InspectionReport report) {
        var dto = new InspectionReportDto();
        dto.setReportId(report.getId());
        dto.setCustomerBikeId(report.getCustomerBike().getId());
        dto.setComment(report.getComment());
        dto.setBrand(report.getBrand());
        dto.setModel(report.getModel());
        dto.setBatteryCapacityWh(report.getBatteryCapacityWh());
        dto.setMaxSupportPct(report.getMaxSupportPct());
        dto.setEnginePowerMaxW(report.getEnginePowerMaxW());
        dto.setEnginePowerNomW(report.getEnginePowerNomW());
        dto.setEngineTorqueNm(report.getEngineTorqueNm());
        dto.setTestProcScore(report.getTestProcScore());
        dto.setNominalLoadContW(report.getNominalLoadContW());
        dto.setNominalTempRiseCPerWh(report.getNominalTempRiseCPerWh());
        dto.setNominalLoadScore(report.getNominalLoadScore());
        dto.setBatteryTestCapacityWh(report.getBatteryTestCapacityWh());
        dto.setBatteryTestHealthPct(report.getBatteryTestHealthPct());
        dto.setBatteryTestScore(report.getBatteryTestScore());
        dto.setBearingScore(report.getBearingScore());
        dto.setTestUuid(report.getTestUuid());
        dto.setCreatedOn(report.getCreatedAt());
        dto.setOverallScore(report.getOverallScore());
        dto.setMotorModel(report.getCustomerBike().getEbike().getEbikeModel().getName());
        dto.setFirstRegistration(report.getCustomerBike().getEbike().getAddedDate());

        dto.setVisualInspection(report.getVisualInspection().stream()
                .map(v -> new ChecklistRow(v.getLabel(), v.getState()))
                .toList());

        dto.setTestProcedures(report.getTestProcedures().stream()
                .map(p -> new ProcedureRow(p.getName(), p.getResult(), p.getDeviationPct(), p.getUnit()))
                .toList());

        dto.setBearingConditions(report.getBearingConditionItems().stream()
                .map(b -> new ConditionRow(b.getName(), b.getValue()))
                .toList());

        dto.setFunctionalChecks(report.getFunctionalChecks().stream()
                .map(f -> new ChecklistRow(f.getLabel(), f.getState()))
                .toList());

        return dto;
    }

    // NEW HELPER METHOD to calculate bearing score from the ConditionRow list
    private double calculateBearingScoreFromConditionRows(List<ConditionRow> bearingConditions) {
        if (bearingConditions == null || bearingConditions.isEmpty()) {
            return 0.0;
        }

        long goodCount = bearingConditions.stream()
                .filter(item -> item.getValue() != null && item.getValue() == 1) // 1 means "good"
                .count();

        // Calculate score based on number of 'good' items relative to the total number of bearing types
        // It's important that `bearingConditions` always contains all expected types (e.g., 3 types)
        // If not, you might divide by a fixed number of expected types instead of `bearingConditions.size()`
        return (double) goodCount / bearingConditions.size() * 100;
    }
}