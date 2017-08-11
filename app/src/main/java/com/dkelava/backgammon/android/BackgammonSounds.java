package com.dkelava.backgammon.android;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

public enum BackgammonSounds {
    WhiteBlotHit(R.raw.chip_lay),
    BlackBlotHit(R.raw.chip_lay),
    LayChip(R.raw.chip_lay),
    Roll(R.raw.roll),
    Victory(R.raw.victory),
    NoMoves(R.raw.no_moves);

    private static List<MediaPlayer> _media = new ArrayList<>();
    private int _resid;

    BackgammonSounds(int resid) {
        _resid = resid;
    }

    public static void load(Context context) {
        release();
        for (BackgammonSounds sound : BackgammonSounds.values()) {
            MediaPlayer player = MediaPlayer.create(context, sound._resid);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            _media.add(player);
        }
    }

    public static void release() {
        for (MediaPlayer player : _media) {
            if (player != null) {
                player.release();
            }
        }
        _media.clear();
    }

    public void play() {
        _media.get(this.ordinal()).start();
    }
}
