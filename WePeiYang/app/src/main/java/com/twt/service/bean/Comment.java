package com.twt.service.bean;

import java.io.Serializable;

/**
 * Created by sunjuntao on 15/11/21.
 */
public class Comment implements Serializable {
    public int nid;//所属新闻id
    public String ccontent;//评论内容
    public int cid;//评论id
    public String cuser;//用户名
    public String ctime;//评论时间
}
