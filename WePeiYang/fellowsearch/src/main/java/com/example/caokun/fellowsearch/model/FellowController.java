package com.example.caokun.fellowsearch.model;

import com.example.caokun.fellowsearch.common.IViewController;

import java.util.List;

/**
 * Created by caokun on 2017/2/21.
 */

public interface FellowController extends IViewController {
    void  bindStudentData(List<Student> students);

}
