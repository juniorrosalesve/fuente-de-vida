package com.conceptodigital.fuentedevida.users;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.conceptodigital.fuentedevida.R;
import com.conceptodigital.fuentedevida.helpers.Url;
import com.google.android.material.sidesheet.SideSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class UsersFragment extends Fragment {

    public String TAG   =   "UsersFragment";
    public Url url  =   new Url();

    public RecyclerView recyclerView;
    private ArrayList<UserItem> users   =   new ArrayList<>();

    private static final int REQUEST_CODE_PDF = 1999;


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
                                    String name = userObject.getString("nombre");
                                    String email = userObject.getString("correo");
                                    int access = 0; // default value
                                    users.add(new UserItem(id, name, email, access));
                                }
                                recyclerView.setAdapter(new MyUsersRecyclerViewAdapter(users, new MyUsersRecyclerViewAdapter.OnItemClick() {
                                    @Override
                                    public void onItemClick(UserItem item) {
                                        Intent i    =   new Intent(requireActivity(), VerUsuario.class);
                                        i.putExtra("userId", item.id);
                                        startActivity(i);
                                    }
                                }));
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

        Button addMotorizado =   view.findViewById(R.id.addMotorizado);
        addMotorizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i    =   new Intent(requireContext(), CrearUsuarioActivity.class);
                startActivity(i);
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