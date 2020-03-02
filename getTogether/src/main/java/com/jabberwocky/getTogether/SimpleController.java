package com.jabberwocky.getTogether;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {	
    @GetMapping("/sprint2")
    public String homePage() {
        return "home";
    }
}