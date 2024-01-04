package com.ista.apirest;

import static java.nio.file.Files.find;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ista.apirest.io.ApiService;
import com.ista.apirest.model.Producto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    EditText edtid;
    TextView tvnombre;
    TextView tvdescripcion;
    TextView tvgenero;
    ImageView imagFoto;
    Button btnbuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtid= findViewById(R.id.edtCodigo);
        tvnombre= findViewById(R.id.tvNombre);
        tvdescripcion= findViewById(R.id.tvDescripcion);
        tvgenero= findViewById(R.id.tvPrecio);
        imagFoto = findViewById(R.id.imgProducto);
        btnbuscar= findViewById(R.id.btnBuscar);

        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscar(edtid.getText().toString());
            }
        });

    }

    public void buscar(String codigo){
        Retrofit retrofit= new Retrofit.Builder().baseUrl("http://rickandmortyapi.com/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiService producto= retrofit.create(ApiService.class);
        Call<Producto> call = producto.find(codigo);
        call.enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                try {
                    if (response.isSuccessful()){
                        Producto p= response.body();
                        String urlImage="http://rickandmortyapi.com/api/character/avatar/"+p.getId()+".jpeg";
                        tvnombre.setText(p.getName());
                        tvdescripcion.setText(p.getStatus());
                        tvgenero.setText(p.getGender());
                        Glide.with(getApplication()).load(urlImage).into(imagFoto);
                    }else{
                        Toast.makeText(MainActivity.this,"No se encontro resultados", Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {

            }
        });
    }
}