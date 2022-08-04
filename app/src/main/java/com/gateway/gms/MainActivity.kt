package com.gateway.gms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gateway.gms.di.GMServiceLocator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CoroutineScope(Dispatchers.IO).launch{
            with(GMServiceLocator.cloudMessagingRepository) {
                this.subscribeToTopic("Technology").collect {

                    Timber.d(it.toString().substringAfter('$'))
                }
            }
        }
    }
}