package com.example.rockscissorpaper2;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;

public class PlayBgmSound {

    private static SoundPool soundPool;

    private static HashMap<Integer,Integer> soundPoolMap;
    private static int[] soundSource = new int[7];


    //playBgm 초기화
    public static void initSounds(Context context){
        // API 21이상일때
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(10).build();
            // API 21이하일때
        }else {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }
        //soundSource 변수 연결 및 음원 추가(hashMap)
        for (int i = 0; i < soundSource.length ; i++) {
            soundSource[i] =  soundPool.load(context, R.raw.sound_0 +i, 1 +i);
        }
    }

    public static void playSoundWin(){
        soundPool.play(soundSource[0],1, 1, 1, 0, 1.3f);
    }

    public static void playSoundDraw(){
        soundPool.play(soundSource[3],1, 1, 1, 0, 1.3f);
    }

    public static void playSoundLose(){
        soundPool.play(soundSource[1],1, 1, 1, 0, 1.3f);
    }

    public void playButtonSound() {
        soundPool.play(soundSource[2],1, 1, 1, 0, 1.3f);
    }

    public static void playSoundStop(){
        soundPool.stop( soundSource[0]);
        soundPool.stop( soundSource[1]);
        soundPool.stop( soundSource[2]);
        soundPool.stop( soundSource[3]);
        soundPool.release();
    }

}
