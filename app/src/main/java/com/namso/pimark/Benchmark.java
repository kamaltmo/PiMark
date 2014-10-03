package com.namso.pimark;

import android.app.ProgressDialog;
import android.os.*;
import android.os.Process;
import android.view.View;
import android.widget.TextView;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;


/**
 * Created by Kamal on 01/10/2014.
 */
public class Benchmark {
    View rootView;
    Apfloat c3_OVER_24;
    Apfloat sqrtC;
    Apfloat[][] totals;
    Long startTime, endTime;
    AtomicInteger count = new AtomicInteger(0);
    ProgressDialog dialog;
    boolean visible = false; // shows if progressDialog has been started
    int DIGITS = 100000;
    int nCores;


    public Benchmark(View view) {
        rootView = view;
        dialog = new ProgressDialog(rootView.getContext());
    }

    private int getNumCores() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if(Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }
        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch(Exception e) {
            //Default to return 1 core
            return 1;
        }
    }


    public void startBenchmark() {
        int s = 0,e = 0; //store increaments for binaryspliting
        nCores = getNumCores();
        totals = new Apfloat[nCores][3];
        int increment = (DIGITS/13)/nCores + 1;
        //Get start time
        startTime = System.currentTimeMillis();

        //Calulate constant variables
        c3_OVER_24 = ApfloatMath.pow((new Apfloat(640320)), 3).precision(DIGITS).divide(new Apfloat(24));
        sqrtC = ApfloatMath.sqrt(new Apfloat(10005).precision(DIGITS));

        //Split calutation by cores and start threads
        for (int i = 0; i < nCores; i++) {
            e += increment;
            int[] data = {s, e, i};
            BenchmarkTask benchmarkTask = new BenchmarkTask();
            benchmarkTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
            s += increment;
        }
    }

    private class BenchmarkTask extends AsyncTask<int[], Integer, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!visible) {
                visible = true;
                dialog.setMessage("Running Benchmark...");
                dialog.setIndeterminate(false);
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            dialog.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(int[]... ints) {
            //Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            Process.setThreadPriority(-19);
            Apfloat[] results;
            int[] data = ints[0];
            results = bs(data[0], data[1]);
            totals[data[2]] = results;
            count.incrementAndGet();

            //Check if finally core has finished and calulate Pi
            if (count.intValue() == nCores) {
                //calulate final values and store them in results, then find pi
                results = totalResult();
                Apfloat pi = results[1].multiply(new Apfloat(426880).multiply(sqrtC)).divide(results[2]).precision(DIGITS);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (count.intValue() == nCores) {
                dialog.dismiss();
                endTime = System.currentTimeMillis();
                endTime = endTime - startTime;
                TextView score = (TextView) rootView.findViewById(R.id.textView4);
                score.setText(endTime.toString());
            }
        }

        public Apfloat[] bs(int s, int e) {
            Apfloat[] results = new Apfloat[3];
            if (e - s == 1) {
                if (s == 0) {
                    results[0] = results[1] = new Apfloat(1);
                } else {
                    results[0] = new Apfloat((6*s-5)*(2*s-1)*(6*s-1));
                    results[1] = c3_OVER_24.multiply(ApfloatMath.pow(new Apfloat(s), 3));
                }
                results[2] = results[0].multiply(new Apfloat(13591409).add(new Apfloat(545140134).multiply(new Apfloat(s))));
                if (s % 2 == 1) {
                    results[2] = results[2].negate();
                }
            } else {
                int m = (int) ((s + e)/2); //Find midpoint

                Apfloat[] sM = bs(s, m).clone();
                Apfloat[] mE = bs(m, e).clone();

                results[0] = sM[0].multiply(mE[0]);
                results[1] = sM[1].multiply(mE[1]);
                results[2] = mE[1].multiply(sM[2]).add(sM[0].multiply(mE[2]));

            }
            return results;
        }

        public Apfloat[] totalResult() {
            int i;
            for (i = 0; i < nCores-1; i++) {
                totals[i+1][2] = totals[i+1][1].multiply(totals[i][2]).add(totals[i][0].multiply(totals[i+1][2]));
                totals[i+1][0] = totals[i+1][0].multiply(totals[i][0]);
                totals[i+1][1] = totals[i+1][1].multiply(totals[i][1]);
            }
            return totals[i];
        }

    }
}



