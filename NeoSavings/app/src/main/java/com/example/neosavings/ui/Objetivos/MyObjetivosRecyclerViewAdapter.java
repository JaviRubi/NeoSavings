package com.example.neosavings.ui.Objetivos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.neosavings.databinding.FragmentObjetivosItemListBinding;
import com.example.neosavings.ui.Adapters.placeholder.ItemClickListener;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Modelo.Objetivo;
import com.example.neosavings.ui.Objetivos.placeholder.PlaceholderContent.PlaceholderItem;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyObjetivosRecyclerViewAdapter extends RecyclerView.Adapter<MyObjetivosRecyclerViewAdapter.ViewHolder> {

    private final List<Objetivo> mValues;
    private ItemClickListener clickListener;

    public MyObjetivosRecyclerViewAdapter(List<Objetivo> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentObjetivosItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNombre.setText(mValues.get(position).getNombre());
        holder.mFechaFin.setText(new SimpleDateFormat("dd/MM/yyyy").format(mValues.get(position).getFechaDeseada()));
        DecimalFormat formato=new DecimalFormat("#,###.### €");
        Double Objetivo=Double.valueOf(holder.mItem.getCantidadObjetivo());
        Double Ahorrado=Double.valueOf(holder.mItem.getAhorrado());
        holder.mCantidadObjetivo.setText("Objetivo: "+formato.format(Objetivo));
        holder.mCantidadAhorrada.setText("Ahorrado: "+formato.format(Ahorrado));
        if(Objetivo<=Ahorrado){
            int progress=100;
            holder.progressBar.setProgress(progress);
            UsuarioRepository usuarioRepository=new UsuarioRepository(holder.itemView.getContext());
            holder.mItem.setEstadoConcluido();
            usuarioRepository.Update(holder.mItem);
            holder.mFechaFin.setText("CONCLUIDO");
        }else {
            int progress=(int) ((Ahorrado/Objetivo)*100);
            holder.progressBar.setProgress(progress);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mNombre;
        public final TextView mFechaFin;
        public final TextView mCantidadAhorrada;
        public final TextView mCantidadObjetivo;
        public final ProgressBar progressBar;
        public Objetivo mItem;

        public ViewHolder(FragmentObjetivosItemListBinding binding) {
            super(binding.getRoot());
            mNombre = binding.textViewNombreObjetivo;
            mFechaFin = binding.textViewFechaFin;
            mCantidadAhorrada = binding.textViewAhorrado;
            mCantidadObjetivo = binding.textViewCantidadObjetivo;
            progressBar = binding.progressBarAhorradoItem;
            itemView.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNombre.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }
}