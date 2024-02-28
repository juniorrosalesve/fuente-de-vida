package com.conceptodigital.fuentedevida.users;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.conceptodigital.fuentedevida.databinding.FragmentItemUsersBinding;

import java.util.List;


public class MyUsersRecyclerViewAdapter extends RecyclerView.Adapter<MyUsersRecyclerViewAdapter.ViewHolder> {

    private final List<UsersFragment.UserItem> mValues;

    public MyUsersRecyclerViewAdapter(List<UsersFragment.UserItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemUsersBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position).name);
        holder.mEmail.setText(mValues.get(position).email);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mName;
        public final TextView mEmail;
        public UsersFragment.UserItem mItem;

        public ViewHolder(FragmentItemUsersBinding binding) {
            super(binding.getRoot());
            mName   =   binding.name;
            mEmail  =   binding.email;
        }
    }
}