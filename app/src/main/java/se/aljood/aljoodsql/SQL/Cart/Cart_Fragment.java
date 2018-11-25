package se.aljood.aljoodsql.SQL.Cart;

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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class Cart_Fragment extends Fragment {
    Activity activity;
    RecyclerView rv_items_cart;
    RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout swiper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//Make sure you have this line of code.
        activity = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        rv_items_cart = rootView.findViewById(R.id.rv_fragment_cart);
        swiper = rootView.findViewById(R.id.swipe_cart_fragment);
        swiper.setRefreshing(true);

        initialize_Cart_Items();

        return rootView;

    }

    private void initialize_Cart_Items() {
        rv_items_cart.setHasFixedSize(true);
        rv_items_cart.setLayoutManager(new LinearLayoutManager(getActivity()));
        String cus_id = String.valueOf(SharedPrefManager.getInstance(getActivity()).get_user_ID());
        Retrive_Cart_Items(rv_items_cart, cus_id);


    }

    public void Retrive_Cart_Items(final RecyclerView recyclerView, String cus_id) {
        final ArrayList<Cart_Class> items = new ArrayList<>();
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
                .addQueryParameter("action", "get_cart_items")
                .addQueryParameter("cus_id", cus_id)
                .addQueryParameter("language_in", lang_Name)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {
                            JSONObject jo;
                            Cart_Class s;

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
                                        Double qty_cart = jo.getDouble("cart_qty");
                                        s = new Cart_Class();
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
                                        s.setCart_qty(qty_cart);
                                        items.add(s);

                                    }
                                    recyclerView.setAdapter(new MyAdapter_Cart_Items(activity, items, swiper));

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

    public static class MyViewHolder_Cart_Items extends RecyclerView.ViewHolder {
        ANImageView imageView;
        ImageView menu_imageView;

        TextView txt_sale, txt_new;

        TextView txt_normal_price, txt_Old_Price, txt_Special_Price, txt_title_item, txt_precent, txt_discount_count, txt_discount_price, txt_original_price;

        TextView txt_countdown_day, txt_countdown_hour, txt_countdown_min, txt_countdown_sec;
        long START_TIME_IN_MILLIS;
        Spinner sp;

        int tagItem;

        public MyViewHolder_Cart_Items(View view, int viewType) {
            super(view);
            if (viewType == 2) { //Discount Item_Class
                tagItem = 0;
                imageView = view.findViewById(R.id.img_item_in_cart_lv_discount_item);
                txt_sale = view.findViewById(R.id.txt_isdiscountsale_in_cart_lv_discount_item);
                txt_new = (TextView) view.findViewById(R.id.txt_is_new_product_in_cart_lv_discount_item);
                txt_title_item = (TextView) view.findViewById(R.id.txt_title_item_in_cart_lv_discount_item);
                txt_discount_count = (TextView) view.findViewById(R.id.txt_discount_count_in_cart_lv_discount_item);
                txt_discount_price = (TextView) view.findViewById(R.id.txt_discount_price_in_cart_lv_discount_item);
                txt_original_price = (TextView) view.findViewById(R.id.txt_original_price_in_cart_lv_discount_item);
                menu_imageView = (ImageView) view.findViewById(R.id.imv_menu_in_cart_lv_discount_item);
                imageView.setDefaultImageResId(R.mipmap.loading);
                sp = view.findViewById(R.id.spnr_qty_in_cart_lv_discount);

            } else if (viewType == 1) { //Specail Item_Class
                tagItem = 0;
                imageView = (ANImageView) view.findViewById(R.id.img_cart_special_items_with_c_d);
                txt_Special_Price = (TextView) view.findViewById(R.id.txt_new_price_cart_special_items_with_c_d);
                txt_title_item = (TextView) view.findViewById(R.id.txt_title_item_cart_special_items_with_c_d);
                txt_Old_Price = (TextView) view.findViewById(R.id.txt_old_price_cart_special_items_with_c_d);
                txt_precent = (TextView) view.findViewById(R.id.txt_precent_value_cart_special_items_with_c_d);
                txt_new = (TextView) view.findViewById(R.id.txt_is_new_product_cart_special_items_with_c_d);
                menu_imageView = (ImageView) view.findViewById(R.id.imv_menu_cart_special_items_with_c_d);
                txt_countdown_day = (TextView) view.findViewById(R.id.txt_countdown_day_cart_special_items_with_c_d);
                txt_countdown_hour = (TextView) view.findViewById(R.id.txt_countdown_hours_cart_special_items_with_c_d);
                txt_countdown_min = (TextView) view.findViewById(R.id.txt_countdown_minutes_cart_special_items_with_c_d);
                txt_countdown_sec = (TextView) view.findViewById(R.id.txt_countdown_seconds_cart_special_items_with_c_d);
                imageView.setDefaultImageResId(R.mipmap.loading);
                sp = view.findViewById(R.id.spnr_qty_cart_special_items_with_c_d);

            } else {//Normal Item_Class
                tagItem = 0;
                imageView = (ANImageView) view.findViewById(R.id.img_normal_cart_item);
                txt_title_item = (TextView) view.findViewById(R.id.txt_title_item_normal_cart_item);
                txt_normal_price = (TextView) view.findViewById(R.id.txt_price_normal_cart_item);
                txt_sale = (TextView) view.findViewById(R.id.txt_is_sale_normal_cart_item);
                txt_new = (TextView) view.findViewById(R.id.txt_is_new_product_normal_cart_item);
                menu_imageView = (ImageView) view.findViewById(R.id.imv_menu_normal_cart_item);
                imageView.setDefaultImageResId(R.mipmap.loading);
                sp = view.findViewById(R.id.spnr_qty_normal_cart_item);


            }

        }

    }

    public class MyAdapter_Cart_Items extends RecyclerView.Adapter<MyViewHolder_Cart_Items> {
        private Context c;
        private ArrayList<Cart_Class> items;
        private SwipeRefreshLayout swiper;
        private final static int Special_VIEW = 1;
        private final static int Discount_VIEW = 2;
        private final static int NORMAL_VIEW = 0;

        public MyAdapter_Cart_Items(Context c, ArrayList<Cart_Class> items, SwipeRefreshLayout swiper) {
            this.c = c;
            this.items = items;
            this.swiper = swiper;
        }

        @Override
        public MyViewHolder_Cart_Items onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            if (viewType == 1) {
                v = LayoutInflater.from(c).inflate(R.layout.listview_cart_special_items_with_count_down, parent, false);
            } else if (viewType == 2) {
                v = LayoutInflater.from(c).inflate(R.layout.listview_cart_discount_items, parent, false);
            } else {
                v = LayoutInflater.from(c).inflate(R.layout.listview_cart_normal_item, parent, false);
            }
            return new MyViewHolder_Cart_Items(v, viewType);
        }

        @Override
        public int getItemViewType(int position) {
            if (items.get(position).getEnd_special_price_per_sec() > 0) {
                return Special_VIEW;
            } else if (items.get(position).getIs_discount_product().toUpperCase().equals("TRUE")) {
                return Discount_VIEW;
            } else {
                return NORMAL_VIEW;
            }
        }

        @Override
        public void onBindViewHolder(final MyViewHolder_Cart_Items holder, final int position) {
            switch (holder.getItemViewType()) {
                case 1: //Special items
                    final Cart_Class item = items.get(position);
                    holder.tagItem = item.getItem_id();
                    holder.txt_title_item.setText(item.getItem_name().toString());
                    holder.txt_Special_Price.setText(String.format("%.2f", item.getSpecial_price()));
                    holder.txt_Old_Price.setText(String.format("%.2f", item.getItem_price()));
                    holder.txt_Old_Price.setPaintFlags(holder.txt_Old_Price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    String d = String.format("%.0f", ((item.getSpecial_price() / item.getItem_price() - 1) * -1) * 100) + "%";
                    if (item.isIs_new_product()) {
                        holder.txt_new.setVisibility(View.VISIBLE);
                    } else {
                        holder.txt_new.setVisibility(View.GONE);
                    }

//                if (item.getIs_discount_product().equals("TRUE")){
//                    holder.txt_sale.setVisibility(View.VISIBLE);
//                }else {
//                    holder.txt_sale.setVisibility(View.GONE);
//                }
                    holder.txt_precent.setText(d);

                    //displaying the popup


                 holder.menu_imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showOptionsMenu(holder.menu_imageView, SharedPrefManager.getInstance(c).get_user_ID(), item.getItem_id());
                    }
                });
                    final int item_key1 = item.getItem_id();
                    holder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), Item_Card.class);
                            intent.putExtra("ItemID", item_key1);
                            v.getContext().startActivity(intent);

                        }
                    });

                    holder.imageView.setImageBitmap(null);
                    holder.imageView.setImageUrl("http://usrafinefood.se/tester2/image/" + item.getItem_def_image());

                    holder.START_TIME_IN_MILLIS = item.getEnd_special_price_per_sec() * 1000;
                    updateCountDownText(holder.START_TIME_IN_MILLIS, holder.txt_countdown_day, holder.txt_countdown_hour, holder.txt_countdown_min, holder.txt_countdown_sec);
                    startTimer(holder.START_TIME_IN_MILLIS, holder.txt_countdown_day, holder.txt_countdown_hour, holder.txt_countdown_min, holder.txt_countdown_sec);

                    final String[] arraySpinner_special = new String[150];
                    for (int i = 0; i < arraySpinner_special.length; i++) {
                        arraySpinner_special[i] = String.valueOf(i + 1);

                    }


                    ArrayAdapter<String> adapter_specail = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, arraySpinner_special);
                    adapter_specail.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    holder.sp.setAdapter(adapter_specail);

                    break;
                case 2://Discount items
                    final Cart_Class itm = items.get(position);

                    if (itm.isIs_new_product()) {
                        holder.txt_new.setVisibility(View.VISIBLE);
                    } else {
                        holder.txt_new.setVisibility(View.GONE);
                    }

                    if (Boolean.getBoolean(items.get(position).getIs_discount_product())) {
                        holder.txt_sale.setVisibility(View.VISIBLE);
                    } else {
                        holder.txt_sale.setVisibility(View.GONE);
                    }
                    holder.tagItem = itm.getItem_id();
                    holder.imageView.setImageUrl("http://usrafinefood.se/tester2/image/" + itm.getItem_def_image());
                    holder.txt_title_item.setText(itm.getItem_name().toString());
                    holder.txt_discount_count.setText(String.format("%.2f", itm.getDiscount_qty()));
                    holder.txt_discount_price.setText(String.format("%.2f", itm.getDiscount_price()));
                    holder.txt_original_price.setText(String.format("%.2f", itm.getItem_price()));

                    final int item_key = itm.getItem_id();
                    holder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), Item_Card.class);
                            intent.putExtra("ItemID", item_key);
                            v.getContext().startActivity(intent);
                        }
                    });
                    holder.menu_imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showOptionsMenu(holder.menu_imageView, SharedPrefManager.getInstance(c).get_user_ID(), itm.getItem_id());
                        }
                    });

                    final String[] arraySpinner = new String[150];
                    for (int i = 0; i < arraySpinner.length; i++) {
                        arraySpinner[i] = String.valueOf(i + 1);

                    }


                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, arraySpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    holder.sp.setAdapter(adapter);

                    break;
                default:
                    final Cart_Class normal_item = items.get(position);
                    holder.txt_title_item.setText(normal_item.getItem_name().toString());
                    holder.txt_normal_price.setText(String.format("%.2f", normal_item.getItem_price()));
                    holder.tagItem = normal_item.getItem_id();
                    if (normal_item.isIs_new_product()) {
                        holder.txt_new.setVisibility(View.VISIBLE);
                    } else {
                        holder.txt_new.setVisibility(View.GONE);
                    }

                    if (normal_item.getIs_discount_product().equals("TRUE")) {
                        holder.txt_sale.setVisibility(View.VISIBLE);
                    } else {
                        holder.txt_sale.setVisibility(View.GONE);
                    }
                    holder.menu_imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            showOptionsMenu(holder.menu_imageView, SharedPrefManager.getInstance(c).get_user_ID(), normal_item.getItem_id());
                        }
                    });
                    final int item_key2 = normal_item.getItem_id();
                    holder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), Item_Card.class);
                            intent.putExtra("ItemID", item_key2);
                            v.getContext().startActivity(intent);

                        }
                    });

                    holder.imageView.setImageUrl("http://usrafinefood.se/tester2/image/" + normal_item.getItem_def_image());
                    final String[] arraySpinner_normal = new String[150];
                    for (int i = 0; i < arraySpinner_normal.length; i++) {
                        arraySpinner_normal[i] = String.valueOf(i + 1);

                    }


                    ArrayAdapter<String> adapter_spnr_normal = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, arraySpinner_normal);
                    adapter_spnr_normal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    holder.sp.setAdapter(adapter_spnr_normal);

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

        private void showOptionsMenu(final View view, final int cus_id, final int product_id) {
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_item_in_cart, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.remove_from_cart:

                            AndroidNetworking.get("http://usrafinefood.se/tester2/crud.php")
                                    .addQueryParameter("action", "add_remove_cart_item")
                                    .addQueryParameter("is_delete", "TRUE")
                                    .addQueryParameter("cus_id", String.valueOf(cus_id))
                                    .addQueryParameter("product_id", String.valueOf(product_id))
                                    .addQueryParameter("qty", "0")
                                    .setPriority(Priority.HIGH)
                                    .build()
                                    .getAsOkHttpResponse(new OkHttpResponseListener() {
                                        @Override
                                        public void onResponse(Response response) {
                                            // do anything with response
                                            if (response.isSuccessful()) {
//                                                Toast.makeText(c, R.string.String_confrim_delete_item_from_cart, Toast.LENGTH_SHORT).show();
                                                swiper.setRefreshing(true);
                                                 fetchTimelineAsync(0);

                                            }
                                        }

                                        @Override
                                        public void onError(ANError anError) {
                                            Toast.makeText(c, "Unsuccessful : Error Is :" + anError.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            Toast.makeText(view.getContext(), "ssss", Toast.LENGTH_LONG);
                            break;
                    }
                    return false;
                }
            });
            popup.show();
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
            initialize_Cart_Items();
        }
    }
}
