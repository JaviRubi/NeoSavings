package com.example.neosavings.ui.PagosProgramados;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.neosavings.databinding.FragmentItemPagoprogramadoBinding;
import com.example.neosavings.ui.Adapters.placeholder.ItemClickListener;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Modelo.PagoProgramado;
import com.example.neosavings.ui.Modelo.RegistrosPagosProgramados;
import com.example.neosavings.ui.PagosProgramados.placeholder.PlaceholderContent.PlaceholderItem;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPagoProgramadoRecyclerViewAdapter extends RecyclerView.Adapter<MyPagoProgramadoRecyclerViewAdapter.ViewHolder> {

    private final List<PagoProgramado> mValues;
    private ItemClickListener clickListener;

    public MyPagoProgramadoRecyclerViewAdapter(List<PagoProgramado> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemPagoprogramadoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        if(holder.mItem.getNombre()!=null) {
            holder.mNombre.setText(holder.mItem.getNombre());
        }else{
            holder.mNombre.setText("SIN NOMBRE");
        }
        holder.mNombreUsuario.setText(holder.mItem.getNombreUser());
        holder.mCategoria.setText(holder.mItem.getCategoría());
        holder.mPeriocidad.setText(holder.mItem.getPeriodicidad());
        holder.mFechaInicio.setText(new SimpleDateFormat("dd/MM/yyyy").format(holder.mItem.getFechaInicio()));
        holder.mFechaFin.setText(new SimpleDateFormat("dd/MM/yyyy").format(holder.mItem.getFechaFin()));
        DecimalFormat formato=new DecimalFormat("#,###.### €");
        Double presupuesto= Double.valueOf(holder.mItem.getCoste());

        if(holder.mItem.isGasto()){
            holder.mGasto.setTextColor(Color.RED);
            holder.mGasto.setText("-"+formato.format(Double.valueOf(presupuesto)));
        }else{
            holder.mGasto.setTextColor(Color.GREEN);
            holder.mGasto.setText(formato.format(Double.valueOf(presupuesto)));
        }


        UsuarioRepository usuarioRepository=new UsuarioRepository(holder.itemView.getContext());
        RegistrosPagosProgramados pagos=usuarioRepository.getRegistroPagosProgramadoByID(holder.mItem.getPagoProgramadoID()).blockingFirst();

        Date NextPago=pagos.getNextPago();
        if(NextPago.getTime()>holder.mItem.getFechaFin().getTime()){
            holder.mNextFecha.setText("CONCLUIDO");
        }else {
            holder.mNextFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(NextPago));
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
        public final TextView mGasto;
        public final TextView mNextFecha;
        public final TextView mPeriocidad;
        public PagoProgramado mItem;

        public ViewHolder(FragmentItemPagoprogramadoBinding binding) {
            super(binding.getRoot());
            mNombre = binding.textViewNombrePresupuesto;
            mCategoria = binding.textViewCategoriaPresupuesto;
            mNombreUsuario = binding.textViewUsuarioPresupuesto;
            mFechaInicio = binding.textViewFechaInicioPresupuesto;
            mFechaFin = binding.textViewFechaFinPresupuesto;
            mGasto = binding.textViewPresupuestoFinal;
            mNextFecha = binding.textViewFechaNextPagoProgramado;
            mPeriocidad=binding.textViewUsuarioPagoProgramadoPeriocidad;
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