package id.ac.petra.lapangan;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListLapangan extends AppCompatActivity {

    private ListView listViewLapangan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_lapangan);
        listViewLapangan = findViewById(R.id.listViewLapangan);

        new datalapangan().execute();
    }

    private class datalapangan extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String result;
            try {
                URL url = new URL("http://172.22.27.149:7000/listlapangan");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    result = response.toString();
                } else {
                    result = "Error: " + responseCode;
                }
                connection.disconnect();
            } catch (Exception e) {
                result = "Error: " + e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            List<String> listlapangan = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject lapangan = jsonArray.getJSONObject(i);
                    String jenis = lapangan.getString("jenis");
                    String stok = lapangan.getString("stok");
                    String harga = lapangan.getString("harga");
                    String lokasi = lapangan.getString("lokasi");

                    String detaillapangan = "Jenis Lapangan: " + jenis + "\n"
                            + "Stok Lapangan Tersedia: " + stok + "\n"
                            + "Harga: " + harga + "\n" + "Lokasi Lapangan: " + lokasi;

                    listlapangan.add(detaillapangan);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ListLapangan.this,
                        android.R.layout.simple_list_item_1, listlapangan);
                listViewLapangan.setAdapter(adapter);

            } catch (JSONException e) {
                Toast.makeText(ListLapangan.this, "Error parsing JSON data", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}