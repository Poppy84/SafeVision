package com.example.safevision;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.safevision.api.ApiService;
import com.example.safevision.api.ApiClient;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaceCaptureActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private SurfaceView surfaceView;
    private Camera camera;
    private Button btnCapture, btnRetry, btnFinish;
    private TextView textViewStatus;
    private FrameLayout previewLayout;

    private ApiService apiService;
    private String personName;
    private int captureCount = 0;
    private final int MAX_CAPTURES = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_capture);

        // Obtener nombre de la persona del intent
        personName = getIntent().getStringExtra("person_name");
        if (personName == null) {
            personName = "Usuario";
        }

        initializeViews();
        setupApi();
        setupCameraPreview();
    }

    private void initializeViews() {
        surfaceView = findViewById(R.id.surfaceView);
        btnCapture = findViewById(R.id.btnCapture);
        btnRetry = findViewById(R.id.btnRetry);
        btnFinish = findViewById(R.id.btnFinish);
        textViewStatus = findViewById(R.id.textViewStatus);
        previewLayout = findViewById(R.id.previewLayout);

        btnCapture.setOnClickListener(v -> capturePhoto());
        btnRetry.setOnClickListener(v -> retryCapture());
        btnFinish.setOnClickListener(v -> finishCapture());

        updateStatus("Preparando cámara...");
    }

    private void setupApi() {
        apiService = ApiClient.getApiService();
    }

    private void setupCameraPreview() {
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void capturePhoto() {
        if (camera != null) {
            camera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    processCapturedImage(data);
                    camera.startPreview(); // Reanudar vista previa
                }
            });
        }
    }

    private void processCapturedImage(byte[] data) {
        try {
            // Convertir bytes a Bitmap
            Bitmap originalBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            // Rotar la imagen si es necesario (las cámaras suelen guardar rotadas)
            Bitmap rotatedBitmap = rotateBitmap(originalBitmap, 90);

            // Comprimir y convertir a base64
            String imageBase64 = bitmapToBase64(rotatedBitmap);

            // Subir al servidor
            uploadFaceToServer(imageBase64);

        } catch (Exception e) {
            Log.e("FaceCapture", "Error procesando imagen", e);
            Toast.makeText(this, "Error procesando imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void uploadFaceToServer(String imageBase64) {
        updateStatus("Subiendo imagen " + (captureCount + 1) + "/" + MAX_CAPTURES + "...");

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("image", imageBase64);
            requestBody.put("person_name", personName);

            // Obtener token de autenticación
            String token = getSharedPreferences("SafeVision", MODE_PRIVATE)
                    .getString("auth_token", "");

            Call<ResponseBody> call = apiService.uploadFace("Bearer " + token, requestBody);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        captureCount++;
                        handleUploadSuccess();
                    } else {
                        handleUploadError("Error subiendo imagen: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    handleUploadError("Error de conexión: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.e("FaceCapture", "Error creando request", e);
            handleUploadError("Error preparando imagen");
        }
    }

    private void handleUploadSuccess() {
        runOnUiThread(() -> {
            if (captureCount >= MAX_CAPTURES) {
                updateStatus("¡Captura completada! " + captureCount + " imágenes subidas");
                btnCapture.setEnabled(false);
                btnFinish.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Rostro registrado exitosamente", Toast.LENGTH_LONG).show();
            } else {
                updateStatus("Imagen " + captureCount + "/" + MAX_CAPTURES + " subida ✓");
                Toast.makeText(this, "Imagen " + captureCount + " subida", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleUploadError(String error) {
        runOnUiThread(() -> {
            updateStatus("Error: " + error);
            btnRetry.setVisibility(View.VISIBLE);
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        });
    }

    private void retryCapture() {
        btnRetry.setVisibility(View.GONE);
        updateStatus("Listo para capturar...");
    }

    private void finishCapture() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("captures_count", captureCount);
        resultIntent.putExtra("person_name", personName);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void updateStatus(String message) {
        runOnUiThread(() -> textViewStatus.setText(message));
    }

    // SurfaceHolder Callbacks
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT); // Cámara frontal
            camera.setPreviewDisplay(holder);
            camera.startPreview();
            updateStatus("Cámara lista - Captura " + (captureCount + 1) + "/" + MAX_CAPTURES);
        } catch (IOException e) {
            Log.e("FaceCapture", "Error abriendo cámara", e);
            updateStatus("Error abriendo cámara");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Reconfigurar cámara si cambia la superficie
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
}