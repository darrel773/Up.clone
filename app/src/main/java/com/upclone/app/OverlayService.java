package com.upclone.app;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.provider.Settings;
import android.view.*;
import android.webkit.*;
import android.widget.*;

public class OverlayService extends Service {

    private WindowManager wm;
    private LinearLayout panel;
    private WindowManager.LayoutParams params;

    private static final String CHANNEL = "UPCLONE";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotification();

        if (!Settings.canDrawOverlays(this)) {
            stopSelf();
            return;
        }

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setBackgroundColor(Color.rgb(5, 10, 8));

        WebView web = new WebView(this);

        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        web.setBackgroundColor(Color.TRANSPARENT);

        web.loadUrl("file:///android_asset/index.html");

        panel.addView(
            web,
            new LinearLayout.LayoutParams(
                780,
                1050
            )
        );

        int type;

        if (Build.VERSION.SDK_INT >= 26) {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        params = new WindowManager.LayoutParams(
            780,
            1050,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.CENTER;

        wm.addView(panel, params);

        panel.setOnTouchListener(
            new View.OnTouchListener() {

                float downX;
                float downY;

                int startX;
                int startY;

                @Override
                public boolean onTouch(
                    View v,
                    MotionEvent e
                ) {

                    if (e.getAction() ==
                        MotionEvent.ACTION_DOWN) {

                        downX = e.getRawX();
                        downY = e.getRawY();

                        startX = params.x;
                        startY = params.y;

                        return true;
                    }

                    if (e.getAction() ==
                        MotionEvent.ACTION_MOVE) {

                        params.x =
                            startX +
                            (int)(e.getRawX() - downX);

                        params.y =
                            startY +
                            (int)(e.getRawY() - downY);

                        wm.updateViewLayout(
                            panel,
                            params
                        );

                        return true;
                    }

                    return false;
                }
            }
        );
    }

    private void createNotification() {

        if (Build.VERSION.SDK_INT >= 26) {

            NotificationChannel channel =
                new NotificationChannel(
                    CHANNEL,
                    "UP CLONE",
                    NotificationManager.IMPORTANCE_LOW
                );

            NotificationManager manager =
                getSystemService(
                    NotificationManager.class
                );

            manager.createNotificationChannel(channel);
        }

        Notification.Builder builder;

        if (Build.VERSION.SDK_INT >= 26) {
            builder =
                new Notification.Builder(
                    this,
                    CHANNEL
                );
        } else {
            builder =
                new Notification.Builder(this);
        }

        builder
            .setContentTitle("UP CLONE")
            .setContentText(
                "Floating dashboard active"
            )
            .setSmallIcon(
                android.R.drawable.ic_dialog_info
            );

        startForeground(
            1001,
            builder.build()
        );
    }

    @Override
    public void onDestroy() {

        if (panel != null && wm != null) {

            try {
                wm.removeView(panel);
            } catch (Exception ignored) {}
        }

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
          }
