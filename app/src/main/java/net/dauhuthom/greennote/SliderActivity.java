package net.dauhuthom.greennote;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

import static net.dauhuthom.greennote.R.drawable.slider1;

public class SliderActivity extends AppCompatActivity {

    SettingDBHelper settingDBHelper;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        //setting simple
        insertSettingSimple();
        checkFirstRun();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_slider, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_slider, container, false);

            ImageView imageViewSlider = (ImageView) rootView.findViewById(R.id.imageViewSlider);
            Button btnStart = (Button) rootView.findViewById(R.id.btnStart);

            btnStart.setVisibility(View.INVISIBLE);
            int index = getArguments().getInt(ARG_SECTION_NUMBER);
            int image = 0;
            int background = 0;
            if (index == 1) {
                image = R.drawable.slider1;
                background = ContextCompat.getColor(getContext(), R.color.colorBackgroundSlider1);
            } else if (index == 2) {
                image = R.drawable.slider2;
                background = ContextCompat.getColor(getContext(), R.color.colorBackgroundSlider2);
            } else if (index == 3) {
                image = R.drawable.slider3;
                background = ContextCompat.getColor(getContext(), R.color.colorBackgroundSlider3);
                btnStart.setVisibility(View.VISIBLE);
            }
            imageViewSlider.setImageResource(image);
            imageViewSlider.setBackgroundColor(background);

            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SettingDBHelper settingDBHelper = new SettingDBHelper(view.getContext());
                    Cursor cursor = settingDBHelper.getAll();
                    while (cursor.moveToNext()) {
                        String key = cursor.getString(cursor.getColumnIndex("key"));
                        if (key.equals("first_run")) {
                            int id = cursor.getInt(0);
                            settingDBHelper.update(id, key, "1", null, null);
                        }
                    }

                    Intent intent = new Intent(view.getContext(), NoteActivity.class);
                    startActivity(intent);
                }
            });

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    //key, value, default_value, description
    private void insertSettingSimple() {
        settingDBHelper = new SettingDBHelper(this);
        Cursor cursor = settingDBHelper.getAll();
        if (cursor.getCount() == 0) {
            String[] settingsKey = {
                    "email",
                    "first_run"
            };
            for (int i = 0; i < settingsKey.length; i++) {
                String value = null;
                if (settingsKey[i].equals("first_run")) {
                    value = "0";
                }
                settingDBHelper = new SettingDBHelper(this);
                settingDBHelper.insert(settingsKey[i], value, null, null);
            }
        }
//        settingDBHelper = new SettingDBHelper(this);
//        Cursor cursor1 = settingDBHelper.getAll();
//        Toast.makeText(this, cursor1.getCount() + "", Toast.LENGTH_SHORT).show();
    }

    private void checkFirstRun() {
        settingDBHelper = new SettingDBHelper(this);
        Cursor cursor = settingDBHelper.getAll();
        while (cursor.moveToNext()) {
            String key = cursor.getString(cursor.getColumnIndex("key"));
            if (key.equals("first_run")) {
                String value = cursor.getString(cursor.getColumnIndex("value"));
                if (value.equals("1")) {
                    Intent intent = new Intent(this, NoteActivity.class);
                    startActivity(intent);
                }
            }
        }
    }
}
