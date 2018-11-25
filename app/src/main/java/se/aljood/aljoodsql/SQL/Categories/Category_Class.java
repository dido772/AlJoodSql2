package se.aljood.aljoodsql.SQL.Categories;

/**
 * Created by Eyad on 2018-05-23.
 */

public class Category_Class {

    int category_id;
    int sort_order;
    String name;
    String description;
    String lname,img_mob;

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getSort_order() {
        return sort_order;
    }

    public void setSort_order(int sort_order) {
        this.sort_order = sort_order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getImg_mob() {
        return img_mob;
    }

    public void setImg_mob(String img_mob) {
        this.img_mob = img_mob;
    }

    @Override
    public String toString() {
        return "Category_Class{" +
                "name='" + name + '\'' +
                ", img_mob='" + img_mob + '\'' +
                '}';
    }
}
