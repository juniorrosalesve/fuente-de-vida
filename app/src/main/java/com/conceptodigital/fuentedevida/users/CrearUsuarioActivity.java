package com.conceptodigital.fuentedevida.users;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.conceptodigital.fuentedevida.R;
import com.conceptodigital.fuentedevida.helpers.Url;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class CrearUsuarioActivity extends AppCompatActivity {

    public String TAG   =   "CrearUsuarioActivity";
    public Url url      =   new Url();

    private static final int REQUEST_CODE_PDF_ONE = 123;
    private static final int REQUEST_CODE_PDF_TWO = 321;

    Uri file1 = null, file2 = null;
    Button btnFile1, btnFile2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);

        Dialog loading =   url.loading(this);

        // Obtén una referencia al botón dentro del diálogo
        Button btnSubmit = findViewById(R.id.btnSubmit);
        TextInputEditText[] inputs   =   new TextInputEditText[] {
                findViewById(R.id.control),
                findViewById(R.id.nombre),
                findViewById(R.id.correo),
                findViewById(R.id.telefono),
                findViewById(R.id.direccion),
                findViewById(R.id.placa),
                findViewById(R.id.hijos),
        };
        btnFile1     =   findViewById(R.id.btn_file1);
        btnFile2     =   findViewById(R.id.btn_file2);

        btnFile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("application/pdf"); // Filtrar por archivos PDF
                startActivityForResult(intent, REQUEST_CODE_PDF_ONE);
            }
        });
        btnFile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("application/pdf"); // Filtrar por archivos PDF
                startActivityForResult(intent, REQUEST_CODE_PDF_TWO);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url.get + "/")  // Asegúrate de que la URL base termine con una barra '/'
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi service = retrofit.create(MyApi.class);
       btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(file1 == null || file2 == null) {
                    Toast.makeText(CrearUsuarioActivity.this, "Debes elegir dos archivos", Toast.LENGTH_SHORT).show();
                    return;
                }

                for(int i = 0; i < inputs.length; i++)
                {
                    if(inputs[i].getText().toString().isEmpty())
                    {
                        inputs[i].setError("Este campo es requerido");
                        loading.dismiss();
                        return;
                    }
                }

                try {
                    // Crear InputStream a partir de la Uri del archivo 1
                    InputStream inputStream1 = getContentResolver().openInputStream(file1);
                    byte[] bytes1 = IOUtils.toByteArray(inputStream1);

                    // Crear InputStream a partir de la Uri del archivo 2
                    InputStream inputStream2 = getContentResolver().openInputStream(file2);
                    byte[] bytes2 = IOUtils.toByteArray(inputStream2);

                    // Crear RequestBody instance de file1
                    RequestBody requestFile1 = RequestBody.create(MediaType.parse("application/pdf"), bytes1);
                    MultipartBody.Part body1 = MultipartBody.Part.createFormData("file1", "archivo1.pdf", requestFile1);

                    // Crear RequestBody instance de file2
                    RequestBody requestFile2 = RequestBody.create(MediaType.parse("application/pdf"), bytes2);
                    MultipartBody.Part body2 = MultipartBody.Part.createFormData("file2", "archivo2.pdf", requestFile2);

                    // Agregar otro campo de texto a la solicitud
                    RequestBody control = RequestBody.create(MediaType.parse("text/plain"), inputs[0].getText().toString());
                    RequestBody nombre = RequestBody.create(MediaType.parse("text/plain"), inputs[1].getText().toString());
                    RequestBody correo = RequestBody.create(MediaType.parse("text/plain"), inputs[2].getText().toString());
                    RequestBody telefono = RequestBody.create(MediaType.parse("text/plain"), inputs[3].getText().toString());
                    RequestBody direccion = RequestBody.create(MediaType.parse("text/plain"), inputs[4].getText().toString());
                    RequestBody placa = RequestBody.create(MediaType.parse("text/plain"), inputs[5].getText().toString());
                    RequestBody hijos = RequestBody.create(MediaType.parse("text/plain"), inputs[6].getText().toString());

                    Call<ResponseBody> call = service.upload(control, nombre, correo, telefono, direccion, placa, hijos, body1, body2);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(CrearUsuarioActivity.this, "Error #001: " + response.code(), Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                                return;
                            }
                            Toast.makeText(CrearUsuarioActivity.this, "Creado correctamente!", Toast.LENGTH_SHORT).show();
                            for(int i = 0; i < inputs.length; i++)
                                inputs[i].setText("");
                            loading.dismiss();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(CrearUsuarioActivity.this, "Error #002: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    });

                    loading.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CrearUsuarioActivity.this, "Error al leer los archivos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PDF_ONE && resultCode == RESULT_OK) {
            if (data != null) {
                file1 = data.getData();
                btnFile1.setText("Título seleccionado correctamente!");
            }
        }
        if (requestCode == REQUEST_CODE_PDF_TWO && resultCode == RESULT_OK) {
            if (data != null) {
                file2 = data.getData();
                btnFile2.setText("Licencia seleccionado correctamente!");
            }
        }
    }

    public interface MyApi {
        @Multipart
        @POST("motorizado/add")
        Call<ResponseBody> upload(
                @Part("control") RequestBody control,
                @Part("nombre") RequestBody nombre,
                @Part("correo") RequestBody correo,
                @Part("telefono") RequestBody telefono,
                @Part("direccion") RequestBody direccion,
                @Part("placa") RequestBody placa,
                @Part("hijos") RequestBody hijos,
                @Part MultipartBody.Part file1,
                @Part MultipartBody.Part file2
        );
    }

}