package com.example.neosavings.ui.Reciver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.neosavings.R;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Modelo.RegistrosPagosProgramados;
import com.google.common.util.concurrent.ListenableFuture;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;


public class NotificationWorker extends Worker {

    Context context;
    private UsuarioRepository mRepository;
    private List<RegistrosPagosProgramados> PagosProgramados;
    private List<Notification> Notificaciones;
    final String CHANNEL_ID_PAGOSPROGRAMADOS="Notificaciones Pagos Programados";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
    }

    @NonNull
    @Override
    public ListenableFuture<ForegroundInfo> getForegroundInfoAsync() {
        return super.getForegroundInfoAsync();
    }

    @NonNull
    @Override
    public Result doWork() {

        mRepository=new UsuarioRepository(context);
        Flowable<List<RegistrosPagosProgramados>> allRegistroPagosProgramadoByID = mRepository.getAllRegistroPagosProgramadoByID();
        PagosProgramados = allRegistroPagosProgramadoByID.blockingFirst();
        if (PagosProgramados == null) {
            PagosProgramados = new ArrayList<>();
        }

        Notificaciones = new ArrayList<>();
        String GROUP_KEY_WORK_PAGOPROGRAMADO = "com.android.example.WORK_PAGOPROGRAMADO";
        int SUMMARY_ID = 1;

        for (RegistrosPagosProgramados r : PagosProgramados) {
            try {
                boolean GastoNuevo = r.GenerarRegistrosHastaFechaActual(context);

                if (GastoNuevo) {
                    if (r.getPagoProgramado().isRecordatorio()) {
                        Notificaciones.add(new NotificationCompat.Builder(context, CHANNEL_ID_PAGOSPROGRAMADOS)
                                .setSmallIcon(R.drawable.naira_line_icon)
                                .setContentTitle(r.getPagoProgramado().getNombre())
                                .setContentText("Se ha generado un nuevo Registro automático")
                                .setGroup(GROUP_KEY_WORK_PAGOPROGRAMADO)
                                .build());
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (Notificaciones.size() > 0) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                CharSequence name = "Notificaciones Pagos Programados";
                String Description = "Notificaciones activadas al generarse un nuevo registro automático";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID_PAGOSPROGRAMADOS, name, importance);
                mChannel.setDescription(Description);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setShowBadge(false);
                notificationManagerCompat.createNotificationChannel(mChannel);
            }
            Notification summaryNotification =
                    new NotificationCompat.Builder(context, CHANNEL_ID_PAGOSPROGRAMADOS)
                            .setContentTitle("NeoSavings")
                            //set content text to support devices running API level < 24
                            .setContentText("Se han actualizado " + Notificaciones.size() + " Pagos Programados")
                            .setSmallIcon(R.drawable.naira_line_icon)
                            //build summary info into InboxStyle template
                            .setStyle(new NotificationCompat.InboxStyle()
                                    .setSummaryText(Notificaciones.size() + " Pagos Programados Actualizados"))
                            //specify which group this notification belongs to
                            .setGroup(GROUP_KEY_WORK_PAGOPROGRAMADO)
                            //set this notification as the summary for the group
                            .setGroupSummary(true)
                            .build();
            int index = 500;

            for (Notification n : Notificaciones) {
                notificationManagerCompat.notify(index, n);
                index++;
            }
            notificationManagerCompat.notify(SUMMARY_ID, summaryNotification);

            mRepository.NotificarPresupuestos();
        }

        return Result.success();
    }
}
