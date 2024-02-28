package com.conceptodigital.fuentedevida.users;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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
                                    String name = userObject.getString("nombre");
                                    String email = userObject.getString("correo");
                                    int access = 0; // default value
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

        Button addMotorizado =   view.findViewById(R.id.addMotorizado);
        addMotorizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SideSheetDialog sideSheetDialog = new SideSheetDialog(requireActivity());
                sideSheetDialog.setContentView(R.layout.side_add_motorizado);

                // Obtén una referencia al botón dentro del diálogo
                Button btnSubmit = sideSheetDialog.findViewById(R.id.btnSubmit);
                TextInputEditText[] inputs   =   new TextInputEditText[] {
                        sideSheetDialog.findViewById(R.id.nombre),
                        sideSheetDialog.findViewById(R.id.correo),
                        sideSheetDialog.findViewById(R.id.telefono),
                        sideSheetDialog.findViewById(R.id.direccion),
                        sideSheetDialog.findViewById(R.id.placa)
                };

                // Establece un OnClickListener para el botón
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loading.show();
                        for(int i = 0; i < inputs.length; i++)
                        {
                            if(inputs[i].getText().toString().isEmpty())
                            {
                                inputs[i].setError("Este campo es requerido");
                                loading.dismiss();
                                return;
                            }
                        }
                        HashMap<String, String> params = new HashMap<>();
                        params.put("nombre", inputs[0].getText().toString());
                        params.put("correo", inputs[1].getText().toString());
                        params.put("telefono", inputs[2].getText().toString());
                        params.put("direccion", inputs[3].getText().toString());
                        params.put("placa", inputs[4].getText().toString());
                        url.sendPostRequest("/motorizado/add", params, new Url.MyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                String[] response   =   url.responseText(result);
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        loading.dismiss();
                                        if(response[0].equals("success"))
                                        {
                                            users.add(new UserItem(response[1], inputs[0].getText().toString(), inputs[1].getText().toString(), 0));
                                            recyclerView.getAdapter().notifyDataSetChanged();
                                            sideSheetDialog.dismiss();
                                        }
                                        Toast.makeText(getContext(), response[1], Toast.LENGTH_SHORT).show();
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
                    }
                });

                sideSheetDialog.show();
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