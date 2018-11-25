package se.aljood.aljoodsql.SQL.Customer;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;

import se.aljood.aljoodsql.R;
import se.aljood.aljoodsql.SQL.Module.MySQLClient;
import se.aljood.aljoodsql.SQL.Splash_Login.LoginActivity;

import static android.Manifest.permission.READ_CONTACTS;
import static se.aljood.aljoodsql.R.string.title_dialog_create_Cus;


public class CreateCusActivity extends AppCompatActivity {
    EditText txtFirstName ;
    EditText txtLastName ;
    AutoCompleteTextView txtEmail ;
    EditText txtTelephone ;
    EditText txtAddress ;
    Spinner spCity  ;
    EditText txtPostCode;
    EditText txtPassword ;
    EditText txtConfrimPass;
    TextView txtCity;
    CheckBox chkSubscribe;
    Button btnSignuup;


    String firstname ;
    String lastname ;
    String email ;
    String telephone ;
    String address ;
    String city ;
    String postcode ;
    String password ;
    String confrimpass;
    String subscribe ;

    ProgressDialog progressDialog;
    private static final int REQUEST_READ_CONTACTS = 0;
    AlertDialog.Builder builder1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cus);

        this.initializeViews();
    }

    private void initializeViews() {
        builder1 = new AlertDialog.Builder(CreateCusActivity.this);
         txtFirstName = (EditText) findViewById(R.id.firstname);
         txtLastName = (EditText) findViewById(R.id.lastname);
         txtEmail = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();
         txtTelephone = (EditText) findViewById(R.id.telephone);
         txtAddress = (EditText) findViewById(R.id.address);
        txtCity = (TextView) findViewById(R.id.txt_city);
         spCity = (Spinner) findViewById(R.id.city);
         txtPostCode = (EditText) findViewById(R.id.postcode);
         txtPassword = (EditText) findViewById(R.id.password);
         txtConfrimPass = (EditText) findViewById(R.id.confrimpassword);
         chkSubscribe = (CheckBox) findViewById(R.id.subscribe);
         btnSignuup = (Button) findViewById(R.id.cus_sign_up_button);

        new MySQLClient(CreateCusActivity.this).select_zones(spCity);

        btnSignuup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignup();

            }

        });
    }

    private void attemptSignup() {
        // Reset errors.
        txtFirstName.setError(null);
        txtLastName.setError(null);
        txtEmail.setError(null);
            txtTelephone.setError(null);
        txtAddress.setError(null);
        txtCity.setError(null);
        txtPostCode.setError(null);
        txtPassword.setError(null);
        txtConfrimPass.setError(null);
        chkSubscribe.setError(null);

        // Store values at the time of the login attempt.
         firstname = txtFirstName.getText().toString();
         lastname = txtLastName.getText().toString();
         email = txtEmail.getText().toString();
         telephone = txtTelephone.getText().toString();
         address = txtAddress.getText().toString();
         city = spCity.getSelectedItem().toString();
         postcode = txtPostCode.getText().toString();
         password = txtPassword.getText().toString();
         confrimpass = txtConfrimPass.getText().toString();
         subscribe = chkSubscribe.getText().toString();

        Boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(firstname)) {
            txtFirstName.setError(getString(R.string.error_field_required));
            focusView = txtFirstName;
            cancel = true;
            return;
        }
        if (TextUtils.isEmpty(lastname)) {
            txtLastName.setError(getString(R.string.error_field_required));
            focusView = txtLastName;
            cancel = true;
            return;
        }
        if (TextUtils.isEmpty(email)) {
            txtEmail.setError(getString(R.string.error_field_required));
            focusView = txtEmail;
            cancel = true;
            return;
        }
        if (!isEmailValid(email)) {
            txtEmail.setError(getString(R.string.error_invalid_email));
            focusView = txtPassword;
            cancel = true;        return;
        }

        if (TextUtils.isEmpty(telephone)) {
            txtTelephone.setError(getString(R.string.error_field_required));
            focusView = txtTelephone;
            cancel = true;
            return;
        }
        if (TextUtils.isEmpty(address)) {
            txtAddress.setError(getString(R.string.error_field_required));
            focusView = txtAddress;
            cancel = true;
            return;

        }
        if (TextUtils.isEmpty(city)) {
            txtCity.setError(getString(R.string.error_field_required));
            focusView = txtCity;
            cancel = true;
            return;

        }
        if (TextUtils.isEmpty(postcode)) {
            txtPostCode.setError(getString(R.string.error_field_required));
            focusView = txtPostCode;
            cancel = true;
            return;

        }
        if (TextUtils.isEmpty(password)) {
            txtPassword.setError(getString(R.string.error_field_required));
            focusView = txtPassword;
            cancel = true;        return;

        }
        if (TextUtils.isEmpty(confrimpass)) {
            txtConfrimPass.setError(getString(R.string.error_field_required));
            focusView = txtConfrimPass;
            cancel = true;        return;

        }
        if (password.length() < 5) {
            txtPassword.setError(getString(R.string.error_invalid_password));
            focusView = txtPassword;
            cancel = true;        return;

        }
        if (!password.equals(confrimpass)){
            txtPassword.setError(getString(R.string.error_paswordnotmatch));
            focusView = txtPassword;
            cancel = true;        return;
        }

       // MySQLClient sqlClient = new MySQLClient(CreateCusActivity.this);
       // sqlClient.cus_exists(email);
        //new MySQLClient(CreateCusActivity.this).cus_exists(email);
//        if (Boolean.TRUE) {
//            txtEmail.setError(getString(R.string.email_already_register));
//            focusView = txtEmail;
//            cancel = true;
//            return;
//        }
        progressDialog = new ProgressDialog(CreateCusActivity.this);

        progressDialog.setTitle(title_dialog_create_Cus);
        progressDialog.setMessage(String.valueOf(R.string.message_dialog_create_Cus));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.show(); // Display Progress Dialog
        cus_exists(email);

        if (cancel){
            focusView.requestFocus();
        }
    }

    public void cus_exists(String email){
        AndroidNetworking.get("http://usrafinefood.se/tester2/crud.php")
                .addQueryParameter("action", "cus_exists")
                .addQueryParameter("email", email)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response !=null){
                            try{
                                // Show Response From Server
                                String responseString = response.get(0).toString();
                                if( responseString.equals("Unsuccess")){
                                    setCustomer();

                                }else {
                                    txtEmail.setError(getString(R.string.email_already_register));
                                    progressDialog.cancel();

                                    return;

                                }
                                progressDialog.cancel();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(CreateCusActivity.this,"Goods Response But Java Can`t Parse Json It Received: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();

                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(CreateCusActivity.this,"Unsuccessful : Error Is :" + anError.getMessage(),Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();

                    }
                });
    }
    public void setCustomer(){
        Customers_Class cus = new Customers_Class();

        cus.setCustomer_group_id(1);
        cus.setStore_id(0);
        cus.setFirstname(firstname);
        cus.setLastname(lastname);
        cus.setEmail(email);
        cus.setTelephone(telephone);
        cus.setFax("");
        cus.setPassword(password);
        cus.setSalt("");
        cus.setCart(null);
        cus.setWishlist(null);
if (chkSubscribe.isChecked()) {
    cus.setNewsletter("1");
}else {
    cus.setNewsletter("0");
}
        cus.setAddress_1(address);
        cus.setCity(city);
        cus.setPostcode(postcode);
        cus.setCustom_field("");
        cus.setIp("127.0.0.1");
        cus.setStatus(1);
        cus.setApproved(1);
        cus.setSafe(0);
        cus.setToken("");
        cus.setDate_added("");
        add(cus);
        //new MySQLClient(CreateCusActivity.this).add(cus);

    }
    public void add(Customers_Class s){
        AndroidNetworking.get("http://usrafinefood.se/tester2/crud.php")
                .addQueryParameter("action", "save")
                .addQueryParameter("city",s.getCity())
                .addQueryParameter("firstname_in", s.getFirstname())
                .addQueryParameter("lastname_in",s.getLastname())
                .addQueryParameter("address_1", s.getAddress_1())
                .addQueryParameter("postcode", s.getPostcode())
                .addQueryParameter("email_in", s.getEmail())
                .addQueryParameter("telephone_in", s.getTelephone())
                .addQueryParameter("password_in",s.getPassword())
                .addQueryParameter("newsletter_in", s.getNewsletter())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response !=null){
                            try{


                                // Show Response From Server
                                String responseString = response.get(0).toString();
                                if( responseString.equals("Success")){
                                    final AlertDialog.Builder builder;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        builder = new AlertDialog.Builder(CreateCusActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                    } else {
                                        builder = new AlertDialog.Builder(CreateCusActivity.this);
                                    }
                                    builder.setTitle(R.string.title_alert_done_cus)
                                            .setMessage(R.string.active_message_cus)
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent i=new Intent(CreateCusActivity.this,LoginActivity.class);
                                                startActivity(i);
                                                }

                                            })
                                            .setIcon(android.R.drawable.ic_dialog_info)
                                            .show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(CreateCusActivity.this,"Goods Response But Java Can`t Parse Json It Received: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(CreateCusActivity.this,"Unsuccessful : Error Is :" + anError.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

    //    getLoaderManager().initLoader(0, null, this);
    }
    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(txtEmail, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

}
