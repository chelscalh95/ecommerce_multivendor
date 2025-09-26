package com.ecom.service;

import java.util.List;

import com.ecom.model.Home;
import com.ecom.model.HomeCategory;

public interface HomeService {

    Home creatHomePageData(List<HomeCategory> categories);

}
