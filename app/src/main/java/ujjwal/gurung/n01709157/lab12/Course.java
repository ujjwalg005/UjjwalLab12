// Ujjwal Gurung - [Your Student ID]
package ujjwal.gurung.n01709157.lab12;

public class Course {
    private String id;
    private String name;
    private String description;

    public Course() {
        // Default constructor required for Firebase
    }

    public Course(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}