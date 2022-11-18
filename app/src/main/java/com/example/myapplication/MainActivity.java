package com.example.myapplication;

import android.os.Bundle;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.view.View;
import android.os.IBinder;
import android.widget.Button;

// ---------------------------------------------------------------------------------------
public class MainActivity extends Activity
        implements
        ServiceConnection
{




    // indicates whether the activity is linked to service player.
    private boolean mIsBound = false;

    // Saves the binding instance with the service.
    private MusicService mServ;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button = findViewById(R.id.btn);



        // Starting the service of the player, if not already started.
        Intent music = new Intent(this, MusicService.class);
        startService(music);

        doBindService();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
            }
        });
    }
    // interface connection with the service activity
    public void onServiceConnected(ComponentName name, IBinder binder)
    {
        mServ = ((MusicService.ServiceBinder) binder).getService();
    }

    public void onServiceDisconnected(ComponentName name)
    {
        mServ = null;
    }

    // local methods used in connection/disconnection activity with service.

    public void doBindService()
    {
        // activity connects to the service.
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    public void doUnbindService()
    {
        // disconnects the service activity.
        if(mIsBound)
        {
            unbindService(this);
            mIsBound = false;
        }
    }
    // when closing the current activity, the service will automatically shut down(disconnected).
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        doUnbindService();
    }
    // interface buttons that call methods of service control on the activity.

    @Override
    protected void onPause() {
        super.onPause();
        mServ.pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mServ.resume();
    }
}