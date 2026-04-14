//Ujjwal Gurung n01709157 CENG 258-0CC
package ujjwal.gurung.n01709157.n01709157.lab12;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private List<Course> courseList;
    private DatabaseReference dbCourses;

    public CourseAdapter(List<Course> courseList) {
        this.courseList = courseList;
        this.dbCourses = FirebaseDatabase.getInstance().getReference("courses");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.name.setText(course.getName());
        holder.desc.setText(course.getDescription());

        // Delete specific record on long press
        holder.itemView.setOnLongClickListener(v -> {
            dbCourses.child(course.getId()).removeValue();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class ViewHolder extends RecyclerView.Adapter<ViewHolder> {
        TextView name, desc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ujjRowCourseName);
            desc = itemView.findViewById(R.id.ujjRowCourseDesc);
        }
    }
}