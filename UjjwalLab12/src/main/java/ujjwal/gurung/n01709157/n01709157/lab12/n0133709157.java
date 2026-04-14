//Ujjwal Gurung n01709157 CENG 258-0CC
package ujjwal.gurung.n01709157.n01709157.lab12;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class n0133709157 extends Fragment {

    private Spinner ujjSpinner;
    private WebView ujjWebView;
    private AdView ujjAdView;
    private int clickCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Make sure you create this XML file in your layout folder!
        View view = inflater.inflate(R.layout.fragment_n0133709157, container, false);

        ujjSpinner = view.findViewById(R.id.ujjSpinner);
        ujjWebView = view.findViewById(R.id.ujjWebView);
        ujjAdView = view.findViewById(R.id.ujjAdView);

        // 1. Initialize AdMob and Load Banner
        MobileAds.initialize(requireContext(), initializationStatus -> {});
        AdRequest adRequest = new AdRequest.Builder().build();
        ujjAdView.loadAd(adRequest);

        // 2. Ad Click Counter Logic
        ujjAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
                clickCount++;
                Toast.makeText(getContext(), "Ujjwal + " + clickCount, Toast.LENGTH_SHORT).show();
            }
        });

        // 3. Configure WebView
        WebSettings webSettings = ujjWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        ujjWebView.setWebViewClient(new WebViewClient());

        // 4. Populate Spinner
        String[] websites = {"Local Lab HTML", "CBC News", "Humber College", "GitHub"};
        String[] urls = {
                "file:///android_asset/index.html",
                "https://www.cbc.ca",
                "https://humber.ca",
                "https://github.com"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, websites);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ujjSpinner.setAdapter(adapter);

        // 5. Handle Spinner Selection
        ujjSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ujjWebView.loadUrl(urls[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }
}