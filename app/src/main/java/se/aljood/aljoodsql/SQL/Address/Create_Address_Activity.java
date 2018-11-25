package se.aljood.aljoodsql.SQL.Address;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import se.aljood.aljoodsql.R;
import se.aljood.aljoodsql.SQL.Module.MySQLClient;

public class Create_Address_Activity extends AppCompatActivity {
    String first_Name,last_Name,address,address2,city,state,post_Code;
    TextView txt_First_Name,txt_Last_Name,txt_Address,txt_Postcode,txt_city,txt_Address2;
    Spinner spinner_State;
    Button btn_Create_Address;
    LinearLayout linearLayout_progress;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_address);

        txt_First_Name=findViewById(R.id.txt_first_name_create_adr);
        txt_First_Name.setHint(txt_First_Name.getHint()+"*");
        txt_Last_Name=findViewById(R.id.txt_last_name_create_adr);
        txt_Address=findViewById(R.id.txt_address_create_adr);
        txt_Address2=findViewById(R.id.txt_address2_create_adr);
        txt_city=findViewById(R.id.txt_city_create_adr);
        txt_Postcode=findViewById(R.id.txt_postcode_create_adr);
        spinner_State=findViewById(R.id.spi_state_create_adr);
        btn_Create_Address=findViewById(R.id.btn_create_adr);

        scrollView=findViewById(R.id.scroll_create_adr);
        scrollView.setVisibility(View.GONE);

        linearLayout_progress = findViewById(R.id.linear_progress_create_adr);
        linearLayout_progress.setVisibility(View.VISIBLE);

        select_zones(spinner_State);

        btn_Create_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptCreate_Adr();
            }
        });
//        progressBar=findViewById(R.id.prg_bar_create_adr);

    }

    private void attemptCreate_Adr() {
        txt_Postcode.setError(null);
        txt_Address.setError(null);
        txt_Last_Name.setError(null);
        txt_First_Name.setError(null);
        txt_city.setError(null);

        // Store values at the time of the create address attempt.
        first_Name=txt_First_Name.getText().toString().trim();
        last_Name = txt_Last_Name.getText().toString().trim();
        address=txt_Address.getText().toString().trim();
        address2=txt_Address2.getText().toString().trim();
        city=txt_city.getText().toString().trim();
        post_Code=txt_Postcode.getText().toString().trim();
        Boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(first_Name)) {
            txt_First_Name.setError(getString(R.string.error_field_required));
            focusView = txt_First_Name;
            cancel = true;
            return;
        }
        if (first_Name.length()<=2){
            txt_First_Name.setError(getString(R.string.error_firstname_must_be_more));
            focusView = txt_First_Name;
            cancel = true;
            return;
        }

        if (TextUtils.isEmpty(last_Name)) {
            txt_Last_Name.setError(getString(R.string.error_field_required));
            focusView = txt_Last_Name;
            cancel = true;
            return;
        }
        if (last_Name.length()<=2){
            txt_Last_Name.setError(getString(R.string.error_firstname_must_be_more));
            focusView = txt_Last_Name;
            cancel = true;
            return;
        }

        if (TextUtils.isEmpty(city)) {
            txt_city.setError(getString(R.string.error_field_required));
            focusView = txt_city;
            cancel = true;
            return;
        }
        if (city.length()<=2){
            txt_city.setError(getString(R.string.error_city_must_be_more));
            focusView = txt_city;
            cancel = true;
            return;
        }


        if (TextUtils.isEmpty(address)) {
            txt_Address.setError(getString(R.string.error_field_required));
            focusView = txt_Address;
            cancel = true;
            return;
        }
        if (address.length()<=2){
            txt_Address.setError(getString(R.string.error_firstname_must_be_more));
            focusView = txt_Address;
            cancel = true;
            return;
        }

        if(spinner_State.getSelectedItemId()!=0) {
            state=spinner_State.getSelectedItem().toString().trim();
        } else  {
            Toast.makeText(getApplicationContext(), R.string.selected_state , Toast.LENGTH_SHORT).show();
            focusView=spinner_State;
            cancel=true;
            return;
        }



        if (TextUtils.isEmpty(post_Code)) {
            txt_Postcode.setError(getString(R.string.error_field_required));
            focusView = txt_Postcode;
            cancel = true;
            return;
        }

        if (post_Code.length()!=5 || post_Code.isEmpty() || !TextUtils.isDigitsOnly(post_Code)){
            txt_Postcode.setError(getString(R.string.error_invalid_post_code));
            focusView = txt_Last_Name;
            cancel = true;
            return;
        }



    }

    private ArrayAdapter<Zone_Class> adapter;

    private void select_zones(final Spinner sp) {
        final ArrayList addressArrayList = new ArrayList<>();
        AndroidNetworking.get("http://usrafinefood.se/tester2/crud.php")
                .addQueryParameter("action", "select_zone")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        JSONObject jo;
                        Zone_Class address;
                        try {
                            addressArrayList.add("Choose City:");
                            for (int i = 0; i < response.length(); i++) {
                                jo = response.getJSONObject(i);
                                int tr = jo.getInt("zone_id");
                                String zone_name = jo.getString("name");

                                address = new Zone_Class();
                                address.setZone_name(zone_name);

                                addressArrayList.add(address.getZone_name());

                            }

                            adapter= new ArrayAdapter(Create_Address_Activity.this, android.R.layout.simple_list_item_1, addressArrayList);
                            sp.setAdapter(adapter);
                            linearLayout_progress.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Create_Address_Activity.this, "Goods Response But Java Can`t Parse Json It Received: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(Create_Address_Activity.this, "Unsuccessful : Error Is :" + anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("tag", anError.getMessage());
                    }
                });

    }

}
