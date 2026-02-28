package com.example.ebike_testing_system.controller;

import com.example.ebike_testing_system.dto.TestReportDto;
import com.example.ebike_testing_system.model.Customer;
import com.example.ebike_testing_system.service.CustomerService;
import com.example.ebike_testing_system.service.TestReportService;
import com.example.ebike_testing_system.service.TestService;
import com.example.ebike_testing_system.util.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/qr-code")
public class QRCodeController {

    private CustomerService customerService;
    private final TestService testService;

    private final TestReportService testReportService;

    @Autowired
    public QRCodeController(TestReportService testReportService,
                            TestService testService
    ) {
        this.testReportService = testReportService;
        this.testService = testService;
    }

    @GetMapping("/qrgenerated")
    public String viewQRCode(@RequestParam int bikeId, Model model) {
        String publicUrl = "https://int4server-team13.duckdns.org/qr-code/public/" + bikeId;
        try {
            BufferedImage qrCodeImage = QRCodeGenerator.generateQRCode(publicUrl);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(qrCodeImage, "PNG", outputStream);
            byte[] qrCodeBytes = outputStream.toByteArray();
            String base64QRCode = Base64.getEncoder().encodeToString(qrCodeBytes);
            model.addAttribute("qrCode", base64QRCode);
            model.addAttribute("bikeId", bikeId);
            return "qr/qrgenerated";
        } catch (Exception e) {
            model.addAttribute("error", "Error generating QR code: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/public/{bikeId}")
    public String viewPublicTestHistory(@PathVariable int bikeId, Model model) {
        model.addAttribute("ebikeId", bikeId);
        model.addAttribute("testsForBike", testService.findByBikeId(bikeId));
        List<TestReportDto> testReports = testReportService.getReportsForTest(bikeId);
        model.addAttribute("testReports", testReports);
        return "qr/public-test-history";
    }

    @GetMapping("/tech/qr-qr")
    public String viewCustomerBikes(@RequestParam int customerId, Model model) {
        Customer customer = customerService.getCustomerWithBikes(customerId);
        model.addAttribute("customer", customer);
        return "tech/qr-qr";
    }
}