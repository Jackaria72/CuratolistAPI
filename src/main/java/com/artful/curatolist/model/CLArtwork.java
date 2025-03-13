package com.artful.curatolist.model;

public record CLArtwork(
   String id,
   String title,
   String artist,
   String date,
   String period,
   String imageUrl,
   String source
) {}
