package com.artful.curatolist.util;

import com.artful.curatolist.model.CLArtwork;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class SortComparator {

    public Comparator<CLArtwork> getComparator(String sortTerm) {
        if (sortTerm == null || sortTerm.isBlank()) {
            sortTerm = "id";
        }
        return switch (sortTerm) {
            case "title" -> Comparator.comparing(CLArtwork::title, Comparator.nullsLast(String::compareTo));
            case "classification" -> Comparator.comparing(CLArtwork::classification, Comparator.nullsLast(String::compareTo));
            default -> Comparator.comparing(CLArtwork::id, Comparator.nullsLast(String::compareTo));
        };
    }
}
