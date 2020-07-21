package com.epam.esm.controller;

import com.epam.esm.dto.AuthenticationResponseDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.api.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponseDto login(@RequestBody UserDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        String token = authenticationService.login(username, password);

        AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto(username, token);
        authenticationResponseDto.add(linkTo(methodOn(AuthenticationController.class).login(userDto)).withSelfRel());

        return authenticationResponseDto;
    }
}
