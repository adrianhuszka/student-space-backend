package hu.StudentSpace.controllers;

import hu.StudentSpace.utils.JwtDecoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class Test {
    @GetMapping
    public ResponseEntity<String> test(@RequestHeader("Authorization") String token) {
        var decoder = new JwtDecoder();
        System.out.println(token);
        System.out.println(decoder.decode(token));

        return ResponseEntity.ok(decoder.decode(token));
    }
}
