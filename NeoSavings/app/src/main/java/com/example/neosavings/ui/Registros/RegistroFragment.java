package com.example.neosavings.ui.Registros;

import android.content.Context;
import android.content.Intent;
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
import com.example.neosavings.ui.Adapters.MyRegistroRecyclerViewAdapter;
import com.example.neosavings.ui.Adapters.placeholder.ItemClickListener;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Modelo.Registro;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

/**
 * A fragment representing a list of Items.
 */
public class RegistroFragment extends Fragment implements ItemClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private MyRegistroRecyclerViewAdapter adapter;
    private List<Registro> ListaCuentas=new ArrayList<>();
    private UsuarioRepository mRepository;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RegistroFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RegistroFragment newInstance(int columnCount) {
        RegistroFragment fragment = new RegistroFragment();
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

        SwipeRefreshLayout srLayout=(SwipeRefreshLayout) inflater.inflate(R.layout.fragment_item_list,container,false);
        View view = srLayout.findViewById(R.id.list);

        ItemTouchHelper.SimpleCallback myCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {



            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mRepository.DeleteRegistro(ListaCuentas.get(viewHolder.getAbsoluteAdapterPosition()));
                ListaCuentas.remove(viewHolder.getAbsoluteAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAbsoluteAdapterPosition());
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Drawable trashBinIcon = getResources().getDrawable(R.drawable.ic_baseline_delete_forever_24);
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
        mRepository=new UsuarioRepository(getContext());

        // Set the adapter
        Flowable<List<Registro>> allRegistros = mRepository.getAllRegistros();
        ListaCuentas=allRegistros.blockingFirst();

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MyRegistroRecyclerViewAdapter(ListaCuentas);
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

    public void refresh(){
        adapter.notifyDataSetChanged();
    }

    public void setRegistros(List<Registro> ITEMS){
        ListaCuentas.clear();
        ListaCuentas.addAll(ITEMS);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view, int position) {
        Registro registro=ListaCuentas.get(position);
        Intent i = new Intent(getContext(), RegistroInfo.class);
        i.putExtra("RegistroID", registro.getRegistroID());
        startActivity(i);
    }
}