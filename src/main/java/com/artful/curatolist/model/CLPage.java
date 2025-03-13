package com.artful.curatolist.model;

import java.util.List;

public record CLPage(PageInfo pageInfo, List<CLArtwork> artwork) {
    public record PageInfo(
            int chicagoTotal,
            int chicagoPageTotal,
            int harvardTotal,
            int HarvardPageTotal
    ){}
}
