package com.example.neosavings.ui.PagosProgramados;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.neosavings.R;
import com.example.neosavings.ui.Adapters.placeholder.ItemClickListener;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Modelo.PagoProgramado;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class PagoProgramadoFragment extends Fragment implements ItemClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    static List<PagoProgramado> ListaPagosProgramados;
    private UsuarioRepository mRepository;
    private MyPagoProgramadoRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PagoProgramadoFragment(){
    }

    public PagoProgramadoFragment(List<PagoProgramado> items) {
        ListaPagosProgramados= new ArrayList<>();
        ListaPagosProgramados.addAll(items);
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PagoProgramadoFragment newInstance(int columnCount) {
        PagoProgramadoFragment fragment = new PagoProgramadoFragment(ListaPagosProgramados);
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SwipeRefreshLayout srLayout=(SwipeRefreshLayout) inflater.inflate(R.layout.fragment_item_list_pagoprogramado,container,false);
        View view = srLayout.findViewById(R.id.list);

        mRepository=new UsuarioRepository(getContext());

        ItemTouchHelper.SimpleCallback myCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {



            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("ALERTA");
                builder.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_warning,Resources.getSystem().newTheme()));
                builder.setMessage("¿Quiere eliminar todos los registros asignados a este pago programado, o quiere que los gastos asignados a este pago programado se mantengan guardados?");
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PagoProgramado p=ListaPagosProgramados.get(viewHolder.getAbsoluteAdapterPosition());
                        mRepository.DeleteRegistrosPagosProgramados(p.getPagoProgramadoID());
                        mRepository.DeletePagoProgramado(p);
                        ListaPagosProgramados.remove(viewHolder.getAbsoluteAdapterPosition());
                        adapter.notifyItemRemoved(viewHolder.getAbsoluteAdapterPosition());
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRepository.DeletePagoProgramado(ListaPagosProgramados.get(viewHolder.getAbsoluteAdapterPosition()));
                        ListaPagosProgramados.remove(viewHolder.getAbsoluteAdapterPosition());
                        adapter.notifyItemRemoved(viewHolder.getAbsoluteAdapterPosition());
                    }
                });
                builder.setCancelable(true);
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        adapter.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
                    }
                });
                builder.create();
                builder.show();

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Drawable trashBinIcon = getResources().getDrawable(R.drawable.ic_baseline_delete_forever_24, Resources.getSystem().newTheme());
                c.clipRect(0f,((float)viewHolder.itemView.getTop()),dX,(float)viewHolder.itemView.getBottom());
                if(dX < viewHolder.itemView.getWidth() / 3f)
                    c.drawColor(Color.GRAY);
                else
                    c.drawColor(Color.parseColor("#EF606C"));

                int textMargin=(int)getResources().getDimension(R.dimen.text_margin);
                trashBinIcon.setBounds(new Rect(textMargin,viewHolder.itemView.getTop()+textMargin,textMargin+trashBinIcon.getIntrinsicWidth()+30,viewHolder.itemView.getTop()+trashBinIcon.getIntrinsicHeight()+textMargin+30));
                trashBinIcon.draw(c);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper myHelper = new ItemTouchHelper(myCallback);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            adapter=new MyPagoProgramadoRecyclerViewAdapter(ListaPagosProgramados);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
            myHelper.attachToRecyclerView(recyclerView);
        }
        srLayout.setOnRefreshListener(()->{
            adapter.notifyDataSetChanged();
            srLayout.setRefreshing(false);
        });


        return srLayout;
    }

    public void setListaRefresh(List<PagoProgramado> items){
        ListaPagosProgramados.clear();
        ListaPagosProgramados.addAll(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view, int position) {
        PagoProgramado presupuesto=ListaPagosProgramados.get(position);
        Intent i = new Intent(getContext(), PagosProgramadosInfo.class);
        i.putExtra("PagoProgramadoID", presupuesto.getPagoProgramadoID());
        startActivity(i);
    }
}