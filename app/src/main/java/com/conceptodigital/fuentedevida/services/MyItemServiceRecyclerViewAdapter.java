package com.conceptodigital.fuentedevida.services;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.conceptodigital.fuentedevida.databinding.FragmentItemServiceBinding;

import java.util.List;


public class MyItemServiceRecyclerViewAdapter extends RecyclerView.Adapter<MyItemServiceRecyclerViewAdapter.ViewHolder> {

    private final List<ItemServiceFragment.ItemService> mValues;

    public MyItemServiceRecyclerViewAdapter(List<ItemServiceFragment.ItemService> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemServiceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mServicio.setText(mValues.get(position).nombre);
        holder.mPrecio.setText("$"+mValues.get(position).precio);
        if(mValues.get(position).mensual.equals("1"))
            holder.mTipo.setText("Mensual");
        else
            holder.mTipo.setText("Único");
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mServicio;
        public final TextView mPrecio;
        public final TextView mTipo;
        public ItemServiceFragment.ItemService mItem;

        public ViewHolder(FragmentItemServiceBinding binding) {
            super(binding.getRoot());
            mServicio   = binding.servicio;
            mPrecio     = binding.precio;
            mTipo       = binding.tipo;
        }
    }
}