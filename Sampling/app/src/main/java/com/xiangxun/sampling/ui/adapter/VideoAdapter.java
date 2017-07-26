package com.xiangxun.sampling.ui.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.ParentAdapter;
import com.xiangxun.sampling.base.SystemCfg;

import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/10.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:现场采集页面，适配器效果，存放图片和视频文件。
 */
public class VideoAdapter extends ParentAdapter<String> implements OnPreparedListener, OnErrorListener, OnCompletionListener {
    private MediaController mediaco;

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setVolume(0, 0);
        mp.start();
        mp.setLooping(true);
    }


    public VideoAdapter(List<String> data, int resID, Context context) {
        super(data, resID, context);
    }


    @Override
    public View HockView(final int position, View view, ViewGroup parent, int resID, Context context, String info) {
        ViewHocker hocker = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resID, null);
            hocker = new ViewHocker();
            hocker.relative = (RelativeLayout) view.findViewById(R.id.id_video_iv_relative);
            hocker.relative.setLayoutParams(new AbsListView.LayoutParams(SystemCfg.getWidth(context) / 2, SystemCfg.getWidth(context) / 2));
            hocker.videoView = (VideoView) view.findViewById(R.id.id_video_recorder);
            hocker.image = (ImageView) view.findViewById(R.id.id_video_image);
            hocker.close = (ImageView) view.findViewById(R.id.id_video_iv_close);
            hocker.desc = (TextView) view.findViewById(R.id.id_video_tv_desc);
            view.setTag(hocker);
        } else {
            hocker = (ViewHocker) view.getTag();
        }
        hocker.close.setVisibility(View.GONE);
        mediaco = new MediaController(context);
        mediaco.setVisibility(View.INVISIBLE);
        hocker.videoView.setVisibility(View.VISIBLE);
        hocker.image.setVisibility(View.GONE);
        hocker.desc.setText("");
        hocker.videoView.setVideoPath(info);
        hocker.videoView.setMediaController(mediaco);
        hocker.videoView.setOnPreparedListener(this);
        hocker.videoView.setOnErrorListener(this);
        hocker.videoView.setOnCompletionListener(this);
        mediaco.setMediaPlayer(hocker.videoView);
        //让VideiView获取焦点
        hocker.videoView.requestFocus();
        hocker.videoView.start();
        return view;
    }

    class ViewHocker {
        RelativeLayout relative;
        VideoView videoView;
        ImageView image;
        ImageView close;
        TextView desc;
    }
}
