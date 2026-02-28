package com.example.ebike_testing_system.service;

import com.example.ebike_testing_system.dto.TestReportDto;
import com.example.ebike_testing_system.model.Test;
import com.example.ebike_testing_system.model.TestReport;
import com.example.ebike_testing_system.repository.TestReportRepository;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TestReportService {

    private final TestReportRepository testReportRepository;
    private final ModelMapper modelMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(TestReportService.class);

    public TestReportService(TestReportRepository testRportRepository, ModelMapper modelMapper) {
        this.testReportRepository = testRportRepository;
        this.modelMapper = modelMapper;
    }

    private List<TestReport> parseCSVCommon(Reader reader, UUID batchId) {
        List<TestReport> dataList = new ArrayList<>();
        try (CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            for (CSVRecord record : csvParser) {
                TestReport data = new TestReport();
                data.setUuid(batchId);
                data.setTimestamp(LocalDateTime.parse(record.get("timestamp")));

                // Parsing batteryVoltage (double)
                data.setBatteryVoltage(Double.parseDouble(record.get("batteryVoltage")));

                // Parsing batteryCurrent (int)
                data.setBatteryCurrent(Integer.parseInt(record.get("batteryCurrent")));

                // Parsing batteryCapacity (double, changed from long to double)
                data.setBatteryCapacity(Double.parseDouble(record.get("batteryCapacity")));

                // Parsing batteryTemperatureCelsius (double)
                data.setBatteryTemperatureCelsius(Double.parseDouble(record.get("batteryTemperatureCelsius")));

//                System.out.println(">>>Read value from CSV: " + data.getBatteryTemperatureCelsius()); // Print for debugging

                // Parsing chargeStatus (boolean: 0 -> false, 1 -> true)
                data.setChargeStatus(Integer.parseInt(record.get("chargeStatus")));

                // Parsing assistanceLevel (int)
                data.setAssistanceLevel(Integer.parseInt(record.get("assistanceLevel")));

                // Parsing torqueCrankNm (double)
                data.setTorqueCrankNm(Double.parseDouble(record.get("torqueCrankNm")));

                // Parsing bikeWheelSpeedKmh (double)
                data.setBikeWheelSpeedKmh(Double.parseDouble(record.get("bikeWheelSpeedKmh")));

                // Parsing cadanceRpm (int)
                data.setCadanceRpm(Integer.parseInt(record.get("cadanceRpm")));

                // Parsing engineRpm (int)
                data.setEngineRpm(Integer.parseInt(record.get("engineRpm")));

                // Parsing enginePowerWatt (double)
                data.setEnginePowerWatt(Double.parseDouble(record.get("enginePowerWatt")));

                // Parsing wheelPowerWatt (double)
                data.setWheelPowerWatt(Double.parseDouble(record.get("wheelPowerWatt")));

                // Parsing rollTorque (double)
                data.setRollTorque(Double.parseDouble(record.get("rollTorque")));

                // Parsing loadcellN (double)
                data.setLoadcellN(Double.parseDouble(record.get("loadcellN")));

                // Parsing rolHz (double)
                data.setRolHz(Double.parseDouble(record.get("rolHz")));

                // Parsing horizontalInclination (double)
                data.setHorizontalInclination(Double.parseDouble(record.get("horizontalInclination")));

                // Parsing verticalInclination (double)
                data.setVerticalInclination(Double.parseDouble(record.get("verticalInclination")));

                // Parsing loadPower (int)
                data.setLoadPower(Integer.parseInt(record.get("loadPower")));

                // Parsing statusPlug (boolean)
                data.setStatusPlug(Boolean.parseBoolean(record.get("statusPlug")));
                dataList.add(data);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
        return dataList;
    }

    public List<TestReport> parseCSVFromFile(String filePath, UUID batchId) {
        try (FileReader fileReader = new FileReader(filePath)) {
            return parseCSVCommon(fileReader, batchId);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file: " + e.getMessage());
        }
    }

    public List<TestReport> parseCSVFromMultipart(MultipartFile file, UUID batchId) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            return parseCSVCommon(reader, batchId);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file: " + e.getMessage());
        }
    }


    public List<TestReportDto> getReportsForTest(int testId) {
        List<TestReport> reports = testReportRepository.findByTest_Id(testId);
        List<TestReportDto> dtos = reports.stream().map(d -> modelMapper.map(d, TestReportDto.class)).toList();
        return dtos;
    }


    @Async
    public void handleImport(InputStream inputStream, UUID uuid, Test test) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            List<String> lines = reader.lines().toList();

            LOGGER.info("Importing {} data rows", lines.size() - 1);

            for (int i = 1; i < lines.size(); i++) { // Start from 1 to skip header
                String line = lines.get(i);
                importLine(line, uuid, test);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to read input stream", e);
        }
    }

    private void importLine(final String line, UUID uuid, Test test) {
        if (line.trim().isEmpty()) return;

        final String[] parts = line.split(",");

        if (parts.length != 20) {
            LOGGER.warn("Invalid line format ({} fields): {}", parts.length, line);
            return;
        }

        try {
            LocalDateTime timestamp = LocalDateTime.parse(parts[0]);
            double batteryVoltage = Double.parseDouble(parts[1]);
            int batteryCurrent = Integer.parseInt(parts[2]);
            double batteryCapacity = Double.parseDouble(parts[3]);
            double batteryTemperatureCelsius = Double.parseDouble(parts[4]);
            int chargeStatus = Integer.parseInt(parts[5]);
            int assistanceLevel = Integer.parseInt(parts[6]);
            double torqueCrankNm = Double.parseDouble(parts[7]);
            double bikeWheelSpeedKmh = Double.parseDouble(parts[8]);
            int cadanceRpm = Integer.parseInt(parts[9]);
            int engineRpm = Integer.parseInt(parts[10]);
            double enginePowerWatt = Double.parseDouble(parts[11]);
            double wheelPowerWatt = Double.parseDouble(parts[12]);
            double rollTorque = Double.parseDouble(parts[13]);
            double loadcellN = Double.parseDouble(parts[14]);
            double rolHz = Double.parseDouble(parts[15]);
            double horizontalInclination = Double.parseDouble(parts[16]);
            double verticalInclination = Double.parseDouble(parts[17]);
            int loadPower = Integer.parseInt(parts[18]);
            boolean statusPlug = Boolean.parseBoolean(parts[19].trim());

            TestReport report = new TestReport(
                    uuid, timestamp, batteryVoltage, batteryCurrent, batteryCapacity, batteryTemperatureCelsius,
                    chargeStatus, assistanceLevel, torqueCrankNm, bikeWheelSpeedKmh, cadanceRpm, engineRpm,
                    enginePowerWatt, wheelPowerWatt, rollTorque, loadcellN, rolHz, horizontalInclination,
                    verticalInclination, loadPower, statusPlug
            );
            report.setTest(test); // link to test

            // Save the report (assume repository injected)
            testReportRepository.save(report);

        } catch (Exception e) {
            LOGGER.warn("Failed to parse line: {}", line, e);
        }
    }



}
