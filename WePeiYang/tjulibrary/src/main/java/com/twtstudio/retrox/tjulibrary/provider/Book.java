package com.twtstudio.retrox.tjulibrary.provider;

import com.twtstudio.retrox.tjulibrary.home.BookTimeHelper;

import java.util.Objects;

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
    public String id;

    /**
     * 距离还书日期还有多少天
     * @return
     */
    public int timeLeft(){
        return BookTimeHelper.getBetweenDays(returnTime);
//        return 20;
    }

    /**
     * 看是否超过还书日期
     * @return
     */
    public boolean isOverTime(){
        return this.timeLeft() < 0;
    }

    public boolean willBeOver(){
        return this.timeLeft() < 7 && !isOverTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(barcode, book.barcode) &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author) &&
                Objects.equals(callno, book.callno) &&
                Objects.equals(local, book.local) &&
                Objects.equals(type, book.type) &&
                Objects.equals(loanTime, book.loanTime) &&
                Objects.equals(returnTime, book.returnTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(barcode, title, author, callno, local, type, loanTime, returnTime);
    }
}
