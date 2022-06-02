package com.example.neosavings.ui.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.neosavings.ui.DAO.Converters;
import com.example.neosavings.ui.DAO.CuentaDAO;
import com.example.neosavings.ui.Modelo.Categoria;
import com.example.neosavings.ui.Modelo.PagoProgramado;
import com.example.neosavings.ui.Modelo.Presupuesto;
import com.example.neosavings.ui.Modelo.Registro;
import com.example.neosavings.ui.Modelo.Usuario;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Usuario.class, Registro.class, Categoria.class, Presupuesto.class, PagoProgramado.class}, version = 11,exportSchema = false)
@TypeConverters({Converters.class})
    public abstract class DatabaseNeosavings extends RoomDatabase {
        public abstract CuentaDAO userDao();

        private static final String DATABASE_NAME = "User_DB";

        private static DatabaseNeosavings INSTANCE;

        private static final int THREADS = 4;

        public static final ExecutorService dbExecutor = Executors.newFixedThreadPool(THREADS);

        public static DatabaseNeosavings getInstance(final Context context) {
            if (INSTANCE == null) {
                synchronized (DatabaseNeosavings.class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                                context.getApplicationContext(), DatabaseNeosavings.class,
                                DATABASE_NAME)
                                .fallbackToDestructiveMigration().build();
                    }
                }
            }
            return INSTANCE;
        }
    }

