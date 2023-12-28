package com.applifehack.knowledge.data.firebase


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.applifehack.knowledge.R
import com.applifehack.knowledge.ui.activity.splash.SplashActivity
import com.applifehack.knowledge.util.AppBundleKey
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.NotificationTarget
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var payload : String? = null
    private val CHANNEL_ID: String = "Knowledge"
    private val NOTIFICATION_ID = -1






    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {





        // Check if message contains a data payload.
        remoteMessage?.data?.isNotEmpty()?.let {
            Log.d(javaClass.simpleName, "Message data payload: " + remoteMessage.data)
            val  payload = getPayLoad(remoteMessage.data)



            sendQuoteNotification(applicationContext, payload)

        }

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
       // Log.d(javaClass.simpleName, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(token)
    }
    // [END on_new_token]


    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(javaClass.simpleName, "Short lived task is done.")
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */


    private fun sendQuoteNotification(context: Context, result: PayloadResult) {

        val intent = Intent(context, SplashActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)



        intent?.putExtra(AppBundleKey.KEY_NOTIFICATION,result)
        val remoteView = getRemoveViewArticle(context,result)
        val pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentTitle(result.title)
            .setContentText(result.message)
            .setContentIntent(pendingIntent)
            .setWhen(System.currentTimeMillis())
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContent(remoteView)
            .setPriority(Notification.PRIORITY_HIGH)
        val notificationManager: NotificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            notificationManager.createNotificationChannel(channel)
        }
        val notification = notificationBuilder.build()
        if(result.pushStyle==0) {
            notificationManager.notify(NOTIFICATION_ID, notification)
        }else{
            try {
                loadImage(context, remoteView, notification, result?.imageUrl!!)
            }catch (ex:Exception){
                ex.message?.let { Log.d("exce", it) }
            }
        }


    }




    private  fun getRemoveViewArticle(context: Context, payload: PayloadResult):RemoteViews{

        val remoteViews =
            RemoteViews(context.getPackageName(), R.layout.view_push_notification)



        remoteViews.setTextViewText(R.id.remoteview_notification_headline, payload.message)
        remoteViews.setTextViewText(R.id.remoteview_notification_short_message, payload.title)
        if(payload.postType == "quote"){
            remoteViews.setViewVisibility(R.id.bigPicture,View.GONE)
        }else if(payload.postType == "article"){
            if(payload.pushStyle == 1){
                remoteViews.setViewVisibility(R.id.bigPicture,View.VISIBLE)
            }
        } else if(payload.postType == "video"){
            payload.imageUrl = payload.imageUrl?.replace("hqdefault", "maxresdefault")
            remoteViews.setViewVisibility(R.id.bigPicture,View.VISIBLE)
        }
        return  remoteViews
    }

    private fun loadImage(context: Context,remoteView: RemoteViews,notification: Notification,url:String){
        val target = NotificationTarget(
            context,
            R.id.bigPicture,
            remoteView,
            notification,
            NOTIFICATION_ID)

        Glide.with(context.applicationContext)
            .asBitmap()
            .load(url)
            .into(target)

    }

    private fun getPayLoad(map:Map<String,String>):PayloadResult{

        return PayloadResult().apply {
            title = map["title"]
            postId = map["postId"]
            imageUrl = map["imageUrl"]
            message = map["message"]
            postType = map["postType"]
            pushStyle = map["pushStyle"]?.toInt()!!

        }

    }



}