package com.namso.pimark;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

/**
 * Created by Kamal on 12/09/2014.
 */
public class CompareFragment extends Fragment {

    private GraphicalView mChart;
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    private XYSeries mCurrentSeries;
    private XYSeriesRenderer mCurrentRenderer;
    private int score;
    private double[] limits = new double[] {-0.5, 6, 0, 50000};


    public CompareFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_compare, container, false);
        loadSaved();
        if (mChart == null) {
            initChart();
            addSampleData();
            mChart = ChartFactory.getBarChartView(getActivity(), mDataset, mRenderer, BarChart.Type.DEFAULT);
        } else {
            mChart.repaint();
        }
        return mChart;
    }

    private void loadSaved() {
        SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        score =  Integer.parseInt(localSharedPreferences.getString("score", "0"));
    }

    private void initChart() {
        mCurrentSeries = new XYSeries("Sample Data");
        mDataset.addSeries(mCurrentSeries);
        mCurrentRenderer = new XYSeriesRenderer();
        setChartSettings(mRenderer, "", "Devices", "Scores", -0.5,
                4.5, 0, 50000, Color.BLACK, Color.BLACK);
        mRenderer.setXLabels(0);
        mRenderer.addXTextLabel(0, "Your Device");
        mRenderer.addXTextLabel(1, "Galaxy S5");
        mRenderer.addXTextLabel(2, "Nexus 5");
        mRenderer.addXTextLabel(3, "S4 i9500");
        mRenderer.addXTextLabel(4, "Moto G");
        mRenderer.addXTextLabel(5, "HTC Sensation");

        mCurrentRenderer.setDisplayChartValues(true);
        mCurrentRenderer.setChartValuesSpacing(15);
        mCurrentRenderer.setColor(Color.parseColor("#33B5E5"));
        mRenderer.addSeriesRenderer(mCurrentRenderer);

        mRenderer.setPanLimits(limits);
        mRenderer.setZoomEnabled(false, false);
        mRenderer.setMarginsColor(Color.parseColor("#EEEDED"));
        mRenderer.setXLabelsColor(Color.BLACK);
        mRenderer.setYLabelsColor(0,Color.BLACK);


        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.parseColor("#EEEDED"));

        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        float medium = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, metrics);
        float small = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, metrics);

        mRenderer.setChartValuesTextSize(small);
        mRenderer.setAxisTitleTextSize(medium);
        mRenderer.setChartTitleTextSize(medium);
        mRenderer.setLabelsTextSize(small);
        mRenderer.setBarSpacing(0.5);
        mRenderer.setShowLegend(false);
    }

    private void addSampleData() {
        mCurrentSeries.add(0, score);
        mCurrentSeries.add(1, 10567);
        mCurrentSeries.add(2, 10878);
        mCurrentSeries.add(3, 13277);
        mCurrentSeries.add(4, 21924);
        mCurrentSeries.add(5, 27911);
    }

    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
                                    String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
                                    int labelsColor) {
        renderer.setChartTitle(title);
        renderer.setYLabelsAlign(Paint.Align.RIGHT);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);

        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int large = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 50, metrics);
        int medium = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 19, metrics);
        int small = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8, metrics);

        renderer.setMargins(new int[] { medium, large, medium, small});
    }

}