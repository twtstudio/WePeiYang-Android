package com.rex.wepeiyang;

import com.twt.service.rxsrc.utils.TimeStampUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getTime(){
        String s = TimeStampUtils.getSimpleMonthString(String.valueOf(System.currentTimeMillis()));
        System.out.println(s);
    }
}