package com.sunkyuj.tarotdream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DreamController {
    @GetMapping("/")
    public String home(){
        return "hi";
    }
}
