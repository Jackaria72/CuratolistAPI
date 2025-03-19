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
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/curatolist/api/v1")
public class CuratolistController {

    @Autowired
    private CuratolistService curatolistService;

    @GetMapping("art")
    public ResponseEntity<Mono<CLPage>> getArtwork(@RequestParam(name = "page", defaultValue = "1") int page,
                                                   @RequestParam(name = "source", defaultValue = "both") String source,
                                                   @RequestParam(name = "q", required = false) String searchQuery,
                                                   @RequestParam(name = "sort", defaultValue = "id") String sortTerm,
                                                   @RequestParam(name = "filter", required = false) String filters) {
       return new ResponseEntity<>(curatolistService.getArt(page, source, searchQuery, sortTerm, filters), HttpStatus.OK);
    }
}
