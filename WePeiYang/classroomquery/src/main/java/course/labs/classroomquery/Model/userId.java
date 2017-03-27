package course.labs.classroomquery.Model;

/**
 * Created by Administrator on 2017/2/26.
 */

public class userId {
    private int twtid;
    private String twtuname;
    private String realname;
    private String studentid;
    private String avatar;
    private Accounts accounts;
    public String getID(){
        return studentid;
    }
    public static class Accounts{
        private boolean tju;
        private boolean lib;
    }
}
