package sjsu.zankhna.android.sensor.clapapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private SensorManager sensorManager;
    private Sensor clapProximitySensor;
    private SensorEventListener proximitySensorListener;

    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setSensorListener();
        setUpSensor();
        playMusic();
    }

    /**
     * Check for Proximity sensor availability. If available, registers listener wth sensor
     */
    private void setUpSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        clapProximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (clapProximitySensor == null) {
            showMessage(getResources().getString(R.string.msg_no_sensor));
        } else {
            sensorManager.registerListener(
                    proximitySensorListener,
                    clapProximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    /**
     * Initialize UI.
     */
    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
    }

    /**
     * Displays Toast message on screen.
     *
     * @param message The message to show.
     */
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * Sets Proximity sensor listener.
     */
    private void setSensorListener() {
        proximitySensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                    float volume = 1 - (event.values[0] / 10f);
                    Log.d("Volume := ", String.valueOf(volume));
                    player.setVolume(volume, volume);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    /**
     * Initialize media player and start playing music
     */
    private void playMusic() {
        player = MediaPlayer.create(getApplicationContext(), R.raw.music_clip);
        player.setLooping(true);
        player.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }
}
