package net.dauhuthom.greennote;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StatisticalActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    NoteDBHelper noteDBHelper;
    ServiceDBHelper serviceDBHelper;
    Calendar calendar;
    Date date;
    String currentDate = null, currentDateShow;
    double totalToday = 0, totalYesterday = 0, totalThisMonth = 0, totalLastMonth = 0;

    PieChart pieChartDay, pieCharMonth;
    ArrayList<Integer> tmpListServiceID = new ArrayList();
    ArrayList<String> tmpListServiceName = new ArrayList();
    ArrayList<Integer> tmpListServiceIDMonth = new ArrayList();
    ArrayList<String> tmpListServiceNameMonth = new ArrayList();

    TextView tvChangeDate, tvToday, tvThisMonth, tvWarning;//, tvYesterday, tvLastMonth, tvWarningDay, tvWarningMonth;
    TextView tvPositionToday, tvPositionThisMonth;
    Button btnChangeDate, btnNote, btnStatistical, btnService, btnOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical);

        addControls();
        readData();
        addEvents();

        getServiceForChart();
        drawChart();
        drawChartMonth();
    }

    private void addControls() {
        tvToday = (TextView) findViewById(R.id.tvToday);
        tvThisMonth = (TextView) findViewById(R.id.tvThisMonth);
//        tvYesterday = (TextView) findViewById(R.id.tvYesterday);
//        tvLastMonth = (TextView) findViewById(R.id.tvLastMonth);
//        tvWarningDay = (TextView) findViewById(R.id.tvWarningDay);
//        tvWarningMonth = (TextView) findViewById(R.id.tvWarningMonth);
        tvChangeDate = (TextView) findViewById(R.id.tvChangeDate);
        btnChangeDate = (Button) findViewById(R.id.btnChangeDate);
        btnNote = (Button) findViewById(R.id.btnNote);
        btnStatistical = (Button) findViewById(R.id.btnStatistical);
        btnService = (Button) findViewById(R.id.btnService);
        btnOther = (Button) findViewById(R.id.btnOther);
        pieChartDay = (PieChart) findViewById(R.id.chartDay);
        pieCharMonth = (PieChart) findViewById(R.id.chartMonth);
        tvPositionToday = (TextView) findViewById(R.id.tvPositionToday);
        tvPositionThisMonth = (TextView) findViewById(R.id.tvPositionThisMonth);
        tvWarning = (TextView) findViewById(R.id.tvWarning);

        //date current
        calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = null;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String strDate = simpleDateFormat.format(calendar.getTime());
        currentDate= strDate;
        currentDateShow = new Function().formatFromyyyyMMdd(currentDate, new Function().getDefaultFormatDate(getBaseContext()));
        tvChangeDate.setText(currentDateShow);
    }

    public void readData() {
        noteDBHelper = new NoteDBHelper(this);
        //today
        totalToday = noteDBHelper.getSumToday(currentDate);
        tvToday.setText(new Function().formatDecimal(totalToday, "###,###,###,###,###", Locale.GERMANY) + " VND");
        //this month
        totalThisMonth = noteDBHelper.getSumThisMonth(currentDate);
        tvThisMonth.setText(new Function().formatDecimal(totalThisMonth, "###,###,###,###,###", Locale.GERMANY) + " VND");
        //yesterday
        totalYesterday = noteDBHelper.getSumYesterday(currentDate);
//        tvYesterday.setText(new Function().formatDecimal(totalYesterday, "###,###,###,###,###", Locale.GERMANY) + " VND");
        //last month
        totalLastMonth = noteDBHelper.getSumLastMonth(currentDate);
//        tvLastMonth.setText(new Function().formatDecimal(totalLastMonth, "###,###,###,###,###", Locale.GERMANY) + " VND");

        //warning
        String strWarning = "";
        if (totalToday > totalYesterday) {
            strWarning += "- Hôm nay bạn chi tiêu nhiều hơn hôm qua " + new Function().formatDecimal(totalToday - totalYesterday, "###,###,###,###,###", Locale.GERMANY) + " VND. HÃY TIẾT KIỆM!\n";
        }
        if (totalThisMonth > totalLastMonth) {
            strWarning += "- Tháng này bạn chi tiêu nhiều hơn tháng trước " + new Function().formatDecimal(totalThisMonth - totalLastMonth, "###,###,###,###,###", Locale.GERMANY) + " VND. HÃY TIẾT KIỆM!";
        }
        tvWarning.setText(strWarning);
    }

    private void addEvents() {
        btnChangeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        tvChangeDate.setText(new Function().formatFromyyyyMMdd(i + "-" + (i1 + 1) + "-" + i2, new Function().getDefaultFormatDate(getBaseContext())));
                        calendar.set(i, i1, i2);
                        date = calendar.getTime();

                        currentDate = new Function().formatToyyyyMMdd(tvChangeDate.getText().toString(), new Function().getDefaultFormatDate(getBaseContext()));
                        readData();

                        //refest chart
                        getServiceForChart();
                        drawChart();
                        drawChartMonth();
                    }
                };
                String string = tvChangeDate.getText() + "";
                //Lấy ra chuỗi của textView Date
                int day = new Function().getDay(string, new Function().getDefaultFormatDate(getBaseContext()));
                int month = new Function().getMonth(string, new Function().getDefaultFormatDate(getBaseContext())) - 1;
                int year = new Function().getYear(string, new Function().getDefaultFormatDate(getBaseContext()));
                //Hiển thị ra Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(StatisticalActivity.this,
                        onDateSetListener, year, month, day);
                datePickerDialog.setTitle("Change date");
                datePickerDialog.show();
            }
        });
        btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                startActivity(intent);
            }
        });
        btnStatistical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServiceActivity.class);
                startActivity(intent);
            }
        });
        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OtherActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight) {
        if (entry == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + entry.getVal() + ", xIndex: " + entry.getXIndex()
                        + ", DataSet index: " + i);
        Toast.makeText(this, "Tỷ lệ: " + (double)Math.round(entry.getVal()*10)/10 + "%", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }

    private void drawChart() {
        //giá trị phần trăm
        pieChartDay.setUsePercentValues(true);
        //hiện mô tả phía dưới
        String nameChart = "Không thể vẽ biểu đồ. Hãy thêm ghi chú!";
        if (totalToday > 0) {
            nameChart = "Biểu đồ chi tiêu (ngày)";
        }
        pieChartDay.setDescription(nameChart);
        //mChart.setExtraOffsets(5, 10, 5, 5);

        //mChart.setDragDecelerationFrictionCoef(5.95f);

        //cái lỗ chính giữa
        pieChartDay.setDrawHoleEnabled(false);
        pieChartDay.setHoleColor(Color.WHITE);
        pieChartDay.setTransparentCircleColor(Color.WHITE);
        pieChartDay.setTransparentCircleAlpha(110);
        pieChartDay.setHoleRadius(58f);
        pieChartDay.setTransparentCircleRadius(61f);

        pieChartDay.setDrawCenterText(false);

        //góc quay
        pieChartDay.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChartDay.setRotationEnabled(true);
        pieChartDay.setHighlightPerTapEnabled(true);

        // add a selection listener
        pieChartDay.setOnChartValueSelectedListener(this);

        setData((float)totalToday); //1: +1 phan tu; 100: 100%

        pieChartDay.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        //mChart.spin(2000, 0, 360);

        Legend l = pieChartDay.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    private void setData(float range) {
        noteDBHelper = new NoteDBHelper(this);
        //set giá trị của từng miếng bánh
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

//        for (int i = 0; i < count + 1; i++) {
//            yVals1.add(new Entry((float) (3 * mult) + mult / 5, i));
//        }

        //Enty(giá trị phần bánh, vị trí index phần bánh)
        for (int i = 0; i < tmpListServiceID.size(); i++) {
            int serviceID = tmpListServiceID.get(i);
            float valuePie = (float)noteDBHelper.getSumTodayByService(currentDate, serviceID);
            float valuePiePercent = (valuePie * 100) / range;
            if (valuePiePercent != 0) {
                yVals1.add(new Entry(valuePiePercent, i));
            }
        }

        //set tên của từng miếng bánh
        //chỉ những service nào có total_price mới hiện ra
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < tmpListServiceName.size(); i++) {
            xVals.add(tmpListServiceName.get(i));
        }

        String desChart = "";
        PieDataSet dataSet = new PieDataSet(yVals1, desChart);
        //khoảng cách giữa các phần bánh
        dataSet.setSliceSpace(1f);
        //khoảng cách to ra khi lick vào phần bánh
        dataSet.setSelectionShift(6f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        //data.setValueTypeface(tf);
        pieChartDay.setData(data);

        // undo all highlights
        pieChartDay.highlightValues(null);

        pieChartDay.invalidate();
    }

    private void drawChartMonth() {
        //giá trị phần trăm
        pieCharMonth.setUsePercentValues(true);
        //hiện mô tả phía dưới
        String nameChart = "Không thể vẽ biểu đồ. Hãy thêm ghi chú!";
        if (totalThisMonth > 0) {
            nameChart = "Biểu đồ chi tiêu (tháng)";
        }
        pieCharMonth.setDescription(nameChart);
        //mChart.setExtraOffsets(5, 10, 5, 5);

        //mChart.setDragDecelerationFrictionCoef(5.95f);

        //cái lỗ chính giữa
        pieCharMonth.setDrawHoleEnabled(false);
        pieCharMonth.setHoleColor(Color.WHITE);
        pieCharMonth.setTransparentCircleColor(Color.WHITE);
        pieCharMonth.setTransparentCircleAlpha(110);
        pieCharMonth.setHoleRadius(58f);
        pieCharMonth.setTransparentCircleRadius(61f);

        pieCharMonth.setDrawCenterText(false);

        //góc quay
        pieCharMonth.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieCharMonth.setRotationEnabled(true);
        pieCharMonth.setHighlightPerTapEnabled(true);

        // add a selection listener
        pieCharMonth.setOnChartValueSelectedListener(this);

        setDataMonth((float)totalThisMonth); //1: +1 phan tu; 100: 100%

        pieCharMonth.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        //mChart.spin(2000, 0, 360);

        Legend l = pieCharMonth.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    private void setDataMonth(float range) {
        noteDBHelper = new NoteDBHelper(this);
        //set giá trị của từng miếng bánh
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

//        for (int i = 0; i < count + 1; i++) {
//            yVals1.add(new Entry((float) (3 * mult) + mult / 5, i));
//        }

        //Enty(giá trị phần bánh, vị trí index phần bánh)
        for (int i = 0; i < tmpListServiceIDMonth.size(); i++) {
            int serviceID = tmpListServiceIDMonth.get(i);
            float valuePie = (float)noteDBHelper.getSumThisMonthByService(currentDate, serviceID);
            float valuePiePercent = (valuePie * 100) / range;
            if (valuePiePercent != 0) {
                yVals1.add(new Entry(valuePiePercent, i));
            }
        }

        //set tên của từng miếng bánh
        //chỉ những service nào có total_price mới hiện ra
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < tmpListServiceNameMonth.size(); i++) {
            xVals.add(tmpListServiceNameMonth.get(i));
        }

        String desChart = "";
        PieDataSet dataSet = new PieDataSet(yVals1, desChart);
        //khoảng cách giữa các phần bánh
        dataSet.setSliceSpace(1f);
        //khoảng cách to ra khi lick vào phần bánh
        dataSet.setSelectionShift(6f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        //data.setValueTypeface(tf);
        pieCharMonth.setData(data);

        // undo all highlights
        pieCharMonth.highlightValues(null);

        pieCharMonth.invalidate();
    }

    private void getServiceForChart() {
        //day
        serviceDBHelper = new ServiceDBHelper(this);
        noteDBHelper = new NoteDBHelper(this);
        tmpListServiceID = new ArrayList<>();
        tmpListServiceName = new ArrayList<>();
        Cursor cursor = serviceDBHelper.getAll();
        while (cursor.moveToNext()) {
            int serviceID = cursor.getInt(cursor.getColumnIndex("id"));
            //day
            float valuePie = (float)noteDBHelper.getSumTodayByService(currentDate, serviceID);
            if (valuePie > 0) {
                tmpListServiceID.add(cursor.getInt(0));
                tmpListServiceName.add(cursor.getString(cursor.getColumnIndex("name")));
            }
        }

        //month
        serviceDBHelper = new ServiceDBHelper(this);
        noteDBHelper = new NoteDBHelper(this);
        tmpListServiceIDMonth = new ArrayList<>();
        tmpListServiceNameMonth = new ArrayList<>();
        Cursor cursor2 = serviceDBHelper.getAll();
        while (cursor2.moveToNext()) {
            int serviceID = cursor2.getInt(cursor2.getColumnIndex("id"));
            //month
            float valuePieMonth = (float)noteDBHelper.getSumThisMonthByService(currentDate, serviceID);
            if (valuePieMonth > 0) {
                tmpListServiceIDMonth.add(cursor2.getInt(0));
                tmpListServiceNameMonth.add(cursor2.getString(cursor2.getColumnIndex("name")));
            }
        }
    }
}


//https://github.com/PhilJay/MPAndroidChart
