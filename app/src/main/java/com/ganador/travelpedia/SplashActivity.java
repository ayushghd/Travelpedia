package com.ganador.travelpedia;

import android.content.Intent;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.ganador.travelpedia.LoginSignup.LoginActivity;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class SplashActivity extends AwesomeSplash {

    LinearLayout view ;
    @Override
    public void initSplash(ConfigSplash configSplash) {
        setContentView(R.layout.activity_splash);

        configSplash.setBackgroundColor(R.color.colorPrimary);
        configSplash.setAnimCircularRevealDuration(1200);
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);

        configSplash.setLogoSplash(R.mipmap.world);
        configSplash.setAnimLogoSplashDuration(1000);
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn);

        configSplash.setTitleSplash("  ");
        configSplash.setTitleTextColor(R.color.colorPrimary);
        configSplash.setTitleTextSize(50f);
        configSplash.setAnimTitleDuration(750);
        configSplash.setAnimTitleTechnique(Techniques.ZoomIn);
    }

    @Override
    public void animationsFinished() {
        finish();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}
