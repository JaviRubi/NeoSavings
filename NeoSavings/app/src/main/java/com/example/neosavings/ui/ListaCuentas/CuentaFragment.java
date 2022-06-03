package com.example.neosavings.ui.ListaCuentas;

import android.content.Context;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.neosavings.R;
import com.example.neosavings.ui.Adapters.MyCuentaRecyclerViewAdapter;
import com.example.neosavings.ui.Adapters.placeholder.ItemClickListener;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Formularios.Formulario_Cuentas;
import com.example.neosavings.ui.Modelo.Cuenta;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

/**
 * A fragment representing a list of Items.
 */
public class CuentaFragment extends Fragment implements ItemClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private List<Cuenta> listaCuentas;
    private UsuarioRepository mRepository;
    private MyCuentaRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CuentaFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CuentaFragment newInstance(int columnCount) {
        CuentaFragment fragment = new CuentaFragment();
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

        SwipeRefreshLayout srLayout=(SwipeRefreshLayout) inflater.inflate(R.layout.fragment_item_cuenta,container,false);
        View view = srLayout.findViewById(R.id.list);
        mRepository=new UsuarioRepository(getContext());

        ItemTouchHelper.SimpleCallback myCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {



            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mRepository.DeleteUsuario(listaCuentas.get(viewHolder.getAbsoluteAdapterPosition()).getUser());
                listaCuentas.remove(viewHolder.getAbsoluteAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAbsoluteAdapterPosition());
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

            listaCuentas=new ArrayList<>();
            Flowable<List<Cuenta>> CuentasBlock=mRepository.getAllCuentasFW();
            listaCuentas=CuentasBlock.blockingFirst();
            adapter=new MyCuentaRecyclerViewAdapter(listaCuentas);
            adapter.setClickListener(this);

            recyclerView.setAdapter(adapter);

            myHelper.attachToRecyclerView(recyclerView);
        }

        srLayout.setOnRefreshListener(()->{
            listaCuentas.clear();
            listaCuentas.addAll(mRepository.getAllCuentasFW().blockingFirst());
            adapter.notifyDataSetChanged();
            srLayout.setRefreshing(false);
        });
        return srLayout;
    }

    public void refresh(){
        listaCuentas.clear();
        listaCuentas.addAll(mRepository.getAllCuentasFW().blockingFirst());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view, int position) {
        Cuenta cuenta=listaCuentas.get(position);
        Intent i = new Intent(getContext(), Formulario_Cuentas.class);
        i.putExtra("UserID", cuenta.getUser().getUserID());
        startActivity(i);
    }
}