package se.aljood.aljoodsql.SQL.Customer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import se.aljood.aljoodsql.R;
import se.aljood.aljoodsql.SQL.Module.SharedPrefManager;
import se.aljood.aljoodsql.SQL.Splash_Login.LoginActivity;

import static se.aljood.aljoodsql.R.string.title_dialog_create_Cus;

public class Update_Cus_Activity extends AppCompatActivity {
    EditText txtFirstName ;
    EditText txtLastName ;
    EditText txtTelephone ;
    CheckBox chkSubscribe;
    Button btn_update;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder1;
    ArrayList<Customers_Class> customers_ArrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_cus);
        initializeViews();

    }

    private void initializeViews() {
        builder1 = new AlertDialog.Builder(Update_Cus_Activity.this);
        txtFirstName = (EditText) findViewById(R.id.first_name_update_cus_form);
        txtLastName = (EditText) findViewById(R.id.last_name_update_cus_form);
        txtTelephone = (EditText) findViewById(R.id.telephone_update_cus);
        chkSubscribe = (CheckBox) findViewById(R.id.subscribe_update_cus_form);
        btn_update = (Button) findViewById(R.id.save_update_cus_form);
        get_cus_details();
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempt_Update_cus();
            }
        });
    }

    private void get_cus_details() {
        String cus_id = String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).get_user_ID());
        AndroidNetworking.get("http://usrafinefood.se/tester2/crud.php")
                .addQueryParameter("action", "get_cus_details")
                .addQueryParameter("cus_id",cus_id )
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {
                            JSONObject jo;
                            Customers_Class s;
                            try {
                                if (response.length() > 0) {
                                    for (int i = 0; i < response.length(); i++) {
                                         jo = response.getJSONObject(i);
                                         int Customer_group_id=jo.getInt("customer_id");
                                        int Store_id=jo.getInt("store_id");
                                        String Firstname=jo.getString("firstname");
                                        String Lastname=jo.getString("lastname");
                                        String Email=jo.getString("email");
                                        String Telephone=jo.getString("telephone");
                                        String Fax=jo.getString("fax");
                                        String Password=jo.getString("password");
                                        String Salt=jo.getString("salt");
                                        String Cart=jo.getString("cart");
                                        String Wishlist=jo.getString("wishlist");
                                        String Newsletter=jo.getString("newsletter");
                                        int Address=jo.getInt("address_id");
                                        String Custom_field=jo.getString("custom_field");
                                        String Ip=jo.getString("ip");
                                        int Status=jo.getInt("status");
                                        int Approved=jo.getInt("approved");
                                        int Safe=jo.getInt("safe");
                                        String Token=jo.getString("token");
                                        String Date_added=jo.getString("date_added");
                                        s = new Customers_Class();
                                        s.setCustomer_group_id(Customer_group_id);
                                        s.setStore_id(Store_id);
                                        s.setFirstname(Firstname);
                                        s.setLastname(Lastname);
                                        s.setEmail(Email);
                                        s.setTelephone(Telephone);
                                        s.setFax(Fax);
                                        s.setPassword(Password);
                                        s.setSalt(Salt);
                                        s.setCart(Cart);
                                        s.setWishlist(Wishlist);
                                        s.setNewsletter(Newsletter);
                                        s.setAddress_id(Address);
                                        s.setCustom_field(Custom_field);
                                        s.setIp(Ip);
                                        s.setStatus(Status );
                                        s.setApproved(Approved);
                                        s.setSafe(Safe);
                                        s.setToken(Token);
                                        s.setDate_added(Date_added);
                                        customers_ArrayList.add(s);
                                        fill_objects();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Goods Response But Java Can`t Parse Json It Received: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Unsuccessful : Error Is :" + anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("tag", anError.getMessage());
                    }
                });
    }

    private void fill_objects() {
        txtFirstName.setText(customers_ArrayList.get(0).getFirstname().toString());
        txtLastName.setText(customers_ArrayList.get(0).getLastname().toString());
        txtTelephone.setText(customers_ArrayList.get(0).getTelephone().toString());
        if (customers_ArrayList.get(0).getNewsletter().toString().equals("0")){
            chkSubscribe.setChecked(false);
        }else {
            chkSubscribe.setChecked(true);
        }
    }

    private void attempt_Update_cus() {
        // Reset errors.
        txtFirstName.setError(null);
        txtLastName.setError(null);
        txtTelephone.setError(null);
        chkSubscribe.setError(null);
        // Store values at the time of the login attempt.
        first_name = txtFirstName.getText().toString();
        last_name = txtLastName.getText().toString();
        telephone = txtTelephone.getText().toString();
//        subscribe = chkSubscribe.getText().toString();
        Boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(first_name)) {
            txtFirstName.setError(getString(R.string.error_field_required));
            focusView = txtFirstName;
            cancel = true;
            return;
        }
        if (TextUtils.isEmpty(last_name)) {
            txtLastName.setError(getString(R.string.error_field_required));
            focusView = txtLastName;
            cancel = true;
            return;
        }
        if (TextUtils.isEmpty(telephone)) {
            txtTelephone.setError(getString(R.string.error_field_required));
            focusView = txtTelephone;
            cancel = true;
            return;
        }
        progressDialog = new ProgressDialog(Update_Cus_Activity.this);
        progressDialog.setTitle(title_dialog_create_Cus);
        progressDialog.setMessage(String.valueOf(R.string.message_dialog_create_Cus));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show(); // Display Progress Dialog
        Update_Cus();
        if (cancel){
            focusView.requestFocus();
        }
    }

    String first_name,last_name,telephone;

    public void Update_Cus(){
        String newsletter;
        if (chkSubscribe.isChecked()) {
            newsletter="1";
        }else {
            newsletter = "0";
        }
        String cus_id = String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).get_user_ID());
        AndroidNetworking.get("http://usrafinefood.se/tester2/crud.php")
                .addQueryParameter("action", "update_cus")
                .addQueryParameter("cus_id",cus_id)
                .addQueryParameter("cus_first_name",first_name)
                .addQueryParameter("cus_last_name",last_name)
                .addQueryParameter("cus_tel", telephone)
                .addQueryParameter("cus_newsletter",newsletter)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response !=null){
                            try{
                                // Show Response From Server
                                String responseString = response.getJSONObject(0).get("Result").toString();
                                if( responseString.equals("Success")){
                                    final AlertDialog.Builder builder;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        builder = new AlertDialog.Builder(Update_Cus_Activity.this, android.R.style.Theme_Material_Dialog_Alert);
                                    } else {
                                        builder = new AlertDialog.Builder(Update_Cus_Activity.this);
                                    }
                                    builder.setTitle(R.string.title_alert_done_cus)
                                            .setMessage(R.string.active_message_cus)
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_info)
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Update_Cus_Activity.this,"Goods Response But Java Can`t Parse Json It Received: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(Update_Cus_Activity.this,"Unsuccessful : Error Is :" + anError.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
