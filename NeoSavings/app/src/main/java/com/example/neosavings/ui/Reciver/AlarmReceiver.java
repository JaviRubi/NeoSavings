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
        PeriodicWorkRequest NotificacionesPagosProgramados = new PeriodicWorkRequest.Builder(NotificationWorker.class,4, TimeUnit.HOURS)
                .addTag("notificaciones PagosProgramados")
                .setInitialDelay(2,TimeUnit.HOURS)
                .build();

        PeriodicWorkRequest NotificacionesDeudas = new PeriodicWorkRequest.Builder(NotificationWorkerDeudas.class,3, TimeUnit.HOURS)
                .addTag("notificaciones Deudas")
                .build();

        WorkManager workManager=WorkManager.getInstance(context);
        workManager.enqueueUniquePeriodicWork(
                "notificaciones Deudas",
                ExistingPeriodicWorkPolicy.KEEP,
                NotificacionesDeudas);

        workManager.enqueueUniquePeriodicWork(
                "notificaciones PagosProgramados",
                ExistingPeriodicWorkPolicy.KEEP,
                NotificacionesPagosProgramados);

    }
}
