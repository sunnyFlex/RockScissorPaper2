package com.example.rockscissorpaper2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ImageView titleView, titleHandView, mainReadyView, mainResultView;
    ImageButton scissorButton, rockButton, paperButton, playButton;

    AnimationDrawable titleHandAnim, mainPlayAnim;
    Animation mainReadyAnim, mainShakeAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //변수 정의
        titleView = findViewById(R.id.title_view);
        titleHandView = findViewById(R.id.com_hand_view);
        mainReadyView = findViewById(R.id.main_ready_view);
        mainResultView = findViewById(R.id.main_result_view);
        scissorButton = findViewById(R.id.scissor_button);
        rockButton = findViewById(R.id.rock_button);
        paperButton = findViewById(R.id.paper_button);
        playButton = findViewById(R.id.play_button);

        //***animation 정의***
        // main readyAnimation(scale 에니)
        mainReadyAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scale_anim);
        mainReadyView.startAnimation(mainReadyAnim);

        // title handAnimation(시퀀스 에니)
        titleHandAnim = (AnimationDrawable) titleHandView.getDrawable();
//        titleHandAnim.start();

        // main shakeAnimation(Move 에니)
        mainShakeAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake_anim);
//        mainResultView.startAnimation(mainShakeAnim);


        //button 콜백 정의
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


    }


    //player 가위바위보Button 클릭시 상세정의
    private void getButtonClick(View view) {
        mainResultView.setVisibility(View.VISIBLE);
        mainReadyView.setVisibility(View.GONE);
        titleView.setVisibility(View.VISIBLE);
        titleHandView.setVisibility(View.GONE);
        titleView.setPadding(80, 80, 80, 80);

        titleHandAnim.stop();
        mainPlayAnim.stop();

        int playerHands = Integer.parseInt(view.getTag().toString());
        int comHands = getComHand();

        getResult(playerHands, comHands);
    }


    // playButton 클릭시 콜백 상세정의
    private void playButtonClick(View view) {
        //View visible
        mainResultView.setVisibility(View.VISIBLE);
        mainReadyView.setVisibility(View.GONE);
        titleHandView.setVisibility(View.VISIBLE);
        titleView.setVisibility(View.GONE);
        titleView.setPadding(0,0,0,0);

        getPlayAnimation();

        titleHandAnim.start();
        mainReadyView.clearAnimation();
        mainShakeAnim.reset();
    }

    //player, com 가위바위보 결과 정의
    private void getResult(int playerHands, int comHands) {
        int result = (3 + playerHands - comHands)%3;
        switch (result){
            case 0:
                getPlayAnimation();
                Toast.makeText(this, "비겼습니다.", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                mainResultView.startAnimation(mainShakeAnim);
                mainResultView.setImageResource(R.drawable.main_player_attack);
                Toast.makeText(this, "당신이 이겼습니다.추카추카", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                mainResultView.startAnimation(mainShakeAnim);
                mainResultView.setImageResource(R.drawable.main_com_attack);
                Toast.makeText(this, "당신이 졌습니다.ㅠㅠ", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //com 랜덤숫자로 가위바위보 정의
    private int getComHand() {
        int randomComHand = new Random().nextInt(3) + 1;
        switch (randomComHand){
            case 1:
                titleView.setImageResource(R.drawable.enemy_scissor);
                break;
            case 2:
                titleView.setImageResource(R.drawable.enemy_rock);
                break;
            case 3:
                titleView.setImageResource(R.drawable.enemy_paper);
                break;
        }
        return randomComHand;
    }

    //mainResultView mainPlayAnimation 정의
    private void getPlayAnimation() {
        mainResultView.setImageResource(R.drawable.play_anim);
        mainPlayAnim = (AnimationDrawable) mainResultView.getDrawable();
        mainPlayAnim.start();
    }


}