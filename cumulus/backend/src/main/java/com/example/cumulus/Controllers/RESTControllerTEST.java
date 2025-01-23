package com.example.cumulus.Controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class RESTControllerTEST {

    @GetMapping("/getrequest")
    @CrossOrigin(origins = "http://localhost:3000")
    public String getRequest() {
        return "https://media-cldnry.s-nbcnews.com/image/upload/t_fit-1500w,f_auto,q_auto:best/msnbc/Components/Photos/070802/070802_orangutan_hmed_10a.jpg";
    }
}
