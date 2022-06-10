package com.example.neosavings.ui.Estadisticas;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.neosavings.R;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Modelo.Categoria;
import com.example.neosavings.ui.Modelo.Cuenta;
import com.example.neosavings.ui.Modelo.Registro;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Flowable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EstadisticasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EstadisticasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

     List<Cuenta> ListaCuentas;
     List<Cuenta> ListaCuentasSelec;
     List<String> spinnerCuentas;
     ArrayAdapter<String> adapter;
     List<Categoria> ListaCategoriaGastos;

     HashMap<String,Double> ListaGastosCategorias;
     HashMap<String,Double> ListaIngresosCategorias;

     PieChart pieChart;
     PieChart pieChartIngresos;
     List<PieEntry> GastosCategorias;
     List<PieEntry> GastosCategoriasIngresos;
     PieDataSet pieDataSet;
     PieData pieData;

     UsuarioRepository mRepository;
     Spinner spinner_cuentas;

    Calendar calendar;
     TextView TextDate;
     TextView TextSaldoInicial;
     TextView TextSaldoFinal;
     TextView TextIngresos;
     TextView TextGastos;
     TextView TextTotal;

     View Vista;


    public EstadisticasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Estadisticas.
     */
    // TODO: Rename and change types and number of parameters
    public static EstadisticasFragment newInstance(String param1, String param2) {
        EstadisticasFragment fragment = new EstadisticasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_estadisticas, container, false);

        calendar=Calendar.getInstance();
        ListaCuentasSelec=new ArrayList<>();
        ListaGastosCategorias=new HashMap<>();
        ListaIngresosCategorias=new HashMap<>();
        TextDate= view.findViewById(R.id.textViewDate);
        TextDate.setText(getMonth()+" "+String.valueOf(calendar.get(Calendar.YEAR)));

        pieChart= view.findViewById(R.id.FramePieChart);
        pieChartIngresos=view.findViewById(R.id.FramePieChartIngresos);
        GastosCategorias=new ArrayList<>();
        GastosCategoriasIngresos=new ArrayList<>();

        TextSaldoInicial=view.findViewById(R.id.textViewSaldoInicial);
        TextSaldoFinal=view.findViewById(R.id.textViewSaldoFinal);
        TextIngresos=view.findViewById(R.id.textViewIngresos);
        TextGastos=view.findViewById(R.id.textViewGastos);
        TextTotal=view.findViewById(R.id.textViewTotal);

        view.findViewById(R.id.imageButtonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH,-1);
                TextDate.setText(getMonth()+" "+String.valueOf(calendar.get(Calendar.YEAR)));
                actualizarVista();
            }
        });

        view.findViewById(R.id.imageButtonNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(calendar.get(Calendar.MONTH)!=Calendar.getInstance().get(Calendar.MONTH)) {
                    calendar.add(Calendar.MONTH, +1);
                    TextDate.setText(getMonth()+" "+String.valueOf(calendar.get(Calendar.YEAR)));
                    actualizarVista();
                }

            }
        });

        ///Spinner Usuarios
        mRepository=new UsuarioRepository(getContext());
        Flowable<List<Cuenta>> CuentasBlock=mRepository.getAllCuentasFW();
        ListaCuentas=CuentasBlock.blockingFirst();

        spinnerCuentas=new ArrayList<>();
        spinnerCuentas.add("TODOS");

        if(ListaCuentas!=null) {
            if(ListaCuentas.size()!=0) {
                for (Cuenta u : ListaCuentas) {
                    spinnerCuentas.add(u.getUser().getUsuario());
                }
            }else{
                spinnerCuentas.clear();
                spinnerCuentas.add("NO HAY CUENTAS");
            }
        }

        spinner_cuentas=(Spinner) view.findViewById(R.id.spinnerCuentasEstadisticas);
        adapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCuentas);
        spinner_cuentas.setAdapter(adapter);
        spinner_cuentas.setSelection(0);

        spinner_cuentas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_cuentas.getSelectedItemPosition();
                actualizarVista();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        actualizarVista();







        return view;
    }

    public String getMonth(){
        switch (calendar.get(Calendar.MONTH)){
            case 0: return "Enero";

            case 1: return "Febrero";

            case 2: return "Marzo";

            case 3: return "Abril";

            case 4: return "Mayo";

            case 5: return "Junio";

            case 6: return "Julio";

            case 7: return "Agosto";

            case 8: return "Septiembre";

            case 9: return "Octubre";

            case 10: return "Noviembre";

            case 11: return "Diciembre";

        }
        return "";
    }



    public void actualizarVista(){
        if(spinner_cuentas.getSelectedItemPosition()==0){
            ListaCuentasSelec.clear();
            ListaCuentasSelec.addAll(ListaCuentas);
        }else{
            ListaCuentasSelec.clear();
            ListaCuentasSelec.add(ListaCuentas.get(spinner_cuentas.getSelectedItemPosition()-1));
        }
        ListaGastosCategorias.clear();
        ListaIngresosCategorias.clear();

        Double SaldoInical= Double.valueOf(0);
        Double SaldoFinal= Double.valueOf(0);
        Double Ingresos= Double.valueOf(0);
        Double Gastos= Double.valueOf(0);

        Calendar CalendarioInicioMes= (Calendar) calendar.clone();
        Calendar CalendarioFinalMes= Calendar.getInstance();
        Calendar CalendarioRegistro= (Calendar) calendar.clone();

        CalendarioInicioMes.set(Calendar.DAY_OF_MONTH,1);
        int actualMaximum;
        actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        CalendarioFinalMes.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),actualMaximum);
        String fechaIni=new SimpleDateFormat("dd/MM/yyyy").format(CalendarioInicioMes.getTime());
        String fechaFin=new SimpleDateFormat("dd/MM/yyyy").format(CalendarioFinalMes.getTime());

        Date FechaInicioMes=new Date();
        Date FechaFinalMes=new Date();
        try {
            FechaInicioMes=new SimpleDateFormat("dd/MM/yyyy").parse(fechaIni);
            FechaFinalMes=new SimpleDateFormat("dd/MM/yyyy").parse(fechaFin);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(Cuenta c:ListaCuentasSelec){
            if(c.getMinDate().getTime()>=FechaFinalMes.getTime()){
                SaldoInical=SaldoInical+0;
            }else {
                SaldoInical = SaldoInical + Double.valueOf(c.getUser().getValor());
            }

            for (Registro r:c.Registros){


                if(r.getFecha().getTime()<(FechaInicioMes.getTime())){

                    if(r.isGasto()){
                        SaldoInical=SaldoInical-Double.valueOf(r.getCoste());
                    }else{
                        SaldoInical=SaldoInical+Double.valueOf(r.getCoste());
                    }

                }

                if((r.getFecha().getTime()>=FechaInicioMes.getTime() && r.getFecha().getTime()<=FechaFinalMes.getTime())){

                    if(r.isGasto()){
                        Gastos=Gastos+Double.valueOf(r.getCoste());
                        if(ListaGastosCategorias.containsKey(r.getCategoria())){
                            Double aux=ListaGastosCategorias.get(r.getCategoria());
                            ListaGastosCategorias.replace(r.getCategoria(),aux+Double.valueOf(r.getCoste()));
                        }else{
                            ListaGastosCategorias.putIfAbsent(r.getCategoria(),Double.valueOf(r.getCoste()));
                        }
                    }else{
                        Ingresos=Ingresos+Double.valueOf(r.getCoste());
                        if(ListaIngresosCategorias.containsKey(r.getCategoria())){
                            Double aux=ListaIngresosCategorias.get(r.getCategoria());
                            ListaIngresosCategorias.replace(r.getCategoria(),aux+Double.valueOf(r.getCoste()));
                        }else{
                            ListaIngresosCategorias.putIfAbsent(r.getCategoria(),Double.valueOf(r.getCoste()));
                        }
                    }



                }

            }

        }

        DecimalFormat formato=new DecimalFormat("#,###.### €");

        TextSaldoInicial.setText(formato.format(SaldoInical));
        TextGastos.setText(formato.format(Gastos));
        TextIngresos.setText(formato.format(Ingresos));
        TextTotal.setText(formato.format(Ingresos-Gastos));
        TextSaldoFinal.setText(formato.format(SaldoInical+(Ingresos-Gastos)));

        pieChart.clear();
        pieChartIngresos.clear();

        GastosCategorias.clear();
        GastosCategoriasIngresos.clear();
        if(!ListaGastosCategorias.isEmpty()){
            for(String key:ListaGastosCategorias.keySet()){
                GastosCategorias.add(new PieEntry(ListaGastosCategorias.get(key).floatValue(),key));
            }
        }else{
            GastosCategorias.add(new PieEntry(0,"Ningun Gasto"));
        }
        pieDataSet=new PieDataSet(GastosCategorias,"GASTOS");
        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        pieDataSet.setValueTextColor(Color.LTGRAY);
        pieDataSet.setValueTextSize(16f);

        pieData =new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("GASTOS");
        pieChart.animate();

        if(!ListaIngresosCategorias.isEmpty()){
            for(String key:ListaIngresosCategorias.keySet()){
                GastosCategoriasIngresos.add(new PieEntry(ListaIngresosCategorias.get(key).floatValue(),key));
            }
        }else{
            GastosCategoriasIngresos.add(new PieEntry(0,"Ningun Ingreso"));
        }

        pieDataSet=new PieDataSet(GastosCategoriasIngresos,"INGRESOS");
        pieDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        pieData =new PieData(pieDataSet);

        pieChartIngresos.setData(pieData);
        pieChartIngresos.getDescription().setEnabled(false);
        pieChartIngresos.setCenterText("INGRESOS");
        pieChartIngresos.animate();
        pieChart.getLegend().setWordWrapEnabled(true);
        pieChartIngresos.getLegend().setWordWrapEnabled(true);
        pieChart.getLegend().setTextColor(ColorTemplate.getHoloBlue());
        pieChartIngresos.getLegend().setTextColor(ColorTemplate.getHoloBlue());


    }
}