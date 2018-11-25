package se.aljood.aljoodsql.SQL.Main_Menu_Lang;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import se.aljood.aljoodsql.R;
import se.aljood.aljoodsql.SQL.Module.SharedPrefManager;
import se.aljood.aljoodsql.SQL.Splash_Login.LoginActivity;

public class LanguageActivity extends AppCompatActivity {

    ListView listView;
    String [] languages_name = {"Arabic", "English", "Swedish"};
    Integer[] imgid = {R.drawable.flag_arab, R.drawable.flag_english, R.drawable.flag_sweden};
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        listView = (ListView) findViewById(R.id.lv_lang);

        LanguageListView languageListView = new LanguageListView(this, languages_name, imgid);
        listView.setAdapter(languageListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(LanguageActivity.this, "" + position, Toast.LENGTH_SHORT).show();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(LanguageActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(LanguageActivity.this);
                }

                switch (position){
    case 0:
        builder.setTitle(R.string.title_select_language_arabic)
                .setMessage(R.string.message_select_language_arabic)

                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPrefManager.getInstance(getApplicationContext()).set_Lang("ar");
                        Intent i = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);

                    }
                });
        builder.show();
        break;
    case 1:
        builder.setTitle(R.string.title_select_language_english)
                .setMessage(R.string.message_select_language_english)

                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPrefManager.getInstance(getApplicationContext()).set_Lang("en");
                        Intent i = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);

                    }
                });
        builder.show();
        break;
    case 2:
        builder.setTitle(R.string.title_select_language_svenska)
                .setMessage(R.string.message_select_language_svenska)

                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPrefManager.getInstance(getApplicationContext()).set_Lang("sv");
                        Intent i = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);

                    }
                });
        builder.show();
        break;
}
            }
        });
    }
    public class LanguageListView extends ArrayAdapter<String>{
        private String[] languages_name;
        private Integer[] imgid;
        private Activity context;

        public LanguageListView(Activity context,String[] languages_name ,Integer[] imgid ) {
            super(context, R.layout.listview_language,languages_name);

            this.context = context;
            this.languages_name = languages_name;
            this.imgid = imgid;
        }

        @NonNull
        @Override
        public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent){
            View r = convertView;
            ViewHolder viewHolder= null;
            if (r==null){
                LayoutInflater layoutInflater = context.getLayoutInflater();
                r = layoutInflater.inflate(R.layout.listview_language, null, true);
                viewHolder = new ViewHolder(r);
                r.setTag(viewHolder);
            }else   {
                viewHolder=(ViewHolder) r.getTag();
            }

            viewHolder.imageView.setImageResource(imgid[position]);
            viewHolder.textView.setText(languages_name[position]);

            return r;
        }

        class ViewHolder{
            TextView textView;
            ImageView imageView;
            ViewHolder(View v){
                textView = (TextView) v.findViewById(R.id.tv_lang);
                imageView = (ImageView) v.findViewById(R.id.iv_lang);
            }
        }

    }

}
