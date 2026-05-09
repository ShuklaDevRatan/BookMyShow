package com.drs.BookMyShow.Controller;

import com.drs.BookMyShow.Service.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/screens")
public class ScreenController {

    @Autowired
    private ScreenService screenService;
}
