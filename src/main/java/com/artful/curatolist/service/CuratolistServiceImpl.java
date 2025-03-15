package com.artful.curatolist.service;

import com.artful.curatolist.client.ChicagoClient;
import com.artful.curatolist.client.HarvardClient;
import com.artful.curatolist.controller.exception.ResourcesNotFoundException;
import com.artful.curatolist.mapper.ChicagoMapper;
import com.artful.curatolist.mapper.HarvardMapper;
import com.artful.curatolist.model.CLArtwork;
import com.artful.curatolist.model.CLPage;
import com.artful.curatolist.model.ChicagoPage;
import com.artful.curatolist.model.HarvardPage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Service
public class CuratolistServiceImpl implements CuratolistService{

    private final ChicagoClient chicagoClient;
    private final ChicagoMapper chicagoMapper;
    private final HarvardClient harvardClient;
    private final HarvardMapper harvardMapper;

    public CuratolistServiceImpl(ChicagoClient chicagoClient, ChicagoMapper chicagoMapper, HarvardClient harvardClient, HarvardMapper harvardMapper) {
        this.chicagoClient = chicagoClient;
        this.chicagoMapper = chicagoMapper;
        this.harvardClient = harvardClient;
        this.harvardMapper = harvardMapper;
    }


    @Override
    public Mono<CLPage> getArt(int page, int limit) {
        int halfLimit = limit/2;

        Mono<HarvardPage> harvardMono = harvardClient.getHarvardArtwork(page, halfLimit)
                .onErrorResume(ex -> Mono.just(new HarvardPage(new HarvardPage.HarvardPageInfo(0,0), Collections.emptyList())));
        Mono<ChicagoPage> chicagoMono = chicagoClient.getChicagoArtwork(page, halfLimit)
                .onErrorResume(ex -> Mono.just(new ChicagoPage(new ChicagoPage.ChicagoPageInfo(0,0), Collections.emptyList())));

        return Mono.zip(harvardMono, chicagoMono)
                .map(tuple -> {
                    HarvardPage harvardRaw = tuple.getT1();
                    ChicagoPage chicagoRaw = tuple.getT2();

                    List<CLArtwork> harvard = harvardMapper.mapHarvardArt(harvardRaw);
                    List<CLArtwork> chicago = chicagoMapper.mapChicagoArt(chicagoRaw);

                    CLPage.PageInfo pageInfo = new CLPage.PageInfo(
                            chicagoRaw.pagination().total(),
                            chicagoRaw.pagination().total_pages(),
                            harvardRaw.info().totalrecords(),
                            harvardRaw.info().pages());

                    List<CLArtwork> combined = Stream.of(harvard, chicago)
                            .flatMap(Collection::stream)
                            .toList();
                    if (combined.isEmpty()) {
                        throw new ResourcesNotFoundException("No Results Available");
                    }

                    return new CLPage(pageInfo, combined);
                });
    }
}
