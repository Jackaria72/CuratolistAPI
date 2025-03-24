package com.artful.curatolist.model;

public record CLArtwork(
   String id,
   String title,
   String artist,
   String date,
   String description,
   String medium,
   String technique,
   String classification,
   String culturalOrigin,
   String dimensions,
   String imageUrl,
   String source
) {}
