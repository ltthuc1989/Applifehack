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
import androidx.core.app.NotificationCompat

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.applifehack.knowledge.KnowledgeApp
import com.applifehack.knowledge.R
import com.applifehack.knowledge.ui.activity.splash.SplashActivity
import com.applifehack.knowledge.util.AppBundleKey
import javax.inject.Inject


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var payload : String? = null
    private val CHANNEL_ID: String = "Knowledge"

    @Inject lateinit var firebaseAnalyticsHelper: FirebaseAnalyticsHelper




    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        var event ="notification_receive"
        firebaseAnalyticsHelper?.logEvent(event,event,"notification")
        if(!(applicationContext as KnowledgeApp).isBackGround){
            return
        }
        Log.d(javaClass.simpleName, "From: ${remoteMessage?.from}")

        // Check if message contains a data payload.
        remoteMessage?.data?.isNotEmpty()?.let {
            Log.d(javaClass.simpleName, "Message data payload: " + remoteMessage.data)
            payload = remoteMessage.data.toString()

        }

        // Check if message contains a notification payload.
        remoteMessage?.notification?.let {
            Log.d(javaClass.simpleName, "Message Notification Body: ${it.body}")


                sendNotification(applicationContext,it.body.toString(), payload,it?.title)

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    // [START on_new_token]
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(javaClass.simpleName, "Refreshed token: $token")

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


    private fun sendNotification(context: Context, messageBody: String, payload: String?,title:String?="") {
        var id: Int = -1
        val intent = Intent(context, SplashActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        try {

            Log.e(javaClass.simpleName, "messageBodyJson $payload")
         //   val json = JsonHelper.hasMapToJson(payload)
            val result = Gson().fromJson<PayloadResult>(payload, PayloadResult::class.java)
            intent?.putExtra(AppBundleKey.KEY_NOTIFICATION,result)
        }catch (ex:Exception){
             Log.d("pushyExce",ex.message)
        }



        val pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
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


        notificationManager.notify(id, notificationBuilder.build())

    }



}