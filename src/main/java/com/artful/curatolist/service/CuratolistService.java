package com.artful.curatolist.service;

import com.artful.curatolist.model.CLPage;

public interface CuratolistService {
    CLPage getArt(int page, int limit);
}
