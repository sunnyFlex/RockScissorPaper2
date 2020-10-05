package com.example.rockscissorpaper2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    FrameLayout headerTitleView, headerHandView, gameOver;

    TextView resultTextView;
    ImageView titleView, comHandView, comHandResult, titleBackView, mainReadyView, mainResultView
              , playerHandView, playerHandResult, vsView, gameOverBackground;
    ImageButton scissorButton, rockButton, paperButton, playButton;
    Button backButton;

    AnimationDrawable titleHandAnim, playerHandAnim, mainPlayAnim, readyAnim;
    Animation mainShakeAnim, titleRotateAnim;

    PlayBgmSound playBgmSound = new PlayBgmSound();

    boolean isFirstInput = true;
    int playerHp = 100;
    int comHp = 100;
    int damage = 0;

    private static MediaPlayer mainBgm;
    private static MediaPlayer mainVoice;
    private static MediaPlayer playBgm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //***변수 연결***
        gameOver = findViewById(R.id.game_over);
        gameOverBackground = findViewById(R.id.game_over_background);
        headerHandView = findViewById(R.id.header_hand_view);
        headerTitleView = findViewById(R.id.header_title_view);
        vsView = findViewById(R.id.vs_view);
        titleView = findViewById(R.id.title_view);
        comHandView = findViewById(R.id.com_hand_view);
        comHandResult = findViewById(R.id.com_hand_result);
        playerHandView = findViewById(R.id.player_hand_view);
        playerHandResult = findViewById(R.id.player_hand_result);
        titleBackView = findViewById(R.id.title_back_view);
        mainReadyView = findViewById(R.id.main_ready_view);
        mainResultView = findViewById(R.id.main_result_view);
        resultTextView = findViewById(R.id.result_text_view);
        scissorButton = findViewById(R.id.scissor_button);
        backButton = findViewById(R.id.back_button);
        rockButton = findViewById(R.id.rock_button);
        paperButton = findViewById(R.id.paper_button);
        playButton = findViewById(R.id.play_button);

        //***animation 정의***
        // handAnimation(시퀀스 에니)
        playerHandAnim = (AnimationDrawable) playerHandView.getDrawable();
//        playerHandAnim.start();
        // title playerHandAnimation(시퀀스 에니)
        titleHandAnim = (AnimationDrawable) comHandView.getDrawable();
//        titleHandAnim.start();
        // main shakeAnimation(Move 에니)
        mainShakeAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake_anim);
//        mainResultView.startAnimation(mainShakeAnim);
        //title rotateAnimation(회전에니)
        titleRotateAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anim);
        titleBackView.startAnimation(titleRotateAnim);
        //readyAnimation(시퀀스 에니)
        readyAnim = (AnimationDrawable) mainReadyView.getDrawable();
        readyAnim.start();

        //***View Visible***
        startViewVisible();

        //***button 콜백 정의***
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backButtonClick(view);
            }
        });

        paperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getButtonClick(view);
            }
        });

        rockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getButtonClick(view);
            }
        });

        scissorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getButtonClick(view);
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playButtonClick(view);
            }
        });

        //***사운드***
        //playBgm 초기화
        playBgmSound.initSounds(getApplicationContext());
        //mainBgm 실행
        mainBgmStart();

        //***button disable 정의***
        getButtonDisable();

    }

    private void mainBgmStart() {
        mainVoice = MediaPlayer.create(getApplicationContext(),R.raw.sound_6);
        mainVoice.setLooping(false);
        mainVoice.start();

        mainBgm = MediaPlayer.create(getApplicationContext(),R.raw.sound_4);
        mainBgm.setLooping(true);
        mainBgm.start();
    }

    private void mainBgmStop() {
        mainVoice.stop();
        mainVoice.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                    mainVoice.release();
            }
        });

        mainBgm.stop();
        mainBgm.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                try{
                    mainBgm.release();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    //backButton 클릭시 정의
    private void backButtonClick(View view) {
        startViewVisible();
        titleView.setPadding(0,0,0,0);

        getButtonDisable();
        titleHandAnim.stop();
        playerHandAnim.stop();
        readyAnim.start();

        titleBackView.startAnimation(titleRotateAnim);
        titleView.setImageResource(R.drawable.title_front);

        playerHp = 100;
        comHp = 100;
        damage = 0;
        isFirstInput = true;

        playBgmSound.playSoundStop();
        setPlayBgmStop();
        mainBgmStart();
    }


    //***onCreate 이외 함수 정의***

    private void startViewVisible() {
        gameOver.setVisibility(View.GONE);
        headerTitleView.setVisibility(View.VISIBLE);
        headerHandView.setVisibility(View.GONE);
        mainReadyView.setVisibility(View.VISIBLE);
        mainResultView.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);
    }

    private void getButtonDisable() {
        rockButton.setEnabled(false);
        scissorButton.setEnabled(false);
        paperButton.setEnabled(false);
        playButton.setEnabled(true);
    }

    // playButton 클릭시 콜백 상세정의
    private void playButtonClick(View view) {
        getPlayAnimation();
        playButtonClickView();
        getProgressBar();

        rockButton.setEnabled(true);
        scissorButton.setEnabled(true);
        paperButton.setEnabled(true);
        playButton.setEnabled(false);

        vsView.setImageResource(R.drawable.vs);

        //bgm컨트롤
        mainBgmStop();
        setPlayBgmStart();
        playBgmSound.playButtonSound();
    }

    //player 가위바위보Button 클릭시 상세정의
    private void getButtonClick(View view) {
        int playerHands = Integer.parseInt(view.getTag().toString());
        int comHands = getComHand();

        getResult(playerHands, comHands);

        getPlayerHand(view);

        getResultHpProgressBar(playerHands, comHands);

        getButtonDisable();

        titleHandAnim.stop();
        playerHandAnim.stop();
    }

    private void getResultHpProgressBar(int playerHands, int comHands) {
        damage = 25;
        //player 가위바위보 승리시 HP
        if (getResult(playerHands, comHands) == 1){
            comHp = comHp - damage;
            ProgressBar comProgress = (ProgressBar) findViewById(R.id.com_progress) ;
            comProgress.setProgress(comHp) ;
            //player 승리 조건시
            if (comHp == 0){
                gameOver.setVisibility(View.VISIBLE);
                gameOverBackground.setImageResource(R.drawable.game_winner);
                resultTextView.setText("You Winner");
                resultTextView.setTextColor(0xFFffffff);
            }
            //com 승리시 HP
        }else if (getResult(playerHands, comHands) == 2){
            playerHp = playerHp - damage;
            ProgressBar playerProgress = (ProgressBar) findViewById(R.id.player_progress) ;
            playerProgress.setProgress(playerHp);
            //player 패배 조건시
            if (playerHp == 0){
                gameOver.setVisibility(View.VISIBLE);
                gameOverBackground.setImageResource(R.drawable.game_loser);
                resultTextView.setText("You Loser");
                resultTextView.setTextColor(0xFF636363);
            }
        }
    }

    //play화면 progressBar실행
    private void getProgressBar() {

        try {
            if (isFirstInput){

                if (playerHp < 0 || playerHp > 100) {  //값을 0~100까지 지정
                    Toast toast = Toast.makeText(MainActivity.this, "0 to 100 can be input.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    ProgressBar playerProgress = (ProgressBar) findViewById(R.id.player_progress) ;
                    playerProgress.setProgress(playerHp);
                    ProgressBar comProgress = (ProgressBar) findViewById(R.id.com_progress) ;
                    comProgress.setProgress(comHp) ;
                    isFirstInput = false;
                }
            }
        } catch (Exception e) {
            // 토스트(Toast) 메시지 표시.
            Toast toast = Toast.makeText(MainActivity.this, "Invalid number format",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //playButton 클릭시 view 정의
    private void playButtonClickView() {
        headerHandView.setVisibility(View.VISIBLE);
        vsView.setVisibility(View.VISIBLE);
        gameOver.setVisibility(View.GONE);
        comHandResult.setVisibility(View.GONE);
        playerHandResult.setVisibility(View.GONE);
        headerTitleView.setVisibility(View.GONE);
        mainResultView.setVisibility(View.VISIBLE);
        mainReadyView.setVisibility(View.GONE);
        backButton.setVisibility(View.VISIBLE);
        titleView.setPadding(0,0,0,0);
    }

    //player, com 가위바위보 결과 정의
    private int getResult(int playerHands, int comHands) {
        int result = (3 + playerHands - comHands)%3;
        switch (result){
            case 0:
                getPlayAnimation();
                vsView.setImageResource(R.drawable.draw);
                PlayBgmSound.playSoundDraw();
                break;
            case 1:
                mainResultView.startAnimation(mainShakeAnim);
                mainResultView.setImageResource(R.drawable.main_player_attack);
                vsView.setImageResource(R.drawable.win);
                PlayBgmSound.playSoundWin();
                break;
            case 2:
                mainResultView.startAnimation(mainShakeAnim);
                mainResultView.setImageResource(R.drawable.main_com_attack);
                vsView.setImageResource(R.drawable.lose);
                PlayBgmSound.playSoundLose();
                break;
        }
        return result;
    }

    //player button 클릭시 Visible view 에 이미지 출력
    private void getPlayerHand(View view) {
        playerHandResult.setVisibility(View.VISIBLE);

        switch (view.getTag().toString()){
            case "1":
                playerHandResult.setImageResource(R.drawable.player_scissor);
                break;
            case "2":
                playerHandResult.setImageResource(R.drawable.player_rock);
                break;
            case "3":
                playerHandResult.setImageResource(R.drawable.player_paper);
                break;
        }
    }

    //com 가위바위보 결과 랜덤추출 및 결과 이미지 출력 정의
    private int getComHand() {
        int randomComHand = new Random().nextInt(3) + 1;
        comHandResult.setVisibility(View.VISIBLE);

        switch (randomComHand){
            case 1:
                comHandResult.setImageResource(R.drawable.enemy_scissor);
                break;
            case 2:
                comHandResult.setImageResource(R.drawable.enemy_rock);
                break;
            case 3:
                comHandResult.setImageResource(R.drawable.enemy_paper);
                break;
        }
        return randomComHand;
    }

    //mainResultView mainPlayAnimation 정의
    private void getPlayAnimation() {
        mainResultView.setImageResource(R.drawable.play_anim);
        mainPlayAnim = (AnimationDrawable) mainResultView.getDrawable();
        mainPlayAnim.start();
        titleRotateAnim.reset();
        titleBackView.clearAnimation();
        titleHandAnim.start();
        playerHandAnim.start();
        mainReadyView.clearAnimation();
        mainShakeAnim.reset();
    }

    //playbutton 클릭후 반복되는 군중BGM mediaPlayer로 정의
    public void setPlayBgmStart() {
        playBgm = MediaPlayer.create(getApplicationContext(),R.raw.sound_5);
        playBgm.setLooping(true);
        playBgm.start();
        playBgm.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    //play시 반복되는 군중 BGM 종료 정의
    public void setPlayBgmStop(){
        playBgm.stop();
        playBgm.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                try{
                    mp.release();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    //***soundPool 사용설정***
    @Override
    protected void onStart() {
        super.onStart();
        PlayBgmSound.initSounds(getApplicationContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
        PlayBgmSound.playSoundStop();
    }

}