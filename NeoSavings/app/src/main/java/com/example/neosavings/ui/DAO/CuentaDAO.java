package com.example.neosavings.ui.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.neosavings.ui.Modelo.Categoria;
import com.example.neosavings.ui.Modelo.Cuenta;
import com.example.neosavings.ui.Modelo.Presupuesto;
import com.example.neosavings.ui.Modelo.Registro;
import com.example.neosavings.ui.Modelo.Usuario;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CuentaDAO {

    ///USUARIOS
    @Query("SELECT * FROM Usuario")
    public Flowable<List<Usuario>> getAll();

    @Query("SELECT * FROM Usuario")
    LiveData<List<Usuario>> getAllLD();

    @Query("SELECT * FROM Usuario WHERE userID IN (:userIds)")
    List<Usuario> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM Usuario WHERE Usuario.Usuario LIKE :first LIMIT 1")
    Usuario findByName(String first);

    @Query("SELECT * FROM Usuario WHERE Usuario.userID = :userID LIMIT 1")
    Flowable<Usuario> findUsuarioByID(long userID);

    @Transaction
    @Query("SELECT * FROM Usuario")
    public Flowable<List<Cuenta>> getALLCuentasFW();

    @Insert
    void insertAll(Usuario... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Usuario users);

    @Update
    void update(Usuario user);


    @Delete
    void delete(Usuario user);

    @Query("DELETE FROM Usuario")
    void deleteALLUsuarios();

    @Query("DELETE FROM Usuario where Usuario.userID=:UserID")
    void deleteUsuario(long UserID);

    ///REGISTROS

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Registro registro);

    @Insert
    void insertAll(Registro... registros);

    @Update
    void update(Registro registro);

    @Delete
    void deleteRegistro(Registro registro);

    @Query("DELETE FROM Registro where Registro.RegistroID=:registroID")
    void deleteRegistro(long registroID);


    @Query("SELECT * FROM Registro")
    public Flowable<List<Registro>> getAllRegistros();

    @Query("SELECT * FROM Registro")
    LiveData<List<Registro>> getAllLDRegistros();

    @Query("SELECT * FROM Registro where Registro.RegistroID=:registroID Limit 1")
    Flowable<Registro> getRegistroByID(long registroID);

    @Query("SELECT * FROM Registro where Registro.Fecha BETWEEN :fechaIni AND :fechaFin")
    Flowable<List<Registro>> getALLRegistroByFecha(Date fechaIni,Date fechaFin);

    @Query("SELECT * FROM Registro where Registro.Categoria=:categoria AND RegistroUserID=:UserID AND Registro.Fecha BETWEEN :fechaIni AND :fechaFin")
    Flowable<List<Registro>> getALLRegistroByCategoriaByUsuarioBetweenDate(String categoria,long UserID,Date fechaIni, Date fechaFin);

    @Query("SELECT * FROM Registro where Registro.Categoria=:categoria AND RegistroUserID=:UserID AND Registro.Fecha <=:fechaFin")
    Flowable<List<Registro>> getALLRegistroByCategoriaByUsuarioUntilDate(String categoria,long UserID, Date fechaFin);

    @Query("SELECT * FROM Registro where Registro.Categoria=:categoria AND Registro.Fecha <=:fechaFin")
    Flowable<List<Registro>> getALLRegistroByCategoriaUntilDate(String categoria,Date fechaFin);

    @Query("SELECT * FROM Registro where Registro.Fecha <=:fechaFin")
    Flowable<List<Registro>> getALLRegistroUntilDate(Date fechaFin);


    @Query("DELETE FROM Registro")
    void deleteALLRegistros();

    ///CATEGORIA

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Categoria categoria);

    @Insert
    void insertAll(Categoria... categorias);

    @Update
    void update(Categoria categoria);

    @Delete
    void delete(Categoria categoria);

    @Query("DELETE FROM Categoria")
    void deleteALLCategoria();

    @Query("SELECT * FROM Categoria")
    public Flowable<List<Categoria>> getAllCategorias();

    @Query("SELECT * FROM Categoria where Categoria.Tipo=:GASTOINGRESO")
    public Flowable<List<Categoria>> getAllCategoriasGastosIngresos(String GASTOINGRESO);

    ///Presupuestos

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Presupuesto presupuesto);


    @Update
    void update(Presupuesto presupuesto);

    @Delete
    void delete(Presupuesto presupuesto);

    @Query("DELETE FROM Presupuesto where Presupuesto.PresupuestoID=:PresupuestoID")
    void deletePresupuesto(long PresupuestoID);

    @Query("DELETE FROM Presupuesto")
    void deleteALLPresupuesto();

    @Query("SELECT * FROM Presupuesto")
    public Flowable<List<Presupuesto>> getAllPresupuestos();

    @Query("SELECT * FROM Presupuesto where Presupuesto.PresupuestoID=:PresupuestoID Limit 1")
    public Flowable<Presupuesto> getPresupuestosByID(long PresupuestoID);

    @Query("SELECT * FROM Registro where Registro.Categoria=:Categoria AND RegistroUserID=:UserID AND Registro.Fecha Between :FechaIni AND :FechaFin")
    public Flowable<List<Registro>> getAllRegistrosPresupuesto(String Categoria,long UserID,Date FechaIni,Date FechaFin);

    @Query("SELECT * FROM Registro where Registro.Categoria=:Categoria AND Registro.Fecha Between :FechaIni AND :FechaFin")
    public Flowable<List<Registro>> getAllRegistrosPresupuestoAllUsers(String Categoria,Date FechaIni,Date FechaFin);

    @Query("SELECT * FROM Registro where RegistroUserID=:UserID AND Registro.Fecha Between :FechaIni AND :FechaFin")
    public Flowable<List<Registro>> getAllRegistrosPresupuestoAllCategorias(long UserID,Date FechaIni,Date FechaFin);




}
