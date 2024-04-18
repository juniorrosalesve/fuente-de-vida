package com.conceptodigital.fuentedevida.cobranza;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.conceptodigital.fuentedevida.R;
import com.conceptodigital.fuentedevida.cobranza.placeholder.PlaceholderContent;
import com.conceptodigital.fuentedevida.helpers.Url;

/**
 * A fragment representing a list of Items.
 */
public class CobranzaFragment extends Fragment {

    private Url url     =   new Url();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list_cobranza, container, false);

        String link  =   url.get + "/admin/gestionar-pagos";
        WebView myWebView = new WebView(requireContext());
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setSupportMultipleWindows(true);
        myWebView.loadUrl(link);

        // Asegúrate de tener un ViewGroup en tu layout donde se pueda añadir el WebView
        ((ViewGroup) view).addView(myWebView);

        return view;
    }
}