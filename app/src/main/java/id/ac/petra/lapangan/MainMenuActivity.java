package id.ac.petra.lapangan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Button listlapanganButton;
    private Button bookingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        listlapanganButton = findViewById(R.id.listLapanganButton);
        bookingButton = findViewById(R.id.bookingButton);

        listlapanganButton.setOnClickListener(this);
        bookingButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.listLapanganButton:
                // Handle List Kendaraan button click
                openListLapanganActivity();
                break;
            case R.id.bookingButton:
                // Handle Booking button click
                openBookingActivity();
                break;
        }
    }

    private void openListLapanganActivity() {
        Intent intent = new Intent(this, ListLapangan.class);
        startActivity(intent);
    }

    private void openBookingActivity() {
        Intent intent = new Intent(this, BookingActivity.class);
        startActivity(intent);
    }
}