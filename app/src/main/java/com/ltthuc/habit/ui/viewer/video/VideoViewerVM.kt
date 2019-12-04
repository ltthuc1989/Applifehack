package com.ltthuc.habit.ui.viewer.video

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.media.MediaPlayer
import android.media.MediaSync
import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager

import com.ltthuc.habit.data.AppDataManager
import com.ltthuc.habit.ui.fragment.videolist.VideoListNav
import javax.inject.Inject

class VideoViewerVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<VideoViewerNav, String>(schedulerProvider, connectionManager) {


    fun playVideo(context: Activity, url: String?, video: VideoView) {
        navigator?.showProgress()
        try {
            val mediaController = MediaController(context)
            mediaController.setAnchorView(video)
            video.setMediaController(mediaController)
            video.setVideoURI(Uri.parse(url))

        } catch (e: Exception) {

        }
        video.setOnPreparedListener {
            navigator?.hideProgress()
            it.start()

        }
        video.setOnErrorListener { mp, code, extra ->

            navigator?.hideProgress()
            navigator?.showAlert("Playback error with code:$code , extras $extra")
            false
        }
    }

}





