package com.namso.pimark;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Kamal on 12/09/2014.
 */
public class MyPiFragment extends Fragment {
    View rootView;
    TextView pi, loadingText;
    ProgressBar loading;
    String piValue;

    public MyPiFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_my_pi, container, false);
        pi = (TextView) rootView.findViewById(R.id.pi);
        loading = (ProgressBar) rootView.findViewById(R.id.progressBar);
        loadingText = (TextView) rootView.findViewById(R.id.loadingPi);
        loadPi load = new loadPi();
        load.execute();
        return rootView;
    }


    private void savePreferences(String paramString1, String paramString2) {
        SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        localEditor.putString(paramString1, paramString2);
        localEditor.commit();
    }

    private class loadPi extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
            loadingText.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (pi.getText().equals("Nothing to see, try running the benchmark first (loading the Digits may take a couple of seconds).") || pi.getText().equals("")) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                piValue = localSharedPreferences.getString("pi", "Nothing to see, try running the benchmark first (loading the Digits may take a couple of seconds).");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pi.setText(piValue);
            pi.setVisibility(View.VISIBLE);
            loading.setVisibility(View.INVISIBLE);
            loadingText.setVisibility(View.INVISIBLE);
        }



    }
}