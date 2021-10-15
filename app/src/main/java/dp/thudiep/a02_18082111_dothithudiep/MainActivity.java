package dp.thudiep.a02_18082111_dothithudiep;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    ImageView play, prev, next, imageView;
    TextView songTitle, songSinger;
    SeekBar mSeekBarTime;
    List<Song> mSongs;
    Song song;
    static MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
//    int currentIndex = 0;
    TextView playerDuration,playerPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        play = findViewById(R.id.stop);
        prev = findViewById(R.id.lui);
        next = findViewById(R.id.tien);
        imageView = findViewById(R.id.imgSongPlay);
        songTitle = findViewById(R.id.textTitle);
        songSinger = findViewById(R.id.textViewSinger);
        mSeekBarTime = findViewById(R.id.seekBar);

        playerDuration =findViewById(R.id.playerDuration);
        playerPosition =findViewById(R.id.playerPosition);

//        Animation xoay = AnimationUtils.loadAnimation(this,R.anim.anim_taylor);
//        Animation dung = AnimationUtils.loadAnimation(this,R.anim.anim_taylor_dung);

        song = new Song("Love yourseft","Justin Biber",R.drawable.song1,R.raw.phiasau);
        if(song != null) {
            imageView.setImageResource(song.getImageSong());
            songTitle.setText(song.getTitle());
            songSinger.setText(song.getSingle());
            mMediaPlayer = MediaPlayer.create(getApplicationContext(), song.getResourece());//cai lenh nay failed ne Dip

//             time
            playerDuration.setText(convertFormat(mMediaPlayer.getDuration()));
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });

        }

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMediaPlayer == null){ //
                    Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
                }else{
                    mSeekBarTime.setMax(mMediaPlayer.getDuration());
                    if(mMediaPlayer!=null && mMediaPlayer.isPlaying()){
//                        clickStopService();
                        mMediaPlayer.pause();
                        play.setImageResource(R.drawable.stop);

                        //animation
//                        imageView.startAnimation(dung);
                    }else{
//                        clickStartService();
                        mMediaPlayer.start();
                        play.setImageResource(R.drawable.play);

                        //animation
//                        imageView.startAnimation(xoay);
                    }
                    SongNames();
                }

            }

        });
    }
    private String convertFormat(int duration) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

//    private void clickStartService() {
//        Intent intent = new Intent(this, MyService.class);
//        Bundle bundle = new Bundle();
//
////        Song song = mSongs.get(currentIndex);
//        bundle.putSerializable("object_song",song);
//        intent.putExtras(bundle);
//
//        startService(intent);
//    }
//
//    private void clickStopService() {
//        Intent intent = new Intent(this, MyService.class);
//        stopService(intent);
//    }
//
    private void SongNames(){
        imageView.setImageResource(song.getImageSong());
        songTitle.setText(song.getTitle());
        songSinger.setText(song.getSingle());

        //seek bar duration
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mSeekBarTime.setMax(mMediaPlayer.getDuration());
                mMediaPlayer.start();
            }
        });

        mSeekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mMediaPlayer.seekTo(progress);
                    mSeekBarTime.setProgress(progress);
                }
                playerPosition.setText(convertFormat(mMediaPlayer.getCurrentPosition()));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMediaPlayer!= null){
                    try {
                        if(mMediaPlayer.isPlaying()){
                            Message message = new Message();

                            message.what = mMediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    @SuppressLint("Handle Leak")
    Handler handler = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            mSeekBarTime.setProgress(msg.what);
        }
    };

}