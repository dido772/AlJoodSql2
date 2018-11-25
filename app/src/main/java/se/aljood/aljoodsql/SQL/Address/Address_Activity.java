package se.aljood.aljoodsql.SQL.Address;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.widget.ANImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import se.aljood.aljoodsql.R;
import se.aljood.aljoodsql.SQL.Module.SharedPrefManager;

public class Address_Activity extends AppCompatActivity {
    RecyclerView rv_address;
    SwipeRefreshLayout swiper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        rv_address = findViewById(R.id.rv_addresses);
        swiper = findViewById(R.id.swipe_activity_address);
        swiper.setRefreshing(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_address);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Address_Activity.this,Create_Address_Activity.class);
                startActivity(i);
            }
        });
        initialize_Address_objects();

    }

    private void initialize_Address_objects() {
        rv_address.setHasFixedSize(true);
        rv_address.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        String cus_id = String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).get_user_ID());
        Retrieve_Addresses(rv_address, cus_id);
    }

    private void Retrieve_Addresses(final RecyclerView rv_address, String cus_id) {
        final ArrayList<Address_Class> address_ArrayList = new ArrayList<>();
        AndroidNetworking.get("http://usrafinefood.se/tester2/crud.php")
                .addQueryParameter("action", "get_cus_address")
                .addQueryParameter("cus_id", cus_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {
                            JSONObject jo;
                            Address_Class s;

                            try {
                                if (response.length() > 0) {
                                    for (int i = 0; i < response.length(); i++) {
                                        jo = response.getJSONObject(i);
                                        int customer_id=jo.getInt("customer_id");
                                        int country_id=jo.getInt("country_id");
                                        String zone_string=jo.getString("zone_id");
                                        String firstname = jo.getString("firstname");
                                        String lastname= jo.getString("lastname");
                                        String company= jo.getString("company");
                                        String address_1= jo.getString("address_1");
                                        String address_2= jo.getString("address_2");
                                        String city= jo.getString("city");
                                        String postcode= jo.getString("postcode");
                                        String custom_field= jo.getString("custom_field");

                                        s = new Address_Class();
                                        s.setCustomer_id(customer_id);
                                        s.setCountry_id(country_id);
                                        s.setZone_string(zone_string);
                                        s.setFirstname(firstname);
                                        s.setLastname(lastname);
                                        s.setCompany(company);
                                        s.setAddress_1(address_1);
                                        s.setAddress_2(address_2);
                                        s.setCity(city);
                                        s.setPostcode(postcode);
                                        s.setCustom_field(custom_field);

                                        address_ArrayList.add(s);

                                    }
                                    rv_address.setAdapter(new MyAdapter_Address(getApplicationContext(), address_ArrayList, swiper));
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

    public static class MyViewHolder_Address extends RecyclerView.ViewHolder {
        TextView txt_full_name,txt_address,txt_post_city;
        ImageView img_edit_address1,img_delete_address1,img_plats_address1;
        int tagItem;

        public MyViewHolder_Address(View view, int viewType) {
            super(view);
            txt_full_name = view.findViewById(R.id.txt_full_name_address);
            txt_address = view.findViewById(R.id.txt_address);
            txt_post_city = view.findViewById(R.id.txt_post_city);
            img_edit_address1=view.findViewById(R.id.img_edit_address);
            img_delete_address1 = view.findViewById(R.id.img_delete_address);
        }

    }

    public class MyAdapter_Address extends RecyclerView.Adapter<MyViewHolder_Address> {
        private Context c;
        private ArrayList<Address_Class> address_ArrayList;
        private SwipeRefreshLayout swiper;

        public MyAdapter_Address(Context c, ArrayList<Address_Class> address_ArrayList, SwipeRefreshLayout swiper) {
            this.c = c;
            this.address_ArrayList = address_ArrayList;
            this.swiper = swiper;
        }

        @Override
        public MyViewHolder_Address onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            v = LayoutInflater.from(c).inflate(R.layout.listview_address, parent, false);
            return new MyViewHolder_Address(v, viewType);
        }
        @Override
        public void onBindViewHolder(final MyViewHolder_Address holder, final int position) {
            final Address_Class itm = address_ArrayList.get(position);
            holder.img_delete_address1.setImageResource(R.drawable.delete_icon);
            holder.img_edit_address1.setImageResource(R.drawable.edit_icon);

            if (getItemCount()>0) {
                holder.img_delete_address1.setVisibility(View.VISIBLE);
            }else
            {
                holder.img_delete_address1.setVisibility(View.GONE);
            }
            holder.txt_full_name.setText( itm.getFirstname()+ ' ' + itm.getLastname());
            holder.txt_address.setText('('+itm.getAddress_1()+')');
            holder.txt_post_city.setText('('+itm.getPostcode() + ','+itm.getCity()+')');
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    fetchTimelineAsync(0);
                }
            });
            if (getItemCount()==position+1){
                swiper.setRefreshing(false);
            }
        }

        @Override
        public int getItemCount() {
            return address_ArrayList.size();
        }

        public void clear() {
            address_ArrayList.clear();
            notifyDataSetChanged();
        }

        public void fetchTimelineAsync(int page) {
            clear();
            initialize_Address_objects();
        }
    }
}
