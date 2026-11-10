package com.tech4u.echo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.view.View;
import android.view.WindowManager;
import android.view.DisplayCutout;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

/**
 * ECHO by Tech4U.
 * A full-screen WebView that runs the same index.html as the web version.
 */
public class MainActivity extends Activity {

    private WebView web;
    private long lastBack = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        web = new WebView(this);
        web.setBackgroundColor(0xFF05060D);
        web.setOverScrollMode(View.OVER_SCROLL_NEVER);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);               // high scores (localStorage)
        s.setMediaPlaybackRequiresUserGesture(false); // music & explosions
        s.setAllowFileAccess(true);

        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient());
        web.addJavascriptInterface(new Bridge(), "Android");

        // Keep the HUD clear of camera notches / cutouts (Android 15+ is always edge-to-edge).
        web.setOnApplyWindowInsetsListener((v, insets) -> {
            int l = 0, t = 0, r = 0, b = 0;
            if (Build.VERSION.SDK_INT >= 28) {
                DisplayCutout c = insets.getDisplayCutout();
                if (c != null) {
                    l = c.getSafeInsetLeft(); t = c.getSafeInsetTop();
                    r = c.getSafeInsetRight(); b = c.getSafeInsetBottom();
                }
            }
            v.setPadding(l, t, r, b);
            return insets;
        });

        setContentView(web);
        hideSystemBars();

        // Android 13+ back handling (required when targeting Android 16).
        if (Build.VERSION.SDK_INT >= 33) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT, this::handleBack);
        }

        if (savedInstanceState != null) {
            web.restoreState(savedInstanceState);
        } else {
            web.loadUrl("file:///android_asset/index.html");
        }
    }

    @SuppressWarnings("deprecation")
    private void hideSystemBars() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) hideSystemBars();
    }

    @Override
    protected void onPause() {
        // Pause the game and silence audio when the app goes to the background.
        web.evaluateJavascript("window.dispatchEvent(new Event('apppause'))", null);
        web.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        web.onResume();
        hideSystemBars();
    }

    /** Back on Android 12 and older. */
    @SuppressWarnings("deprecation")
    @Override
    public void onBackPressed() {
        handleBack();
    }

    /** Back = pause / go to menu. On the menu, press back twice to exit. */
    private void handleBack() {
        web.evaluateJavascript("window.echoBack ? window.echoBack() : 'exit'", value -> {
            if (value != null && value.contains("exit")) {
                long now = System.currentTimeMillis();
                if (now - lastBack < 2000) {
                    finish();
                } else {
                    lastBack = now;
                    Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        web.saveState(outState);
    }

    @Override
    protected void onDestroy() {
        if (web != null) web.destroy();
        super.onDestroy();
    }

    @SuppressWarnings("deprecation")
    private Vibrator vibrator() {
        if (Build.VERSION.SDK_INT >= 31) {
            VibratorManager vm = (VibratorManager) getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            return vm != null ? vm.getDefaultVibrator() : null;
        }
        return (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    /** Exposed to JavaScript as window.Android */
    private class Bridge {
        @SuppressWarnings("deprecation")
        @JavascriptInterface
        public void vibrate(int ms, int amplitude) {
            try {
                Vibrator v = vibrator();
                if (v == null || !v.hasVibrator()) return;
                ms = Math.max(1, Math.min(ms, 1000));
                if (Build.VERSION.SDK_INT >= 26) {
                    int amp = amplitude > 0 ? Math.min(255, amplitude) : VibrationEffect.DEFAULT_AMPLITUDE;
                    v.vibrate(VibrationEffect.createOneShot(ms, amp));
                } else {
                    v.vibrate(ms);
                }
            } catch (Exception ignored) { }
        }

        @SuppressWarnings("deprecation")
        @JavascriptInterface
        public void vibratePattern(String csv) {
            try {
                Vibrator v = vibrator();
                if (v == null || !v.hasVibrator() || csv == null) return;
                String[] parts = csv.split(",");
                long[] pattern = new long[Math.min(parts.length, 20)];
                for (int i = 0; i < pattern.length; i++) pattern[i] = Math.max(0, Math.min(1000, Long.parseLong(parts[i].trim())));
                if (Build.VERSION.SDK_INT >= 26) v.vibrate(VibrationEffect.createWaveform(pattern, -1));
                else v.vibrate(pattern, -1);
            } catch (Exception ignored) { }
        }

        @JavascriptInterface
        public void share(String text) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(i, "Share your ECHO score"));
        }
    }
}
