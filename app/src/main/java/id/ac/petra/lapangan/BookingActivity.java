package id.ac.petra.lapangan;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BookingActivity extends AppCompatActivity {

    private EditText usernameField;
    private EditText waktuField;

    private EditText jamMulai;
    private Spinner jenisField;
    private Spinner jamField;
    private Spinner lokasiField;
    private Button bookButton;
    private Button backButton;

    private int responseCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        usernameField = findViewById(R.id.usernameField);
        lokasiField = findViewById(R.id.lokasiField);
//        waktuField = findViewById(R.id.waktuField);
//        jamMulai = findViewById(R.id.jamMulai);
        jenisField = findViewById(R.id.jenisField);
        jamField = findViewById(R.id.jamField);
        lokasiField = findViewById(R.id.lokasiField);
        bookButton = findViewById(R.id.bookButton);
        backButton = findViewById(R.id.backButton);

        // Create an array of jenis options
        List<String> jenisOptions = new ArrayList<>();
        jenisOptions.add("badminton");
        jenisOptions.add("basket");
        jenisOptions.add("futsal");

        ArrayAdapter<String> jenisAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jenisOptions);
        jenisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jenisField.setAdapter(jenisAdapter);

        List<String> jamOptions = new ArrayList<>();
        jamOptions.add("10.00 - 12.00");
        jamOptions.add("12.00 - 14.00");
        jamOptions.add("14.00 - 16.00");
        jamOptions.add("16.00 - 18.00");
        jamOptions.add("18.00 - 20.00");
        jamOptions.add("20.00 - 22.00");

        ArrayAdapter<String> jamAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jamOptions);
        jamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jamField.setAdapter(jamAdapter);

        List<String> lokasiOptions = new ArrayList<>();
        lokasiOptions.add("Surabaya");
        lokasiOptions.add("Sidoarjo");
        lokasiOptions.add("Gresik");

        ArrayAdapter<String> lokasiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lokasiOptions);
        lokasiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lokasiField.setAdapter(lokasiAdapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString().trim();
                String lokasi = lokasiField.getSelectedItem().toString().trim();
                String jam = jamField.getSelectedItem().toString().trim();
                String jenis = jenisField.getSelectedItem().toString().trim();


                if (username.isEmpty() || lokasi.isEmpty() || jam.isEmpty() || jenis.isEmpty()) {
                    Toast.makeText(BookingActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject bookingData = new JSONObject();
                    try {
                        bookingData.put("username", username);
                        bookingData.put("lokasi", lokasi);
                        bookingData.put("waktu", jam);
                        bookingData.put("jenis", jenis);

                        new BookAsyncTask().execute(bookingData.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private class BookAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                String bookingData = params[0];
                URL url = new URL("http://172.22.27.149:7000/sewa");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                os.write(bookingData.getBytes());
                os.flush();

                responseCode = connection.getResponseCode();
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                    StringBuilder responseBuilder = new StringBuilder();
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        responseBuilder.append(line);
//                    }
//                    result = responseBuilder.toString();
//                } else {
//                    result = "Error: " + responseCode;
//                }

                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                result = "Error occurred while connecting to the server.";
            }
            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            if(responseCode == 200){
                Toast.makeText(BookingActivity.this, "Berhasil Booking!", Toast.LENGTH_SHORT).show();
            }
            else if(responseCode == 201){
                Toast.makeText(BookingActivity.this, "Maaf, lapangan sudah di booking", Toast.LENGTH_SHORT).show();
            }
            else if(responseCode == 202){
                Toast.makeText(BookingActivity.this, "Maaf, stok lapangan sudah habis", Toast.LENGTH_SHORT).show();
            }
            else if(responseCode == 500){
                Toast.makeText(BookingActivity.this, "Internal Server Error!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}