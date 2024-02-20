package com.conceptodigital.fuentedevida.cobranza;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.conceptodigital.fuentedevida.cobranza.placeholder.PlaceholderContent.PlaceholderItem;
import com.conceptodigital.fuentedevida.databinding.FragmentItemCobranzaBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyCobranzaRecyclerViewAdapter extends RecyclerView.Adapter<MyCobranzaRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderItem> mValues;

    public MyCobranzaRecyclerViewAdapter(List<PlaceholderItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemCobranzaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public PlaceholderItem mItem;

        public ViewHolder(FragmentItemCobranzaBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
        }
    }
}