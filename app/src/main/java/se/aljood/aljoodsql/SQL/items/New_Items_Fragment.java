package se.aljood.aljoodsql.SQL.items;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;

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
import se.aljood.aljoodsql.SQL.Module.SharedPrefManager;
import se.aljood.aljoodsql.SQL.items.Item_Card.Item_Card;

public class New_Items_Fragment extends Fragment {
    Activity activity;
    RecyclerView rv_items_new;
    SwipeRefreshLayout swiper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//Make sure you have this line of code.
        activity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_items, container, false);
        rv_items_new = rootView.findViewById(R.id.rv_fragment_new_items);
        swiper = rootView.findViewById(R.id.swipe_new_items_fragment);
        swiper.setRefreshing(true);
        initialize_All_New_Items();
        return rootView;
    }

    private void initialize_All_New_Items() {
        rv_items_new.setHasFixedSize(true);
        rv_items_new.setLayoutManager(new LinearLayoutManager(getActivity()));
        String cus_id = String.valueOf(SharedPrefManager.getInstance(getActivity()).get_user_ID());
        Retrieve_All_New_Items(rv_items_new, cus_id);
    }

    public void Retrieve_All_New_Items(final RecyclerView recyclerView, String cus_id) {
        final ArrayList<Item_Class> items = new ArrayList<>();
        String lang_Name;
        switch (SharedPrefManager.getInstance(activity).get_Lang()) {
            case "ar":
                lang_Name = "arabic";
                break;
            case "en":
                lang_Name = "english";
                break;
            case "sv":
                lang_Name = "swedish";
                break;
            default:
                lang_Name = "english";
                break;
        }

        AndroidNetworking.get("http://usrafinefood.se/tester2/crud.php")
                .addQueryParameter("action", "get_all_itm_new_det")
                .addQueryParameter("cus_id", cus_id)
                .addQueryParameter("language_in", lang_Name)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {
                            JSONObject jo;
                            Item_Class s;

                            try {
                                if (response.length() > 0) {
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
                                    recyclerView.setAdapter(new MyAdapter_All_New_Items(activity, items, swiper));
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

    public static class MyViewHolder_All_New_Items extends RecyclerView.ViewHolder {
        ANImageView imageView;
        ImageView menu_imageView;
        TextView txt_sale, txt_new;
        TextView txt_normal_price, txt_Old_Price, txt_Special_Price, txt_title_item, txt_precent, txt_discount_count, txt_discount_price, txt_original_price;
        MenuItem menuItem;

        TextView txt_countdown_day, txt_countdown_hour, txt_countdown_min, txt_countdown_sec;
        long START_TIME_IN_MILLIS;

        public MyViewHolder_All_New_Items(View view, int viewType) {
            super(view);
            menuItem = view.findViewById(R.id.add_to_cart);
            if (viewType == 1) { //Discount Item_Class
                imageView = (ANImageView) view.findViewById(R.id.img_new_discount_item);
                txt_sale = (TextView) view.findViewById(R.id.txt_is_discount_sale_new_discount_item);
                txt_new = (TextView) view.findViewById(R.id.txt_is_new_product_new_discount_item);
                txt_title_item = (TextView) view.findViewById(R.id.txt_title_item_new_discount_item);
                txt_discount_count = (TextView) view.findViewById(R.id.txt_discount_count_new_discount_item);
                txt_discount_price = (TextView) view.findViewById(R.id.txt_discount_price_new_discount_item);
                txt_original_price = (TextView) view.findViewById(R.id.txt_original_price_new_discount_item);
                menu_imageView = (ImageView) view.findViewById(R.id.imv_menu_new_discount_item);
                imageView.setDefaultImageResId(R.mipmap.loading);
                imageView.setErrorImageResId(R.drawable.c);

            } else if (viewType == 0) { //Specail Item_Class
                imageView = (ANImageView) view.findViewById(R.id.img_new_special_items_with_c_d);
                txt_Special_Price = (TextView) view.findViewById(R.id.txt_new_price_new_special_items_with_c_d);
                txt_title_item = (TextView) view.findViewById(R.id.txt_title_item_new_special_items_with_c_d);
                txt_Old_Price = (TextView) view.findViewById(R.id.txt_old_price_new_special_items_with_c_d);
                txt_precent = (TextView) view.findViewById(R.id.txt_precent_value_new_special_items_with_c_d);
                txt_sale = (TextView) view.findViewById(R.id.txt_precent_value_new_special_items_with_c_d);
                txt_new = (TextView) view.findViewById(R.id.txt_is_new_product_new_special_items_with_c_d);
                menu_imageView = (ImageView) view.findViewById(R.id.imv_menu_new_special_items_with_c_d);
                txt_countdown_day = (TextView) view.findViewById(R.id.txt_countdown_day_new_special_items_with_c_d);
                txt_countdown_hour = (TextView) view.findViewById(R.id.txt_countdown_hours_new_special_items_with_c_d);
                txt_countdown_min = (TextView) view.findViewById(R.id.txt_countdown_minutes_new_special_items_with_c_d);
                txt_countdown_sec = (TextView) view.findViewById(R.id.txt_countdown_seconds_new_special_items_with_c_d);
                imageView.setDefaultImageResId(R.mipmap.loading);
                imageView.setErrorImageResId(R.drawable.c);
            } else {
                imageView = (ANImageView) view.findViewById(R.id.img_normal_new_item);
                txt_title_item = (TextView) view.findViewById(R.id.txt_title_item_normal_new_item);
                txt_normal_price = (TextView) view.findViewById(R.id.txt_price_normal_new_item);
                txt_new = (TextView) view.findViewById(R.id.txt_is_new_product_normal_new_item);
                menu_imageView = (ImageView) view.findViewById(R.id.imv_menu_normal_new_item);
                imageView.setDefaultImageResId(R.mipmap.loading);
                imageView.setErrorImageResId(R.drawable.c);
            }

        }
    }

    public class MyAdapter_All_New_Items extends RecyclerView.Adapter<MyViewHolder_All_New_Items> {
        private Context c;
        private ArrayList<Item_Class> items;
        private final static int Special_VIEW = 0;
        private final static int Discount_VIEW = 1;
        private final static int NORMAL_VIEW=2;
        private SwipeRefreshLayout swiper;

        public MyAdapter_All_New_Items(Context c, ArrayList<Item_Class> items, SwipeRefreshLayout swiper) {
            this.c = c;
            this.items = items;
            this.swiper = swiper;
        }

        @Override
        public MyViewHolder_All_New_Items onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            if (viewType==1){
                v = LayoutInflater.from(c).inflate(R.layout.listview_new_discount_items, parent, false);
            }else if(viewType==0) {
                v = LayoutInflater.from(c).inflate(R.layout.listview_new_special_items_with_count_down, parent, false);
            }else {
                v = LayoutInflater.from(c).inflate(R.layout.listview_new_normal_item, parent, false);
            }
            return new MyViewHolder_All_New_Items(v, viewType);
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

        @Override
        public void onBindViewHolder(final MyViewHolder_All_New_Items holder, final int position) {
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
                    holder.START_TIME_IN_MILLIS = item.getEnd_special_price_per_sec() * 1000;
                    updateCountDownText(holder.START_TIME_IN_MILLIS, holder.txt_countdown_day, holder.txt_countdown_hour, holder.txt_countdown_min, holder.txt_countdown_sec);
                    startTimer(holder.START_TIME_IN_MILLIS, holder.txt_countdown_day, holder.txt_countdown_hour, holder.txt_countdown_min, holder.txt_countdown_sec);

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

//                    if (normal_item.getIs_discount_product().equals("TRUE")){
//                        holder.txt_sale.setVisibility(View.VISIBLE);
//                    }else {
//                        holder.txt_sale.setVisibility(View.GONE);
//                    }
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
        private long mTimeLeftinMillis;
        private CountDownTimer mCuntDownTimer;
        private boolean mTimerRunning;

        private void startTimer(final long START_TIME_IN_MILLIS, final TextView txt_day, final TextView txt_hour, final TextView txt_min, final TextView txt_sec) {
            mTimeLeftinMillis = START_TIME_IN_MILLIS;
            mCuntDownTimer = new CountDownTimer(mTimeLeftinMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimeLeftinMillis = millisUntilFinished;
                    updateCountDownText(START_TIME_IN_MILLIS, txt_day, txt_hour, txt_min, txt_sec);
                }


                @Override
                public void onFinish() {

                }
            }.start();

            mTimerRunning = true;
        }

        private void updateCountDownText(long START_TIME_IN_MILLIS, TextView txt_day, TextView txt_hour, TextView txt_min, TextView txt_sec) {
            long seconds = mTimeLeftinMillis / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            String time = days + ":" + hours % 24 + ":" + minutes % 60 + ":" + seconds % 60;
            String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d:%02d", days, hours % 24, minutes % 60, seconds % 60);
            String timeLeft_Days = String.format(Locale.getDefault(), "%02d", days);
            String timeLeft_Hours = String.format(Locale.getDefault(), "%02d", hours % 24);
            String timeLeft_Minutes = String.format(Locale.getDefault(), "%02d", minutes % 60);
            String timeLeft_Seconds = String.format(Locale.getDefault(), "%02d", seconds % 60);
            txt_day.setText(timeLeft_Days);
            txt_hour.setText(timeLeft_Hours);
            txt_min.setText(timeLeft_Minutes);
            txt_sec.setText(timeLeft_Seconds);

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
            initialize_All_New_Items();
        }

    }

}