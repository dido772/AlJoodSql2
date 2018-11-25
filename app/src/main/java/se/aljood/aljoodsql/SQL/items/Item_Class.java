package se.aljood.aljoodsql.SQL.items;

/**
 * Created by Eyad on 2018-05-31.
 */

public class Item_Class {
    int item_id,item_stock_status_id,item_manufacturer_id,item_tax_class_id,item_tax_rate_id,item_sort_order,item_status,days_for_availble,category_id,count_rating,attribute_id;
    long end_special_price_per_sec;
            float avg_rating;
    Double item_quantity,discount_qty,discount_price,item_price,item_tax_rate_value,item_weight,item_point_pris,special_price;
    String item_model,item_stock_status_name,item_def_image,item_manufacturer_name,item_manufacturer_image,item_title_tax_class,item_tax_rate_name,item_name,item_description,item_tag,is_discount_product,category_name,attribute_title,attribute_value;
    boolean is_new_product,is_cart;

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getItem_stock_status_id() {
        return item_stock_status_id;
    }

    public void setItem_stock_status_id(int item_stock_status_id) {
        this.item_stock_status_id = item_stock_status_id;
    }

    public int getItem_manufacturer_id() {
        return item_manufacturer_id;
    }

    public void setItem_manufacturer_id(int item_manufacturer_id) {
        this.item_manufacturer_id = item_manufacturer_id;
    }

    public int getItem_tax_class_id() {
        return item_tax_class_id;
    }

    public void setItem_tax_class_id(int item_tax_class_id) {
        this.item_tax_class_id = item_tax_class_id;
    }

    public int getItem_tax_rate_id() {
        return item_tax_rate_id;
    }

    public void setItem_tax_rate_id(int item_tax_rate_id) {
        this.item_tax_rate_id = item_tax_rate_id;
    }

    public int getItem_sort_order() {
        return item_sort_order;
    }

    public void setItem_sort_order(int item_sort_order) {
        this.item_sort_order = item_sort_order;
    }

    public int getItem_status() {
        return item_status;
    }

    public void setItem_status(int item_status) {
        this.item_status = item_status;
    }

    public int getDays_for_availble() {
        return days_for_availble;
    }

    public void setDays_for_availble(int days_for_availble) {
        this.days_for_availble = days_for_availble;
    }

    public long getEnd_special_price_per_sec() {
        return end_special_price_per_sec;
    }

    public void setEnd_special_price_per_sec(long end_special_price_per_sec) {
        this.end_special_price_per_sec = end_special_price_per_sec;
    }

    public Double getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(Double item_quantity) {
        this.item_quantity = item_quantity;
    }

    public Double getItem_price() {
        return item_price;
    }

    public void setItem_price(Double item_price) {
        this.item_price = item_price;
    }

    public Double getItem_tax_rate_value() {
        return item_tax_rate_value;
    }

    public void setItem_tax_rate_value(Double item_tax_rate_value) {
        this.item_tax_rate_value = item_tax_rate_value;
    }

    public Double getItem_weight() {
        return item_weight;
    }

    public void setItem_weight(Double item_weight) {
        this.item_weight = item_weight;
    }

    public Double getItem_point_pris() {
        return item_point_pris;
    }

    public void setItem_point_pris(Double item_point_pris) {
        this.item_point_pris = item_point_pris;
    }

    public Double getSpecial_price() {
        return special_price;
    }

    public void setSpecial_price(Double special_price) {
        this.special_price = special_price;
    }

    public String getItem_model() {
        return item_model;
    }

    public void setItem_model(String item_model) {
        this.item_model = item_model;
    }

    public String getItem_stock_status_name() {
        return item_stock_status_name;
    }

    public void setItem_stock_status_name(String item_stock_status_name) {
        this.item_stock_status_name = item_stock_status_name;
    }

    public String getItem_def_image() {
        return item_def_image;
    }

    public void setItem_def_image(String item_def_image) {
        this.item_def_image = item_def_image;
    }

    public String getItem_manufacturer_name() {
        return item_manufacturer_name;
    }

    public Double getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(Double discount_price) {
        this.discount_price = discount_price;
    }

    public void setItem_manufacturer_name(String item_manufacturer_name) {
        this.item_manufacturer_name = item_manufacturer_name;
    }

    public String getItem_manufacturer_image() {
        return item_manufacturer_image;
    }

    public void setItem_manufacturer_image(String item_manufacturer_image) {
        this.item_manufacturer_image = item_manufacturer_image;
    }

    public String getItem_title_tax_class() {
        return item_title_tax_class;
    }

    public void setItem_title_tax_class(String item_title_tax_class) {
        this.item_title_tax_class = item_title_tax_class;
    }

    public String getItem_tax_rate_name() {
        return item_tax_rate_name;
    }

    public void setItem_tax_rate_name(String item_tax_rate_name) {
        this.item_tax_rate_name = item_tax_rate_name;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public String getItem_tag() {
        return item_tag;
    }

    public void setItem_tag(String item_tag) {
        this.item_tag = item_tag;
    }

    public String getIs_discount_product() {
        return is_discount_product;
    }

    public void setIs_discount_product(String is_discount_product) {
        this.is_discount_product = is_discount_product;
    }

    public boolean getIs_new_product() {
        return is_new_product;
    }

    public void setIs_new_product(boolean is_new_product) {
        this.is_new_product = is_new_product;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getCount_rating() {
        return count_rating;
    }

    public void setCount_rating(int count_rating) {
        this.count_rating = count_rating;
    }

    public float getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(float avg_rating) {
        this.avg_rating = avg_rating;
    }

    public int getAttribute_id() {
        return attribute_id;
    }

    public void setAttribute_id(int attribute_id) {
        this.attribute_id = attribute_id;
    }

    public String getAttribute_title() {
        return attribute_title;
    }

    public void setAttribute_title(String attribute_title) {
        this.attribute_title = attribute_title;
    }

    public String getAttribute_value() {
        return attribute_value;
    }

    public void setAttribute_value(String attribute_value) {
        this.attribute_value = attribute_value;
    }

    public Double getDiscount_qty() {
        return discount_qty;
    }

    public void setDiscount_qty(Double discount_qty) {
        this.discount_qty = discount_qty;
    }

    public boolean getIs_cart() {
        return is_cart;
    }

    public void setIs_cart(boolean is_cart) {
        this.is_cart = is_cart;
    }

    @Override
    public String toString() {
        return "Item_Class{" +
                "item_id='" + item_id + '\'' +
                "item_name='" + item_name + '\'' +
                '}';
    }
}
