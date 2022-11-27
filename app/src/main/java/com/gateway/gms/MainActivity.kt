package com.gateway.gms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gateway.gms.domain.interfaces.CloudMessagingServiceListener
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity(), CloudMessagingServiceListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val gmsManager = GMSManager(this, this)
        CoroutineScope(Dispatchers.IO).launch{
            with(gmsManager) {
                this.subscribeToTopic("Technology").collect {
                    Timber.d(it.toString().substringAfter('$'))
                }
            }
        }
        Timber.d(gmsManager.token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Timber.d(message.toString())
    }

    override fun onMessageReceived(message: com.huawei.hms.push.RemoteMessage?) {
        Timber.d(message.toString())
    }

    override fun onNewToken(token: String) {
        Timber.d(token)
    }
}
