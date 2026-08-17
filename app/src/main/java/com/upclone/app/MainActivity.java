package com.upclone.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.*;
import android.graphics.Color;
import android.view.Gravity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(40, 40, 40, 40);
        layout.setBackgroundColor(Color.BLACK);

        TextView title = new TextView(this);
        title.setText("UP CLONE");
        title.setTextColor(Color.GREEN);
        title.setTextSize(28);
        title.setGravity(Gravity.CENTER);

        Button start = new Button(this);
        start.setText("START FLOATING PANEL");

        layout.addView(title);
        layout.addView(start);

        setContentView(layout);

        start.setOnClickListener(v -> {

            if (!Settings.canDrawOverlays(this)) {

                Intent i = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())
                );

                startActivity(i);

            } else {

                Intent i = new Intent(
                    this,
                    OverlayService.class
                );

                startForegroundService(i);
            }
        });
    }
          }
