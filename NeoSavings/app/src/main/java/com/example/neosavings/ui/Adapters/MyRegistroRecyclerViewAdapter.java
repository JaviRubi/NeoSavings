package com.example.neosavings.ui.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.neosavings.R;
import com.example.neosavings.databinding.FragmentItemregistroBinding;
import com.example.neosavings.ui.Adapters.placeholder.ItemClickListener;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Modelo.Registro;
import com.example.neosavings.ui.Modelo.Usuario;

import java.text.SimpleDateFormat;
import java.util.List;

import io.reactivex.Flowable;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.example.neosavings.ui.Modelo.Registro}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyRegistroRecyclerViewAdapter extends RecyclerView.Adapter<MyRegistroRecyclerViewAdapter.ViewHolder>{

    private final List<Registro> mValues;
    private ItemClickListener clickListener;

    public MyRegistroRecyclerViewAdapter(List<Registro> items) {
        mValues = items;
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemregistroBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        UsuarioRepository mRepository=new UsuarioRepository(holder.itemView.getContext());
        Flowable<Usuario>userBlock=mRepository.getUserByID(holder.mItem.RegistroUserID);
        Usuario user=userBlock.blockingFirst();

        holder.mCuenta.setText(user.getUsuario());
        holder.mCategoria.setText(mValues.get(position).Categoria);
        holder.mDescripcion.setText(holder.mItem.getDescripcion());
        if(holder.mItem.isGasto()){
            holder.mGasto.setText("-"+holder.mItem.getCoste());
            holder.mGasto.setTextColor(Color.RED);
        }else{
            holder.mGasto.setText(holder.mItem.getCoste());
            holder.mGasto.setTextColor(Color.GREEN);
        }

        if(holder.mItem.getTicket()!=null){
            holder.mImageView.setImageBitmap(holder.mItem.getTicket());
        }else {
            holder.mImageView.setImageResource(R.drawable.ic_menu_camera);
        }

        if(holder.mItem.getFecha()!=null)
        holder.mFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(holder.mItem.getFecha()));



    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mCuenta;
        public final TextView mCategoria;
        public final TextView mDescripcion;
        public final TextView mGasto;
        public final TextView mFecha;
        public final ImageView mImageView;
        public Registro mItem;

        public ViewHolder(FragmentItemregistroBinding binding) {
            super(binding.getRoot());
            mCuenta = binding.TextViewCuenta;
            mCategoria = binding.textViewCategoria;
            mDescripcion = binding.textViewDescripcion;
            mGasto = binding.textViewGasto;
            mFecha = binding.textViewFecha;
            mImageView = binding.Ticket;
            itemView.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCuenta.getText() + "'";
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}