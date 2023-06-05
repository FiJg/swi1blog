package cz.osu.swi1.blog.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cz.osu.swi1.blog.dto.AuthRequest;
import cz.osu.swi1.blog.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> login(@RequestBody AuthRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> signup(@RequestBody AuthRequest authRequest) {
        return authService.signup(authRequest)
                ? ResponseEntity.ok(responseMessage(authRequest.getUserName()))
                : ResponseEntity.unprocessableEntity().body(responseMessage(authRequest.getUserName()));
    }

    private ObjectNode responseMessage(String userName) {
        return new ObjectMapper().createObjectNode()
                .put("userName", userName);
    }
}
