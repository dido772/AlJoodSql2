package se.aljood.aljoodsql.SQL.Home;

import android.app.Activity;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.androidnetworking.widget.ANImageView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.Response;
import se.aljood.aljoodsql.R;
import se.aljood.aljoodsql.SQL.Module.Featured_Manufacturer_Class;
import se.aljood.aljoodsql.SQL.Module.SharedPrefManager;
import se.aljood.aljoodsql.SQL.items.Flash_Deals_Items_Fragment;
import se.aljood.aljoodsql.SQL.items.Item_Card.Item_Card;
import se.aljood.aljoodsql.SQL.items.Item_Class;

public class HomeFragment extends Fragment{
    ViewFlipper viewFlipper;

    private String [] imagess={
            "http://usrafinefood.se/tester2/image/catalog/featuredbrands/slide1.jpg",
            "http://usrafinefood.se/tester2/image/catalog/featuredbrands/slide2.jpg",
            "http://usrafinefood.se/tester2/image/catalog/featuredbrands/slide3.jpg"
    };
    ViewPager viewPager;

    Activity activity;
    RecyclerView rv_Special_Items,rv_Discount_Items,rv_Best_Selling_items;
    RecyclerView.LayoutManager lm_Special_Items,lm_Discount_Items,lm_Best_Selling_Items;
    SwipeRefreshLayout swiper;

    private long START_TIME_IN_MILLIS ;
    private CountDownTimer mCuntDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftinMillis ;
    TextView txt_countdown_day,txt_countdown_hour,txt_countdown_min,txt_countdown_sec;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//Make sure you have this line of code.
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        rv_Discount_Items = rootView.findViewById(R.id.rv_discounts_items);
        rv_Special_Items=rootView.findViewById(R.id.rv_special_items);
        rv_Best_Selling_items=rootView.findViewById(R.id.rv_best_selling_items);
        swiper = rootView.findViewById(R.id.swipe_home_fragment);
        swiper.setRefreshing(true);
        TextView flash_deals=rootView.findViewById(R.id.txtflash_deals);
        flash_deals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment someFragment = new Flash_Deals_Items_Fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content,someFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });
        txt_countdown_day=rootView.findViewById(R.id.txt_countdown_day);
        txt_countdown_hour=rootView.findViewById(R.id.txt_countdown_hours);
        txt_countdown_min=rootView.findViewById(R.id.txt_countdown_minutes);
        txt_countdown_sec=rootView.findViewById(R.id.txt_countdown_seconds);
        get_time_special_itm(txt_countdown_day,txt_countdown_hour,txt_countdown_min,txt_countdown_sec);
        initialize_flipper_image(rootView);
        initialize_flipper_featured_brands(rootView);

        initialize_Home_Items();

//        RelativeLayout header_flash_deals=rootView.findViewById(R.id.header_flash_deals);
//        header_flash_deals.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Fragment fragmentt = null;
//                        fragmentt = new Flash_Deals_Items_Fragment();
//        }
//        });

        return rootView;

    }

    private void initialize_flipper_featured_brands(View rootView) {
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager_featured_brands);
//        adapter = new ViewPagerAdapter(getActivity(),imagess);
//        viewPager.setAdapter(adapter);
//        new MySQLClient(getActivity()).featured_manufacturer(viewPager);

        viewPager.setClipToPadding(false);
        viewPager.setClipToPadding(false);
        // set padding manually, the more you set the padding the more you see of prev & next page
        viewPager.setPadding(40, 0, 40, 0);
        // sets a margin b/w individual pages to ensure that there is a gap b/w them
        viewPager.setPageMargin(20);

        featured_manufacturer(viewPager);
    }

    private void initialize_flipper_image(View view) {
        viewFlipper = (ViewFlipper) view.findViewById(R.id.flipper_home);
        int[] images = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide3};

        for (int image: images){
            flipper_Images(image);
        }

    }

    public void flipper_Images(int image){
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(image);


        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);

    }

    public  void featured_manufacturer(final ViewPager viewPager){
        final ArrayList<Featured_Manufacturer_Class> featured_manufacturersArrayList= new ArrayList<>();

        AndroidNetworking.get("http://usrafinefood.se/tester2/crud.php")
                .addQueryParameter("action", "get_featured_manufacturer")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jo;
                        Featured_Manufacturer_Class s;

                        try {

                            for (int i=0;i< response.length();i++){
                                jo = response.getJSONObject(i);
                                int manufacturer_id = jo.getInt("manufacturer_id");
                                String name= jo.getString("name");
                                String featured_image = jo.getString("featured_image");

                                s= new Featured_Manufacturer_Class();
                                s.setManufacturer_id(manufacturer_id);
                                s.setName(name);
                                s.setFeatured_image(featured_image);
                                featured_manufacturersArrayList.add(s);

                            }
                            ViewPager_image_Adapter1 viewPagerAdapter = new ViewPager_image_Adapter1(getContext(), featured_manufacturersArrayList);
                            viewPager.setAdapter(viewPagerAdapter);

                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Goods Response But Java Can`t Parse Json It Received: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), "Unsuccessful : Error Is :" + anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("tag", anError.getMessage());
                    }



                });
    }

    private void initialize_Home_Items() {
        String cus_id = String.valueOf(SharedPrefManager.getInstance(getActivity()).get_user_ID());

        rv_Discount_Items.setHasFixedSize(true);
        lm_Discount_Items=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rv_Discount_Items.setLayoutManager(lm_Discount_Items);

        rv_Special_Items.setHasFixedSize(true);
        lm_Special_Items=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rv_Special_Items.setLayoutManager(lm_Special_Items);

        rv_Best_Selling_items.setHasFixedSize(true);
        lm_Best_Selling_Items=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rv_Best_Selling_items.setLayoutManager(lm_Best_Selling_Items);

        Retrieve_Discount_Items(rv_Discount_Items, cus_id);
        Retrieve_Special_Items(rv_Special_Items,cus_id);
        Retrieve_Best_Selling_Items(rv_Best_Selling_items,cus_id);

    }

    private void Retrieve_Discount_Items(final RecyclerView rv_items, String cus_id) {

        final ArrayList<Item_Class> items = new ArrayList<>();
        String lang_Name = "en";
        switch (SharedPrefManager.getInstance(activity).get_Lang()){
            case "ar":
                lang_Name="arabic";
                break;
            case "en":
                lang_Name = "english";
                break;
            case "sv":
                lang_Name="swedish";
                break;
            default:
                lang_Name = "english";
                break;
        }

        AndroidNetworking.get("http://usrafinefood.se/tester2/crud.php")
                .addQueryParameter("action", "get_itm_discount_det")
                .addQueryParameter("language_in", lang_Name)
                .addQueryParameter("cus_id",cus_id)
                .setPriority(Priority.HIGH)

                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {
                            JSONObject jo;
                            Item_Class s;

                            try {
                                if (response.length()>0) {
                                    for (int i = 0; i < response.length(); i++) {
                                        jo = response.getJSONObject(i);
                                        int product_id = jo.getInt("product_id");
                                        String product_model = jo.getString("model");
                                        int product_stock_status_id = jo.getInt("stock_status_id");
                                        String product_stock_status_name = jo.getString("stock_status_name");
                                        Double product_price = jo.getDouble("price");
                                        Double product_point_pris = jo.getDouble("point_pris");
                                        Double product_quantity = jo.getDouble("quantity");
                                        String product_def_image = jo.getString("def_image");
                                        int product_manufacturer_id = jo.getInt("manufacturer_id");
                                        String product_manufacturer_name = jo.getString("manufacturer_name");
                                        String product_manufacturer_image = jo.getString("manufacturer_image");
                                        int product_tax_class_id = jo.getInt("tax_class_id");
                                        String product_title_tax_class = jo.getString("title_tax_class");
                                        int product_tax_rate_id = jo.getInt("tax_rate_id");
                                        String product_tax_rate_name = jo.getString("tax_rate_name");
                                        Double product_tax_rate_value = jo.getDouble("tax_rate_value");
                                        Double product_weight = jo.getDouble("weight");
                                        int product_sort_order = jo.getInt("sort_order");
                                        int product_status = jo.getInt("status");
                                        String product_name = jo.getString("product_name");
                                        String product_description = jo.getString("product_description");
                                        String product_tag = jo.getString("product_tag");
                                        Double product_special_price = jo.getDouble("special_price");
                                        long end_special_price_per_sec = jo.getLong("end_special_price_per_sec");
                                        boolean is_new_product = jo.getBoolean("is_new_product");
                                        int days_for_availble = jo.getInt("days_for_availble");
                                        String is_discount_product = jo.getString("is_discount_product");
                                        Double discount_qty = jo.getDouble("discount_qty");
                                        Double discount_price = jo.getDouble("discount_price");
                                        Boolean is_cart = jo.getBoolean("is_cart");
                                        s = new Item_Class();
                                        s.setItem_id(product_id);
                                        s.setItem_model(product_model);
                                        s.setItem_stock_status_id(product_stock_status_id);
                                        s.setItem_stock_status_name(product_stock_status_name);
                                        s.setItem_price(product_price);
                                        s.setItem_point_pris(product_point_pris);
                                        s.setItem_quantity(product_quantity);
                                        s.setItem_def_image(product_def_image);
                                        s.setItem_manufacturer_id(product_manufacturer_id);
                                        s.setItem_manufacturer_name(product_manufacturer_name);
                                        s.setItem_manufacturer_image(product_manufacturer_image);
                                        s.setItem_tax_class_id(product_tax_class_id);
                                        s.setItem_title_tax_class(product_title_tax_class);
                                        s.setItem_tax_rate_id(product_tax_rate_id);
                                        s.setItem_tax_rate_name(product_tax_rate_name);
                                        s.setItem_tax_rate_value(product_tax_rate_value);
                                        s.setItem_weight(product_weight);
                                        s.setItem_sort_order(product_sort_order);
                                        s.setItem_status(product_status);
                                        s.setItem_name(product_name);
                                        s.setItem_description(product_description);
                                        s.setItem_tag(product_tag);
                                        s.setSpecial_price(product_special_price);
                                        s.setEnd_special_price_per_sec(end_special_price_per_sec);
                                        s.setIs_new_product(is_new_product);
                                        s.setDays_for_availble(days_for_availble);
                                        s.setIs_discount_product(is_discount_product);
                                        s.setDiscount_qty(discount_qty);
                                        s.setDiscount_price(discount_price);
                                        s.setIs_cart(is_cart);
                                        items.add(s);

                                    }
                                    rv_items.setAdapter(new MyAdapter_Discount_Items(activity, items, swiper));

                                }
                            } catch (JSONException e) {

                                e.printStackTrace();
                                Toast.makeText(activity, "Goods Response But Java Can`t Parse Json It Received: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(activity, "Unsuccessful : Error Is :" + anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("tag", anError.getMessage());
                    }

                });
    }

    private void Retrieve_Special_Items(final RecyclerView rv_items, String cus_id) {

        final ArrayList<Item_Class> items = new ArrayList<>();
        String lang_Name = "en";
        switch (SharedPrefManager.getInstance(activity).get_Lang()){
            case "ar":
                lang_Name="arabic";
                break;
            case "en":
                lang_Name = "english";
                break;
            case "sv":
                lang_Name="swedish";
                break;
            default:
                lang_Name = "english";
                break;
        }

        AndroidNetworking.get("http://usrafinefood.se/tester2/crud.php")
                .addQueryParameter("action", "get_itm_special_det")
                .addQueryParameter("language_in", lang_Name)
                .addQueryParameter("cus_id",cus_id)
                .setPriority(Priority.HIGH)

                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {
                            JSONObject jo;
                            Item_Class s;

                            try {
                                if (response.length()>0) {
                                    for (int i = 0; i < response.length(); i++) {
                                        jo = response.getJSONObject(i);
                                        int product_id = jo.getInt("product_id");
                                        String product_model = jo.getString("model");
                                        int product_stock_status_id = jo.getInt("stock_status_id");
                                        String product_stock_status_name = jo.getString("stock_status_name");
                                        Double product_price = jo.getDouble("price");
                                        Double product_point_pris = jo.getDouble("point_pris");
                                        Double product_quantity = jo.getDouble("quantity");
                                        String product_def_image = jo.getString("def_image");
                                        int product_manufacturer_id = jo.getInt("manufacturer_id");
                                        String product_manufacturer_name = jo.getString("manufacturer_name");
                                        String product_manufacturer_image = jo.getString("manufacturer_image");
                                        int product_tax_class_id = jo.getInt("tax_class_id");
                                        String product_title_tax_class = jo.getString("title_tax_class");
                                        int product_tax_rate_id = jo.getInt("tax_rate_id");
                                        String product_tax_rate_name = jo.getString("tax_rate_name");
                                        Double product_tax_rate_value = jo.getDouble("tax_rate_value");
                                        Double product_weight = jo.getDouble("weight");
                                        int product_sort_order = jo.getInt("sort_order");
                                        int product_status = jo.getInt("status");
                                        String product_name = jo.getString("product_name");
                                        String product_description = jo.getString("product_description");
                                        String product_tag = jo.getString("product_tag");
                                        Double product_special_price = jo.getDouble("special_price");
                                        long end_special_price_per_sec = jo.getLong("end_special_price_per_sec");
                                        boolean is_new_product = jo.getBoolean("is_new_product");
                                        int days_for_availble = jo.getInt("days_for_availble");
                                        String is_discount_product = jo.getString("is_discount_product");
                                        Double discount_qty = jo.getDouble("discount_qty");
                                        Double discount_price = jo.getDouble("discount_price");
                                        Boolean is_cart = jo.getBoolean("is_cart");
                                        s = new Item_Class();
                                        s.setItem_id(product_id);
                                        s.setItem_model(product_model);
                                        s.setItem_stock_status_id(product_stock_status_id);
                                        s.setItem_stock_status_name(product_stock_status_name);
                                        s.setItem_price(product_price);
                                        s.setItem_point_pris(product_point_pris);
                                        s.setItem_quantity(product_quantity);
                                        s.setItem_def_image(product_def_image);
                                        s.setItem_manufacturer_id(product_manufacturer_id);
                                        s.setItem_manufacturer_name(product_manufacturer_name);
                                        s.setItem_manufacturer_image(product_manufacturer_image);
                                        s.setItem_tax_class_id(product_tax_class_id);
                                        s.setItem_title_tax_class(product_title_tax_class);
                                        s.setItem_tax_rate_id(product_tax_rate_id);
                                        s.setItem_tax_rate_name(product_tax_rate_name);
                                        s.setItem_tax_rate_value(product_tax_rate_value);
                                        s.setItem_weight(product_weight);
                                        s.setItem_sort_order(product_sort_order);
                                        s.setItem_status(product_status);
                                        s.setItem_name(product_name);
                                        s.setItem_description(product_description);
                                        s.setItem_tag(product_tag);
                                        s.setSpecial_price(product_special_price);
                                        s.setEnd_special_price_per_sec(end_special_price_per_sec);
                                        s.setIs_new_product(is_new_product);
                                        s.setDays_for_availble(days_for_availble);
                                        s.setIs_discount_product(is_discount_product);
                                        s.setDiscount_qty(discount_qty);
                                        s.setDiscount_price(discount_price);
                                        s.setIs_cart(is_cart);
                                        items.add(s);

                                    }
                                    rv_items.setAdapter(new MyAdapter_Special_Items(activity, items, swiper));

                                }
                            } catch (JSONException e) {

                                e.printStackTrace();
                                Toast.makeText(activity, "Goods Response But Java Can`t Parse Json It Received: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(activity, "Unsuccessful : Error Is :" + anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("tag", anError.getMessage());
                    }

                });
    }

    private void Retrieve_Best_Selling_Items(final RecyclerView rv_items, String cus_id) {

        final ArrayList<Item_Class> items = new ArrayList<>();
        String lang_Name = "en";
        switch (SharedPrefManager.getInstance(activity).get_Lang()){
            case "ar":
                lang_Name="arabic";
                break;
            case "en":
                lang_Name = "english";
                break;
            case "sv":
                lang_Name="swedish";
                break;
            default:
                lang_Name = "english";
                break;
        }

        AndroidNetworking.get("http://usrafinefood.se/tester2/crud.php")
                .addQueryParameter("action", "get_itm_best_selling_det")
                .addQueryParameter("language_in", lang_Name)
                .addQueryParameter("cus_id",cus_id)
                .setPriority(Priority.HIGH)

                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {
                            JSONObject jo;
                            Item_Class s;

                            try {
                                if (response.length()>0) {
                                    for (int i = 0; i < response.length(); i++) {
                                        jo = response.getJSONObject(i);
                                        int product_id = jo.getInt("product_id");
                                        String product_model = jo.getString("model");
                                        int product_stock_status_id = jo.getInt("stock_status_id");
                                        String product_stock_status_name = jo.getString("stock_status_name");
                                        Double product_price = jo.getDouble("price");
                                        Double product_point_pris = jo.getDouble("point_pris");
                                        Double product_quantity = jo.getDouble("quantity");
                                        String product_def_image = jo.getString("def_image");
                                        int product_manufacturer_id = jo.getInt("manufacturer_id");
                                        String product_manufacturer_name = jo.getString("manufacturer_name");
                                        String product_manufacturer_image = jo.getString("manufacturer_image");
                                        int product_tax_class_id = jo.getInt("tax_class_id");
                                        String product_title_tax_class = jo.getString("title_tax_class");
                                        int product_tax_rate_id = jo.getInt("tax_rate_id");
                                        String product_tax_rate_name = jo.getString("tax_rate_name");
                                        Double product_tax_rate_value = jo.getDouble("tax_rate_value");
                                        Double product_weight = jo.getDouble("weight");
                                        int product_sort_order = jo.getInt("sort_order");
                                        int product_status = jo.getInt("status");
                                        String product_name = jo.getString("product_name");
                                        String product_description = jo.getString("product_description");
                                        String product_tag = jo.getString("product_tag");
                                        Double product_special_price = jo.getDouble("special_price");
                                        long end_special_price_per_sec = jo.getLong("end_special_price_per_sec");
                                        boolean is_new_product = jo.getBoolean("is_new_product");
                                        int days_for_availble = jo.getInt("days_for_availble");
                                        String is_discount_product = jo.getString("is_discount_product");
                                        Double discount_qty = jo.getDouble("discount_qty");
                                        Double discount_price = jo.getDouble("discount_price");
                                        Boolean is_cart = jo.getBoolean("is_cart");
                                        s = new Item_Class();
                                        s.setItem_id(product_id);
                                        s.setItem_model(product_model);
                                        s.setItem_stock_status_id(product_stock_status_id);
                                        s.setItem_stock_status_name(product_stock_status_name);
                                        s.setItem_price(product_price);
                                        s.setItem_point_pris(product_point_pris);
                                        s.setItem_quantity(product_quantity);
                                        s.setItem_def_image(product_def_image);
                                        s.setItem_manufacturer_id(product_manufacturer_id);
                                        s.setItem_manufacturer_name(product_manufacturer_name);
                                        s.setItem_manufacturer_image(product_manufacturer_image);
                                        s.setItem_tax_class_id(product_tax_class_id);
                                        s.setItem_title_tax_class(product_title_tax_class);
                                        s.setItem_tax_rate_id(product_tax_rate_id);
                                        s.setItem_tax_rate_name(product_tax_rate_name);
                                        s.setItem_tax_rate_value(product_tax_rate_value);
                                        s.setItem_weight(product_weight);
                                        s.setItem_sort_order(product_sort_order);
                                        s.setItem_status(product_status);
                                        s.setItem_name(product_name);
                                        s.setItem_description(product_description);
                                        s.setItem_tag(product_tag);
                                        s.setSpecial_price(product_special_price);
                                        s.setEnd_special_price_per_sec(end_special_price_per_sec);
                                        s.setIs_new_product(is_new_product);
                                        s.setDays_for_availble(days_for_availble);
                                        s.setIs_discount_product(is_discount_product);
                                        s.setDiscount_qty(discount_qty);
                                        s.setDiscount_price(discount_price);
                                        s.setIs_cart(is_cart);
                                        items.add(s);

                                    }
                                    rv_items.setAdapter(new MyAdapter_Best_Selling_Items(activity, items, swiper));

                                }
                            } catch (JSONException e) {

                                e.printStackTrace();
                                Toast.makeText(activity, "Goods Response But Java Can`t Parse Json It Received: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(activity, "Unsuccessful : Error Is :" + anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("tag", anError.getMessage());
                    }

                });
    }

    public void get_time_special_itm(final TextView txt_countdown_day, final TextView txt_countdown_hour, final TextView txt_countdown_min, final TextView txt_countdown_sec){
        final Long[] sss = {Long.valueOf(0)};

        AndroidNetworking.get("http://usrafinefood.se/tester2/crud.php")
                .addQueryParameter("action", "get_time_special_itm")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jo;
                        try {

                            for (int i=0;i< response.length();i++){
                                jo = response.getJSONObject(i);
                                START_TIME_IN_MILLIS= jo.getLong("end_special_price_per_sec")*1000;

                                updateCountDownText(START_TIME_IN_MILLIS,txt_countdown_day,txt_countdown_hour, txt_countdown_min,txt_countdown_sec);
                                startTimer(START_TIME_IN_MILLIS,txt_countdown_day,txt_countdown_hour, txt_countdown_min,txt_countdown_sec);

                            }
                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Goods Response But Java Can`t Parse Json It Received: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), "Unsuccessful : Error Is :" + anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("tag", anError.getMessage());
                    }
                });
    }

    private void startTimer(final long START_TIME_IN_MILLIS, final TextView txt_day, final TextView txt_hour, final TextView txt_min, final TextView txt_sec) {
        mTimeLeftinMillis=START_TIME_IN_MILLIS;
        mCuntDownTimer=new CountDownTimer(mTimeLeftinMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftinMillis = millisUntilFinished;
                updateCountDownText(mTimeLeftinMillis,txt_day,txt_hour,txt_min,txt_sec);
            }


            @Override
            public void onFinish() {

            }
        }.start();

        mTimerRunning=true;
    }

    private void updateCountDownText(long START_TIME_IN_MILLIS,TextView txt_day,TextView txt_hour,TextView txt_min,TextView txt_sec) {
      long seconds = START_TIME_IN_MILLIS / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        String time = days + ":" + hours % 24 + ":" + minutes % 60 + ":" + seconds % 60;
        String timeLeftFormatted=String.format(Locale.getDefault(),"%02d:%02d:%02d:%02d",days,hours % 24,minutes % 60,seconds % 60);
        String timeLeft_Days=String.format(Locale.getDefault(),"%02d",days);
        String timeLeft_Hours=String.format(Locale.getDefault(),"%02d",hours % 24);
        String timeLeft_Minutes=String.format(Locale.getDefault(),"%02d",minutes % 60);
        String timeLeft_Seconds=String.format(Locale.getDefault(),"%02d",seconds % 60);
        txt_day.setText(timeLeft_Days);
        txt_hour.setText(timeLeft_Hours);
        txt_min.setText(timeLeft_Minutes);
        txt_sec.setText(timeLeft_Seconds);

    }

    public static class MyViewHolder_Discount_Items extends RecyclerView.ViewHolder {
        ANImageView imageView;
        ImageView menu_imageView;
        TextView txt_sale,txt_new,txt_title,txt_discount_count,txt_discount_price,txt_original_price;
        public MyViewHolder_Discount_Items(View view, int viewType) {
            super(view);
            imageView = (ANImageView) view.findViewById(R.id.img_item_in_lv_discount_item);
            txt_sale=(TextView) view.findViewById(R.id.txt_isdiscountsale_in_lv_discount_item);
            txt_new=(TextView) view.findViewById(R.id.txt_is_new_product_in_lv_discount_item);
            txt_title=(TextView)view.findViewById(R.id.txt_title_item_in_lv_discount_item);
            txt_discount_count=(TextView)view.findViewById(R.id.txt_discount_count_in_lv_discount_item);
            txt_discount_price=(TextView)view.findViewById(R.id.txt_discount_price_in_lv_discount_item);
            txt_original_price=(TextView)view.findViewById(R.id.txt_original_price_in_lv_discount_item);
            menu_imageView=(ImageView)view.findViewById(R.id.imv_menu_in_lv_discount_item);

            imageView.setDefaultImageResId(R.mipmap.loading);
            imageView.setErrorImageResId(R.drawable.c);
        }

    }

    public class MyAdapter_Discount_Items extends RecyclerView.Adapter<MyViewHolder_Discount_Items> {
        private Context c;
        private ArrayList<Item_Class> items;
        private SwipeRefreshLayout swiper;

        public MyAdapter_Discount_Items(Context c, ArrayList<Item_Class> items, SwipeRefreshLayout swiper) {
            this.c = c;
            this.items = items;
            this.swiper = swiper;
        }

        @Override
        public MyViewHolder_Discount_Items onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(c).inflate(R.layout.listview_discount_items, parent, false);
            return new MyViewHolder_Discount_Items(v, viewType);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder_Discount_Items holder, final int position) {
            final Item_Class itm = items.get(position);
            if (itm.getIs_new_product()){
                holder.txt_new.setVisibility(View.VISIBLE);
            }else {
                holder.txt_new.setVisibility(View.GONE);
            }

            if (itm.getIs_discount_product().equals("TRUE")){
                holder.txt_sale.setVisibility(View.VISIBLE);
            }else {
                holder.txt_sale.setVisibility(View.GONE);
            }

            holder.imageView.setImageUrl("http://usrafinefood.se/tester2/image/" + itm.getItem_def_image());
            holder.txt_title.setText(itm.getItem_name().toString());
            holder.txt_discount_count.setText(String.format("%.2f",itm.getDiscount_qty()));
            holder.txt_discount_price.setText(String.format("%.2f",itm.getDiscount_price()));
            holder.txt_original_price.setText(String.format("%.2f",itm.getItem_price()));

            final int item_key=itm.getItem_id();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent (v.getContext(), Item_Card.class);
                    intent.putExtra("ItemID",item_key);
                    v.getContext().startActivity(intent);

                }
            });

            final PopupMenu popupMenu=new PopupMenu(c,holder.menu_imageView);

            final Menu menu=popupMenu.getMenu();
            popupMenu.getMenuInflater().inflate(R.menu.menu_item,menu);
            if (itm.getIs_cart()) {
                popupMenu.getMenu().findItem(R.id.add_to_cart).setTitle(R.string.remove_from_cart);
            }else {
                popupMenu.getMenu().findItem(R.id.add_to_cart).setTitle(R.string.add_to_cart);
            }

            holder.menu_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            showOptionsMenu(holder.menu_imageView,itm.getItem_id(),String.valueOf(itm.getIs_cart()).toUpperCase());
                            if (itm.getIs_cart()){
                                itm.setIs_cart(false);
//                                for (int posi=0; posi<bestSelling_Item_Class.size();posi++){
//                                    if (bestSelling_Item_Class.get(posi).getItem_id()==itm.getItem_id()){
//                                        bestSelling_Item_Class.get(posi).setIs_cart(false);
//                                        bestSelling_Item_Class.notifyAll();
//                                        bestSelling_Item_Class.clear();
//                                        initialize_RecyclerViews_Adapters();
//                                    }
//                                }
                            }else {
                                itm.setIs_cart(true);
                            }
                            notifyDataSetChanged();
                            return true;
                        }
                    });
                }
            });
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

        private void showOptionsMenu(final View view, final int product_id, final String is_delete) {
            AndroidNetworking.get("http://usrafinefood.se/tester2/crud.php")
                    .addQueryParameter("action", "add_remove_cart_item")
                    .addQueryParameter("is_delete", is_delete)
                    .addQueryParameter("cus_id", String.valueOf(SharedPrefManager.getInstance(getActivity()).get_user_ID()))
                    .addQueryParameter("product_id", String.valueOf(product_id))
                    .addQueryParameter("qty", "1")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsOkHttpResponse(new OkHttpResponseListener() {
                        @Override
                        public void onResponse(Response response) {
                            // do anything with response
                            if (response.isSuccessful()) {
                                if (Boolean.getBoolean(is_delete)) {
                                    Toast.makeText(view.getContext(), R.string.string_confrim_add_item_to_cart, Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(view.getContext(), R.string.string_confrim_delete_item_from_cart, Toast.LENGTH_SHORT).show();
                                }
                                swiper.setRefreshing(true);
                                fetchTimelineAsync(0);

//                                            swiper.setRefreshing(true);
//                                            fetchTimelineAsync(0);

                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(view.getContext(), "Unsuccessful : Error Is :" + anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
//                        return true;
//                        default:
//                            return false;
//                }
//            }
//        });
//        popupMenu.show();

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void clear() {
            items.clear();
            notifyDataSetChanged();
        }

        public void fetchTimelineAsync(int page) {
            clear();
            initialize_Home_Items();
        }
    }

    public static class MyViewHolder_Special_Items extends RecyclerView.ViewHolder {
        ANImageView imageView;
        ImageView menu_imageView;
        TextView txt_Old_Price,txt_Special_Price,txt_title_item,txt_precent,txt_sale,txt_new;
        public MyViewHolder_Special_Items(View view, int viewType) {
            super(view);
            imageView = (ANImageView) view.findViewById(R.id.img_special_item);
            txt_Special_Price = (TextView) view.findViewById(R.id.txt_new_price);

            txt_Old_Price= (TextView) view.findViewById(R.id.txt_old_price);
            txt_precent= (TextView) view.findViewById(R.id.txt_precent_value);
            txt_sale=(TextView) view.findViewById(R.id.txt_is_sale);
            txt_new=(TextView) view.findViewById(R.id.txt_is_new_product);

            txt_title_item=(TextView)view.findViewById(R.id.txt_title_item);
            menu_imageView=(ImageView)view.findViewById(R.id.imv_menu);
            imageView.setDefaultImageResId(R.mipmap.loading);
            imageView.setErrorImageResId(R.drawable.c);
        }

    }

    public class MyAdapter_Special_Items extends RecyclerView.Adapter<MyViewHolder_Special_Items> {
        private Context c;
        private ArrayList<Item_Class> items;
        private SwipeRefreshLayout swiper;

        public MyAdapter_Special_Items(Context c, ArrayList<Item_Class> items, SwipeRefreshLayout swiper) {
            this.c = c;
            this.items = items;
            this.swiper = swiper;
        }

        @Override
        public MyViewHolder_Special_Items onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(c).inflate(R.layout.listview_special_items, parent, false);
            return new MyViewHolder_Special_Items(v, viewType);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder_Special_Items holder, final int position) {
            final Item_Class itm = items.get(position);
            holder.txt_title_item.setText(itm.getItem_name().toString());
            holder.txt_Special_Price.setText(String.format("%.2f", itm.getSpecial_price()));
            holder.txt_Old_Price.setText(String.format("%.2f", itm.getItem_price()));
            holder.txt_Old_Price.setPaintFlags(holder.txt_Old_Price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            String d = String.format("%.0f", ((itm.getSpecial_price() / itm.getItem_price() - 1) * -1) * 100) + "%";
            if (itm.getIs_new_product()) {
                holder.txt_new.setVisibility(View.VISIBLE);
            } else {
                holder.txt_new.setVisibility(View.GONE);
            }

            if (itm.getIs_discount_product().equals("TRUE")) {
                holder.txt_sale.setVisibility(View.VISIBLE);
            } else {
                holder.txt_sale.setVisibility(View.GONE);
            }


            holder.txt_precent.setText(d);
            //holder.txt_precent.setText(String.format("%.1f",Double.toString(itm.getItem_price()/itm.getSpecial_price())) + "%");
            final int item_key = itm.getItem_id();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Item_Card.class);
                    intent.putExtra("ItemID", item_key);
                    v.getContext().startActivity(intent);

                }
            });

            holder.imageView.setImageUrl("http://usrafinefood.se/tester2/image/" + itm.getItem_def_image());
            final PopupMenu popupMenu=new PopupMenu(c,holder.menu_imageView);

            final Menu menu=popupMenu.getMenu();
            popupMenu.getMenuInflater().inflate(R.menu.menu_item,menu);
            if (itm.getIs_cart()) {
                popupMenu.getMenu().findItem(R.id.add_to_cart).setTitle(R.string.remove_from_cart);
            }else {
                popupMenu.getMenu().findItem(R.id.add_to_cart).setTitle(R.string.add_to_cart);
            }

            holder.menu_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            showOptionsMenu(holder.menu_imageView,itm.getItem_id(),String.valueOf(itm.getIs_cart()).toUpperCase());
                            if (itm.getIs_cart()){
                                itm.setIs_cart(false);
                            }else {
                                itm.setIs_cart(true);
                            }
                            notifyDataSetChanged();

                            return true;
                        }
                    });
                }
            });
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

        private void showOptionsMenu(final View view, final int product_id, final String is_delete) {
            AndroidNetworking.get("http://usrafinefood.se/tester2/crud.php")
                    .addQueryParameter("action", "add_remove_cart_item")
                    .addQueryParameter("is_delete", is_delete)
                    .addQueryParameter("cus_id", String.valueOf(SharedPrefManager.getInstance(getActivity()).get_user_ID()))
                    .addQueryParameter("product_id", String.valueOf(product_id))
                    .addQueryParameter("qty", "1")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsOkHttpResponse(new OkHttpResponseListener() {
                        @Override
                        public void onResponse(Response response) {
                            // do anything with response
                            if (response.isSuccessful()) {
                                if (Boolean.getBoolean(is_delete)) {
                                    Toast.makeText(view.getContext(), R.string.string_confrim_add_item_to_cart, Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(view.getContext(), R.string.string_confrim_delete_item_from_cart, Toast.LENGTH_SHORT).show();
                                }
                                swiper.setRefreshing(true);
                                fetchTimelineAsync(0);

//                                            swiper.setRefreshing(true);
//                                            fetchTimelineAsync(0);

                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(view.getContext(), "Unsuccessful : Error Is :" + anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
//                        return true;
//                        default:
//                            return false;
//                }
//            }
//        });
//        popupMenu.show();

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void clear() {
            items.clear();
            notifyDataSetChanged();
        }

        public void fetchTimelineAsync(int page) {
            clear();
            initialize_Home_Items();
        }
    }

    public static class MyViewHolder_Best_Selling_Items extends RecyclerView.ViewHolder {
        ANImageView imageView;
        ImageView menu_imageView;
        TextView txt_sale,txt_new;
        TextView txt_normal_price,txt_Old_Price,txt_Special_Price,txt_title_item,txt_precent,txt_discount_count,txt_discount_price,txt_original_price;
        MenuItem menuItem;

        public MyViewHolder_Best_Selling_Items(View view, int viewType) {
            super(view);
            menuItem=view.findViewById(R.id.add_to_cart);
            if (viewType==1) { //Discount Item_Class
                imageView = (ANImageView) view.findViewById(R.id.img_item_in_lv_discount_item);
                txt_sale=(TextView) view.findViewById(R.id.txt_isdiscountsale_in_lv_discount_item);
                txt_new=(TextView) view.findViewById(R.id.txt_is_new_product_in_lv_discount_item);
                txt_title_item=(TextView)view.findViewById(R.id.txt_title_item_in_lv_discount_item);
                txt_discount_count=(TextView)view.findViewById(R.id.txt_discount_count_in_lv_discount_item);
                txt_discount_price=(TextView)view.findViewById(R.id.txt_discount_price_in_lv_discount_item);
                txt_original_price=(TextView)view.findViewById(R.id.txt_original_price_in_lv_discount_item);
                menu_imageView=(ImageView)view.findViewById(R.id.imv_menu_in_lv_discount_item);
                imageView.setDefaultImageResId(R.mipmap.loading);
                imageView.setErrorImageResId(R.drawable.c);

            }else if (viewType==0){ //Specail Item_Class
                imageView = (ANImageView) view.findViewById(R.id.img_special_item);
                txt_Special_Price = (TextView) view.findViewById(R.id.txt_new_price);
                txt_title_item=(TextView)view.findViewById(R.id.txt_title_item);
                txt_Old_Price= (TextView) view.findViewById(R.id.txt_old_price);
                txt_precent= (TextView) view.findViewById(R.id.txt_precent_value);
                txt_sale=(TextView) view.findViewById(R.id.txt_is_sale);
                txt_new=(TextView) view.findViewById(R.id.txt_is_new_product);
                menu_imageView=(ImageView)view.findViewById(R.id.imv_menu);
                imageView.setDefaultImageResId(R.mipmap.loading);
                imageView.setErrorImageResId(R.drawable.c);
            }else {
                imageView = (ANImageView) view.findViewById(R.id.img_normal_item_normal_items);
                txt_title_item=(TextView)view.findViewById(R.id.txt_title_item_normal_items);
                txt_normal_price= (TextView) view.findViewById(R.id.txt_price_normal_items);
                txt_sale=(TextView) view.findViewById(R.id.txt_is_sale_normal_items);
                txt_new=(TextView) view.findViewById(R.id.txt_is_new_product_normal_items);
                menu_imageView=(ImageView)view.findViewById(R.id.imv_menu_normal_items);
                imageView.setDefaultImageResId(R.mipmap.loading);
                imageView.setErrorImageResId(R.drawable.c);

            }

        }

    }

    public class MyAdapter_Best_Selling_Items extends RecyclerView.Adapter<MyViewHolder_Best_Selling_Items> {
        private Context c;
        private ArrayList<Item_Class> items;
        private final static int Special_VIEW = 0;
        private final static int Discount_VIEW = 1;
        private final static int NORMAL_VIEW=2;

        private SwipeRefreshLayout swiper;

        public MyAdapter_Best_Selling_Items(Context c, ArrayList<Item_Class> items, SwipeRefreshLayout swiper) {
            this.c = c;
            this.items = items;
            this.swiper = swiper;
        }

        @Override
        public MyViewHolder_Best_Selling_Items onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            if (viewType==1){
                v = LayoutInflater.from(c).inflate(R.layout.listview_discount_items, parent, false);
            }else if(viewType==0) {
                v = LayoutInflater.from(c).inflate(R.layout.listview_special_items, parent, false);
            }else {
                v = LayoutInflater.from(c).inflate(R.layout.listview_normal_items, parent, false);
            }

            return new MyViewHolder_Best_Selling_Items(v,viewType);        }
        @Override
        public int getItemViewType(int position) {

            if (items.get(position).getEnd_special_price_per_sec() > 0 )
            {
                return  Special_VIEW;
            }else if(items.get(position).getIs_discount_product().toUpperCase().equals("TRUE")){
                return  Discount_VIEW;
            }else {
                return NORMAL_VIEW;
            }
        }

        @Override
        public void onBindViewHolder(final MyViewHolder_Best_Selling_Items holder, final int position) {
            switch (holder.getItemViewType()){
                case 1://Discount items
                    final Item_Class itm = items.get(position);
                    if (itm.getIs_new_product()){
                        holder.txt_new.setVisibility(View.VISIBLE);
                    }else {
                        holder.txt_new.setVisibility(View.GONE);
                    }

                    if (Boolean.getBoolean(items.get(position).getIs_discount_product())){
                        holder.txt_sale.setVisibility(View.VISIBLE);
                    }else {
                        holder.txt_sale.setVisibility(View.GONE);
                    }

                    holder.imageView.setImageUrl("http://usrafinefood.se/tester2/image/" + itm.getItem_def_image());
                    holder.txt_title_item.setText(itm.getItem_name().toString());
                    holder.txt_discount_count.setText(String.format("%.2f",itm.getDiscount_qty()));
                    holder.txt_discount_price.setText(String.format("%.2f",itm.getDiscount_price()));
                    holder.txt_original_price.setText(String.format("%.2f",itm.getItem_price()));

                    final int  item_key=itm.getItem_id();
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent (v.getContext(), Item_Card.class);
                            intent.putExtra("ItemID",item_key);
                            v.getContext().startActivity(intent);
                        }
                    });


                    final PopupMenu popupMenu1=new PopupMenu(c,holder.menu_imageView);

                    final Menu menu1=popupMenu1.getMenu();
                    popupMenu1.getMenuInflater().inflate(R.menu.menu_item,menu1);
                    if (itm.getIs_cart()) {
                        popupMenu1.getMenu().findItem(R.id.add_to_cart).setTitle(R.string.remove_from_cart);
                    }else {
                        popupMenu1.getMenu().findItem(R.id.add_to_cart).setTitle(R.string.add_to_cart);
                    }

                    holder.menu_imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupMenu1.show();
                            popupMenu1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    showOptionsMenu(holder.menu_imageView,itm.getItem_id(),String.valueOf(itm.getIs_cart()).toUpperCase());
                                    if (itm.getIs_cart()){
                                        itm.setIs_cart(false);
                                    }else {
                                        itm.setIs_cart(true);
                                    }
                                    notifyDataSetChanged();
                                    return true;
                                }
                            });

                        }
                    });
                    break;
                case 0: //Special items
                    final Item_Class item = items.get(position);
                    holder.txt_title_item.setText(item.getItem_name().toString());
                    holder.txt_Special_Price.setText(String.format("%.2f",item.getSpecial_price()));
                    holder.txt_Old_Price.setText(String.format("%.2f",item.getItem_price()));
                    holder.txt_Old_Price.setPaintFlags(holder.txt_Old_Price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    String d=String.format("%.0f",((item.getSpecial_price()/item.getItem_price()-1)*-1)*100) + "%";
                    if (item.getIs_new_product()){
                        holder.txt_new.setVisibility(View.VISIBLE);
                    }else {
                        holder.txt_new.setVisibility(View.GONE);
                    }

                    if (item.getIs_discount_product().equals("TRUE")){
                        holder.txt_sale.setVisibility(View.VISIBLE);
                    }else {
                        holder.txt_sale.setVisibility(View.GONE);
                    }

                    holder.txt_precent.setText(d);
                    final PopupMenu popupMenu=new PopupMenu(c,holder.menu_imageView);

                    final Menu menu=popupMenu.getMenu();
                    popupMenu.getMenuInflater().inflate(R.menu.menu_item,menu);
                    if (item.getIs_cart()) {
                        popupMenu.getMenu().findItem(R.id.add_to_cart).setTitle(R.string.remove_from_cart);
                    }else {
                        popupMenu.getMenu().findItem(R.id.add_to_cart).setTitle(R.string.add_to_cart);
                    }

                    holder.menu_imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupMenu.show();
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem itemm) {
                                    showOptionsMenu(holder.menu_imageView,item.getItem_id(),String.valueOf(item.getIs_cart()).toUpperCase());
                                    if (item.getIs_cart()){
                                        item.setIs_cart(false);
                                    }else {
                                        item.setIs_cart(true);
                                    }
                                    return true;
                                }
                            });

                        }
                    });
                    final int item_key1 = item.getItem_id();
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent (v.getContext(), Item_Card.class);
                            intent.putExtra("ItemID",item_key1);
                            v.getContext().startActivity(intent);

                        }
                    });

                    holder.imageView.setImageBitmap(null);
                    //holder.txt_precent.setText(String.format("%.1f",Double.toString(itm.getItem_price()/itm.getSpecial_price())) + "%");
                    holder.imageView.setImageUrl("http://usrafinefood.se/tester2/image/" + item.getItem_def_image());
                    break;
                case 2:
                    final Item_Class normal_item = items.get(position);
                    holder.txt_title_item.setText(normal_item.getItem_name().toString());
                    holder.txt_normal_price.setText(String.format("%.2f",normal_item.getItem_price()));

                    if (normal_item.getIs_new_product()){
                        holder.txt_new.setVisibility(View.VISIBLE);
                    }else {
                        holder.txt_new.setVisibility(View.GONE);
                    }

                    if (normal_item.getIs_discount_product().equals("TRUE")){
                        holder.txt_sale.setVisibility(View.VISIBLE);
                    }else {
                        holder.txt_sale.setVisibility(View.GONE);
                    }
                    final PopupMenu popupMenu2=new PopupMenu(c,holder.menu_imageView);

                    final Menu menu2=popupMenu2.getMenu();
                    popupMenu2.getMenuInflater().inflate(R.menu.menu_item,menu2);
                    if (normal_item.getIs_cart()) {
                        popupMenu2.getMenu().findItem(R.id.add_to_cart).setTitle(R.string.remove_from_cart);
                    }else {
                        popupMenu2.getMenu().findItem(R.id.add_to_cart).setTitle(R.string.add_to_cart);
                    }

                    holder.menu_imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupMenu2.show();
                            popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    showOptionsMenu(holder.menu_imageView,normal_item.getItem_id(),String.valueOf(normal_item.getIs_cart()).toUpperCase());
                                    if (normal_item.getIs_cart()){
                                        normal_item.setIs_cart(false);
                                    }else {
                                        normal_item.setIs_cart(true);
                                    }
                                    return true;
                                }
                            });

                        }
                    });
                    final int item_key2 = normal_item.getItem_id();
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent (v.getContext(), Item_Card.class);
                            intent.putExtra("ItemID",item_key2);
                            v.getContext().startActivity(intent);

                        }
                    });

                    holder.imageView.setImageUrl("http://usrafinefood.se/tester2/image/" + normal_item.getItem_def_image());
                    break;
            }
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

        private void showOptionsMenu(final View view, final int product_id, final String is_delete) {
            AndroidNetworking.get("http://usrafinefood.se/tester2/crud.php")
                    .addQueryParameter("action", "add_remove_cart_item")
                    .addQueryParameter("is_delete", is_delete)
                    .addQueryParameter("cus_id", String.valueOf(SharedPrefManager.getInstance(getActivity()).get_user_ID()))
                    .addQueryParameter("product_id", String.valueOf(product_id))
                    .addQueryParameter("qty", "1")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsOkHttpResponse(new OkHttpResponseListener() {
                        @Override
                        public void onResponse(Response response) {
                            // do anything with response
                            if (response.isSuccessful()) {
                                if (Boolean.getBoolean(is_delete)) {
                                    Toast.makeText(view.getContext(), R.string.string_confrim_add_item_to_cart, Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(view.getContext(), R.string.string_confrim_delete_item_from_cart, Toast.LENGTH_SHORT).show();
                                }
                                swiper.setRefreshing(true);
                                fetchTimelineAsync(0);

//                                            swiper.setRefreshing(true);
//                                            fetchTimelineAsync(0);

                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(view.getContext(), "Unsuccessful : Error Is :" + anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
//                        return true;
//                        default:
//                            return false;
//                }
//            }
//        });
//        popupMenu.show();

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void clear() {
            items.clear();
            notifyDataSetChanged();
        }

        public void fetchTimelineAsync(int page) {
            clear();
            initialize_Home_Items();
        }
    }

}


class ViewPager_image_Adapter1 extends PagerAdapter {

//    Activity activity;
//    String[] images;
//    LayoutInflater inflater;
//
//    public ViewPagerAdapter(Activity activity, String[] images) {
//        this.activity = activity;
//        this.images = images;
//    }
//
//    @Override
//
//
//    public int getCount() {
//        return images.length;
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view==object;
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View itemview = inflater.inflate(R.layout.viewpager_featured_brands, container, false);
//
//        ImageView image;
//
//        image = (ImageView) itemview.findViewById(R.id.imv_featured_brands);
//
//
//        DisplayMetrics dis = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(dis);
//        int height = dis.heightPixels;
//        int width = dis.widthPixels;
//        image.setMinimumHeight(height);
//        image.setMaxWidth(width);
//
//        try
//        {
//            Picasso.with(activity.getApplicationContext())
//                    .load(images[position])
//                    .placeholder(R.mipmap.ic_launcher)
//                    .error(R.mipmap.ic_launcher)
//                    .into(image);
//        }
//        catch (Exception ex)
//        {
//
//        }
//        container.addView(itemview);
//        return itemview;
//    }

    Context mContext;
    String[] mImages;
    ArrayList<Featured_Manufacturer_Class> mfeatured_manufacturerArrayList;
    LayoutInflater mLayoutInflater;
    int[] mResources = {
            R.drawable.slide1,
            R.drawable.slide2,
            R.drawable.slide3
    };
    public ViewPager_image_Adapter1(Context context,ArrayList<Featured_Manufacturer_Class> featured_manufacturer)   {
        mContext = context;
//        mImages=images;
        mfeatured_manufacturerArrayList=featured_manufacturer;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mfeatured_manufacturerArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }
    private String [] imagess={
            "http://usrafinefood.se/tester2/image/catalog/featuredbrands/slide1.jpg",
            "http://usrafinefood.se/tester2/image/catalog/featuredbrands/slide2.jpg",
            "http://usrafinefood.se/tester2/image/catalog/featuredbrands/slide3.jpg"
    };
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.viewpager_featured_brands, container, false);

//        Featured_Manufacturer_Class featured_manufacturer = mfeatured_manufacturerArrayList.get(position);
//        ImageView imageView = (ImageView) itemView.findViewById(R.id.imv_featured_brands);
//        imageView.setImageResource(mResources[position]);
        ANImageView an_imageView;
        an_imageView = (ANImageView) itemView.findViewById(R.id.an_imv_featured_brands);
//        an_imageView.setDefaultImageResId(R.drawable.w);
//        an_imageView.setErrorImageResId(R.drawable.c);
//        an_imageView.setImageResource(mResources[position]);
        an_imageView.setImageUrl("http://usrafinefood.se/tester2/image/catalog/brands/mipmap-" + SharedPrefManager.getInstance(mContext).get_Screen_Size() +"/"+mfeatured_manufacturerArrayList.get(position).getFeatured_image().toString());

//        an_imageView.setImageResource(R.drawable.layout_corners_image);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}

