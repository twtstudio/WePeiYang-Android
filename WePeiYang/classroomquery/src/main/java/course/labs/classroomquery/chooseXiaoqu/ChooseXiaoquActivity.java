package course.labs.classroomquery.chooseXiaoqu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import course.labs.classroomquery.R;
import course.labs.classroomquery.homePage.MainActivity;
import rx.Observable;
import rx.Observer;
import rx.functions.Func1;

/**
 * Created by asus on 2017/1/25.
 */
public class ChooseXiaoquActivity extends Activity {
    private LinearLayout peiyangyuanButton;
    private LinearLayout weijinluButton;
    private Button backButton;
    private ImageView peiyangyuanImageview;
    private ImageView weijinluImageview;
    private int xiaoqu;
    private int OLD_CAMPUS = 0;
    private int NEW_CAMPUS = 1;
    private boolean res;
    private Window window;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.purple));
        setContentView(R.layout.choose_xiaoqu_layout);

        xiaoqu = -1;
         loadCampus();
        initial();

    }
    public void initial(){
        peiyangyuanButton =(LinearLayout) findViewById(R.id.peiyanguan);
        weijinluButton = (LinearLayout) findViewById(R.id.weijinlu);
        backButton = (Button)findViewById(R.id.backButton3);
        peiyangyuanImageview = (ImageView)findViewById(R.id.peiyanguanimage);
        weijinluImageview = (ImageView)findViewById(R.id.weijinluimage);
        peiyangyuanImageview.setImageDrawable(getResources().getDrawable(R.drawable.classroom_icon_gouxuan));
        weijinluImageview.setImageDrawable(getResources().getDrawable(R.drawable.classroom_icon_gouxuan));
        peiyangyuanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                justShow(NEW_CAMPUS);
            }
        });
        weijinluButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                justShow(OLD_CAMPUS);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("xiaoqu",xiaoqu);
                setResult( MainActivity.RESULT_CODE,intent);
                finish();
            }
        });

    }
    public boolean loadCampus(){
        File file = new File(Environment.getExternalStorageDirectory()+"/TWT"+"/Cache.txt");
        if(!file.exists()){

            return  false;
        }
        else {
            res = false;
            Observable.just(file).map(new Func1<File,Integer>() {
                @Override
                public Integer call(File file){
                    try {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                        int tempRes = -1;
                        String temp = null;
                        while (null!=(temp=reader.readLine())){
                            tempRes = Integer.valueOf(temp);
                        }
                        return Integer.valueOf(tempRes);
                    }
                    catch (FileNotFoundException e){

                        Log.i("File","No exist");
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }).subscribe(new Observer<Integer>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Integer integer) {
                    if(integer!=null) {
                        res = true;
                        setXiaoqu(integer.intValue());
                    }
                }
            });
            return  res;
        }
    }
    private void justShow(int xiaoqu){
        setXiaoqu(xiaoqu);
        if(xiaoqu==OLD_CAMPUS){
            peiyangyuanImageview.setVisibility(View.INVISIBLE);
            weijinluImageview.setVisibility(View.VISIBLE);
        }
        else if(xiaoqu==NEW_CAMPUS){
            peiyangyuanImageview.setVisibility(View.VISIBLE);
            weijinluImageview.setVisibility(View.INVISIBLE);
        }
    }
    private void setXiaoqu(int xiaoqu){
        this.xiaoqu = xiaoqu;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
            Intent intent = new Intent();
            intent.putExtra("xiaoqu",xiaoqu);
            setResult( MainActivity.RESULT_CODE,intent);
            finish();
            return  true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
