package se.aljood.aljoodsql.SQL.Module;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import se.aljood.aljoodsql.SQL.Address.Zone_Class;
import se.aljood.aljoodsql.SQL.Categories.Category_Adapter;
import se.aljood.aljoodsql.SQL.Categories.Category_Class;
import se.aljood.aljoodsql.SQL.items.Items_Fragment;
import se.aljood.aljoodsql.SQL.items.Item_Card.Item_Card;
import se.aljood.aljoodsql.SQL.items.Item_Class;

public class MySQLClient {
    //Save/Retrive URLS
    private static final String DATA_INSERT_URL = "http://usrafinefood.se/tester2/crud.php";
    private ArrayAdapter<Zone_Class> adapter;
    private Category_Adapter adapter1;
    private Items_Fragment.MyAdapter_Item adapter_item;
//    private HomeFragment.MyAdapter_special_Item adapter_special_item;
//    private HomeFragment.MyAdapter_discount_Item adapter_discount_item;
//    private HomeFragment.MyAdapter_best_selling_Item adapter_best_selling_item;
    private final Context c;

    public MySQLClient(Context c) {
        this.c = c;
    }
    /*
    Save/Insert
     */

    public void select_zones(final Spinner sp) {
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
                                int zone_id = jo.getInt("zone_id");
                                String zone_name = jo.getString("name");

                                address = new Zone_Class();
                                address.setZone_name(zone_name);

                                addressArrayList.add(address.getZone_name());

                            }

                            adapter = new ArrayAdapter(c, android.R.layout.simple_list_item_1, addressArrayList);
                            sp.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(c, "Goods Response But Java Can`t Parse Json It Received: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(c, "Unsuccessful : Error Is :" + anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("tag", anError.getMessage());
                    }
                });

    }

    public void retrive_cat(final RecyclerView recyclerView) {
        final ArrayList<Category_Class> categories = new ArrayList<>();
        String lang_Name = "en";
        switch (SharedPrefManager.getInstance(c).get_Lang()){
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
                .addQueryParameter("action", "get_cat")
                .addQueryParameter("language_in", lang_Name)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jo;
                        Category_Class s;

                        try {

                            for (int i=0;i< response.length();i++){
                                jo = response.getJSONObject(i);
                                int category_id = jo.getInt("category_id");
                                int sort_order= jo.getInt("sort_order");
                                String name= jo.getString("name");
                                String description=jo.getString("description");
                                String lname=jo.getString("lname");
                                String img_mob = jo.getString("img_mob");
                                s= new Category_Class();
                                s.setCategory_id(category_id);
                                s.setDescription(description);
                                s.setLname(lname);
                                s.setName(name);
                                s.setSort_order(sort_order);
                                s.setImg_mob(img_mob);
                                categories.add(s);

                            }
                            adapter1 = new Category_Adapter(c, categories);
                            recyclerView.setAdapter(adapter1);


                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText(c, "Goods Response But Java Can`t Parse Json It Received: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(c, "Unsuccessful : Error Is :" + anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("tag", anError.getMessage());
                    }

                });
    }


    public void retrive_itm_details(final RecyclerView recyclerView,  String type_retrive_itm_details,int item_id) {

        final ArrayList<Item_Class> items = new ArrayList<>();
        final String str_type_retrive_itm_details=type_retrive_itm_details;

        String lang_Name = "en";
        switch (SharedPrefManager.getInstance(c).get_Lang()){
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
                .addQueryParameter("action", str_type_retrive_itm_details)
                .addQueryParameter("language_in", lang_Name)
                .addQueryParameter("product_id", String.valueOf(item_id))
                .addQueryParameter("cus_id",String.valueOf(SharedPrefManager.getInstance(c).get_user_ID()))
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
                                    switch (str_type_retrive_itm_details.toString()) {
                                        case "get_itm_det":

                                            adapter_item = new Items_Fragment.MyAdapter_Item(c, items);
                                            recyclerView.setAdapter(adapter_item);
                                            break;
                                        case "get_itm_special_det":
//                                            HomeFragment homeFragment_special = new HomeFragment();

//                                            HomeFragment.MyAdapter_Special_Items adapter_special_item = homeFragment_special.new MyAdapter_Special_Items(c, items,);
//                                            recyclerView.setAdapter(adapter_special_item);
                                            break;
                                        case "get_itm_discount_det":
//                                            HomeFragment homeFragment_discount = new HomeFragment();
//                                            MyAdapter_discount_Item adapter_discount_item = homeFragment_discount.new MyAdapter_discount_Item(c, items);
//                                            recyclerView.setAdapter(adapter_discount_item);
                                            break;
                                        case "get_itm_best_selling_det":
//                                            HomeFragment homeFragment_best_selling = new HomeFragment();
//                                            HomeFragment.MyAdapter_best_selling_Item adapter_best_selling_item = homeFragment_best_selling.new MyAdapter_best_selling_Item(c, items);
//                                            recyclerView.setAdapter(adapter_best_selling_item);
                                            break;
                                        case "get_related_itm_det":
                                            Item_Card itemCard = new Item_Card();
                                            Item_Card.MyAdapter_related_product adapter_related_product = itemCard.new MyAdapter_related_product(c, items);
                                            if (adapter_related_product.getItemCount()<=4){
                                                GridLayoutManager layoutManager2 =
                                                        new GridLayoutManager(c, 1, GridLayoutManager.HORIZONTAL, false);
                                                LinearLayoutManager mLayoutManager_related_products=layoutManager2;
                                                recyclerView.setLayoutManager(mLayoutManager_related_products);
                                            }else
                                            {
                                                GridLayoutManager layoutManager2 =
                                                        new GridLayoutManager(c, 2, GridLayoutManager.HORIZONTAL, false);
                                                LinearLayoutManager mLayoutManager_related_products=layoutManager2;
                                                recyclerView.setLayoutManager(mLayoutManager_related_products);

                                            }
                                            recyclerView.setAdapter(adapter_related_product);
                                            break;

                                    }
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();
                                Toast.makeText(c, "Goods Response But Java Can`t Parse Json It Received: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(c, "Unsuccessful : Error Is :" + anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("tag", anError.getMessage());
                    }

                });
    }

}