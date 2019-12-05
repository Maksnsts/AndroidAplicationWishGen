package net.learn2develop.myapplication111.AlarmProfile;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import net.learn2develop.myapplication111.R;

public class RingtonePlayingService extends Service {

    MediaPlayer media_song;
    int startId;
    boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        String state = intent.getExtras().getString("extra");

        Log.e("What is the key?", state);

        assert state != null;
        switch (state){
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                Log.e("Start ID is ", state);
                break;
            default:
                startId = 1;
                break;
        }

        //if else znaczenia state

        // jezeli tutaj
        if(!this.isRunning && startId == 1){
            Log.e("tutaj bez muzyki,", "i ty chcesz start");

            media_song = MediaPlayer.create(this, R.raw.music1 );
            media_song.start();

            this.isRunning = true;
            this.startId = 0;

        }else if(this.isRunning && startId == 0){
            Log.e("tutej jest muzyka,", "i ty chcesz start");

            media_song.stop();
            media_song.reset();

            this.isRunning = false;
            this.startId = 0;

        }else if(!this.isRunning && startId == 0){
            Log.e("tutaj bez muzyki,", "i ty chcesz start");

            this.isRunning = false;
            this.startId = 0;

        }else if(this.isRunning && startId == 1) {
            Log.e("Tutaj bez muzyki,", "i ty chcesz start");

            this.isRunning = true;
            this.startId = 1;

        }
        else{
            Log.e("else", "tutaj dziala else");

        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Tell the user we stopped.
        Log.e("on Destroyed called", "ta da");

        super.onDestroy();
        this.isRunning = false;

    }
}