package com.dkelava.backgammon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkelava.bglib.game.actions.actions.AcceptDoubleAction;
import com.dkelava.bglib.game.actions.actions.Action;
import com.dkelava.bglib.game.actions.actions.ClearDiceAction;
import com.dkelava.bglib.game.actions.actions.DoubleStakeAction;
import com.dkelava.bglib.game.actions.actions.InitialRollAction;
import com.dkelava.bglib.game.actions.actions.MoveAction;
import com.dkelava.bglib.game.actions.actions.RejectDoubleAction;
import com.dkelava.bglib.game.actions.actions.RollAction;
import com.dkelava.bglib.game.Game;
import com.dkelava.bglib.game.Player;
import com.dkelava.bglib.game.PlayerInterface;
import com.dkelava.bglib.model.Backgammon;
import com.dkelava.bglib.model.BackgammonState;
import com.dkelava.bglib.model.Color;
import com.dkelava.bglib.model.DiceSet;
import com.dkelava.bglib.model.Die;
import com.dkelava.bglib.model.MoveNode;
import com.dkelava.bglib.model.Point;
import com.dkelava.bglib.model.PointState;
import com.dkelava.bglib.model.Status;
import com.dkelava.bglib.nn.BackgammonNeuralNetwork;
import com.dkelava.bglib.nn.SimpleInputCoder;
import com.dkelava.bglib.nn.SimpleOutputCoder;
import com.dkelava.bglib.strategy.Strategy;

import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private final String PLAYER_ONE_EXTRA = "player_one";
    private final String PLAYER_TWO_EXTRA = "player_two";
    private final String GAME_STATE_EXTRA = "game_state";

    private Game backgammon = new Game(new MyObserver());
    private GuiPlayer guiPlayer = new GuiPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        View rollButton = findViewById(R.id.roll_button);
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guiPlayer.rollDice();
            }
        });

        View singleRollButton = findViewById(R.id.single_roll_button);
        singleRollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guiPlayer.rollSingleDie();
            }
        });

        View doubleStakeButton = findViewById(R.id.double_stake_button);
        doubleStakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guiPlayer.doubleStake();
            }
        });

        View acceptButton = findViewById(R.id.accept_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guiPlayer.accept();
            }
        });

        View rejectButton = findViewById(R.id.reject_button);
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guiPlayer.reject();
            }
        });

        View undoButton = findViewById(R.id.undo_button);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgammon.undo();
            }
        });

        View redoButton = findViewById(R.id.redo_button);
        redoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    backgammon.redo();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        View diceView = findViewById(R.id.dice);
        diceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guiPlayer.pickUpdice();
            }
        });

        for (final Point point : Point.values()) {
            View pointView = getPointView(point);
            pointView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guiPlayer.selectPoint(point);
                }
            });
        }

        BackgammonSounds.load(getApplicationContext());

        String playerOneName = getIntent().getStringExtra(PLAYER_ONE_EXTRA);
        String playerTwoName = getIntent().getStringExtra(PLAYER_TWO_EXTRA);
        Bundle gameState = getIntent().getBundleExtra(GAME_STATE_EXTRA);
        backgammon.setPlayer(Color.White, createPlayer(playerOneName));
        backgammon.setPlayer(Color.Black, createPlayer(playerTwoName));
        backgammon.enable();
        if(gameState != null) {
            try {
                String data = gameState.getString(GAME_STATE_EXTRA);
                backgammon.loadGame(data);
            } catch (Exception ex) {
                ex.printStackTrace();
                backgammon.initialize();
            }
        } else if (savedInstanceState != null) {
            try {
                String data = savedInstanceState.getString(GAME_STATE_EXTRA);
                backgammon.loadGame(data);
            } catch (Exception ex) {
                ex.printStackTrace();
                backgammon.initialize();
            }
        } else {
            backgammon.initialize();
        }
    }

    @Override
    protected void onResume() {
        backgammon.enable();
        backgammon.next();
        // TODO: 24.2.2017. update on backgammon.enable() 
        updateBoardState(backgammon.getState());
        super.onResume();
    }

    @Override
    protected void onPause() {
        backgammon.disable();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        backgammon.removeObserver();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //BackgammonStateData.save(backgammon.getState(), outState);
        String gameState = backgammon.encode();
        outState.putString(GAME_STATE_EXTRA, gameState);
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        Bundle outState = new Bundle();
        //BackgammonStateData.save(backgammon.getState(), outState);
        String gameState = backgammon.encode();
        outState.putString(GAME_STATE_EXTRA, gameState);
        data.putExtra(GAME_STATE_EXTRA, outState);
        setResult(0, data);
        //finish();
        super.onBackPressed();
    }

    @Nullable
    private Player createPlayer(String name) {
        // TODO: 20.2.2017. load plyer from players content provider ????
        PlayerDatabaseHelper dbHelper = new PlayerDatabaseHelper(getApplicationContext());
        PlayerInfo info = dbHelper.getPlayerInfo(name);

        switch (info.type) {
            default:
            case Human:
                return guiPlayer;
            case NeuralNetwork:
                try {
                    BackgammonNeuralNetwork trainer = BackgammonNeuralNetwork.load(new SimpleInputCoder(), new SimpleOutputCoder(), getFileFromRawResource(getUri(info.resource)));
                    Strategy strategy = trainer.getStrategy();
                    return new StrategyPlayer(strategy);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    BackgammonNeuralNetwork trainer = BackgammonNeuralNetwork.create(new SimpleInputCoder(), new SimpleOutputCoder(), 80);
                    Strategy strategy = trainer.getStrategy();
                    return new StrategyPlayer(strategy);
                }
        }
    }

    Uri getUri(int resourceId) {
        Resources resources = getApplicationContext().getResources();
        Uri uri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(resourceId))
                .appendPath(resources.getResourceTypeName(resourceId))
                .appendPath(resources.getResourceEntryName(resourceId))
                .build();
        return uri;
    }

    private File getFileFromRawResource(Uri rUri) {
        String uri = rUri.toString();
        String fn;
        // I've only tested this with raw resources
        if (uri.contains("/raw/")) {
            // Try to get the resource name
            String[] parts = uri.split("/");
            fn = parts[parts.length - 1];
        } else {
            return null;
        }
        // Notice that I've hard-coded the file extension to .jpg
        // I was working with getting a File object of a JPEG image from my raw resources
        //String dest = Environment.getExternalStorageDirectory() + "/nn.dat";
        String dest = getApplicationContext().getFilesDir() + "/nn.dat";
        try {
            // Use reflection to get resource ID of the raw resource
            // as we need to get an InputStream to it
            // getResources(),openRawResource() takes only a resource ID
            R.raw r = new R.raw();
            Field frame = R.raw.class.getDeclaredField(fn);
            frame.setAccessible(true);
            int id = (Integer) frame.get(r);
            // Get the InputStream
            InputStream inputStream = getResources().openRawResource(id);
            FileOutputStream fileOutputStream = new FileOutputStream(dest);
            // IOUtils is a class from Apache Commons IO
            // It writes an InputStream to an OutputStream
            IOUtils.copy(inputStream, fileOutputStream);
            fileOutputStream.close();
            return new File(dest);
        } catch (NoSuchFieldException e) {
            Log.e("MyApp", "NoSuchFieldException in getFileFromRawResource()");
        } catch (IllegalAccessException e) {
            Log.e("MyApp", "IllegalAccessException in getFileFromRawResource()");
        } catch (FileNotFoundException e) {
            Log.e("MyApp", "FileNotFoundException in getFileFromRawResource()");
        } catch (IOException e) {
            Log.e("MyApp", "IOException in getFileFromRawResource()");
        }
        return null;
    }


    private View getPointView(Point point) {
        switch (point) {
            case WhiteBar:
                return findViewById(R.id.white_bar);
            case WhiteHome:
                return findViewById(R.id.white_home);
            case BlackBar:
                return findViewById(R.id.black_bar);
            case BlackHome:
                return findViewById(R.id.black_home);
            case Point1:
                return findViewById(R.id.point1);
            case Point2:
                return findViewById(R.id.point2);
            case Point3:
                return findViewById(R.id.point3);
            case Point4:
                return findViewById(R.id.point4);
            case Point5:
                return findViewById(R.id.point5);
            case Point6:
                return findViewById(R.id.point6);
            case Point7:
                return findViewById(R.id.point7);
            case Point8:
                return findViewById(R.id.point8);
            case Point9:
                return findViewById(R.id.point9);
            case Point10:
                return findViewById(R.id.point10);
            case Point11:
                return findViewById(R.id.point11);
            case Point12:
                return findViewById(R.id.point12);
            case Point13:
                return findViewById(R.id.point13);
            case Point14:
                return findViewById(R.id.point14);
            case Point15:
                return findViewById(R.id.point15);
            case Point16:
                return findViewById(R.id.point16);
            case Point17:
                return findViewById(R.id.point17);
            case Point18:
                return findViewById(R.id.point18);
            case Point19:
                return findViewById(R.id.point19);
            case Point20:
                return findViewById(R.id.point20);
            case Point21:
                return findViewById(R.id.point21);
            case Point22:
                return findViewById(R.id.point22);
            case Point23:
                return findViewById(R.id.point23);
            case Point24:
                return findViewById(R.id.point24);
            default:
                return null;
        }
    }

    private int getDieImageResource(Die.Face die, boolean hasMovesLeft) {
        switch (die) {
            case Die1:
                return hasMovesLeft ? R.drawable.die1 : R.drawable.die1_disabled;
            case Die2:
                return hasMovesLeft ? R.drawable.die2 : R.drawable.die2_disabled;
            case Die3:
                return hasMovesLeft ? R.drawable.die3 : R.drawable.die3_disabled;
            case Die4:
                return hasMovesLeft ? R.drawable.die4 : R.drawable.die4_disabled;
            case Die5:
                return hasMovesLeft ? R.drawable.die5 : R.drawable.die5_disabled;
            case Die6:
                return hasMovesLeft ? R.drawable.die6 : R.drawable.die6_disabled;
            default:
                return 0;
        }
    }

    private View getCheckerView(View pointView, int checker) {
        // TODO: 7.3.2017. merge with other similar metod
        return (View) pointView.findViewWithTag(Integer.toString(checker));
    }

    private final String checkerContainerTag = "checker_container";

    private List<View> getCheckerViews(View pointView) {
        List<View> checkers = new LinkedList<>();
        ViewGroup container = (ViewGroup) pointView.findViewWithTag(checkerContainerTag);
        int numChildren = container.getChildCount();
        for(int i = 0; i < numChildren; ++i) {
            checkers.add(pointView.findViewWithTag(Integer.toString(i+1)));
        }
        return checkers;
    }

    private void setPointState(Point point, PointState state) {
        View pointView = getPointView(point);

        ViewGroup checkerContainer = (ViewGroup) pointView.findViewWithTag(checkerContainerTag);
        int visibleSlots = checkerContainer.getChildCount();
        View slots[] = new View[visibleSlots];
        for (int i = 0; i < visibleSlots; ++i) {
            slots[i] = getCheckerView(pointView, i + 1);
            slots[i].setBackgroundResource(0);
        }

        TextView textView = (TextView) pointView.findViewWithTag("counter");
        switch (state.getOwner()) {
            case White:
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blackText));
                break;
            case Black:
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.whiteText));
                break;
        }

        if (state.getQuantity() > visibleSlots) {
            textView.setText(Integer.toString(state.getQuantity()));
        } else {
            textView.setText("");
        }

        if (!state.isEmpty()) {
            int checkerId = state.isOwner(Color.White) ? R.drawable.selected_white_checker : R.drawable.selected_black_checker;
            int last = Math.min(state.getQuantity(), visibleSlots);
            for (int i = 0; i < last; ++i) {
                slots[i].setBackgroundResource(checkerId);
            }
        }
    }

    private void setPointStatus(Point point, PointStatus pointStatus) {
        View pointView = getPointView(point);
        List<View> checkerViews = getCheckerViews(pointView);

        View topCheckerView = null;
        for(View checkerView : checkerViews) {
            if(checkerView != null) {
                AnimationDrawable frameAnimation = (AnimationDrawable) checkerView.getBackground();
                if(frameAnimation != null) {
                    topCheckerView = checkerView;
                    frameAnimation.stop();
                    frameAnimation.selectDrawable(0);
                }
            }
        }
        if(pointStatus == PointStatus.Source && topCheckerView != null) {
            AnimationDrawable frameAnimation = (AnimationDrawable) topCheckerView.getBackground();
            frameAnimation.start();
        }

        switch (pointStatus) {
            default:
            case Source:
            case Normal:
                pointView.setBackgroundResource(0);
                break;

            case Target:
                pointView.setBackgroundResource(R.drawable.target_point);
                break;
        }
    }

    private void enableUserControls(BackgammonState state) {
        View rollButton = findViewById(R.id.roll_button);
        View singleRollButton = findViewById(R.id.single_roll_button);
        View doubleStakeButton = findViewById(R.id.double_stake_button);
        View acceptButton = findViewById(R.id.accept_button);
        View rejectButton = findViewById(R.id.reject_button);
        View diceView = findViewById(R.id.dice);

        Status status = state.getStatus();

        switch(status) {
            case Initial:
                singleRollButton.setVisibility(View.VISIBLE);
                rollButton.setVisibility(View.GONE);
                doubleStakeButton.setVisibility(View.GONE);
                diceView.setVisibility(View.VISIBLE);
                acceptButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);
                break;

            case Rolling:
                singleRollButton.setVisibility(View.GONE);
                rollButton.setVisibility(View.VISIBLE);
                if(state.getCubeOwner() == state.getCurrentPlayer() || state.getCubeOwner() == Color.None) {
                    doubleStakeButton.setVisibility(View.VISIBLE);
                }
                diceView.setVisibility(View.GONE);
                acceptButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);
                break;

            case DoubleStake:
                singleRollButton.setVisibility(View.GONE);
                rollButton.setVisibility(View.GONE);
                doubleStakeButton.setVisibility(View.GONE);
                diceView.setVisibility(View.GONE);
                acceptButton.setVisibility(View.VISIBLE);
                rejectButton.setVisibility(View.VISIBLE);
                break;

            default:
                singleRollButton.setVisibility(View.GONE);
                rollButton.setVisibility(View.GONE);
                doubleStakeButton.setVisibility(View.GONE);
                diceView.setVisibility(View.VISIBLE);
                acceptButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);
                break;
        }
    }

    private void disableUserControls() {
        View rollButton = findViewById(R.id.roll_button);
        View singleRollButton = findViewById(R.id.single_roll_button);
        View doubleStakeButton = findViewById(R.id.double_stake_button);
        View acceptButton = findViewById(R.id.accept_button);
        View rejectButton = findViewById(R.id.reject_button);

        singleRollButton.setVisibility(View.GONE);
        rollButton.setVisibility(View.GONE);
        doubleStakeButton.setVisibility(View.GONE);
        acceptButton.setVisibility(View.GONE);
        rejectButton.setVisibility(View.GONE);
    }

    private void updateBoardState(BackgammonState state) {
        // Set points
        for (Point point : Point.values()) {
            setPointState(point, state.getPointState(point));
        }

        updateUndoRedoControls(true);

        LinearLayout cubeHolder = (LinearLayout) findViewById(R.id.cube_holder);
        TextView cubeText = (TextView) findViewById(R.id.cube_text);

        switch(backgammon.getState().getCubeOwner()) {
            case None:
                cubeHolder.setGravity(Gravity.CENTER);
                break;

            case White:
                cubeHolder.setGravity(Gravity.CENTER|Gravity.BOTTOM);
                break;

            case Black:
                cubeHolder.setGravity(Gravity.CENTER|Gravity.TOP);
                break;
        }

        cubeText.setText(Integer.toString(backgammon.getState().getStake()));

        updateDiceState(state);
    }

    private void updateUndoRedoControls(boolean isEnabled) {
        View undoButton = findViewById(R.id.undo_button);
        View redoButton = findViewById(R.id.redo_button);
        if(backgammon.canUndo()) {
            undoButton.setVisibility(View.VISIBLE);
        } else {
            undoButton.setVisibility(View.GONE);
        }
        if(backgammon.canRedo()) {
            redoButton.setVisibility(View.VISIBLE);
        } else {
            redoButton.setVisibility(View.GONE);
        }

        undoButton.setEnabled(isEnabled);
        redoButton.setEnabled(isEnabled);
    }

    private void updateDiceState(BackgammonState state) {
        Status status = state.getStatus();

        View diceView = findViewById(R.id.dice);
        switch(status) {
            case Rolling:
            case DoubleStake:
                diceView.setVisibility(View.GONE);
                break;

            default:
                diceView.setVisibility(View.VISIBLE);
                break;
        }


        Die.Face die1 = state.getDieOne();
        Die.Face die2 = state.getDieTwo();

        ImageView die1View = (ImageView) findViewById(R.id.die1);
        ImageView die2View = (ImageView) findViewById(R.id.die2);
        ImageView die3View = (ImageView) findViewById(R.id.die3);
        ImageView die4View = (ImageView) findViewById(R.id.die4);

        MoveNode moves = state.getMoves();
        if(moves != null) {
            DiceSet diceSet = moves.getDiceState();
            if (die1 != die2) {
                die1View.setImageResource(0);
                die2View.setImageResource(getDieImageResource(die1, diceSet.getNumberOfMoves(die1) >= 1));
                die3View.setImageResource(getDieImageResource(die2, diceSet.getNumberOfMoves(die2) >= 1));
                die4View.setImageResource(0);
            } else {
                die1View.setImageResource(getDieImageResource(die1, diceSet.getNumberOfMoves(die1) >= 4));
                die2View.setImageResource(getDieImageResource(die1, diceSet.getNumberOfMoves(die1) >= 3));
                die3View.setImageResource(getDieImageResource(die1, diceSet.getNumberOfMoves(die1) >= 2));
                die4View.setImageResource(getDieImageResource(die1, diceSet.getNumberOfMoves(die1) >= 1));
            }
        } else {
            die1View.setImageResource(0);
            die2View.setImageResource(getDieImageResource(die1, true));
            die3View.setImageResource(getDieImageResource(die2, true));
            die4View.setImageResource(0);
        }
    }

    private View getCheckerView(Point point, int checker) {
        View pointView = getPointView(point);

        ViewGroup checkerContainer = (ViewGroup) pointView.findViewWithTag("checker_container");
        int visibleSlots = checkerContainer.getChildCount();
        int slot = Math.min(checker, visibleSlots);
        String tag = Integer.toString(slot);

        View checkerView = pointView.findViewWithTag(tag);
        return checkerView;
    }

    private float getXRelativeToBoard(View view) {
        float X = 0;
        View boardView = findViewById(R.id.board);
        View currentView = view;
        while (currentView != null && currentView != boardView) {
            X += currentView.getX();
            currentView = (View) currentView.getParent();
        }
        return X;
    }

    private float getYRelativeToBoard(View view) {
        float Y = 0;
        View boardView = findViewById(R.id.board);
        View currentView = view;
        while (currentView != null && currentView != boardView) {
            Y += currentView.getY();
            currentView = (View) currentView.getParent();
        }
        return Y;
    }

    private enum PointStatus {
        Normal,
        Source,
        Target
    }

    private class GuiPlayer implements Player {

        private PlayerInterface playerInterface;
        private Point selectedPoint = null;

        @Override
        public void onActivate(PlayerInterface backgammon) {
            this.playerInterface = backgammon;
            enableUserControls(backgammon.getState());
            setPointsStatus(backgammon.getState());
        }

        @Override
        public void onDeactivate() {
            this.playerInterface = null;
            disableUserControls();
            clearPointsStatus();
        }

        public void selectPoint(Point point) {
            // TODO: 18.2.2017. change name of this method: it does not only select but deselect
            if(playerInterface != null) {
                BackgammonState state = playerInterface.getState();
                if (state.getStatus() == Status.Moving) {
                    if (selectedPoint == point) {
                        selectedPoint = null;
                        setPointsStatus(playerInterface.getState());
                    } else if (selectedPoint != null && playerInterface.getState().getMoves().isMovable(selectedPoint, point)) {
                        execute(new MoveAction(selectedPoint, point));
                        selectedPoint = null;
                    } else if (playerInterface.getState().getMoves().isMovable(point)) {
                        selectedPoint = point;
                        setPointsStatus(playerInterface.getState());
                    }
                }
            }
        }

        private void setPointsStatus(BackgammonState state) {
            for (Point point : Point.values()) {
                if (state.getStatus() == Status.Moving) {
                    if (point == selectedPoint) {
                        setPointStatus(point, PointStatus.Source);
                    } else if (selectedPoint != null && state.getMoves().isMovable(selectedPoint, point)) {
                        setPointStatus(point, PointStatus.Target);
                    } else if (selectedPoint == null && state.getMoves().isMovable(point)) {
                        setPointStatus(point, PointStatus.Source);
                    } else {
                        setPointStatus(point, PointStatus.Normal);
                    }
                } else {
                    setPointStatus(point, PointStatus.Normal);
                }
            }
        }

        private void clearPointsStatus() {
            selectedPoint = null;
            for (Point point : Point.values()) {
                setPointStatus(point, PointStatus.Normal);
            }
        }

        public boolean doubleStake() {
            try {
                if(playerInterface != null && playerInterface.getState().getStatus() == Status.Rolling) {
                    return execute(new DoubleStakeAction());
                } else {
                    return false;
                }
            } catch(Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }

        public boolean accept() {
            try {
                if(playerInterface != null && playerInterface.getState().getStatus() == Status.DoubleStake) {
                    return execute(new AcceptDoubleAction());
                } else {
                    return false;
                }
            } catch(Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }

        public boolean reject() {
            try {
                if(playerInterface != null && playerInterface.getState().getStatus() == Status.DoubleStake) {
                    return execute(new RejectDoubleAction());
                } else {
                    return false;
                }
            } catch(Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }

        public boolean rollSingleDie() {
            try {
                if(playerInterface != null && playerInterface.getState().getStatus() == Status.Initial) {
                    return execute(new InitialRollAction());
                } else {
                    return false;
                }
            } catch(Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }

        public boolean rollDice() {
            try {
                if(playerInterface != null && playerInterface.getState().getStatus() == Status.Rolling) {
                    return execute(new RollAction());
                } else {
                    return false;
                }
            } catch(Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }

        public boolean pickUpdice() {
            try {
                if(playerInterface != null && (playerInterface.getState().getStatus() == Status.NoMoves)) {
                    return execute(new ClearDiceAction());
                } else {
                    return false;
                }
            } catch(Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }

        private boolean execute(Action action) {
            try {
                // TODO: 18.2.2017. return true only if move successful
                playerInterface.execute(action);
                return true;
            } catch(Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
    }

    private class MyObserver implements Game.Observer {

        @Override
        public void onInitialized(BackgammonState state) {
            updateBoardState(state);
            backgammon.next();
        }

        @Override
        public void onUpdate(BackgammonState state) {
            updateBoardState(state);
            backgammon.next();
        }

        @Override
        public void onInitialRoll(BackgammonState state) {
            updateBoardState(state);
            BackgammonSounds.Roll.play();
            if(state.getMoves() != null && state.getMoves().isEnd()) {
                BackgammonSounds.NoMoves.play();
            }
            backgammon.next();
        }

        @Override
        public void onUndoInitialRoll(BackgammonState state) {
            updateBoardState(state);
            backgammon.next();
        }

        @Override
        public void onRoll(BackgammonState state) {
            updateBoardState(state);
            BackgammonSounds.Roll.play();
            if(state.getMoves().isEnd()) {
                BackgammonSounds.NoMoves.play();
            }
            backgammon.next();
        }

        @Override
        public void onUndoRoll(BackgammonState state) {
            updateBoardState(state);
            backgammon.next();
        }

        @Override
        public void onDiceCleared(BackgammonState state) {
            updateBoardState(state);
            backgammon.next();
        }

        @Override
        public void onUndoDiceCleared(BackgammonState state, Die.Face prevDieOne, Die.Face prevDieTwo) {
            updateBoardState(state);
            backgammon.next();
        }

        @Override
        public void onMove(final BackgammonState state, Point source, Point destination, final boolean isHit) {

            final ImageView freeCheckerView = (ImageView) findViewById(R.id.free_checker);
            freeCheckerView.setVisibility(View.VISIBLE);
            switch(state.getCurrentPlayer()) {
                case White: freeCheckerView.setImageResource(R.drawable.white_checker); break;
                case Black: freeCheckerView.setImageResource(R.drawable.black_checker); break;
            }

            final View sourceCheckerView = getCheckerView(source, state.getPointState(source).getQuantity()+1);
            final View destinationCheckerView = getCheckerView(destination, state.getPointState(destination).getQuantity());

            float startX = getXRelativeToBoard(sourceCheckerView);
            float startY = getYRelativeToBoard(sourceCheckerView);

            float endX = getXRelativeToBoard(destinationCheckerView);
            float endY = getYRelativeToBoard(destinationCheckerView);

            ObjectAnimator animX = ObjectAnimator.ofFloat(freeCheckerView, "x", startX, endX);
            ObjectAnimator animY = ObjectAnimator.ofFloat(freeCheckerView, "y", startY, endY);
            AnimatorSet animSet = new AnimatorSet();
            animSet.playTogether(animX, animY);
            if(isHit) {
                Color opponent = state.getCurrentPlayer().getOpponent();
                Point bar = Point.getBar(opponent);
                final View barCheckerView = getCheckerView(bar, state.getPointState(bar).getQuantity());

                float startX2 = getXRelativeToBoard(destinationCheckerView);
                float startY2 = getYRelativeToBoard(destinationCheckerView);

                float endX2 = getXRelativeToBoard(barCheckerView);
                float endY2 = getYRelativeToBoard(barCheckerView);

                ObjectAnimator animX2 = ObjectAnimator.ofFloat(freeCheckerView, "x", startX2, endX2);
                ObjectAnimator animY2 = ObjectAnimator.ofFloat(freeCheckerView, "y", startY2, endY2);

                animSet.play(animX2).after(animX);
                animSet.play(animY2).after(animY);
            }


            setPointState(source, state.getPointState(source));
            for(Point point : Point.values()) {
                setPointStatus(point, PointStatus.Normal);
            }

            updateDiceState(state);
            updateUndoRedoControls(false);

            animSet.start();

            animX.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    switch(state.getCurrentPlayer().getOpponent()) {
                        case White: freeCheckerView.setImageResource(R.drawable.white_checker); break;
                        case Black: freeCheckerView.setImageResource(R.drawable.black_checker); break;
                    }
                }
            });

            animSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    switch (state.getWinner()) {
                        case White:
                            BackgammonSounds.Victory.play();
                            break;

                        case Black:
                            BackgammonSounds.Victory.play();
                            break;

                        default:
                            if (isHit) {
                                switch (state.getCurrentPlayer()) {
                                    case White:
                                        BackgammonSounds.BlackBlotHit.play();
                                        break;
                                    case Black:
                                        BackgammonSounds.WhiteBlotHit.play();
                                        break;
                                }
                            } else {
                                BackgammonSounds.LayChip.play();
                            }
                    }

                    freeCheckerView.setVisibility(View.GONE);
                    updateBoardState(state);
                    backgammon.next();
                }
            });

        }

        @Override
        public void onUndoMove(BackgammonState state, Point source, Point destination, boolean isHit) {
            updateBoardState(state);
            backgammon.next();
        }

        @Override
        public void onDouble(BackgammonState state) {
            updateBoardState(state);
            backgammon.next();
        }

        @Override
        public void onUndoDouble(BackgammonState state) {
            updateBoardState(state);
            backgammon.next();
        }

        @Override
        public void onDoubleAccepted(BackgammonState state) {
            updateBoardState(state);
            backgammon.next();
        }

        @Override
        public void onUndoDoubleAccepted(BackgammonState state) {
            updateBoardState(state);
            backgammon.next();
        }

        @Override
        public void onSurrender(BackgammonState state) {
            updateBoardState(state);
            backgammon.next();
        }

        @Override
        public void onUndoSurrender(BackgammonState state) {
            updateBoardState(state);
            backgammon.next();
        }
    }

    public class StrategyPlayer implements Player {

        private final Strategy strategy;

        StrategyPlayer(Strategy strategy) {
            this.strategy = strategy;
        }

        @Override
        public void onActivate(PlayerInterface playerInterface) {
            new PlayTask(playerInterface, strategy).execute();
        }

        @Override
        public void onDeactivate() {

        }

        private class PlayTask extends AsyncTask<Void, Void, Action> {

            private final PlayerInterface playerInterface;
            private final Backgammon backgammon;
            private final Strategy strategy;

            public PlayTask(PlayerInterface playerInterface, Strategy strategy) {
                this.playerInterface = playerInterface;
                this.backgammon = new Backgammon(playerInterface.getState());
                this.strategy = strategy;
            }

            @Override
            protected Action doInBackground(Void... voids) {
                return strategy.play(backgammon.getState());
            }

            @Override
            protected void onPostExecute(Action action) {
                if(action!= null) {
                    try {
                        playerInterface.execute(action);
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}
