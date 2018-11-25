package se.aljood.aljoodsql.SQL.items.Item_Card;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import java.util.List;
import java.util.Locale;

import se.aljood.aljoodsql.R;
import se.aljood.aljoodsql.SQL.items.Item_Images_Class;
import se.aljood.aljoodsql.SQL.items.Item_Class;
import se.aljood.aljoodsql.SQL.Module.MenuItemClickListener;
import se.aljood.aljoodsql.SQL.Module.MySQLClient;
import se.aljood.aljoodsql.SQL.Module.SharedPrefManager;
import se.aljood.aljoodsql.SQL.Module.WrappingViewPager;
import se.aljood.aljoodsql.SQL.items.Images_View_Activity;
import se.aljood.aljoodsql.SQL.items.Item_Card.fragments.Details_fragment;
import se.aljood.aljoodsql.SQL.items.Item_Card.fragments.Overview_fragment;

public class Item_Card extends AppCompatActivity {
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    private int item_key;
    private ArrayList<Item_Class> one_item = null;
    Context context = this;
    RecyclerView rv_related_products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_card);
        item_key=getIntent().getExtras().getInt("ItemID");

        get_item_details(item_key);

        initialize_Related_Product();
    }

    private void initialize_Related_Product() {
        rv_related_products=(RecyclerView) findViewById(R.id.rv_related_products_item_card);
        rv_related_products.setHasFixedSize(true);
        MySQLClient mySQLClient=new MySQLClient(context);
        mySQLClient.retrive_itm_details(rv_related_products,"get_related_itm_det",item_key);

    }

    public static class MyViewHolder_related_products extends RecyclerView.ViewHolder{

        ANImageView imageView;
        ImageView menu_imageView;

        TextView txt_sale,txt_new;

        TextView txt_normal_price,txt_Old_Price,txt_Special_Price,txt_title_item,txt_precent,txt_discount_count,txt_discount_price,txt_original_price;

        TextView txt_countdown_day,txt_countdown_hour,txt_countdown_min,txt_countdown_sec;
        long START_TIME_IN_MILLIS;

        public MyViewHolder_related_products(View view,int viewType){
            super(view);
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
                imageView = (ANImageView) view.findViewById(R.id.img_special_items_with_c_d);
                txt_Special_Price = (TextView) view.findViewById(R.id.txt_new_price_special_items_with_c_d);
                txt_title_item=(TextView)view.findViewById(R.id.txt_title_item_special_items_with_c_d);
                txt_Old_Price= (TextView) view.findViewById(R.id.txt_old_price_special_items_with_c_d);
                txt_precent= (TextView) view.findViewById(R.id.txt_precent_value_special_items_with_c_d);
                txt_sale=(TextView) view.findViewById(R.id.txt_is_sale_special_items_with_c_d);
                txt_new=(TextView) view.findViewById(R.id.txt_is_new_product_special_items_with_c_d);
                menu_imageView=(ImageView)view.findViewById(R.id.imv_menu_special_items_with_c_d);

                txt_countdown_day=(TextView) view.findViewById(R.id.txt_countdown_day_special_items_with_c_d);
                txt_countdown_hour=(TextView) view.findViewById(R.id.txt_countdown_hours_special_items_with_c_d);
                txt_countdown_min=(TextView) view.findViewById(R.id.txt_countdown_minutes_special_items_with_c_d);
                txt_countdown_sec=(TextView) view.findViewById(R.id.txt_countdown_seconds_special_items_with_c_d);

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

    public class MyAdapter_related_product extends RecyclerView.Adapter<MyViewHolder_related_products> {
        private Context c;
        private ArrayList<Item_Class> items;
        private final static int Special_VIEW = 0;
        private final static int Discount_VIEW = 1;
        private final static int NORMAL_VIEW=2;
        public MyAdapter_related_product(Context c, ArrayList<Item_Class> items){
            this.c = c;
            this.items= items;
        }

        @Override
        public MyViewHolder_related_products onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;


            if (viewType==1){
                v = LayoutInflater.from(c).inflate(R.layout.listview_discount_items, parent, false);
            }else if(viewType==0) {
                v = LayoutInflater.from(c).inflate(R.layout.listview_special_items_with_count_down, parent, false);
            }else {
                v = LayoutInflater.from(c).inflate(R.layout.listview_normal_items, parent, false);
            }

            return new MyViewHolder_related_products(v,viewType);
        }
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
        private CountDownTimer mCuntDownTimer;
        private boolean mTimerRunning;
        private long mTimeLeftinMillis ;


        @Override
        public void onBindViewHolder(final MyViewHolder_related_products holder, int position) {

            switch (holder.getItemViewType()){
                case 1://Discount items
                    Item_Class itm = items.get(position);
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
                    holder.menu_imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showOptionsMenu(holder.menu_imageView);
                        }
                    });

                    break;
                case 0: //Special items
                    Item_Class item = items.get(position);
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
                    holder.menu_imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showOptionsMenu(holder.menu_imageView);
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

                    holder.START_TIME_IN_MILLIS=item.getEnd_special_price_per_sec()*1000;
                    updateCountDownText(holder.START_TIME_IN_MILLIS,holder.txt_countdown_day,holder.txt_countdown_hour, holder.txt_countdown_min,holder.txt_countdown_sec);
                    startTimer(holder.START_TIME_IN_MILLIS,holder.txt_countdown_day,holder.txt_countdown_hour, holder.txt_countdown_min,holder.txt_countdown_sec);

                    break;
                case 2:
                    Item_Class normal_item = items.get(position);
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
                    holder.menu_imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showOptionsMenu(holder.menu_imageView);
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

        }
        private void startTimer(final long START_TIME_IN_MILLIS, final TextView txt_day, final TextView txt_hour, final TextView txt_min, final TextView txt_sec) {
            mTimeLeftinMillis=START_TIME_IN_MILLIS;
            mCuntDownTimer=new CountDownTimer(mTimeLeftinMillis,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimeLeftinMillis = millisUntilFinished;
                    updateCountDownText(START_TIME_IN_MILLIS,txt_day,txt_hour,txt_min,txt_sec);
                }


                @Override
                public void onFinish() {

                }
            }.start();

            mTimerRunning=true;
        }

        private void updateCountDownText(long START_TIME_IN_MILLIS,TextView txt_day,TextView txt_hour,TextView txt_min,TextView txt_sec) {
            long seconds = mTimeLeftinMillis / 1000;
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

        private void showOptionsMenu(View view) {
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_item, popup.getMenu());

            popup.setOnMenuItemClickListener(new MenuItemClickListener());
            popup.show();
            }
        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private void get_item_details(int item_key) {


        String lang_Name = "en";
        switch (SharedPrefManager.getInstance(this).get_Lang()){
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
                .addQueryParameter("action", "get_one_itm_det")
                .addQueryParameter("language_in", lang_Name)
                .addQueryParameter("product_id", String.valueOf(item_key))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jo;
                        Item_Class s;

                        try {

                            for (int i=0;i< response.length();i++){
                                jo = response.getJSONObject(i);
                                int product_id = jo.getInt("product_id");
                                String product_model= jo.getString("model");
                                int product_stock_status_id = jo.getInt("stock_status_id");
                                String product_stock_status_name= jo.getString("stock_status_name");
                                Double product_price= jo.getDouble("price");
                                Double product_point_pris= jo.getDouble("point_pris");
                                Double product_quantity = jo.getDouble("quantity");
                                String product_def_image= jo.getString("def_image");
                                int product_manufacturer_id= jo.getInt("manufacturer_id");
                                String product_manufacturer_name= jo.getString("manufacturer_name");
                                String product_manufacturer_image = jo.getString("manufacturer_image");
                                int product_tax_class_id= jo.getInt("tax_class_id");
                                String product_title_tax_class= jo.getString("title_tax_class");
                                int product_tax_rate_id= jo.getInt("tax_rate_id");
                                String product_tax_rate_name= jo.getString("tax_rate_name");
                                Double product_tax_rate_value= jo.getDouble("tax_rate_value");
                                Double product_weight= jo.getDouble("weight");
                                int product_sort_order= jo.getInt("sort_order");
                                int product_status= jo.getInt("status");
                                String product_name= jo.getString("product_name");
                                String product_description= jo.getString("product_description");
                                String product_tag = jo.getString("product_tag");
                                Double product_special_price= jo.getDouble("special_price");
                                long end_special_price_per_sec=jo.getLong("end_special_price_per_sec");
                                boolean is_new_product=jo.getBoolean("is_new_product");
                                int days_for_availble=jo.getInt("days_for_availble");
                                String is_discount_product=jo.getString("is_discount_product");
                                int category_id = jo.getInt("category_id");
                                String category_name = jo.getString("category_name");
                                int count_rating = jo.getInt("count_rating");
                                float avg_rating = (float) jo.getDouble("avg_rating");
                                int attribute_id = jo.getInt("attribute_id");
                                String attribute_title = jo.getString("attribute_title");
                                String attribute_value = jo.getString("attribute_value");
                                Double discount_qty = jo.getDouble("discount_qty");
                                Double discount_price = jo.getDouble("discount_price");

                                s= new Item_Class();
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
                                s.setCategory_id(category_id);
                                s.setCategory_name(category_name);
                                s.setCount_rating(count_rating);
                                s.setAvg_rating(avg_rating);
                                s.setAttribute_id(attribute_id);
                                s.setAttribute_title(attribute_title);
                                s.setAttribute_value(attribute_value);
                                s.setDiscount_qty(discount_qty);
                                s.setDiscount_price(discount_price);

                                one_item=new ArrayList<>();
                                one_item.add(s);
                                initialize_form();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Goods Response But Java Can`t Parse Json It Received: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Unsuccessful : Error Is :" + anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("tag", anError.getMessage());
                    }
                });

    }

    Button btn_plus_qty,btn_min_qty;
    TextView txt_qty;
    private void initialize_form() {

        TextView txt_category_name=(TextView) findViewById(R.id.txt_category_name_item_card);
        txt_category_name.setText(one_item.get(0).getCategory_name());

        TextView txt_product_name=(TextView) findViewById(R.id.txt_product_name_item_card);
        txt_product_name.setText(one_item.get(0).getItem_name());

        RatingBar ratingBar = (RatingBar) findViewById(R.id.rating_bar_item_card);
        ratingBar.setRating(one_item.get(0).getAvg_rating());

        TextView txt_count_rating = (TextView) findViewById(R.id.txt_count_rating_bar_item_card);
        int in =one_item.get(0).getCount_rating();
        txt_count_rating.setText("(" + String.valueOf(in) + ")");

        TextView txt_stock_status_name = (TextView) findViewById(R.id.txt_stock_status_name_item_card);
        txt_stock_status_name.setText(one_item.get(0).getItem_stock_status_name());

        viewPager = (ViewPager) findViewById(R.id.viewPager_images_item_card);

        item_images(viewPager,one_item);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots_images_item_card);

        TextView txt_price = (TextView) findViewById(R.id.txt_price_item_card);
        txt_price.setText(String.format("%.2f",  one_item.get(0).getItem_price()));
        if (one_item.get(0).getEnd_special_price_per_sec()>0 || one_item.get(0).getIs_discount_product().equals("TRUE")){
            txt_price.setPaintFlags(txt_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }

        LinearLayout del_point_price_item_card = (LinearLayout) findViewById(R.id.del_point_price_item_card);
        if (one_item.get(0).getItem_point_pris() == 0){
            del_point_price_item_card.setVisibility(View.GONE);
        }else {
            TextView txt_point_price = (TextView) findViewById(R.id.txt_point_price_item_card);
            txt_point_price.setText(String.format("%.2f",one_item.get(0).getItem_point_pris()));
        }


        WrappingViewPager viewPagerrr = (WrappingViewPager) findViewById(R.id.viewpagerrr);
        Tabs_setupViewPager(viewPagerrr,one_item);

        // Set Tabs inside Toolbar

        RelativeLayout relativeLayout_sale=(RelativeLayout) findViewById(R.id.del_discount_inom_del_4_item_card);
        RelativeLayout relativeLayout_special=(RelativeLayout) findViewById(R.id.del_special_inom_del_4_item_card);
        TextView txt_special_price=(TextView) findViewById(R.id.txt_special_price_item_card);

        if (one_item.get(0).getEnd_special_price_per_sec() > 0){
            String d=String.format("%.0f",((one_item.get(0).getSpecial_price()/one_item.get(0).getItem_price()-1)*-1)*100) + "%";
            relativeLayout_special.setVisibility(View.VISIBLE);
            txt_special_price.setText(String.format("%.2f",one_item.get(0).getSpecial_price()));
        }else {
            relativeLayout_special.setVisibility(View.GONE);
        }
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs1);
        tabs.setupWithViewPager(viewPagerrr);
        if (one_item.get(0).getIs_discount_product().equals("TRUE")){
            relativeLayout_sale.setVisibility(View.VISIBLE);
            TextView txt_discount_count=(TextView) findViewById(R.id.txt_discount_count_item_card);
            TextView txt_discount_price=(TextView) findViewById(R.id.txt_discount_price_item_card);

            txt_discount_count.setText(String.format("%.1f",one_item.get(0).getDiscount_qty()) + " FOR:");
            txt_discount_price.setText(String.format("%.2f",one_item.get(0).getDiscount_price()*one_item.get(0).getDiscount_qty()) +":-");

        }else {
            relativeLayout_sale.setVisibility(View.GONE);
        }

        btn_min_qty=(Button) findViewById(R.id.btn_minus_qty_item_card);
        btn_plus_qty=(Button)findViewById(R.id.btn_plus_qty_item_card);
        txt_qty = (TextView) findViewById(R.id.txt_qty_value_item_card);


        btn_plus_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (Integer.parseInt(txt_qty.getText().toString())>0){
                    txt_qty.setText(String.valueOf(Integer.parseInt(txt_qty.getText().toString())+1));
//                }else if ()
            }
        });
        btn_min_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(txt_qty.getText().toString())>0){
                    txt_qty.setText(String.valueOf(Integer.parseInt(txt_qty.getText().toString())-1));
                }else if (Integer.parseInt(txt_qty.getText().toString())<=1) {
                    txt_qty.setText("1");
                    }
                }

        });
    }

    public  void item_images(final ViewPager viewPager, final ArrayList<Item_Class> itm){
        final ArrayList<Item_Images_Class> item_imagesArrayList= new ArrayList<>();
        AndroidNetworking.get("http://usrafinefood.se/tester2/crud.php")
                .addQueryParameter("action", "get_one_itm_images")
                .addQueryParameter("product_id", String.valueOf(item_key))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jo;
                        Item_Images_Class s;

                        try {
                            s= new Item_Images_Class();
                            int def_product_id = 22;
                            int def_product_image_id=2233;
                            String def_src_images= one_item.get(0).getItem_def_image().toString();
                            int def_sort_image= 0;
                            s.setProduct_id(def_product_id);
                            s.setProduct_image_id(def_product_image_id);
                            s.setSrc_image(def_src_images);
                            s.setSort_image(def_sort_image);
                            item_imagesArrayList.add(s);

                            for (int i=0;i< response.length();i++){
                                jo = response.getJSONObject(i);
                                int product_id = jo.getInt("product_id");
                                int product_image_id=jo.getInt("product_image_id");
                                String src_images= jo.getString("images");
                                int sort_image= jo.getInt("sort_image")+1;

                                s= new Item_Images_Class();
                                s.setProduct_id(product_id);
                                s.setProduct_image_id(product_image_id);
                                s.setSrc_image(src_images);
                                s.setSort_image(sort_image);
                                item_imagesArrayList.add(s);

                            }

                            initialize_dots_images(item_imagesArrayList.size());
                            ViewPager_images_itm_Adapter viewPager_images_itm_adapter = new ViewPager_images_itm_Adapter(context, item_imagesArrayList,itm,item_key);
                            viewPager.setAdapter(viewPager_images_itm_adapter);

                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Goods Response But Java Can`t Parse Json It Received: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }


                    @Override
                    public void onError(ANError anError) {
                        Item_Images_Class ss;
                        ss= new Item_Images_Class();
                        int def_product_id = 22;
                        int def_product_image_id=2233;
                        String def_src_images= one_item.get(0).getItem_def_image().toString();
                        int def_sort_image= 0;

                        ss.setProduct_id(def_product_id);
                        ss.setProduct_image_id(def_product_image_id);
                        ss.setSrc_image(def_src_images);
                        ss.setSort_image(def_sort_image);

                        item_imagesArrayList.add(ss);
//            initialize_dots_images(item_imagesArrayList.size());
                        ViewPager_images_itm_Adapter viewPager_images_itm_adapter = new ViewPager_images_itm_Adapter(context, item_imagesArrayList,itm,item_key);
                        viewPager.setAdapter(viewPager_images_itm_adapter);

                        //                        Toast.makeText(getApplicationContext(), "Unsuccessful : Error Is :" + anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("tag", anError.getMessage());

                    }

                    @Override
                    protected void finalize() throws Throwable {
                        super.finalize();

                    }


                });


    }

    public void initialize_dots_images(int count_images){

        dotscount=count_images ;
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void Tabs_setupViewPager(final WrappingViewPager viewPager,ArrayList<Item_Class> arrayList) {

        final Tabs_PagerAdapter adapter = new Tabs_PagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();

        bundle.putString( "Overview",arrayList.get(0).getItem_description());
        bundle.putString( "lbl_Details",arrayList.get(0).getAttribute_title());
        bundle.putString( "value_Details",arrayList.get(0).getAttribute_value());

        Overview_fragment overview_fragment =new Overview_fragment();
        Details_fragment details_fragment=new Details_fragment();

        overview_fragment.setArguments(bundle);
        details_fragment.setArguments(bundle);

        adapter.clearFragment();
                adapter.addFragment(overview_fragment,"Overview");
                adapter.addFragment(details_fragment,"Details");
                    viewPager.setAdapter(adapter);
                }


    }


    class Tabs_PagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Tabs_PagerAdapter(FragmentManager manager) {

            super(manager);

        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        public void clearFragment() {
            mFragmentList.clear();
            mFragmentTitleList.clear();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    class ViewPager_images_itm_Adapter extends PagerAdapter {

    Context mContext;
    ArrayList<Item_Images_Class> item_imagesArrayList;
    ArrayList<Item_Class> mItm;
    LayoutInflater mLayoutInflater;
        int mitem_key;
    public ViewPager_images_itm_Adapter(Context context, ArrayList<Item_Images_Class> item_images, ArrayList<Item_Class> itm, int item_key)   {
        mContext = context;
        item_imagesArrayList=item_images;
        mItm=itm;
        mitem_key=item_key;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return item_imagesArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.viewpager_images_one_item, container, false);
    ANImageView an_imageView;
    an_imageView = (ANImageView) itemView.findViewById(R.id.an_imv_vp_imgs_one_itm);
        String images_String = item_imagesArrayList.get(position).getSrc_image().toString();
        String[] separated = images_String.split("/");

        an_imageView.setImageUrl("http://usrafinefood.se/tester2/image/catalog/items_mob/" +SharedPrefManager.getInstance(container.getContext()).get_Screen_Size()+"/"+ separated[2]);

        TextView txt_precent_value=(TextView) itemView.findViewById(R.id.txt_precent_value_vp_imgs_one_itm);
        RelativeLayout relativeLayout_sale=(RelativeLayout) itemView.findViewById(R.id.del_1_vp_imgs_one_itm);
        RelativeLayout relativeLayout_special=(RelativeLayout) itemView.findViewById(R.id.del_2_vp_imgs_one_itm);
        TextView txt_special_price=(TextView) itemView.findViewById(R.id.txt_special_price_vp_imgs_one_itm);

        if (mItm.get(0).getEnd_special_price_per_sec() > 0){
            String d=String.format("%.0f",((mItm.get(0).getSpecial_price()/mItm.get(0).getItem_price()-1)*-1)*100) + "%";
            relativeLayout_special.setVisibility(View.VISIBLE);
            txt_precent_value.setText(d);
            txt_special_price.setText(String.format("%.2f",mItm.get(0).getSpecial_price()));
        }else {
            relativeLayout_special.setVisibility(View.GONE);
            txt_precent_value.setVisibility(View.GONE);
        }
        TextView txt_is_sale=(TextView) itemView.findViewById(R.id.txt_is_sale_vp_imgs_one_itm);
        if (mItm.get(0).getIs_discount_product().equals("TRUE")){
            txt_is_sale.setVisibility(View.VISIBLE);
            relativeLayout_sale.setVisibility(View.VISIBLE);
            TextView txt_discount_count=(TextView) itemView.findViewById(R.id.txt_discount_count_vp_imgs_one_itm);
            TextView txt_discount_price=(TextView) itemView.findViewById(R.id.txt_discount_price_vp_imgs_one_itm);

            txt_discount_count.setText(String.format("%.1f",mItm.get(0).getDiscount_qty()) + " FOR:");
            txt_discount_price.setText(String.format("%.2f",mItm.get(0).getDiscount_price()*mItm.get(0).getDiscount_qty()));

        }else {
            txt_is_sale.setVisibility(View.GONE);
            relativeLayout_sale.setVisibility(View.GONE);

        }
        TextView txt_is_new_product=(TextView) itemView.findViewById(R.id.txt_is_new_product_vp_imgs_one_itm);
        if (mItm.get(0).getIs_new_product()){
            txt_is_new_product.setVisibility(View.VISIBLE);
        }else {
            txt_is_new_product.setVisibility(View.GONE);
        }
        an_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

   Intent intent=new Intent(v.getContext(),Images_View_Activity.class);
                 intent.putExtra("product_id",mitem_key);
                intent.putExtra("Item_def_image",mItm.get(0).getItem_def_image());

                v.getContext().startActivities(new Intent[]{intent});
            }
        });

        container.addView(itemView);
        return itemView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position , Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
