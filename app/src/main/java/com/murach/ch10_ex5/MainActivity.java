package com.murach.ch10_ex5;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.net.URL;



public class MainActivity extends Activity {

    private TextView messageTextView;
    private TextView fileDownloadTextField;
    private Timer timer;
    private TimerTask task;
    private String sec = "0";
    private int x = 0;
    private int y = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        messageTextView = (TextView) findViewById(R.id.messageTextView);
        fileDownloadTextField = (TextView) findViewById(R.id.fileDownloadTextField);
        startTimer();
    }
    
    private void startTimer() {
            //final long startMillis = System.currentTimeMillis();
        timer = new Timer(true);
        task = new TimerTask() {
            @Override
            public void run() {
                //long elapsedMillis = System.currentTimeMillis() - startMillis;
                //updateView(elapsedMillis);
                updateView();
                final String FILENAME = "news_feed.xml";
                try{
                    URL url = new URL("http://rss.cnn.com/rss/cnn_tech.rss");
                    InputStream in = url.openStream();
                    FileOutputStream out =
                            openFileOutput(FILENAME, Context.MODE_PRIVATE);
                    byte[] buffer = new byte[1024];
                    int bytesRead = in.read(buffer);
                    while (bytesRead != -1)
                    {
                        out.write(buffer, 0, bytesRead);
                        bytesRead = in.read(buffer);
                    }
                    y++;
                    out.close();
                    in.close();
                }
                catch (IOException e){
                    Log.e("News Reader", e.toString());
                }
                fileDownloadTextField.post(new Runnable() {
                    @Override
                    public void run() {
                        fileDownloadTextField.setText("File Downloaded " + y + " time(s)");
                    }
                });
            }
        };
            timer.schedule(task, 0, 1000);
    }

    private void updateView(/*final long elapsedMillis*/) {
        // UI changes need to be run on the UI thread
            messageTextView.post(new Runnable() {

                //int elapsedSeconds = (int) elapsedMillis / 1000;
                int i = (Integer.parseInt(sec)) / 10;

                @Override
                public void run() {
                    messageTextView.setText("Seconds: " + i);
                    x++;
                    sec = "" + x;
                }
            });
    }

    public void startButtonClicked(View view) {
        startTimer();
    }

    public void stopButtonClicked(View view) {

        timer.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }
}