package com.twtstudio.retrox.tjulibrary.provider;

import java.util.List;

/**
 * Created by retrox on 2017/2/21.
 */

public class Info {

    /**
     * card :
     * name :
     * status : 有效
     * expire : 2020.06.30
     * type : 本科生
     * borrowAmount : 1
     * borrowLimit : 10
     * credit : 0
     * books : [{"barcode":"TD002424561","title":"设计心理学．2，与复杂共处，= Living with complexity","author":"(美) 唐纳德·A·诺曼著","callno":"TB47/N4(5) v.2","local":"北洋园工学阅览区","type":"中文普通书","loanTime":"2017-01-09","returnTime":"2017-03-23"}]
     */

    public String card;
    public String name;
    public String status;
    public String expire;
    public String type;
    public int borrowAmount;
    public int borrowLimit;
    public String credit;
    public List<Book> books;

}
