//Ujjwal Gurung n01709157 CENG 258-0CC
package ujjwal.gurung.n01709157.lab12;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import java.io.IOException;
import java.util.List;
import java.util.Locale;



public class Gu22rung extends Fragment implements OnMapReadyCallback {

    private MapView ujjMapView;
    private GoogleMap ujjGoogleMap;
    private TextView ujjMapTitleText;
    private static final String CHANNEL_ID = "ujjwal_map_channel";

    // Request Notification Permission for API 33+
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    Toast.makeText(getContext(), "Notification permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gu22rung, container, false);

        ujjMapTitleText = view.findViewById(R.id.ujjMapTitleText);
        ujjMapView = view.findViewById(R.id.ujjMapView);

        ujjMapView.onCreate(savedInstanceState);
        ujjMapView.getMapAsync(this);

        createNotificationChannel();
        askNotificationPermission();

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        ujjGoogleMap = googleMap;
        ujjGoogleMap.getUiSettings().setZoomControlsEnabled(true); // Allow zoom in/out

        // Default Location: Humber College (Davis Campus in Brampton)
        LatLng humberBrampton = new LatLng(43.663, -79.734);
        Marker defaultMarker = ujjGoogleMap.addMarker(new MarkerOptions()
                .position(humberBrampton)
                .title("Humber College")
                .snippet("Brampton + Ujjwal"));

        ujjGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(humberBrampton, 14f));

        // Show info window without clicking
        if (defaultMarker != null) {
            defaultMarker.showInfoWindow();
        }

        // Map Click Listener
        ujjGoogleMap.setOnMapClickListener(latLng -> {
            ujjGoogleMap.clear(); // Clear old markers
            String addressText = getAddressFromLatLng(latLng);

            // Update Map Marker
            Marker newMarker = ujjGoogleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Selected Location")
                    .snippet(addressText));
            ujjGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

            if (newMarker != null) newMarker.showInfoWindow();

            // Update TextView
            ujjMapTitleText.setText(addressText);

            // Show Snackbar
            Snackbar.make(requireView(), addressText, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Dismiss", v -> {})
                    .show();

            // Fire Notification
            sendNotification(addressText);
        });
    }

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown Location at: " + latLng.latitude + ", " + latLng.longitude;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Map Address Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = requireContext().getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void sendNotification(String address) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_home) // Replace with your proper icon
                .setContentTitle("Address Change")
                .setContentText(address)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVibrate(new long[]{0, 500, 200, 500}); // Vibration pattern

        NotificationManager manager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(1, builder.build());
        }
    }

    // MapView Lifecycle methods are required when using MapView in a Fragment
    @Override public void onResume() { super.onResume(); ujjMapView.onResume(); }
    @Override public void onPause() { super.onPause(); ujjMapView.onPause(); }
    @Override public void onDestroy() { super.onDestroy(); ujjMapView.onDestroy(); }
    @Override public void onLowMemory() { super.onLowMemory(); ujjMapView.onLowMemory(); }
}