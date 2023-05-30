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

/**
 * Adapter para los iconos de un spinner
 */
public class IconDeviceSpinnerAdapter extends ArrayAdapter<Integer> {

    Integer[] icons;

    /**
     * Constructor que recibe el contexto y los iconos como sus IDs
     * @param context contexto donde se utilice
     * @param icons iconos
     */
    public IconDeviceSpinnerAdapter(@NonNull Context context,  Integer[] icons) {
        super(context, R.layout.icon_spinner_item, icons);
        this.icons = icons;
    }

    /**
     * Obtiene la posicion de un icono, si no devuelve -1
     * @param icon id del icono
     * @return posicion, -1 si no esta
     */
    public int getPositionOfIcon(Integer icon){
        for (int i = 0; i < icons.length; i++) {
            if(icons[i].equals(icon))
                return i;
        }
        return -1;
    }

    /**
     * Obtiene la imagen para el menu desplegable
     * @param position posicion del elemento
     * @param convertView
     * @param parent
     * @return vista con el icono
     */
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getImageForPosition(position);
    }

    /**
     * Obtiene la imagen para cuando esta seleccioando
     * @param position posicion del elemento
     * @param convertView
     * @param parent
     * @return vista con el icono
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getImageForPosition(position);
    }

    /**
     * Obtiene la vista a partir de una posicion
     * @param pos posicion
     * @return vista con la imagen
     */
    private View getImageForPosition(int pos){
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(icons[pos]);
        imageView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return imageView;
    }
}
