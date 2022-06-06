package com.example.neosavings.ui.Deudas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.neosavings.databinding.FragmentItemDeudaBinding;
import com.example.neosavings.ui.Adapters.placeholder.ItemClickListener;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Deudas.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.neosavings.ui.Modelo.Deuda;
import com.example.neosavings.ui.Modelo.PagosDeudas;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyDeudaRecyclerViewAdapter extends RecyclerView.Adapter<MyDeudaRecyclerViewAdapter.ViewHolder> {

    private final List<Deuda> mValues;
    private ItemClickListener clickListener;

    public MyDeudaRecyclerViewAdapter(List<Deuda> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemDeudaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNombre.setText(holder.mItem.getNombre());
        holder.mNombreUsuario.setText(holder.mItem.getDescripcion());
        holder.mFechaFin.setText(new SimpleDateFormat("dd/MM/yyyy").format(holder.mItem.getFechaVencimiento()));
        UsuarioRepository usuarioRepository=new UsuarioRepository(holder.itemView.getContext());
        PagosDeudas RegistrosDeuda=usuarioRepository.getRegistrosDeudasByID(holder.mItem.getDeudaID()).blockingFirst();
        Double DeudaRestante= Double.valueOf(RegistrosDeuda.GetDeudaRestante());
        if(DeudaRestante==0){
            holder.mCategoria.setText("CONCLUIDO");
        }else{
            DecimalFormat formato=new DecimalFormat("#,###.### €");
            Double deuda= Double.valueOf(holder.mItem.getCosteDeuda());
            holder.mCategoria.setText("restante "+formato.format(DeudaRestante));
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
        public final TextView mFechaFin;
        public Deuda mItem;

        public ViewHolder(FragmentItemDeudaBinding binding) {
            super(binding.getRoot());
            mNombre = binding.textViewNombrePresupuesto;
            mCategoria = binding.textViewDeuda;
            mNombreUsuario = binding.textViewUsuarioPresupuesto;
            mFechaFin = binding.textViewFechaFinPresupuesto;
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