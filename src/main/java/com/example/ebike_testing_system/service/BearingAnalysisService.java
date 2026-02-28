package com.example.ebike_testing_system.service;

import com.example.ebike_testing_system.dto.BearingAnalysisDto;
import com.example.ebike_testing_system.exception.NotFoundException;
import com.example.ebike_testing_system.model.BearingThreshold;
import com.example.ebike_testing_system.model.BearingType;
import com.example.ebike_testing_system.model.EbikeModel;
import com.example.ebike_testing_system.model.TestReport;
import com.example.ebike_testing_system.repository.BearingThresholdRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BearingAnalysisService {
    private final BearingThresholdRepository thresholdRepo;

    public BearingAnalysisService(BearingThresholdRepository thresholdRepo) {
        this.thresholdRepo = thresholdRepo;
    }

    public BearingAnalysisDto analyze(List<TestReport> testData) {
        Map<BearingType, BearingThreshold> thresholds = loadAllThresholds();

        return new BearingAnalysisDto(
                analyzeSingleBearing(testData, thresholds.get(BearingType.FRONT_AXIS)),
                analyzeSingleBearing(testData, thresholds.get(BearingType.REAR_AXIS)),
                analyzeSingleBearing(testData, thresholds.get(BearingType.MOTOR)),
                calculateOverallScore(testData, thresholds) // Pass thresholds here
        );
    }


    private Map<BearingType, BearingThreshold> loadAllThresholds() {
        return thresholdRepo.findAll().stream()
                .collect(Collectors.toMap(
                        BearingThreshold::getBearingType,
                        Function.identity()
                ));
    }

    public String analyzeSingleBearing(List<TestReport> data, BearingThreshold threshold) {
        double maxHoriz = data.stream()
                .mapToDouble(TestReport::getHorizontalInclination)
                .max().orElse(0);

        double maxVert = data.stream()
                .mapToDouble(TestReport::getVerticalInclination)
                .max().orElse(0);

        return (maxHoriz <= threshold.getMaxHorizontalVibration() &&
                maxVert <= threshold.getMaxVerticalVibration())
                ? "good" : "bad";
    }

    private double calculateOverallScore(List<TestReport> data,
                                         Map<BearingType, BearingThreshold> thresholds) {
        if (data.isEmpty()) return 0;

        long goodMeasurements = data.stream()
                .filter(r -> isGoodMeasurement(r, thresholds))
                .count();

        return (double) goodMeasurements / data.size() * 100;
    }


    private boolean isGoodMeasurement(TestReport report,
                                      Map<BearingType, BearingThreshold> thresholds) {
        return thresholds.values().stream()
                .allMatch(threshold ->
                        report.getHorizontalInclination() <= threshold.getMaxHorizontalVibration() &&
                                report.getVerticalInclination() <= threshold.getMaxVerticalVibration()
                );
    }


    public List<BearingThreshold> findAll() {
        return thresholdRepo.findAll();
    }

    public BearingThreshold findById(BearingType bearingType) {
        return thresholdRepo.findById(bearingType).orElseThrow(() -> new NotFoundException("Bearing Threshold not found: " + bearingType));
    }

    public BearingThreshold updateThreshold(BearingType bearingType, double maxHoriz, double maxVert) {
        BearingThreshold bt = findById(bearingType);
        bt.setMaxHorizontalVibration(maxHoriz);
        bt.setMaxVerticalVibration(maxVert);
        return thresholdRepo.save(bt);
    }


}
