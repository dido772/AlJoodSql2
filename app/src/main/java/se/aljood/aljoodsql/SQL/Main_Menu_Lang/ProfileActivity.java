package se.aljood.aljoodsql.SQL.Main_Menu_Lang;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import se.aljood.aljoodsql.R;
import se.aljood.aljoodsql.SQL.Address.Address_Activity;
import se.aljood.aljoodsql.SQL.Customer.Update_Cus_Activity;
import se.aljood.aljoodsql.SQL.Module.SharedPrefManager;

public class ProfileActivity extends AppCompatActivity {
    ImageView img_profile,btn_Edit_Profile,btn_Edit_Address;
    TextView txt_full_user_name,txt_email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        img_profile = findViewById(R.id.img_profile);
        txt_full_user_name = findViewById(R.id.txt_full_user_name);
        txt_email = findViewById(R.id.txt_user_email);
        setText_Image_Cus(img_profile);
        btn_Edit_Profile=findViewById(R.id.btn_edit_profile);
        btn_Edit_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, Update_Cus_Activity.class);
                startActivity(i);
            }
        });
        btn_Edit_Address = findViewById(R.id.btn_edit_address);
        btn_Edit_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, Address_Activity.class);
                startActivity(i);
            }
        });

    }

    private void setText_Image_Cus(ImageView imageView) {
        if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
            txt_full_user_name.setText(SharedPrefManager.getInstance(getApplicationContext()).get_user_Name());
            txt_email.setText(SharedPrefManager.getInstance(getApplicationContext()).get_user_Email());
            switch (txt_full_user_name.getText().toString().substring(0,1).toLowerCase()){
                case "a":
                    imageView.setImageResource(R.drawable.a);
                    return;
                case "b":
                    imageView.setImageResource(R.drawable.b);
                    return;
                case "c":
                    imageView.setImageResource(R.drawable.c);
                    return;
                case "d":
                    imageView.setImageResource(R.drawable.d);
                    return;
                case "e":
                    imageView.setImageResource(R.drawable.e);
                    return;
                case "f":
                    imageView.setImageResource(R.drawable.f);
                    return;
                case "g":
                    imageView.setImageResource(R.drawable.g);
                    return;
                case "h":
                    imageView.setImageResource(R.drawable.h);
                    return;
                case "i":
                    imageView.setImageResource(R.drawable.i);
                    return;
                case "j":
                    imageView.setImageResource(R.drawable.j);
                    return;
                case "k":
                    imageView.setImageResource(R.drawable.k);
                    return;
                case "l":
                    imageView.setImageResource(R.drawable.l);
                    return;
                case "m":
                    imageView.setImageResource(R.drawable.m);
                    return;
                case "n":
                    imageView.setImageResource(R.drawable.n);
                    return;
                case "o":
                    imageView.setImageResource(R.drawable.o);
                    return;
                case "p":
                    imageView.setImageResource(R.drawable.p);
                    return;
                case "q":
                    imageView.setImageResource(R.drawable.q);
                    return;
                case "r":
                    imageView.setImageResource(R.drawable.r);return;
                case "s":
                    imageView.setImageResource(R.drawable.s);return;
                case "t":
                    imageView.setImageResource(R.drawable.t);return;
                case "u":
                    imageView.setImageResource(R.drawable.u);return;
                case "v":
                    imageView.setImageResource(R.drawable.v);return;
                case "w":
                    imageView.setImageResource(R.drawable.w);return;
                case "x":
                    imageView.setImageResource(R.drawable.x);return;
                case "y":
                    imageView.setImageResource(R.drawable.y);return;
                case "z":
                    imageView.setImageResource(R.drawable.z);return;
                default:
                    imageView.setImageResource(R.drawable.z);return;
            }
        }
    }

}
