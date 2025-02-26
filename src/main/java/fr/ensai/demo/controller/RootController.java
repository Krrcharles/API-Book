package fr.ensai.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "0 Root", description = "Check if API is online with GET /")
@RestController
public class RootController {
    @GetMapping("/")
    public ResponseEntity<String> index() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
