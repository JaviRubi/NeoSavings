package com.example.neosavings.ui.Deudas;

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
import com.example.neosavings.ui.Adapters.placeholder.ItemClickListener;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Modelo.Deuda;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class DeudaFragment extends Fragment implements ItemClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private MyDeudaRecyclerViewAdapter adapter;
    private List<Deuda> ListaDeudas=new ArrayList<>();
    private UsuarioRepository mRepository;
    private boolean isDeuda=true;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DeudaFragment() {
    }

    public DeudaFragment(boolean deuda) {
        isDeuda=deuda;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DeudaFragment newInstance(int columnCount) {
        DeudaFragment fragment = new DeudaFragment();
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

        SwipeRefreshLayout srLayout=(SwipeRefreshLayout) inflater.inflate(R.layout.fragment_item_list_deuda,container,false);
        View view = srLayout.findViewById(R.id.list);

        mRepository= new UsuarioRepository(getContext());

        ItemTouchHelper.SimpleCallback myCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mRepository.DeleteDeuda(ListaDeudas.get(viewHolder.getAbsoluteAdapterPosition()));
                ListaDeudas.remove(viewHolder.getAbsoluteAdapterPosition());
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
        mRepository=new UsuarioRepository(getContext());

        ListaDeudas=mRepository.getAllDeudasByisDeuda(isDeuda).blockingFirst();

        if(ListaDeudas==null){
            ListaDeudas=new ArrayList<>();
        }




        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            adapter=new MyDeudaRecyclerViewAdapter(ListaDeudas);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
            myHelper.attachToRecyclerView(recyclerView);
        }
        srLayout.setOnRefreshListener(()->{
            ListaDeudas.clear();
            ListaDeudas.addAll(mRepository.getAllDeudasByisDeuda(isDeuda).blockingFirst());
            adapter.notifyDataSetChanged();
            srLayout.setRefreshing(false);
        });

        return srLayout;
    }

    @Override
    public void onClick(View view, int position) {
        Deuda deuda=ListaDeudas.get(position);
        Intent i = new Intent(getContext(), DeudasInfo.class);
        i.putExtra("DeudaID", deuda.getDeudaID());
        startActivity(i);
    }
}