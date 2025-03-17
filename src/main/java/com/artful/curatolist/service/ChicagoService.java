package com.artful.curatolist.service;

import com.artful.curatolist.client.ChicagoClient;
import com.artful.curatolist.mapper.ChicagoMapper;
import com.artful.curatolist.model.CLArtwork;
import com.artful.curatolist.model.CLPage;
import com.artful.curatolist.model.ChicagoPage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ChicagoService implements ApiService {

    private final ChicagoClient chicagoClient;
    private final ChicagoMapper chicagoMapper;

    public ChicagoService(ChicagoClient chicagoClient, ChicagoMapper chicagoMapper) {
        this.chicagoClient = chicagoClient;
        this.chicagoMapper = chicagoMapper;
    }

    @Override
    public Mono<CLPage> getArt(int page) {
        Mono<ChicagoPage> chicagoMono = chicagoClient.getChicagoArtwork(page);

        return chicagoMono.map(
                chicagoPage -> {
                    List<CLArtwork> artworks = chicagoMapper.mapChicagoArt(chicagoPage);
                    CLPage.PageInfo pageInfo = new CLPage.PageInfo(
                            chicagoPage.pagination().total(),
                            chicagoPage.pagination().total_pages(),
                            0,0);
                    return new CLPage(pageInfo, artworks);
                });
    }
}
