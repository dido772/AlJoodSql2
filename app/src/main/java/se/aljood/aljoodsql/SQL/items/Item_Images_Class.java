package se.aljood.aljoodsql.SQL.items;

public class Item_Images_Class {
    int product_id,product_image_id,sort_image;
    String src_image;

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getProduct_image_id() {
        return product_image_id;
    }

    public void setProduct_image_id(int product_image_id) {
        this.product_image_id = product_image_id;
    }

    public int getSort_image() {
        return sort_image;
    }

    public void setSort_image(int sort_image) {
        this.sort_image = sort_image;
    }

    public String getSrc_image() {
        return src_image;
    }

    public void setSrc_image(String src_image) {
        this.src_image = src_image;
    }

    @Override
    public String toString() {
        return "Item_Images_Class{" +
                "product_id=" + product_id +
                ", src_image='" + src_image + '\'' +
                '}';
    }
}
