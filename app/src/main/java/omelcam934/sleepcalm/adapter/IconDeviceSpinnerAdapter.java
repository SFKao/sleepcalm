package omelcam934.sleepcalm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import omelcam934.sleepcalm.R;

public class IconDeviceSpinnerAdapter extends ArrayAdapter<Integer> {

    Integer[] icons;

    public IconDeviceSpinnerAdapter(@NonNull Context context,  Integer[] icons) {
        super(context, R.layout.icon_spinner_item, icons);
        this.icons = icons;
    }

    public int getPositionOfIcon(Integer icon){
        for (int i = 0; i < icons.length; i++) {
            if(icons[i].equals(icon))
                return i;
        }
        return 0;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getImageForPosition(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getImageForPosition(position);
    }

    private View getImageForPosition(int pos){
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(icons[pos]);
        imageView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return imageView;
    }
}
