package se.aljood.aljoodsql.SQL.Categories;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import se.aljood.aljoodsql.R;

/**
 * Created by Eyad on 2018-05-23.
 */

public  class Category_Adapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context c;
    private ArrayList<Category_Class> categories;

    public Category_Adapter(Context c, ArrayList<Category_Class> categories){
        this.c = c;
        this.categories = categories;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.listview_categories, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        layoutParams.setFullSpan(true);
//        holder.itemView.setLayoutParams(layoutParams);
        Category_Class cat = categories.get(position);
        holder.txtMedium.setText(cat.getName());
        holder.imageView.setImageUrl("http://usrafinefood.se/tester2/image/" + cat.getImg_mob());

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
