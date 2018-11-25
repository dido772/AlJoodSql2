package se.aljood.aljoodsql.SQL.Module;

public class Featured_Manufacturer_Class {
    int manufacturer_id;
    String name,featured_image;

    public int getManufacturer_id() {
        return manufacturer_id;
    }

    public void setManufacturer_id(int manufacturer_id) {
        this.manufacturer_id = manufacturer_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeatured_image() {
        return featured_image;
    }

    public void setFeatured_image(String featured_image) {
        this.featured_image = featured_image;
    }


    @Override
    public String toString() {
        return "Featured_Manufacturer_Class{" +
                "manufacturer_id=" + manufacturer_id +
                ", name='" + name + '\'' +
                '}';
    }
}
