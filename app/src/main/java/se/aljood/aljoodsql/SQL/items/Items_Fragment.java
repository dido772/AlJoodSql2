package se.aljood.aljoodsql.SQL.items;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.androidnetworking.widget.ANImageView;
import java.util.ArrayList;
import se.aljood.aljoodsql.R;
import se.aljood.aljoodsql.SQL.items.Item_Class;
import se.aljood.aljoodsql.SQL.Module.MySQLClient;

/**
 * Created by Eyad on 2018-05-21.
 */

public class Items_Fragment extends Fragment{
    private RecyclerView mItems;
//    private RecyclerView.LayoutManager mlayoutManager;
    Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//Make sure you have this line of code.
        activity = getActivity();

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_items, container, false);
        mItems = (RecyclerView) rootView.findViewById(R.id.rv_Items);
        mItems.setHasFixedSize(true);
        mItems.setLayoutManager(new GridLayoutManager(getActivity(),2));
        // Recycler_view Horizontal
//        mlayoutManager=new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
//        mItems.setLayoutManager(mlayoutManager);
        mItems.setAdapter(new MyAdapter_Item(activity,new ArrayList<Item_Class>()));
        new MySQLClient(activity).retrive_itm_details(mItems,"get_itm_det",0);

        return rootView;
    }

    public static class MyViewHolder_items extends RecyclerView.ViewHolder{
        ANImageView imageView;
        TextView txtPrice,txtTitel;
        public MyViewHolder_items(View view){
            super(view);

            imageView = (ANImageView) view.findViewById(R.id.img_item);
            txtPrice = (TextView) view.findViewById(R.id.txt_price);
            txtTitel= (TextView) view.findViewById(R.id.txt_title_item);

            imageView.setDefaultImageResId(R.drawable.w);
            imageView.setErrorImageResId(R.drawable.c);


        }
    }

    public static class MyAdapter_Item extends RecyclerView.Adapter<MyViewHolder_items>
    {
        private Context c;
        private ArrayList<Item_Class> items;

        public MyAdapter_Item(Context c, ArrayList<Item_Class> items){
            this.c = c;
            this.items= items;
        }

        @Override
        public MyViewHolder_items onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(c).inflate(R.layout.listview_item, parent, false);

            return new MyViewHolder_items(v);
        }

        @Override
        public void onBindViewHolder(MyViewHolder_items holder, int position) {
            Item_Class itm = items.get(position);
            holder.txtPrice.setText(itm.getItem_price().toString());
            holder.txtPrice.setPaintFlags(holder.txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtTitel.setText(itm.getItem_name());
            holder.imageView.setImageUrl("http://usrafinefood.se/tester2/image/" + itm.getItem_def_image());

        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

}

