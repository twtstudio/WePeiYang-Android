package course.labs.classroomquery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;


import com.twt.wepeiyang.commons.utils.CommonPrefUtil;


import course.labs.classroomquery.Model.userId;
import course.labs.classroomquery.getUserId.getUserIdController;
import course.labs.classroomquery.getUserId.getUserIdPresenter;
import course.labs.classroomquery.homePage.MainActivity;

/**
 * Created by asus on 2017/2/5.
 * @deprecated
 */
public class WelcomActivity extends Activity {
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        boolean isLogin = CommonPrefUtil.getIsLogin();

        if (isLogin){
            System.out.println("Token是"+CommonPrefUtil.getToken());
            if(CommonPrefUtil.getToken()!=null) {
                startAPP();
                finish();
            }

        }else {

            Class clazz = null;
            try {
                clazz = Class.forName("com.twt.wepeiyang.commons.auth.login.LoginActivity");
                Intent intent = new Intent(this, clazz);
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();

            }
            Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);




        }
        finish();




    }
    @Override
    protected void onStop(){
        super.onStop();
        finish();
    }
    public void startAPP(){
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }
}
