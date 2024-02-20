package com.conceptodigital.fuentedevida.users;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.conceptodigital.fuentedevida.R;
import com.conceptodigital.fuentedevida.helpers.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class UsersFragment extends Fragment {

    public String TAG   =   "UsersFragment";
    public Url url  =   new Url();

    public RecyclerView recyclerView;
    private ArrayList<UserItem> users   =   new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list_users, container, false);

        recyclerView    =   view.findViewById(R.id.list_users);

        Dialog loading =   url.loading(requireActivity());
        loading.show();

        url.sendGetRequest("/users", new Url.MyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, result);
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray getUsers  =   new JSONArray(result);
                            if(getUsers.length() > 0)
                            {
                                for(int i = 0; i < getUsers.length(); i++)
                                {
                                    JSONObject userObject = getUsers.getJSONObject(i);
                                    String id = userObject.getString("id");
                                    String name = userObject.getString("name");
                                    String email = userObject.getString("email");
                                    int access = 0; // default value
                                    if (userObject.has("access") && !userObject.isNull("access")) {
                                        access = Integer.parseInt(userObject.getString("access"));
                                    }
                                    users.add(new UserItem(id, name, email, access));
                                }
                                recyclerView.setAdapter(new MyUsersRecyclerViewAdapter(users));
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        loading.dismiss();
                    }
                });
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, error);
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.dismiss();
                    }
                });
            }
        });

        return view;
    }

    public static class UserItem {
        public final String id;
        public final String name;
        public final String email;
        public final int access;

        public UserItem(String id, String name, String email, int access) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.access = access;
        }
    }
}