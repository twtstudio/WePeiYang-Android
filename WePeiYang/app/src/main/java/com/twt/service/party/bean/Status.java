package com.twt.service.party.bean;

import java.util.List;

/**
 * Created by dell on 2016/7/18.
 */
public class Status {

    /**
     * status : 1
     * status_id : [{"id":0,"status":1,"msg":"真的要去提交入党申请书?"},{"id":1,"status":0,"msg":"不好意思,入党申请书还没有提交!"},{"id":2,"status":0,"msg":"您还没通过20课的学习,不允许参加结业考试!"},{"id":3,"status":0,"msg":"您还没通过申请人结业考试"},{"id":4,"status":0,"msg":"入党申请书未通过,不能提交思想汇报!"},{"id":5,"status":0,"msg":"第一季度思想汇报未提交,或者没有通过,无法递交!"},{"id":6,"status":0,"msg":"第二季度思想汇报未提交,或者没有通过,无法递交!"},{"id":7,"status":0,"msg":"第三季度思想汇报未提交,或者没有通过,无法递交!"},{"id":8,"status":0,"msg":"您还没有递交入党申请书,或者未通过审批,是不能参加学习小组的!"},{"id":9,"status":0,"msg":"您还没有被分配学习小组,是不能被确定为积极分子的!"},{"id":10,"status":0,"msg":"您还没有被分配学习小组,是不能被团推优的!"},{"id":11,"status":0,"msg":"您之前显示灰色或者红色的部分是没有完成的,所以无法成为发展对象!"},{"id":12,"status":0,"msg":"您还没有被确定为发展对象,无法参加集中培训!"},{"id":13,"status":0,"msg":"您还没有参加集中培训,无法通过入党资料齐全!"},{"id":14,"status":0,"msg":"您还没有通过入党资料齐全的审核,无法向上级组织汇报工作!"},{"id":15,"status":0,"msg":"请耐心等待支部委员向上级党组织汇报,否则无法公示!"},{"id":16,"status":0,"msg":"还未进行发展公示,不能提交入党志愿书!"},{"id":17,"status":0,"msg":"入党志愿书未提交或者审批未通过,无法召开发展大会表决!"},{"id":18,"status":0,"msg":"发展大会表决未通过,无法进行下一步!"},{"id":19,"status":0,"msg":"党委审批,谈话未通过,无法成为预备党员!"},{"id":20,"status":0,"msg":"您目前还不是预备党员,无法参加预备党员的结业考试!"},{"id":21,"status":0,"msg":"您还不是预备党员,无法递交个人小结!"},{"id":22,"status":0,"msg":"第一季度个人小结还未通过审核,无法提交第二季度!"},{"id":23,"status":0,"msg":"第二季度个人小结还未通过审核,无法提交第三季度!"},{"id":24,"status":0,"msg":"第三季度个人小结还未通过审核,无法提交第三季度!"},{"id":25,"status":0,"msg":"您还不是预备党员,无法通过按时参加党支部组织生活及党内活动!"},{"id":26,"status":0,"msg":"您还没有完成之前的步骤,无法递交转正申请!"},{"id":27,"status":0,"msg":"您还没有提交转正申请,无法进行转正公示!"},{"id":28,"status":0,"msg":"还没进行转正公示,无法召开发展大会!"},{"id":29,"status":0,"msg":"未召开转正大会表决!"},{"id":30,"status":0,"msg":"党委审批未通过,无法成为正式党员!"}]
     */

    private int status; //0表示失败 1成功
    /**
     * id : 0
     * status : 1
     * msg : 真的要去提交入党申请书?
     */

    //首页使用
    private List<StatusIdBean> status_id;

    //报名模块使用
    private List<TestInfo> test_info;

    private String msg; //失败原因，若成功不反回message

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<StatusIdBean> getStatus_id() {
        return status_id;
    }

    public void setStatus_id(List<StatusIdBean> status_id) {
        this.status_id = status_id;
    }

    public List<TestInfo> getTest_info() {
        return test_info;
    }

    public void setTest_info(List<TestInfo> test_info) {
        this.test_info = test_info;
    }
}
