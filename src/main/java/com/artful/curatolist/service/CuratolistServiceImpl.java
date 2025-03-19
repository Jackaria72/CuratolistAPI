package com.artful.curatolist.service;

import com.artful.curatolist.controller.exception.ResourcesNotFoundException;
import com.artful.curatolist.model.CLArtwork;
import com.artful.curatolist.model.CLPage;
import com.artful.curatolist.util.SortComparator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CuratolistServiceImpl implements CuratolistService{

    private final ChicagoService chicagoService;
    private final HarvardService harvardService;
    private final SortComparator sortComparator;

    public CuratolistServiceImpl(ChicagoService chicagoService, HarvardService harvardService, SortComparator sortComparator) {
        this.chicagoService = chicagoService;
        this.harvardService = harvardService;
        this.sortComparator = sortComparator;
    }


    @Override
    public Mono<CLPage> getArt(int page, String source, String searchQuery, String sortTerm, String filters) {

        Mono<CLPage> harvardMono = Mono.just(new CLPage(new CLPage.PageInfo(0,0,0,0),Collections.emptyList()));
        Mono<CLPage> chicagoMono = Mono.just(new CLPage(new CLPage.PageInfo(0,0,0,0),Collections.emptyList()));

        switch (source.toLowerCase()) {
            case "chicago" -> chicagoMono = chicagoService.getArt(page, searchQuery, sortTerm, filters);
            case "harvard" -> harvardMono = harvardService.getArt(page, searchQuery, sortTerm, filters);
            case "both" -> {
                harvardMono = harvardService.getArt(page, searchQuery, sortTerm, filters)
                        .onErrorResume(ex -> Mono.just(new CLPage(new CLPage.PageInfo(0,0,0,0),Collections.emptyList())));
                chicagoMono = chicagoService.getArt(page, searchQuery, sortTerm, filters)
                        .onErrorResume(ex -> Mono.just(new CLPage(new CLPage.PageInfo(0,0,0,0),Collections.emptyList())));
            }
        }

        return Mono.zip(harvardMono, chicagoMono)
                .map(tuple -> {
                    CLPage harvard = tuple.getT1();
                    CLPage chicago = tuple.getT2();

                    List<CLArtwork> harvardArt = harvard.artwork();
                    List<CLArtwork> chicagoArt = chicago.artwork();

                    List<CLArtwork> combined = Stream.of(harvardArt, chicagoArt)
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList());
                    if (combined.isEmpty()) {
                        throw new ResourcesNotFoundException("No Results Available");
                    }

                    combined.sort(sortComparator.getComparator(sortTerm));
                    CLPage.PageInfo pageInfo = new CLPage.PageInfo(
                            chicago.pageInfo().chicagoTotal(),
                            chicago.pageInfo().chicagoPageTotal(),
                            harvard.pageInfo().harvardTotal(),
                            harvard.pageInfo().HarvardPageTotal());

                    return new CLPage(pageInfo, combined);
                });
    }
}
