package com.example.caokun.fellowsearch.model;

import com.example.caokun.fellowsearch.common.IViewController;

import java.util.List;

/**
 * Created by caokun on 2017/2/22.
 */

public interface FellowfindController extends IViewController {
    void bindAllProvince(List<Province> provinces);
    void bindAllMajor(List<Major> majors);
    void bindAllInstitute(List<Institute> institutes);
    void bindAllSenior(List<Senior>seniors);
}
