//Ujjwal Gurung n01709157 CENG 258-0CC
package ujjwal.gurung.n01709157.lab12;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;



public class Uj11wal extends Fragment {

    private EditText ujjCourseNameInput, ujjCourseDescInput;
    private Button ujjAddBtn, ujjDeleteBtn;
    private RecyclerView ujjRecyclerView;
    private DatabaseReference dbCourses;
    private List<Course> courseList;
    private CourseAdapter adapter;

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uj11wal, container, false);

        ujjCourseNameInput = view.findViewById(R.id.ujjCourseNameInput);
        ujjCourseDescInput = view.findViewById(R.id.ujjCourseDescInput);
        ujjAddBtn = view.findViewById(R.id.ujjAddBtn);
        ujjDeleteBtn = view.findViewById(R.id.ujjDeleteBtn);
        ujjRecyclerView = view.findViewById(R.id.ujjRecyclerView);

        // Force all typed letters to uppercase programmatically as a backup
        ujjCourseNameInput.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        // Initialize Firebase
        dbCourses = FirebaseDatabase.getInstance("https://ujjwallab12-fe2aa-default-rtdb.firebaseio.com").getReference("courses");
        // Setup RecyclerView
        ujjRecyclerView.setHasFixedSize(true);
        ujjRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        courseList = new ArrayList<>();
        adapter = new CourseAdapter(courseList);
        ujjRecyclerView.setAdapter(adapter);

        // Listen for Database changes (Real-time update)
        dbCourses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseList.clear();
                for (DataSnapshot courseSnapshot : snapshot.getChildren()) {
                    Course course = courseSnapshot.getValue(Course.class);
                    courseList.add(course);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

        // ADD BUTTON LOGIC
        ujjAddBtn.setOnClickListener(v -> {
            String name = ujjCourseNameInput.getText().toString().trim();
            String desc = ujjCourseDescInput.getText().toString().trim();

            if (name.isEmpty()) {
                ujjCourseNameInput.setError("Empty not allowed");
                return;
            }
            if (desc.isEmpty()) {
                ujjCourseDescInput.setError("Empty not allowed");
                return;
            }

            // Regex for: 4 letters, hyphen, 3 to 4 numbers
            if (!Pattern.matches("^[A-Z]{4}-\\d{3,4}$", name)) {
                ujjCourseNameInput.setError("Invalid course name. Format: CENG-258");
                return;
            }

            String id = dbCourses.push().getKey();
            Course course = new Course(id, name, desc);

            if (id != null) {
                dbCourses.child(id).setValue(course);
                ujjCourseNameInput.setText("");
                ujjCourseDescInput.setText("");
            }
        });

        // DELETE ALL BUTTON LOGIC
        ujjDeleteBtn.setOnClickListener(v -> {
            if (courseList.isEmpty()) {
                Toast.makeText(getContext(), "No data to delete.", Toast.LENGTH_SHORT).show();
            } else {
                dbCourses.removeValue(); // Deletes entire records from DB
                courseList.clear();
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}
