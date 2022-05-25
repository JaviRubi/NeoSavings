package com.example.neosavings.ui.Database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.neosavings.ui.DAO.CuentaDAO;
import com.example.neosavings.ui.Modelo.Categoria;
import com.example.neosavings.ui.Modelo.Cuenta;
import com.example.neosavings.ui.Modelo.Registro;
import com.example.neosavings.ui.Modelo.Usuario;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;

public class UsuarioRepository {

    private final LiveData<List<Usuario>> mUsuarioLists;
    private final CuentaDAO mUsuarioDao;
    private final DatabaseNeosavings db;

    public UsuarioRepository(Context context) {
        db = DatabaseNeosavings.getInstance(context);
        mUsuarioDao = db.userDao();
        mUsuarioLists = mUsuarioDao.getAllLD();
    }

    public LiveData<List<Usuario>> getAllUsers() {
        return mUsuarioDao.getAllLD();
    }

    public Flowable<List<Usuario>> getAllUsersFW() {
        return mUsuarioDao.getAll();
    }

    public Flowable<Usuario> getUserByID(long userID) {
        return mUsuarioDao.findUsuarioByID(userID);
    }

    public Flowable<List<Registro>> getAllRegistros() {
        return mUsuarioDao.getAllRegistros();
    }

    public Flowable<List<Registro>> getAllRegistrosbyFecha(Date FechaInicio, Date FechaFin) {
        return mUsuarioDao.getALLRegistroByFecha(FechaInicio,FechaFin);
    }


    public Flowable<List<Categoria>> getALLCategorias() {return mUsuarioDao.getAllCategorias(); }

    public Flowable<List<Categoria>> getALLCategoriasIngresos() {return mUsuarioDao.getAllCategoriasGastosIngresos("INGRESO"); }

    public Flowable<List<Categoria>> getALLCategoriasGastos() {return mUsuarioDao.getAllCategoriasGastosIngresos("GASTO"); }

    public Flowable<List<Cuenta>> getAllCuentasFW() {
        return mUsuarioDao.getALLCuentasFW();
    }

    public Flowable<Registro> getRegistroByID(long registroID) {
        return mUsuarioDao.getRegistroByID(registroID);
    }


    public void insert(Usuario user) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> mUsuarioDao.insert(user)
        );
    }

    public void insertRegistro(Registro registro) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> mUsuarioDao.insert(registro)
        );
    }

    public void insertCategoria(Categoria categoria) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> mUsuarioDao.insert(categoria)
        );
    }


    public void Update(Usuario user) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> mUsuarioDao.update(user)
        );
    }

    public void Update(Registro registro) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> mUsuarioDao.update(registro)
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

    public void DeleteUsuario(long user) {
        DatabaseNeosavings.dbExecutor.execute(
                () -> mUsuarioDao.deleteUsuario(user)
        );
    }

}
