package com.example.neosavings.ui.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.neosavings.databinding.FragmentCuentaBinding;
import com.example.neosavings.ui.Adapters.placeholder.ItemClickListener;
import com.example.neosavings.ui.Adapters.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.neosavings.ui.Modelo.Cuenta;

import java.text.DecimalFormat;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyCuentaRecyclerViewAdapter extends RecyclerView.Adapter<MyCuentaRecyclerViewAdapter.ViewHolder> {

    private final List<Cuenta> mValues;
    private ItemClickListener clickListener;

    public MyCuentaRecyclerViewAdapter(List<Cuenta> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentCuentaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNombre.setText(mValues.get(position).getUser().getUsuario());
        holder.mTipo.setText(mValues.get(position).getUser().Tipo);

        DecimalFormat formato=new DecimalFormat("#,###.### ¤");


        holder.mValue.setText(formato.format(holder.mItem.getSaldoActual()));
        if(holder.mItem.getSaldoActual()<0){
            holder.mValue.setTextColor(Color.RED);
        }else{
            holder.mValue.setTextColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mNombre;
        public final TextView mTipo;
        public final TextView mValue;
        public Cuenta mItem;

        public ViewHolder(FragmentCuentaBinding binding) {
            super(binding.getRoot());
            mNombre = binding.textViewNombre;
            mTipo = binding.textViewTipo;
            mValue=binding.textViewSaldoActual;
            itemView.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNombre.getText() + "'";
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}