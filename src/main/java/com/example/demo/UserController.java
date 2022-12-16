package com.example.demo;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class UserController {


    // The url /logout is automatically configured by spring security, so it's not mapped in this controller
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean login(Principal principal) {

        return Boolean.TRUE;
    }

}