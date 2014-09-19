package com.namso.pimark;

import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.namso.smartspecs.SpecSheet;


/**
 * Created by Kamal on 12/09/2014.
 */
public class BenchmarkFragment extends Fragment {

    public BenchmarkFragment(){}

    ListView specList;
    String[] specTitles;
    String[] specValues;
    TypedArray specIcons;
    View rootView;

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       rootView = inflater.inflate(R.layout.fragment_benchmark, container, false);
       specTitles = getResources().getStringArray(R.array.spec_titles);
       specIcons = getResources().obtainTypedArray(R.array.spec_icons);

       specList = (ListView) rootView.findViewById(R.id.specList);
       SpecListAdapter adapter = new SpecListAdapter(getActivity(), specTitles, specIcons);
       specList.setAdapter(adapter);
       return rootView;
    }
}

class SpecListAdapter extends ArrayAdapter<String> {

    Context context;
    TypedArray specIcons;
    String[] specTitles;

    SpecListAdapter(Context context, String[] specTitles, TypedArray specIcons) {
        super(context, R.layout.spec_row, R.id.specTitle, specTitles);
        this.context = context;
        this.specIcons = specIcons;
        this.specTitles = specTitles;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View specRow = inflater.inflate(R.layout.spec_row, parent, false);

        ImageView myImage = (ImageView) specRow.findViewById(R.id.imageView);
        TextView myTitle = (TextView) specRow.findViewById(R.id.specTitle);
        TextView myValue = (TextView) specRow.findViewById(R.id.specValue);

        myImage.setImageResource(specIcons.getResourceId(position, -1));
        myTitle.setText(specTitles[position]);

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        SpecSheet phone = new SpecSheet(model);

        switch (position) {
            case 0:
                myValue.setText(manufacturer);
                break;
            case 1:
                myValue.setText(model);
                break;
            case 2:
                phone.setChipset(myValue);
                break;
            case 3:
                phone.setCpu(myValue);
                break;
            case 4:
                phone.setGpu(myValue);
                break;
            case 5:
                phone.setRam(myValue);
                break;
            case 6:
                phone.setOS(myValue);
                break;
            default:
                myValue.setText("");
        }

        return specRow;
    }

}