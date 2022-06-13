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
import com.example.neosavings.ui.Modelo.PagosDeudas;
import com.google.common.util.concurrent.ListenableFuture;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;


public class NotificationWorkerDeudas extends Worker {

    Context context;
    private UsuarioRepository mRepository;
    private List<PagosDeudas> pagosDeudas;
    private List<Notification> Notificaciones;
    final String CHANNEL_ID_DEUDAS="Notificaciones Deudas";

    public NotificationWorkerDeudas(@NonNull Context context, @NonNull WorkerParameters workerParams) {
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
        Flowable<List<PagosDeudas>> allRegistroPagosProgramadoByID = mRepository.getAllRegistrosDeuda();
        pagosDeudas = allRegistroPagosProgramadoByID.blockingFirst();
        if (pagosDeudas == null) {
            pagosDeudas = new ArrayList<>();
        }

        Notificaciones = new ArrayList<>();
        String GROUP_KEY_WORK_PAGOPROGRAMADO = "com.android.example.WORK_PAGOPROGRAMADO";
        int SUMMARY_ID = 1;

        for (PagosDeudas r : pagosDeudas) {
            try {
                Calendar calendar=Calendar.getInstance();
                String FechaActual= new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
                Date fechaAct=new SimpleDateFormat("dd/MM/yyyy").parse(FechaActual);

                if (r.getDeuda().getFechaNotificacion().getTime()<=fechaAct.getTime()) {
                    if(!r.getDeuda().isNotificado()){
                        if(r.getDeuda().getFechaNotificacion().getTime()<=r.getDeuda().getFechaVencimiento().getTime()){
                            Double DeudaInicial=Double.valueOf(r.getDeuda().getCosteDeuda());
                            Double DeudaRestante=r.GetDeudaRestante();
                            if(DeudaRestante!=0){

                                Notificaciones.add(new NotificationCompat.Builder(context, CHANNEL_ID_DEUDAS)
                                        .setSmallIcon(R.drawable.naira_line_icon)
                                        .setContentTitle(r.getDeuda().getNombre())
                                        .setContentText("Se acerca la fecha de vencimiento para concluir esta Deuda/Préstamo")
                                        .setGroup(GROUP_KEY_WORK_PAGOPROGRAMADO)
                                        .setAutoCancel(true)
                                        .build());

                                Calendar calendar1=(Calendar) Calendar.getInstance().clone();
                                calendar1.setTime(r.getDeuda().getFechaNotificacion());
                                calendar1.add(Calendar.DAY_OF_YEAR,1);
                                String aux=new SimpleDateFormat("dd/MM/yyyy").format(calendar1.getTime());
                                r.getDeuda().setFechaNotificacion(new SimpleDateFormat("dd/MM/yyyy").parse(aux));
                                mRepository.Update(r.getDeuda());
                            }
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (Notificaciones.size() > 0) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                CharSequence name = "Notificaciones Deudas";
                String Description = "Notificaciones activadas al acercarse la fecha límite de una deuda o presupuesto no concluido";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID_DEUDAS, name, importance);
                mChannel.setDescription(Description);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setShowBadge(false);
                notificationManagerCompat.createNotificationChannel(mChannel);
            }

            Notification summaryNotification =
                    new NotificationCompat.Builder(context, CHANNEL_ID_DEUDAS)
                            .setContentTitle("NeoSavings")
                            //set content text to support devices running API level < 24
                            .setContentText("Se acerca la fecha límite de " + Notificaciones.size() + " Deudas")
                            .setSmallIcon(R.drawable.naira_line_icon)
                            //build summary info into InboxStyle template
                            .setStyle(new NotificationCompat.InboxStyle()
                                    .setSummaryText(Notificaciones.size() + " Deudas cerca de llegar a fecha límite sin concluir"))
                            //specify which group this notification belongs to
                            .setGroup(GROUP_KEY_WORK_PAGOPROGRAMADO)
                            //set this notification as the summary for the group
                            .setGroupSummary(true)
                            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                            .build();
            int index = 50;

            for (Notification n : Notificaciones) {
                notificationManagerCompat.notify(index, n);
                index++;
            }
            notificationManagerCompat.notify(SUMMARY_ID, summaryNotification);

        }

        return Result.success();
    }
}
