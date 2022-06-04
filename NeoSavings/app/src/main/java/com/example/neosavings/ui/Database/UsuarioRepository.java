package com.example.neosavings.ui.Database;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.neosavings.R;
import com.example.neosavings.ui.DAO.CuentaDAO;
import com.example.neosavings.ui.Modelo.Categoria;
import com.example.neosavings.ui.Modelo.Cuenta;
import com.example.neosavings.ui.Modelo.PagoProgramado;
import com.example.neosavings.ui.Modelo.Presupuesto;
import com.example.neosavings.ui.Modelo.Registro;
import com.example.neosavings.ui.Modelo.RegistrosPagosProgramados;
import com.example.neosavings.ui.Modelo.Usuario;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;

public class UsuarioRepository {

    private static final String CHANNEL_ID = "Presupuestos";
    private final CuentaDAO mUsuarioDao;
    private final DatabaseNeosavings db;
    private final Context context;
    long id=-1;

    public UsuarioRepository(Context context) {
        db = DatabaseNeosavings.getInstance(context);
        mUsuarioDao = db.userDao();
        this.context=context;
    }

    public Flowable<List<PagoProgramado>> getAllPagosProgramados() { return mUsuarioDao.getAllPagosProgramadosFW(); }

    public Flowable<List<PagoProgramado>> getAllPagosProgramadosByGasto(boolean isGasto) { return mUsuarioDao.getPagoProgramadoByGasto(isGasto); }

    public Flowable<List<Usuario>> getAllUsersFW() {
        return mUsuarioDao.getAll();
    }

    public Flowable<Usuario> getUserByID(long userID) { return mUsuarioDao.findUsuarioByID(userID); }

    public Flowable<List<Registro>> getAllRegistros() {
        return mUsuarioDao.getAllRegistros();
    }

    public Flowable<List<Registro>> getAllRegistrosbyFecha(Date FechaInicio, Date FechaFin) { return mUsuarioDao.getALLRegistroByFecha(FechaInicio,FechaFin); }

    public Flowable<List<Categoria>> getALLCategorias() {return mUsuarioDao.getAllCategorias(); }

    public Flowable<List<Categoria>> getALLCategoriasIngresos() {return mUsuarioDao.getAllCategoriasGastosIngresos("INGRESO"); }

    public Flowable<List<Categoria>> getALLCategoriasGastos() {return mUsuarioDao.getAllCategoriasGastosIngresos("GASTO"); }

    public Flowable<List<Cuenta>> getAllCuentasFW() {
        return mUsuarioDao.getALLCuentasFW();
    }

    public Flowable<List<Presupuesto>> getAllPresupuestosFW() {
        return mUsuarioDao.getAllPresupuestos();
    }

    public Flowable<Presupuesto> getPresupuestosByIDFW(long PresupuestoID) {
        return mUsuarioDao.getPresupuestosByID(PresupuestoID);
    }

    public Flowable<Registro> getRegistroByID(long registroID) {
        return mUsuarioDao.getRegistroByID(registroID);
    }

    public Flowable<PagoProgramado> getPagoProgramadoByID(Integer PagoProgramadoID) {
        return mUsuarioDao.getPagoProgramadoByID(PagoProgramadoID);
    }

    public Flowable<RegistrosPagosProgramados> getRegistroPagosProgramadoByID(Integer PagoProgramadoID) {
        return mUsuarioDao.getRegistroPagosProgramadoByIDFW(PagoProgramadoID);
    }

    public Flowable<List<RegistrosPagosProgramados>> getAllRegistroPagosProgramadoByID() {
        return mUsuarioDao.getALLRegistrosPagosProgramadosFW();
    }



    public void insert(Usuario user) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> mUsuarioDao.insert(user)
        );
    }

    public void insertPagoProgramado(PagoProgramado user) {

        DatabaseNeosavings.dbExecutor.execute(
                () -> {
                    long id=-1;
                    id=mUsuarioDao.insert(user);
                    user.setPagoProgramadoID((int) id);
                    try {
                        user.crearRegistrosIniciales(context);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
        );
    }

    public void insertRegistro(Registro registro) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> {
                    mUsuarioDao.insert(registro);
                }
        );
        NotificarPresupuestos();
    }

    public void insertCategoria(Categoria categoria) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> mUsuarioDao.insert(categoria)
        );
    }

    public void insertPresupuesto(Presupuesto presupuesto) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> mUsuarioDao.insert(presupuesto)
        );
    }




    public void Update(Usuario user) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> mUsuarioDao.update(user)
        );
    }

    public void Update(Registro registro) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> {
                    mUsuarioDao.update(registro);
                }
        );
        NotificarPresupuestos();
    }

    public void Update(Presupuesto presupuesto) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> {
                    mUsuarioDao.update(presupuesto);
                }
        );
        NotificarPresupuestos();
    }

    public void Update(PagoProgramado presupuesto) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> mUsuarioDao.update(presupuesto)
        );
    }


    public void DeleteUsuario(Usuario user) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> mUsuarioDao.delete(user)
        );
    }

    public void DeleteRegistro(long registro) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> mUsuarioDao.deleteRegistro(registro)
        );
    }

    public void DeleteRegistro(Registro registro) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> mUsuarioDao.deleteRegistro(registro)
        );
    }

    public void DeletePresupuesto(long presupuesto) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> mUsuarioDao.deletePresupuesto(presupuesto)
        );
    }

    public void DeletePresupuesto(Presupuesto presupuesto) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> mUsuarioDao.delete(presupuesto)
        );
    }

    public void DeletePagoProgramado(Integer presupuesto) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> mUsuarioDao.deletePagoProgramado(presupuesto)
        );
    }

    public void DeleteRegistrosPagosProgramados(Integer PagoProgramadoID) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> mUsuarioDao.deleteAllRegistrosPagoProgramado(PagoProgramadoID)
        );
    }


    public void DeletePagoProgramado(PagoProgramado presupuesto) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> mUsuarioDao.delete(presupuesto)
        );
    }

    public void DeleteUsuario(long user) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> mUsuarioDao.deleteUsuario(user)
        );
    }

    public void NotificarPresupuestos(){
        String GROUP_KEY_WORK_PRESUPUESTO = "com.android.example.WORK_PRESUPUESTO";
        int SUMMARY_ID = 0;
        List<Notification> Notificaciones = new ArrayList<>();

        Notification newMessageNotification;
        Flowable<List<Presupuesto>> allPresupuestosFW = getAllPresupuestosFW();
        List<Presupuesto> presupuestos = allPresupuestosFW.blockingFirst();
        if(presupuestos==null){
            presupuestos=new ArrayList<>();
        }

        for (Presupuesto p : presupuestos){
            Double presupuesto = Double.valueOf(p.getPresupuesto());
            Double PresupuestoActual = p.presupuestoHastaAhora(context);

            if(!p.isNotificado() && presupuesto<PresupuestoActual){
                    Notificaciones.add( new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setSmallIcon(R.drawable.naira_line_icon)
                            .setContentTitle(p.getName())
                            .setContentText("Ha superado el presupuesto configurado.")
                            .setGroup(GROUP_KEY_WORK_PRESUPUESTO)
                            .build());
                    p.setNotificado(true);
                    Update(p);
            }

        }

        if(Notificaciones.size()>0){
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                CharSequence name = "Notificaciones Presupuestos";
                String Description = "Notificaciones activadas al sobrepasar un presupuesto";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mChannel.setDescription(Description);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setShowBadge(false);
                notificationManagerCompat.createNotificationChannel(mChannel);
            }
            Notification summaryNotification =
                    new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setContentTitle("NeoSavings")
                            //set content text to support devices running API level < 24
                            .setContentText("Ha sobrepasado "+Notificaciones.size()+" Presupuestos")
                            .setSmallIcon(R.drawable.naira_line_icon)
                            //build summary info into InboxStyle template
                            .setStyle(new NotificationCompat.InboxStyle()
                                    .setSummaryText(Notificaciones.size()+" presupuestos sobrepasados"))
                            //specify which group this notification belongs to
                            .setGroup(GROUP_KEY_WORK_PRESUPUESTO)
                            //set this notification as the summary for the group
                            .setGroupSummary(true)
                            .build();
            int index=1;

            for (Notification n: Notificaciones){
                notificationManagerCompat.notify(index,n);
                index++;
            }
            notificationManagerCompat.notify(SUMMARY_ID,summaryNotification);
        }

    }

}
