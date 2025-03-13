package com.artful.curatolist.service;

import com.artful.curatolist.client.ChicagoClient;
import com.artful.curatolist.client.HarvardClient;
import com.artful.curatolist.mapper.ChicagoMapper;
import com.artful.curatolist.mapper.HarvardMapper;
import com.artful.curatolist.model.CLArtwork;
import com.artful.curatolist.model.CLPage;
import com.artful.curatolist.model.ChicagoPage;
import com.artful.curatolist.model.HarvardPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
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
    public CLPage getArt(int page, int limit) {
        int halfLimit = limit/2;

        HarvardPage harvardRaw = harvardClient.getHarvardArtwork(page, halfLimit).block();
        ChicagoPage chicagoRaw = chicagoClient.getChicagoArtwork(page, halfLimit).block();
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

        return new CLPage(pageInfo, combined);
    }
}
