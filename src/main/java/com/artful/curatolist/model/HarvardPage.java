package com.artful.curatolist.model;

import java.util.List;

public record HarvardPage(HarvardPageInfo info, List<HarvardArt> records) {
    public record HarvardPageInfo(
            int totalrecords,
            int pages
    ){}
    public record HarvardArt(
            int id,
            String title,
            List<Person> people,
            String dated,
            String period,
            String primaryimageurl
    ){}
    public record Person(String name){}
}
