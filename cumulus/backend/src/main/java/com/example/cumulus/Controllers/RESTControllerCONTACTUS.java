package com.example.cumulus.Controllers;
import com.example.cumulus.Services.GmailAPIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;


@RestController
@RequestMapping("/api/contactus")
public class RESTControllerCONTACTUS {
    private final GmailAPIService gmailAPIService;

    public RESTControllerCONTACTUS(GmailAPIService gmailAPIService) {
        this.gmailAPIService = gmailAPIService;
    }

    @PostMapping("/sendemail")
    public ResponseEntity<Map<String, String>> sendEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String subject = request.get("subject");
        String message = request.get("message");

        try {
            gmailAPIService.sendEmail(email, subject, message);
            return ResponseEntity.ok(Map.of("message", "Email sent successfully!"));
        } catch (IOException | GeneralSecurityException | MessagingException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to send email: " + e.getMessage()));
        }
    }
}
