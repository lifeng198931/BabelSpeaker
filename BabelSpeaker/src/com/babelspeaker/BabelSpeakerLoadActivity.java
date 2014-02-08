package com.babelspeaker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BabelSpeakerLoadActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_babel_speaker_load);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.babel_speaker_load, menu);
        return true;
    }

}
