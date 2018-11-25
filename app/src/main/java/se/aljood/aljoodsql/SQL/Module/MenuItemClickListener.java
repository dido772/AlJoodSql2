package se.aljood.aljoodsql.SQL.Module;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.widget.Toast;

import se.aljood.aljoodsql.R;

public class MenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_to_cart:
//                Toast.makeText(ite, R.string.add_to_cart, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.add_to_list:
//                Toast.makeText(c, R.string.add_to_lists, Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }
}

