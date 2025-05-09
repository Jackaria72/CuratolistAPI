package com.artful.curatolist.service;

import com.artful.curatolist.model.CLPage;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface ApiService {
    Mono<CLPage> getArt(int page, String searchQuery, String sortTerm, String filters);
}
