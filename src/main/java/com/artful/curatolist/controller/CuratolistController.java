package com.artful.curatolist.controller;

import com.artful.curatolist.model.CLPage;
import com.artful.curatolist.service.CuratolistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/curatolist/api/v1")
public class CuratolistController {

    @Autowired
    private CuratolistService curatolistService;

    @GetMapping
    public ResponseEntity<CLPage> getArtwork(@RequestParam(name = "page", defaultValue = "1") int page,
                                             @RequestParam(name = "limit", defaultValue = "10") int limit) {
       return new ResponseEntity<>(curatolistService.getArt(page, limit), HttpStatus.OK);
    }
}
