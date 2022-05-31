package com.example.neosavings.ui.Presupuestos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.neosavings.databinding.FragmentItemPresupuestoBinding;
import com.example.neosavings.ui.Adapters.placeholder.ItemClickListener;
import com.example.neosavings.ui.Modelo.Presupuesto;
import com.example.neosavings.ui.Presupuestos.placeholder.PlaceholderContent.PlaceholderItem;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPresupuestoRecyclerViewAdapter extends RecyclerView.Adapter<MyPresupuestoRecyclerViewAdapter.ViewHolder> {

    private final List<Presupuesto> mValues;
    private ItemClickListener clickListener;

    public MyPresupuestoRecyclerViewAdapter(List<Presupuesto> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemPresupuestoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNombre.setText(holder.mItem.getName());
        holder.mNombreUsuario.setText(holder.mItem.getUserName());
        holder.mCategoria.setText(holder.mItem.getCategoria());
        holder.mFechaInicio.setText(new SimpleDateFormat("dd/MM/yyyy").format(holder.mItem.getFechaInicio()));
        holder.mFechaFin.setText(new SimpleDateFormat("dd/MM/yyyy").format(holder.mItem.getFechaFin()));
        DecimalFormat formato=new DecimalFormat("#,###.### €");
        Double presupuesto= Double.valueOf(holder.mItem.getPresupuesto());
        Double presupuestoFinal= holder.mItem.presupuestoHastaAhora(holder.itemView.getContext());
        holder.mPresupuesto.setText(formato.format(Double.valueOf(holder.mItem.getPresupuesto())));
        holder.mPresupuestoFinal.setText(formato.format(presupuestoFinal));
        if(presupuesto<presupuestoFinal){
            int progress=100;
            holder.mProgressBar.setProgress(progress);
        }else {
            int progress=(int) ((presupuestoFinal/presupuesto)*100);
            holder.mProgressBar.setProgress(progress);
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
        public final TextView mCategoria;
        public final TextView mNombreUsuario;
        public final TextView mFechaInicio;
        public final TextView mFechaFin;
        public final TextView mPresupuesto;
        public final TextView mPresupuestoFinal;
        public final ProgressBar mProgressBar;
        public Presupuesto mItem;

        public ViewHolder(FragmentItemPresupuestoBinding binding) {
            super(binding.getRoot());
            mNombre = binding.textViewNombrePresupuesto;
            mCategoria = binding.textViewCategoriaPresupuesto;
            mNombreUsuario = binding.textViewUsuarioPresupuesto;
            mFechaInicio = binding.textViewFechaInicioPresupuesto;
            mFechaFin = binding.textViewFechaFinPresupuesto;
            mPresupuesto = binding.textViewPresupuestoInicial;
            mPresupuestoFinal = binding.textViewPresupuestoFinal;
            mProgressBar = binding.progressBarPresupuesto;
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