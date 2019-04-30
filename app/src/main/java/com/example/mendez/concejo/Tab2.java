package com.example.mendez.concejo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.mendez.concejo.web.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 27/10/2017.
 */

public class Tab2 extends Fragment {

    private RecyclerView recycler;
    private RecyclerView.LayoutManager lManager;
    public List<pdf_cl> items;
    private pdfAdapter adapter;
    EditText tx;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recb, container, false);

        recycler = (RecyclerView) view.findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);
        items = new ArrayList<>();

        tx = (EditText) view.findViewById(R.id.busc);
        tx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        FloatingActionButton fab =(FloatingActionButton)view.findViewById(R.id.re) ;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarAdaptador3();
            }
        });
        return view;
    }

    private void filter(String text) {
        ArrayList<pdf_cl> filteredList = new ArrayList<>();

        for (pdf_cl item : items) {
            if (item.getNom().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
            adapter.filterList(filteredList);
        }
    }

    public void cargarAdaptador3() {
        String newURL = Constantes.GET_PDF_ALL;
        Log.e("ver","url111VENTAS DET-> "+newURL);
        final ProgressDialog loading = ProgressDialog.show(getContext(),"Cargando...","Espere por favor...",false,false);

        VolleySingleton.getInstance(getContext()).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                newURL,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        procesarRespuesta3(response);
                                        loading.dismiss();
                                        Log.e("ver","manda-procesar-respuesta -");
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("ver", String.valueOf(error));
                                        loading.dismiss();

                                    }

                                }
                        )
                );
    }

    /**
     * Interpreta los resultados de la respuesta y así
     * realizar las operaciones correspondientes
     *
     * @param response Objeto Json con la respuesta
     */
    private void procesarRespuesta3(JSONObject response) {
        Log.e("ver","entra-procesar-respuesta -");
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO

                    JSONArray mensajes = response.getJSONArray("mensaje");
                    Log.e("ver","entra-caso1 -"+mensajes);

                    Log.e("ver","TAMAÑO"+mensajes.length());

                    for (int i=0;i<mensajes.length();i++){

                        items.add(new pdf_cl(R.drawable.imagenpdf,mensajes.getJSONObject(i).getString("nombre"),mensajes.getJSONObject(i).getString("direccion"),mensajes.getJSONObject(i).getString("anio"),mensajes.getJSONObject(i).getString("tipo")));
                    }
                    // Crear un nuevo adaptador
                    adapter = new pdfAdapter(items,getContext());
                    recycler.setAdapter(adapter);
                    tx.setEnabled(true);
                    break;
                case "2": // FALLIDO

                    String mensaje2 = response.getString("mensaje");
                    Log.e("ver","entra-caso2 -"+mensaje2);
                    Toast.makeText(getContext(), "Todavia no existe un documento", Toast.LENGTH_SHORT).show();
                    tx.setEnabled(false);
                    break;
            }

        } catch (JSONException e) {

        }

    }


}
