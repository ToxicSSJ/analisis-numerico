package com.numetrify.controller;

import com.numetrify.dto.BisectionResponse;
import com.numetrify.service.BisectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class CommonController {

    @Autowired
    private BisectionService bisectionService;

    @PostMapping("/bisection")
    public BisectionResponse bisection(@RequestParam String function,
                                       @RequestParam double a,
                                       @RequestParam double b,
                                       @RequestParam int tipoError,
                                       @RequestParam double numTol,
                                       @RequestParam int niter) {
        return bisectionService.bisection(function, a, b, tipoError, numTol, niter);
    }
}
