package com.swivel.cc.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Base Controller
 */
@RestController
public class BaseController {

    /**
     * index action
     *
     * @return
     */
    @GetMapping(path = "/", produces = "text/html")
    public String index() {
        return "<h1>QPon Auth Service</h1>";
    }
}
