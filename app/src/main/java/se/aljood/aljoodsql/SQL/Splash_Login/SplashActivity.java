package se.aljood.aljoodsql.SQL.Splash_Login;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import se.aljood.aljoodsql.R;
import se.aljood.aljoodsql.SQL.Main_Menu_Lang.LanguageActivity;
import se.aljood.aljoodsql.SQL.Module.SharedPrefManager;

public class SplashActivity extends AppCompatActivity {
    private TextView tv;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
//            Toast.makeText(this, "Large screen", Toast.LENGTH_LONG).show();
            SharedPrefManager.getInstance(getApplicationContext()).set_Screen_Size("hdpi");
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
//            Toast.makeText(this, "Normal sized screen", Toast.LENGTH_LONG).show();
            SharedPrefManager.getInstance(getApplicationContext()).set_Screen_Size("mdpi");
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
//            Toast.makeText(this, "Small sized screen", Toast.LENGTH_LONG).show();
            SharedPrefManager.getInstance(getApplicationContext()).set_Screen_Size("ldpi");
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
//            Toast.makeText(this, "Small sized screen", Toast.LENGTH_LONG).show();
            SharedPrefManager.getInstance(getApplicationContext()).set_Screen_Size("xhdpi");
        }
        else {
            SharedPrefManager.getInstance(getApplicationContext()).set_Screen_Size("mdpi");
        }
        tv=(TextView) findViewById(R.id.tv);
        iv=(ImageView) findViewById(R.id.tv_order_item_ivm);
        Animation myanim= AnimationUtils.loadAnimation(this,R.anim.mytransiotion);
        tv.startAnimation(myanim);
        iv.startAnimation(myanim);
            if (SharedPrefManager.getInstance(getApplicationContext()).isSelectedLanguage()) {
            final Intent i = new Intent(this, LoginActivity.class);
            Thread timer = new Thread() {
                public void run() {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        startActivity(i);
                        finish();
                    }
                }
            };
            timer.start();
        }else{
            final Intent i = new Intent(this, LanguageActivity.class);
            Thread timer = new Thread() {
                public void run() {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        startActivity(i);
                        finish();
                    }
                }
            };
            timer.start();
        }
    }
}
