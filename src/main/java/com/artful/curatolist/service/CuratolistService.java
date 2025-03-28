package com.artful.curatolist.service;

import com.artful.curatolist.model.CLPage;
import reactor.core.publisher.Mono;

public interface CuratolistService {
    Mono<CLPage> getArt(int page, String source, String searchQuery, String sortTerm, String filters);
}
