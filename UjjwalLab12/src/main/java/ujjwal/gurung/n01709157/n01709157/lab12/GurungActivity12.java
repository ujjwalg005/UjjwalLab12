//Ujjwal Gurung n01709157 CENG 258-0CC
package ujjwal.gurung.n01709157.n01709157.lab12; // Fixed package name

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;



public class GurungActivity12 extends AppCompatActivity {

    private TabLayout ujjTabLayout;
    private ViewPager2 ujjViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gurung); // Make sure your layout file matches this name

        ujjTabLayout = findViewById(R.id.ujjTabLayout);
        ujjViewPager = findViewById(R.id.ujjViewPager);

        // Setup the ViewPager Adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        ujjViewPager.setAdapter(adapter);

        // Link TabLayout and ViewPager2
        new TabLayoutMediator(ujjTabLayout, ujjViewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(getString(R.string.ujj_tab1_name)); // "Ujjwal"
                            tab.setIcon(R.drawable.ic_home);
                            break;
                        case 1:
                            tab.setText(getString(R.string.ujj_tab2_name)); // "Gurung"
                            tab.setIcon(R.drawable.ic_cart);
                            break;
                        case 2:
                            tab.setText(getString(R.string.ujj_tab3_name)); // "N01709157"
                            tab.setIcon(R.drawable.ic_truck);
                            break;
                        case 3:
                            tab.setText(getString(R.string.ujj_tab4_name)); // "UG"
                            tab.setIcon(R.drawable.ic_chat);
                            break;
                    }
                }).attach();
    }

    // 1. This inflates the menu onto the ActionBar (Now safely INSIDE the class!)
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // 2. This handles the click logic
    @Override
    public boolean onOptionsItemSelected(@NonNull android.view.MenuItem item) {
        if (item.getItemId() == R.id.ujj_action_contact) {

            // Find out which tab we are currently on
            int currentTab = ujjViewPager.getCurrentItem();

            if (currentTab == 0 || currentTab == 1 || currentTab == 2) {
                // If on screens 1, 2, or 3, swipe over to the 4th screen
                ujjViewPager.setCurrentItem(3);

            } else if (currentTab == 3) {
                // If on the 4th screen, display the mock contact
                String contactName = "Subash Gole";
                String contactPhone = "416-555-0198";

                // Build the AlertDialog
                android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this)
                        .setTitle(getString(R.string.ujj_dialog_title))
                        .setMessage("Name: " + contactName + "\nPhone: " + contactPhone)
                        .setPositiveButton(getString(R.string.ujj_dialog_ok), null)
                        .create();

                // Force the dialog background to be green
                dialog.setOnShowListener(d -> {
                    dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(
                            getResources().getColor(R.color.alert_green, null)));
                });

                dialog.show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Adapter class to manage fragments
    private static class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull AppCompatActivity activity) {
            super(activity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0: return new Uj11wal();
                case 1: return new Gu22rung();
                case 2: return new n0133709157();
                case 3: return new UG44();
                default: return new Uj11wal();
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}