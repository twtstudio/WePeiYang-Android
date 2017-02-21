package com.twtstudio.retrox.tjulibrary.provider;

import com.twtstudio.retrox.tjulibrary.home.BookTimeHelper;

/**
 * Created by retrox on 2017/2/21.
 */

public class Book {

    /**
     * barcode : TD002424561
     * title : 设计心理学．2，与复杂共处，= Living with complexity
     * author : (美) 唐纳德·A·诺曼著
     * callno : TB47/N4(5) v.2
     * local : 北洋园工学阅览区
     * type : 中文普通书
     * loanTime : 2017-01-09
     * returnTime : 2017-03-23
     */

    public String barcode;
    public String title;
    public String author;
    public String callno;
    public String local;
    public String type;
    public String loanTime;
    public String returnTime;

    public int timeLeft(){
        return BookTimeHelper.getBetweenDays(returnTime);
//        return 20;
    }

    public boolean isOverTime(){
        return this.timeLeft() < 0;
    }

    public boolean willBeOver(){
        return this.timeLeft() < 7 && !isOverTime();
    }
}
