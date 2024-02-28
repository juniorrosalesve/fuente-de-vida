package com.conceptodigital.fuentedevida.services;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.conceptodigital.fuentedevida.R;
import com.conceptodigital.fuentedevida.helpers.Url;
import com.conceptodigital.fuentedevida.users.UsersFragment;
import com.google.android.material.sidesheet.SideSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A fragment representing a list of Items.
 */
public class ItemServiceFragment extends Fragment {

    public String TAG   =   "ItemServiceFragment";
    public Url url  =   new Url();
    public RecyclerView recyclerView;
    private ArrayList<ItemService> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list_service, container, false);

        recyclerView    =   view.findViewById(R.id.list);

        Dialog loading  =   url.loading(requireActivity());
        loading.show();

        Button addService = view.findViewById(R.id.addService);
        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SideSheetDialog sideSheetDialog = new SideSheetDialog(requireActivity());
                sideSheetDialog.setContentView(R.layout.side_add_service);
                sideSheetDialog.show();

                // Obtén una referencia al botón dentro del diálogo
                Button btnSubmit = sideSheetDialog.findViewById(R.id.btnSubmit);
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loading.show();
                        // Obtén una referencia a los campos de texto dentro del diálogo
                        TextInputEditText txtNombre = sideSheetDialog.findViewById(R.id.nombre);
                        TextInputEditText txtPrecio = sideSheetDialog.findViewById(R.id.precio);
                        CheckBox mensual = sideSheetDialog.findViewById(R.id.mensual);

                        // Crea un mapa con los parámetros
                        HashMap<String, String> params = new HashMap<>();
                        params.put("nombre", txtNombre.getText().toString());
                        params.put("precio", txtPrecio.getText().toString());
                        params.put("mensual", String.valueOf(mensual.isChecked()));
                        url.sendPostRequest("/servicios/add", params, new Url.MyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                String[] response   =   url.responseText(result);
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        loading.dismiss();
                                        if(response[0].equals("success"))
                                        {
                                            items.add(new ItemService(response[1], txtNombre.getText().toString(), Double.parseDouble(txtPrecio.getText().toString()), String.valueOf(mensual.isChecked())));
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
            }
        });
        url.sendGetRequest("/servicios/get", new Url.MyCallback() {
            @Override
            public void onSuccess(String result) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            items = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                items.add(new ItemService(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("nombre"),
                                        jsonObject.getDouble("precio"),
                                        jsonObject.getString("mensual")
                                ));
                            }
                            recyclerView.setAdapter(new MyItemServiceRecyclerViewAdapter(items));
                            loading.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    public static class ItemService {
        public final String id;
        public final String nombre;
        public final double precio;
        public final String mensual;

        public ItemService(String id, String nombre, double precio, String mensual) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
            this.mensual = mensual;
        }
    }
}