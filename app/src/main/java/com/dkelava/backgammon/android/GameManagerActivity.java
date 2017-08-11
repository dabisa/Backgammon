package com.dkelava.backgammon.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class GameManagerActivity extends AppCompatActivity {

    private final int START_GAME = 0;
    private final int SELECT_WHITE_PLAYER = 1;
    private final int SELECT_BLACK_PLAYER = 2;

    private final String PLAYER_ONE_EXTRA = "player_one";
    private final String PLAYER_TWO_EXTRA = "player_two";
    private final String GAME_STATE_EXTRA = "game_state";

    // duplicated
    private final int RESULT_SELECTED = 0;
    private final int RESULT_CANCELED = 1;
    private final String SELECTED_PLAYER_EXTRA = "selected_player";

    private String playerOneName = "Human";
    private String playerTwoName = "Human";
    private Bundle gameState = null;

    private void updateStartResumeButtons() {
        View start = findViewById(R.id.start_game);
        View resume = findViewById(R.id.resume_game);

        if(isGameStarted()) {
            start.setVisibility(View.GONE);
            resume.setVisibility(View.VISIBLE);
        } else {
            start.setVisibility(View.VISIBLE);
            resume.setVisibility(View.GONE);
        }
    }


    private void updateWhitePlayerButton() {
        final Button playerOneButton = (Button) findViewById(R.id.white_player_button);
        final ImageView playerOneImageView = (ImageView) findViewById(R.id.white_player_avatar);
        playerOneImageView.setBackgroundResource(getAvatar(playerOneName));
        playerOneButton.setText(playerOneName);
    }

    private void updateBlackPlayerButton() {
        final Button playerTwoButton = (Button) findViewById(R.id.black_player_button);
        final ImageView playerTwoImageView = (ImageView) findViewById(R.id.black_player_avatar);
        playerTwoImageView.setBackgroundResource(getAvatar(playerTwoName));
        playerTwoButton.setText(playerTwoName);
    }

    private void startGame() {
        Intent intent = new Intent(GameManagerActivity.this, GameActivity.class);
        intent.putExtra(PLAYER_ONE_EXTRA, playerOneName);
        intent.putExtra(PLAYER_TWO_EXTRA, playerTwoName);
        if(isGameStarted()) {
            intent.putExtra(GAME_STATE_EXTRA, gameState);
        }
        startActivityForResult(intent, START_GAME);
    }

    private void selectPlayer(Player player, String playerName) {
        Intent intent = new Intent(GameManagerActivity.this, SelectPlayerActivity.class);
        intent.putExtra(SELECTED_PLAYER_EXTRA, playerName);

        switch (player) {
            case White:
                startActivityForResult(intent, SELECT_WHITE_PLAYER);
                break;
            case Black:
                startActivityForResult(intent, SELECT_BLACK_PLAYER);
                break;
        }
    }

    private boolean isGameStarted() {
        return gameState != null;
    }

    private int getAvatar(String playerName) {
        PlayerDatabaseHelper dbHelper = new PlayerDatabaseHelper(getApplicationContext());
        PlayerInfo playerInfo = dbHelper.getPlayerInfo(playerName);
        if(playerInfo != null) {
            switch (playerInfo.type) {
                case Human:
                    return R.drawable.human;
                case NeuralNetwork:
                    return R.drawable.droid;
                default:
                    return R.drawable.droid;
            }
        } else {
            return R.drawable.human;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_manager_activity);

        Button startButton = (Button) findViewById(R.id.start_game);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });

        Button resumeButton = (Button) findViewById(R.id.resume_game);
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });

        final Button playerOneButton = (Button) findViewById(R.id.white_player_button);
        playerOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPlayer(Player.White, playerOneName);

                /*
                List<String> playerTypes = ImmutableList.copyOf(getResources().getStringArray(R.array.player_types));
                int current = playerTypes.indexOf(playerOneName);
                playerOneName = playerTypes.get((current+1)%playerTypes.size());
                updateWhitePlayerButton();
                */
            }
        });

        final Button playerTwoButton = (Button) findViewById(R.id.black_player_button);
        playerTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPlayer(Player.Black, playerTwoName);

                /*
                List<String> playerTypes = ImmutableList.copyOf(getResources().getStringArray(R.array.player_types));
                int current = playerTypes.indexOf(playerTwoName);
                playerTwoName = playerTypes.get((current+1)%playerTypes.size());
                updateBlackPlayerButton();
                */
            }
        });

        final Button backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        updateWhitePlayerButton();
        updateBlackPlayerButton();
        updateStartResumeButtons();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case START_GAME:
                if(data != null) {
                    gameState = data.getBundleExtra(GAME_STATE_EXTRA);
                }
                updateStartResumeButtons();
            break;

            case SELECT_WHITE_PLAYER:
                if(resultCode == RESULT_SELECTED) {
                    if (data != null) {
                        playerOneName = data.getStringExtra(SELECTED_PLAYER_EXTRA);
                        if(playerOneName != null) {
                            updateWhitePlayerButton();
                        }
                    }
                }
                break;

            case SELECT_BLACK_PLAYER:
                if(resultCode == RESULT_SELECTED) {
                    if (data != null) {
                        playerTwoName = data.getStringExtra(SELECTED_PLAYER_EXTRA);
                        if(playerTwoName != null) {
                            updateBlackPlayerButton();
                        }
                    }
                }
                break;

            default:
        }
    }

    public enum Player {White, Black}
}
