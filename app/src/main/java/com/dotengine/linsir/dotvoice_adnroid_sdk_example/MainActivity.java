package com.dotengine.linsir.dotvoice_adnroid_sdk_example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.dot.voice.DotVoice;
import cc.dot.voice.listener.DotVoiceListener;
import cc.dot.voice.mode.DotBitrateStat;
import cc.dot.voice.type.DotVoiceErrorType;

public class MainActivity extends AppCompatActivity implements DotVoiceListener {

    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.room_id) EditText roomId;
    @BindView(R.id.muted_local) Button mutedLocal;
    @BindView(R.id.muted_remote) Button mutedRemote;

    private DotVoice mDotVoice;

    private String mId;
    private final String appKey = "45";
    private final String app_secret = "dc5cabddba054ffe894ba79c2910866c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDotVoice = DotVoice.instance(this.getApplicationContext(), this);
        mId = "dot" + String.valueOf((int) (Math.random() * 1000000));
        userName.setText(mId);
        mutedLocal.setTag("true");
        mutedRemote.setTag("true");
    }


    @OnClick({R.id.join_room, R.id.want_speak, R.id.leave_room, R.id.muted_local, R.id.muted_remote, R.id.earpiece, R.id.phone_on}) public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.join_room:
                if (roomId.getText().toString().equals("")) {
                    showToast("请输入房间的名字");
                    return;
                }
                mDotVoice.startLocalMedia();
                mDotVoice.getToken(appKey, app_secret, roomId.getText().toString(), mId);
                break;
            case R.id.want_speak:
                mDotVoice.muteLocalAudio(true);
                break;
            case R.id.leave_room:
                mDotVoice.leaveRoom();
                break;
            case R.id.muted_local:
                if (mutedLocal.getTag().equals("true")) {
                    mDotVoice.muteLocalAudio(false);
                    mutedLocal.setTag("false");
                } else {
                    mDotVoice.muteLocalAudio(true);
                    mutedLocal.setTag("true");
                }
                break;
            case R.id.muted_remote:
                if (mutedRemote.getTag().equals("true")) {
                    mDotVoice.muteRemoteAudio(false);
                    mutedRemote.setTag("false");
                } else {
                    mDotVoice.muteRemoteAudio(true);
                    mutedRemote.setTag("true");
                }
                break;
            case R.id.earpiece:
                mDotVoice.enableSpeakerPhone(false);
                break;
            case R.id.phone_on:
                mDotVoice.enableSpeakerPhone(true);
                break;
        }
    }

    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override public void onGetToken(String s) {
        mDotVoice.joinRoom(s);
    }

    @Override public void onJoined(String s) {
        showToast("用户加入");
    }

    @Override public void onLeave() {
        showToast("用户离开");
    }

    @Override public void onOccurError(DotVoiceErrorType dotVoiceErrorType) {
        showToast("发生了错误：" + dotVoiceErrorType);
    }

    @Override public void onGotBitrateStat(String userId, DotBitrateStat stat) {
        Log.i("MainActivity", "用户的Id是-> " + userId + "   " + "用户的上行网速-> " + stat.getAudioBitrateSend());
        Log.i("MainActivity", "用户的Id是-> " + userId + "   " + "用户的下行网速-> " + stat.getAudioBitrateReceive());
    }

    @Override public void onGotAudioVolume(String userId, int volume) {
        Log.i("MainActivity", "用户的Id是-> " + userId + "   " + "用户的音量-> " + volume);
    }

    @Override public void onAddLocalAudio() {
        showToast("本地流加入");
    }

    @Override public void onMuteLocalAudio(boolean b) {
        showToast("本地静音/不静音" + b);
    }

    @Override public void onMuteRemoteAudio(boolean b) {
        showToast("远端静音/不静音" + b);
    }
}
