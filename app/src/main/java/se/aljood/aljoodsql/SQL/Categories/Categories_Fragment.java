package se.aljood.aljoodsql.SQL.Categories;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import se.aljood.aljoodsql.R;
import se.aljood.aljoodsql.SQL.Module.MySQLClient;

/**
 * Created by Eyad on 2018-05-21.
 */

public class Categories_Fragment extends Fragment{
    private RecyclerView mItems;
    private  Context c;
    Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//Make sure you have this line of code.
        activity = getActivity();

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);
        mItems = (RecyclerView) rootView.findViewById(R.id.rv_categories);
        mItems.setHasFixedSize(true);
        mItems.setLayoutManager(new GridLayoutManager(getActivity(),2));
//        mItems.setLayoutManager(new LinearLayoutManager(getActivity()));

        mItems.setAdapter(new Category_Adapter(activity,new ArrayList<Category_Class>()));
        new MySQLClient(activity).retrive_cat(mItems);

        return rootView;
    }




}
