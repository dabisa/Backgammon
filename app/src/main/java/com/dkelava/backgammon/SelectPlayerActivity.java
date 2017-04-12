package com.dkelava.backgammon;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dkelava.backgammon.databinding.PlayerInfoItemBinding;

import java.util.ArrayList;

public class SelectPlayerActivity extends AppCompatActivity {

    private final int RESULT_SELECTED = 0;
    private final int RESULT_CANCELED = 1;
    private final String SELECTED_PLAYER_EXTRA = "selected_player";

    private ListView playersListView;
    private TextView playersDescriptionView;
    private Button selectPlayerButton;

    private String playerName;

    PlayerDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_player);

        dbHelper = new PlayerDatabaseHelper(getApplicationContext());
        final Cursor cursor = dbHelper.getAllPlayers();

        playersListView = (ListView) findViewById(R.id.players_list_view);
        playersDescriptionView = (TextView) findViewById(R.id.player_description);
        selectPlayerButton = (Button) findViewById(R.id.select_player_button);

        PlayerInfoAdapter itemsAdapter = new PlayerInfoAdapter(this, cursor);
        playersListView.setAdapter(itemsAdapter);

        playerName = getIntent().getStringExtra(SELECTED_PLAYER_EXTRA);
        //int selection =
        //playersListView.setSelection(selection);


        playersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView titleTextView = (TextView) view.findViewById(R.id.player_title);
                playerName = titleTextView.getText().toString();
                view.setSelected(true);
                playersDescriptionView.setText(getDescription(playerName));
            }
        });

        selectPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra(SELECTED_PLAYER_EXTRA, playerName);
                setResult(RESULT_SELECTED, data);
                finish();
            }
        });
    }

    private String getDescription(String title)
    {
        PlayerInfo info = dbHelper.getPlayerInfo(title);
        return info.description;
    }


    public static class DataBindingAdatpers {

        @BindingAdapter("android:src")
        public static void setImageResource(ImageView imageView, int resource){
            imageView.setImageResource(resource);
        }
    }


    private class PlayerInfoAdapter extends CursorAdapter {

        private Cursor cursor;
        private LayoutInflater inflater;

        public PlayerInfoAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
            this.cursor = cursor;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            PlayerInfoItemBinding binding = DataBindingUtil.getBinding(view);
            setData(binding, cursor);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            if (inflater == null) {
                inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            PlayerInfoItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.player_info_item, parent, false);
            setData(binding, cursor);
            return binding.getRoot();
        }

        private void setData(PlayerInfoItemBinding binding, Cursor cursor) {
            binding.setInfo(PlayerInfo.load(cursor));
        }
    }
}
