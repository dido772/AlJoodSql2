package se.aljood.aljoodsql.SQL.Categories;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;

import se.aljood.aljoodsql.R;

/**
 * Created by Eyad on 2018-05-23.
 */
    public class MyViewHolder extends RecyclerView.ViewHolder{
        ANImageView imageView;
        TextView txtMedium;
        public MyViewHolder(View view){
            super(view);

            imageView = (ANImageView) view.findViewById(R.id.ivm);
            txtMedium = (TextView) view.findViewById(R.id.txtmedium);
            imageView.setDefaultImageResId(R.drawable.w);
            imageView.setErrorImageResId(R.drawable.c);


        }
    }
