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
            String description,
            String medium,
            String technique,
            String classification,
            String culture,
            String dimensions,
            String primaryimageurl,
            int imagepermissionlevel
    ){}
    public record Person(
            String name,
            String role
    ){}
}
