package com.marshallofsound.gpmdp.remote;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.balthazargronon.react.ZeroconfReactPackage;
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.github.xinthink.rnmk.ReactMaterialKitPackage;
import com.marshallofsound.gpmdp.remote.device.DevicePackage;
import com.marshallofsound.gpmdp.remote.media.MediaPackage;
import com.marshallofsound.gpmdp.remote.volume.VolumePackage;
import com.oblador.vectoricons.VectorIconsPackage;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends ReactActivity {
    private VolumePackage vP = null;
    private DevicePackage dP = null;
    private MediaPackage mP = null;

    private boolean once = true;

    @Override
    protected void onCreate(Bundle a) {
        View decorView = getWindow().getDecorView();
        if (once && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            once = false;
//            enableImmersiveMode(decorView);
        }
        if (mP != null) {
            mP.createNotification();
        }
        super.onCreate(a);
    }

    public static void enableImmersiveMode(final View decorView) {
        decorView.setSystemUiVisibility(setSystemUiVisibility());
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(setSystemUiVisibility());
                }
            }
        });
    }


    public static int setSystemUiVisibility() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
    }

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "GooglePlayMusicDesktopRemote";
    }

    /**
     * Returns whether dev mode should be enabled.
     * This enables e.g. the dev menu.
     */
    @Override
    protected boolean getUseDeveloperSupport() {
        return BuildConfig.DEBUG;
    }

    /**
     * A list of packages used by the app. If the app uses additional views
     * or modules besides the default ones, add more packages here.
     */
    protected List<ReactPackage> getPackages() {
        vP = new VolumePackage();
        dP = new DevicePackage();
        mP = new MediaPackage();
        return Arrays.<ReactPackage>asList(
            new MainReactPackage(),
            new ZeroconfReactPackage(),
            new ReactMaterialKitPackage(),
            new VectorIconsPackage(),
            dP,
            vP,
            mP
        );
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (dP != null) {
            dP.configurationUpdate(newConfig);
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return vP != null && vP.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
