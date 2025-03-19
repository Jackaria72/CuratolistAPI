package com.artful.curatolist.service;

import com.artful.curatolist.builder.HarvardUriBuilder;
import com.artful.curatolist.client.HarvardClient;
import com.artful.curatolist.mapper.HarvardMapper;
import com.artful.curatolist.model.CLArtwork;
import com.artful.curatolist.model.CLPage;
import com.artful.curatolist.model.HarvardPage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.List;

@Service
public class HarvardService implements ApiService {

    private final HarvardClient harvardClient;
    private final HarvardMapper harvardMapper;
    private final HarvardUriBuilder harvardUriBuilder;

    public HarvardService(HarvardClient harvardClient, HarvardMapper harvardMapper, HarvardUriBuilder harvardUriBuilder) {
        this.harvardClient = harvardClient;
        this.harvardMapper = harvardMapper;
        this.harvardUriBuilder = harvardUriBuilder;
    }

    @Override
    public Mono<CLPage> getArt(int page, String searchQuery, String sortTerm, String filters) {
        String uri = harvardUriBuilder.buildHarvardUri(page, searchQuery, sortTerm, filters);
        Mono<HarvardPage> harvardPageMono = harvardClient.getHarvardArtwork(uri);

        return harvardPageMono.map(
                harvardPage -> {
                    List<CLArtwork> artworks = harvardMapper.mapHarvardArt(harvardPage);
                    CLPage.PageInfo pageInfo = new CLPage.PageInfo(
                            0,0,
                            harvardPage.info().totalrecords(),
                            harvardPage.info().pages());
                    return new CLPage(pageInfo, artworks);
                });
    }
}
