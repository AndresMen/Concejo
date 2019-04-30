package com.example.mendez.concejo;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mendez on 31/05/2018.
 */

public class pdfAdapter extends RecyclerView.Adapter<pdfAdapter.pdfViewHolder> {

    private List<pdf_cl> items;
    private Context context;
    ProgressDialog loading;

    public static class pdfViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView nombre,tan,tti;
        public pdfViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            nombre = (TextView) v.findViewById(R.id.nombre);
            tan = (TextView) v.findViewById(R.id.tan);
            tti = (TextView) v.findViewById(R.id.tti);

        }

    }
    public pdfAdapter(List<pdf_cl> items, Context context) {
        this.items = items;
        this.context=context;
    }


    @Override
    public pdfViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);
        final pdfViewHolder vh = new pdfViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final int position = vh.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    final LayoutInflater inflater = LayoutInflater.from(context);
                    final View dialogView = inflater.inflate(R.layout.alert, null);
                    dialogBuilder.setTitle("AVISO");
                    dialogBuilder.setMessage("Selecione accion para: "+items.get(position).getNom());
                    dialogBuilder.setView(dialogView);
                    dialogBuilder.setPositiveButton("Descargar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String s=String.valueOf(Environment.getExternalStorageDirectory()+"/Concejo/PDF_"+items.get(position).getNom()+".pdf" );
                            File pdfFile = new File(s);
                            if (pdfFile.exists()){
                                pdfex(Constantes.IP+"/"+items.get(position).getDir(),items.get(position).getNom());
                            }else {
                                DownloadFileFromU dor=  new DownloadFileFromU();
                                dor.execute(Constantes.IP+"/"+items.get(position).getDir(),items.get(position).getNom());
                            }
                        }
                    });
                    dialogBuilder.setNeutralButton("Ver", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String s=String.valueOf(Environment.getExternalStorageDirectory()+"/Concejo/PDF_"+items.get(position).getNom()+".pdf" );
                            File pdfFile = new File(s);
                            if (pdfFile.exists()){
                                verpdf(s);
                            }else {
                                Toast.makeText(context,"Este pdf no ha sido descargado",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog b = dialogBuilder.create();

                    b.show();
                    Dialog dialog=(Dialog)b;
                }
            }

        });

        return vh;

    }

    @Override
    public void onBindViewHolder(pdfViewHolder holder, int position) {
        holder.imagen.setImageResource(items.get(position).getImage());
        holder.nombre.setText(items.get(position).getNom());
        holder.tan.setText(items.get(position).getAn());
        holder.tti.setText(items.get(position).getTip());

    }
    public void filterList(ArrayList<pdf_cl> filteredList) {
        items = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class DownloadFileFromU extends AsyncTask<String, String, String> {


        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(context,"Descargando...","Espere por favor...",false,false);

            //showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
               try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100%           progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream
                String path= Environment.getExternalStorageDirectory().getPath();
                File dir =new File(path+"/Concejo");
                if(!dir.exists())
                    dir.mkdir();
                File file= new File(dir,"PDF_"+f_url[1]+".pdf");
                if(!file.exists())
                    file.createNewFile();
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return f_url[1];
        }

        protected void onProgressUpdate(String... progress) {
           // loading.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String file_url) {
            loading.dismiss();
            Toast.makeText(context,"Descarga completa", Toast.LENGTH_SHORT).show();
            Log.e("Download complete","----------"+file_url);
            verpdf(file_url);

        }

        @Override
        protected void onCancelled() {
            loading.dismiss();
            super.onCancelled();
        }
    }

    public void verpdf(String nombre){
        String s= String.valueOf(Environment.getExternalStorageDirectory()+"/Concejo/PDF_"+nombre+".pdf" );
        Uri path;
        File pdfFile = new File(s);//File path
        if (pdfFile.exists()){ //Revisa si el archivo existe!
            if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.N) {
                path = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", pdfFile);
            } else{
                path = Uri.fromFile(pdfFile);
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //define el tipo de archivo
            intent.setDataAndType(path,"application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//Inicia pdf viewer
            if(intent.resolveActivity(context.getPackageManager()) != null) {
                //Toast.makeText(context, "No tiene aplicacion para ver PDF", Toast.LENGTH_SHORT).show();
                context.startActivity(Intent.createChooser(intent, "Abrir archivo en"));
            } else{
                Toast.makeText(context, "No tiene aplicacion para ver PDF", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(context, "No existe archivo! ", Toast.LENGTH_SHORT).show();
        }
    }
    public void verpdfon(String dir){
        //String s=String.valueOf(Environment.getExternalStorageDirectory()+"/Concejo/PDF_"+nombre+".pdf" );

            Intent intent = new Intent(Intent.ACTION_VIEW);
            //define el tipo de archivo
            intent.setData(Uri.parse(dir));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//Inicia pdf viewer
            if(intent.resolveActivity(context.getPackageManager()) != null) {
                //Toast.makeText(context, "No tiene aplicacion para ver PDF", Toast.LENGTH_SHORT).show();
                context.startActivity(Intent.createChooser(intent, "Abrir archivo en"));
            } else{
                Toast.makeText(context, "No tiene aplicacion para ver PDF", Toast.LENGTH_SHORT).show();
            }
    }
    public void pdfex(final String ur,final String nom){
        final android.app.AlertDialog.Builder alertOpcion=new android.app.AlertDialog.Builder(context);
        alertOpcion.setTitle("Este documento ya existe");
        alertOpcion.setMessage("Que desea realizar");
        alertOpcion.setPositiveButton("Abrir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //showChangeLangDialog();
                verpdf(nom);
            }
        });
        alertOpcion.setNeutralButton("Descargar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DownloadFileFromU dor=  new DownloadFileFromU();
                dor.execute(ur,nom);
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

    }




