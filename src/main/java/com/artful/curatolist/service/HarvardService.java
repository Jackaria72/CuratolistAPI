package com.artful.curatolist.service;

import com.artful.curatolist.client.HarvardClient;
import com.artful.curatolist.mapper.HarvardMapper;
import com.artful.curatolist.model.CLArtwork;
import com.artful.curatolist.model.CLPage;
import com.artful.curatolist.model.HarvardPage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class HarvardService implements ApiService {

    private final HarvardClient harvardClient;
    private final HarvardMapper harvardMapper;

    public HarvardService(HarvardClient harvardClient, HarvardMapper harvardMapper) {
        this.harvardClient = harvardClient;
        this.harvardMapper = harvardMapper;
    }

    @Override
    public Mono<CLPage> getArt(int page) {
        Mono<HarvardPage> harvardPageMono = harvardClient.getHarvardArtwork(page);

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
