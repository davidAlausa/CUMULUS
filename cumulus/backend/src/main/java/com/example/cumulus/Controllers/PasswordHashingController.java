package com.example.cumulus.Controllers;

import com.example.cumulus.Services.PasswordHashingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class PasswordHashingController {

    @Autowired
    private PasswordHashingService passwordHashingService;

    @GetMapping("/hash-passwords")
    @ResponseBody
    public String hashPasswords() {
        passwordHashingService.hashExistingPasswords();
        return "Passwords hashed successfully.";
    }
}
