package se.aljood.aljoodsql.SQL.items;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.widget.ANImageView;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import se.aljood.aljoodsql.R;

public class Images_View_Activity extends AppCompatActivity {
    private int item_key;
    private String item_def_image;
    Context context = this;
    PhotoViewAttacher viewAttacher;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_view);
        item_key=getIntent().getExtras().getInt("product_id");
        item_def_image=getIntent().getExtras().getString("Item_def_image");
        add_images(item_def_image,0);

        item_images();


    }

    public void add_images(String src_image,int tag){
        final LinearLayout gallery = findViewById(R.id.gallery);
        final LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.images_view,gallery,false);
         ANImageView anImageView = view.findViewById(R.id.img_view_zoom);
        anImageView.setImageUrl("http://usrafinefood.se/tester2/image/" +src_image.toString())  ;
        anImageView.setTag(tag);

        anImageView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                ImageView vv=(ANImageView) view;
//                Bitmap bmSrc1 = ((BitmapDrawable)vv.getDrawable()).getBitmap();
//                Bitmap bmSrc2 = bmSrc1.copy(bmSrc1.getConfig(), true);
//                ImageView anImageView_zoom = (ImageView) findViewById(R.id.imgs_view_zoom);
//                anImageView_zoom.setImageBitmap(bmSrc2);
                Drawable myDrawable = vv.getDrawable();
                PhotoView photoView = (PhotoView) findViewById(R.id.imgs_view_zoom);
                photoView.setImageDrawable(myDrawable);
                final int childCount = gallery.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View v = gallery.getChildAt(i);
                    if (v.getBackground()!=null) {
                        v.setBackground(null);
                    }

                }

                Drawable highlight = getResources().getDrawable( R.drawable.border_imageview);
                view.setBackground(highlight);
//                viewAttacher=new PhotoViewAttacher((ANImageView) view);
//                PhotoView photoView = (PhotoView) findViewById(R.id.imgs_view_zoom);
//                photoView.setImageURI(Uri.parse("http://usrafinefood.se/tester2/image/Abba Inlagd Sill.jpg"));

            }
        });
        gallery.addView(view);

    }

    public  void item_images(){
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
                            String def_src_images=item_def_image ;
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
                                add_images(src_images,i+1);

                            }



                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Goods Response But Java Can`t Parse Json It Received: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }


                    @Override
                    public void onError(ANError anError) {

//                                                Toast.makeText(getApplicationContext(), "Unsuccessful : Error Is :" + anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("tag", anError.getMessage());

                    }

                    @Override
                    protected void finalize() throws Throwable {
                        super.finalize();

                    }


                });


    }

}
