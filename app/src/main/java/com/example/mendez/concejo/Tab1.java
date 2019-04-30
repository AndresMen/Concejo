package com.example.mendez.concejo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.Objects;


public class Tab1 extends Fragment implements View.OnClickListener{

    private EditText span, spti;
    public List<pdf_cl> items;
    private Button btac, bty;
    private RecyclerView recycler;
    private RecyclerView.LayoutManager lManager;
    CharSequence[] itema;
    CharSequence[] itemt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recy, container, false);
        span = (EditText) view.findViewById(R.id.spanio);
        spti = (EditText) view.findViewById(R.id.sptipo);
        btac = (Button) view.findViewById(R.id.butac);
        bty = (Button) view.findViewById(R.id.btnay);
        recycler = (RecyclerView) view.findViewById(R.id.rec);
        recycler.setHasFixedSize(true);
        items = new ArrayList<>();
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        span.setOnClickListener(this);
        spti.setOnClickListener(this);


        cargarAdaptador2();

        btac.setOnClickListener(this);
        bty.setOnClickListener(this);
        return view;
    }


    public void et(final CharSequence[] iteM, final EditText txx, String title) {
        final AlertDialog.Builder alertOpcion = new AlertDialog.Builder(getContext());
        alertOpcion.setTitle(title);
        alertOpcion.setItems(iteM, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                txx.setText(iteM[i].toString());

            }
        });
        alertOpcion.show();
    }

    public void cargarAdaptador1() {
        String newURL = Constantes.GET_ANIO;
        Log.e("ver", "url111VENTAS DET-> " + newURL);
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Cargando...", "Espere por favor...", false, false);

        VolleySingleton.getInstance(getContext()).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                newURL,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        procesarRespuesta1(response);
                                        loading.dismiss();
                                        Log.e("ver", "manda-procesar-respuesta -");
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
    private void procesarRespuesta1(JSONObject response) {
        Log.e("ver", "entra-procesar-respuesta -");
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO

                    JSONArray mensajes = response.getJSONArray("anios");
                    Log.e("ver", "entra-caso1 -" + mensajes);

                    Log.e("ver", "TAMAÑO" + mensajes.length());
                    itema = new CharSequence[mensajes.length()];
                    for (int i = 0; i < mensajes.length(); i++) {

                        itema[i] = mensajes.getJSONObject(i).getString("anio");

                    }
                    bv();
                    break;
                case "2": // FALLIDO

                    String mensaje2 = response.getString("mensaje");
                    Log.e("ver", "entra-caso2 -" + mensaje2);

                    break;
            }

        } catch (JSONException e) {

        }

    }

    public void cargarAdaptador2() {
        String newURL = Constantes.GET_TIPO;
        Log.e("ver", "url111VENTAS DET-> " + newURL);
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Cargando...", "Espere por favor...", false, false);

        VolleySingleton.getInstance(getContext()).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                newURL,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        procesarRespuesta2(response);
                                        loading.dismiss();
                                        Log.e("ver", "manda-procesar-respuesta -");

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
    private void procesarRespuesta2(JSONObject response) {
        Log.e("ver", "entra-procesar-respuesta -");
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO

                    JSONArray mensajes = response.getJSONArray("grupos");
                    Log.e("ver", "entra-caso1 -" + mensajes);

                    Log.e("ver", "TAMAÑO" + mensajes.length());
                    itemt = new CharSequence[mensajes.length()];
                    for (int i = 0; i < mensajes.length(); i++) {

                        itemt[i] = mensajes.getJSONObject(i).getString("tipo");

                    }
                    cargarAdaptador1();

                    break;
                case "2": // FALLIDO

                    String mensaje2 = response.getString("mensaje");
                    Log.e("ver", "entra-caso2 -" + mensaje2);

                    break;
            }

        } catch (JSONException e) {

        }

    }

    public void cargarAdaptadorg(String x, String y) {
        String newURL = Constantes.GET_PDF + "?grupo=" + x + "&anio=" + y;
        Log.e("ver", "url111VENTAS DET-> " + newURL);
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Cargando...", "Espere por favor...", false, false);

        VolleySingleton.getInstance(getContext()).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                newURL,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        procesarRespuestag(response);
                                        loading.dismiss();
                                        Log.e("ver", "manda-procesar-respuesta -");
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
    private void procesarRespuestag(JSONObject response) {
        Log.e("ver", "entra-procesar-respuesta -");
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO

                    JSONArray mensajes = response.getJSONArray("pdfs");
                    Log.e("ver", "entra-caso1 -" + mensajes);

                    Log.e("ver", "TAMAÑO" + mensajes.length());

                    for (int i = 0; i < mensajes.length(); i++) {

                        items.add(new pdf_cl(R.drawable.imagenpdf, mensajes.getJSONObject(i).getString("nombre"), mensajes.getJSONObject(i).getString("direccion"), mensajes.getJSONObject(i).getString("anio"), mensajes.getJSONObject(i).getString("tipo")));
                    }
                    // Crear un nuevo adaptador
                    RecyclerView.Adapter adapter = new pdfAdapter(items, getContext());
                    recycler.setAdapter(adapter);
                    break;
                case "2": // FALLIDO

                    String mensaje2 = response.getString("mensaje");
                    Log.e("ver", "entra-caso2 -" + mensaje2);
                    Toast.makeText(getContext(), "Todavia no existe un documento", Toast.LENGTH_SHORT).show();

                    break;
            }

        } catch (JSONException e) {

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.spanio:
                et(itema, span, "Escoja el año");
                break;
            case R.id.sptipo:
                et(itemt, spti, "Escoja el tipo de documento");
                break;
            case R.id.butac:

                if (!span.getText().toString().equals("") | !spti.getText().toString().equals("")) {
                    recycler.removeAllViewsInLayout();
                    items.clear();
                    cargarAdaptadorg(spti.getText().toString(), span.getText().toString());
                } else {
                    Toast.makeText(getContext(), "Rellene los campos", Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.btnay:
                bv();
                break;
        }
    }

    public void bv() {
        final AlertDialog.Builder alertOpcion = new AlertDialog.Builder(getContext());
        alertOpcion.setTitle("BIENVENIDO");
        alertOpcion.setMessage("Esta es una aplicacion para poder ver y descargar pdf\n Listo para comenzar?");
        alertOpcion.setPositiveButton("Empezar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                etb(itemt, spti, "Primero escoja el tipo de documento que quiere:");
            }

        });
        alertOpcion.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertOpcion.show();

    }

    public void etb(final CharSequence[] iteM, final EditText txx, String title) {
        final AlertDialog.Builder alertOpcion = new AlertDialog.Builder(getContext());
        alertOpcion.setTitle(title);
        alertOpcion.setItems(iteM, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                txx.setText(iteM[i].toString());
                etbb(itema, span, "Ahora escoja de que año es el documeto:");
            }
        });
        alertOpcion.setCancelable(false);
        alertOpcion.show();
    }
    public void etbb(final CharSequence[] iteM, final EditText txx, String title) {
        final AlertDialog.Builder alertOpcion = new AlertDialog.Builder(getContext());
        alertOpcion.setTitle(title);
        alertOpcion.setItems(iteM, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                txx.setText(iteM[i].toString());
                recycler.removeAllViewsInLayout();
                items.clear();
                cargarAdaptadorg(spti.getText().toString(), span.getText().toString());
            }
        });
        alertOpcion.setCancelable(false);
        alertOpcion.show();
    }
}
