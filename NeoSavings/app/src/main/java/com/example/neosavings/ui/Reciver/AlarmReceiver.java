package com.example.neosavings.ui.Reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.neosavings.ui.Database.UsuarioRepository;

import java.util.concurrent.TimeUnit;

public class AlarmReceiver extends BroadcastReceiver {
    private Context context;
    UsuarioRepository mRepository;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        mRepository=new UsuarioRepository(context);

        if (intent.getAction()!=null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
           setWorker();
        }
    }

    public void setWorker(){
        PeriodicWorkRequest saveRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class,15, TimeUnit.MINUTES)
                .addTag("notificaciones")
                .build();

        WorkManager workManager=WorkManager.getInstance(context);
        workManager.enqueueUniquePeriodicWork(
                "notificaciones",
                ExistingPeriodicWorkPolicy.KEEP,
                saveRequest);

    }
}
