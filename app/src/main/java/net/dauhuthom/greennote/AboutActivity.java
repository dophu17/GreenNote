package net.dauhuthom.greennote;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {

    TextView  tvVersion, tvAuthor, tvWebsite, tvCopyright;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        addControlls();
        initUI();
    }

    private void addControlls() {
        tvVersion = (TextView) findViewById(R.id.tvVersion);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        tvWebsite = (TextView) findViewById(R.id.tvWebsite);
        tvCopyright = (TextView) findViewById(R.id.tvCopyright);
    }

    private void initUI() {
        Context context = getApplicationContext(); // or activity.getApplicationContext()
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();

        String versionName = "Phiên bản: ";

        try {
            versionName += packageManager.getPackageInfo(packageName, 0).versionName;
            tvVersion.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tvAuthor.setText("Tác giả: Đậu Hũ Thơm \n\nLiên hệ - góp ý: dophu17@gmail.com");
        tvWebsite.setText("Website: http://dauhuthom.net/");
        tvCopyright.setText("Copyright © 2016 Ho Chi Minh");
    }
}
