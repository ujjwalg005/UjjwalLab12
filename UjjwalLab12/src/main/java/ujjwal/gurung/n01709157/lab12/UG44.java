//Ujjwal Gurung n01709157 CENG 258-0CC
package ujjwal.gurung.n01709157.lab12;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;



public class UG44 extends Fragment {

    private EditText ujjPhoneInput, ujjMessageInput;
    private Button ujjSendSmsBtn;

    // Intent Action strings
    private static final String SENT = "SMS_SENT";
    private static final String DELIVERED = "SMS_DELIVERED";

    // Permission Launcher
    private final ActivityResultLauncher<String> requestSmsPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    validateAndSendSms();
                } else {
                    Toast.makeText(getContext(), "SMS Permission Denied", Toast.LENGTH_SHORT).show();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ug44, container, false);

        ujjPhoneInput = view.findViewById(R.id.ujjPhoneInput);
        ujjMessageInput = view.findViewById(R.id.ujjMessageInput);
        ujjSendSmsBtn = view.findViewById(R.id.ujjSendSmsBtn);

        ujjSendSmsBtn.setOnClickListener(v -> {
            // Check if we already have permission
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                validateAndSendSms();
            } else {
                // Request it if we don't
                requestSmsPermissionLauncher.launch(Manifest.permission.SEND_SMS);
            }
        });

        return view;
    }

    private void validateAndSendSms() {
        String phone = ujjPhoneInput.getText().toString().trim();
        String message = ujjMessageInput.getText().toString().trim();

        // 1. Check for empty fields
        if (phone.isEmpty()) {
            ujjPhoneInput.setError("can not be empty");
            return;
        }
        if (message.isEmpty()) {
            ujjMessageInput.setError("can not be empty");
            return;
        }

        // 2. Validate North American Phone Number (Regex for exactly 10 digits, or 1 followed by 10 digits)
        if (!phone.matches("^1?\\d{10}$")) {
            ujjPhoneInput.setError("must be valid phone number");
            return;
        }

        // 3. Send the SMS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            sendSmsProgrammatically(phone, message);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void sendSmsProgrammatically(String phone, String message) {
        // Pending Intents to track status
        PendingIntent sentPI = PendingIntent.getBroadcast(getContext(), 0, new Intent(SENT), PendingIntent.FLAG_IMMUTABLE);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(getContext(), 0, new Intent(DELIVERED), PendingIntent.FLAG_IMMUTABLE);

        // Receiver for when SMS is Sent
        requireActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                if (getResultCode() == Activity.RESULT_OK) {
                    Toast.makeText(getContext(), "SMS sent", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "SMS failed to send", Toast.LENGTH_SHORT).show();
                }
            }
        }, new IntentFilter(SENT), Context.RECEIVER_EXPORTED);

        // Receiver for when SMS is Delivered
        requireActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                if (getResultCode() == Activity.RESULT_OK) {
                    Toast.makeText(getContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
                }
            }
        }, new IntentFilter(DELIVERED), Context.RECEIVER_EXPORTED);

        // Actual sending logic
        SmsManager sms;
        sms = requireContext().getSystemService(SmsManager.class);

        sms.sendTextMessage(phone, null, message, sentPI, deliveredPI);

        // Clear user input
        ujjPhoneInput.setText("");
        ujjMessageInput.setText("");
    }
}