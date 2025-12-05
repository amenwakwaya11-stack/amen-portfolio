package courseschedulee;

import java.io.*;
import java.util.*;

// Abstract Base Class
abstract class CourseBase {
    protected String courseName;
    protected String[] days = new String[5]; // Monday to Friday
    protected String instructor;
    protected String roomNumber;

    public CourseBase(String name, String monday, String tuesday, String wednesday,
                      String thursday, String friday, String teacher, String room) {
        this.courseName = name;
        this.days[0] = monday;
        this.days[1] = tuesday;
        this.days[2] = wednesday;
        this.days[3] = thursday;
        this.days[4] = friday;
        this.instructor = teacher;
        this.roomNumber = room;
    }

    public abstract void displayCourse();
    public abstract void saveToFile(PrintWriter writer);

    public void setDaySchedule(int index, String schedule) {
        if (index < 0 || index >= 5) throw new IndexOutOfBoundsException("Invalid day index.");
        days[index] = schedule;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDaySchedule(int index) {
        if (index < 0 || index >= 5) throw new IndexOutOfBoundsException("Invalid day index.");
        return days[index];
    }

    public String getInstructor() {
        return instructor;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    // Derived Class
    public static class Course extends CourseBase {
        public Course(String name, String monday, String tuesday, String wednesday,
                      String thursday, String friday, String instructor, String roomNumber) {
            super(name, monday, tuesday, wednesday, thursday, friday, instructor, roomNumber);
        }

        @Override
        public void displayCourse() {
            System.out.printf("| %-30s ", courseName);
            for (String day : days) {
                System.out.printf("| %-18s ", (day.isEmpty() ? "-" : day));
            }
            System.out.printf("| %-28s | %-12s |\n", instructor, roomNumber);
        }

        @Override
        public void saveToFile(PrintWriter writer) {
            writer.print(courseName + ",");
            for (String day : days) {
                writer.print((day.isEmpty() ? "-" : day) + ",");
            }
            writer.println(instructor + "," + roomNumber);
        }
    }

    // Manager Class
    public static class ScheduleManager {
        private final List<Course> courses = new ArrayList<>();

        public void addCourse(Course course) {
            courses.add(course);
        }

        public void displaySchedule() {
            if (courses.isEmpty()) {
                System.out.println("No courses available.");
                return;
            }

            String format = "| %-30s | %-18s | %-18s | %-18s | %-18s | %-18s | %-28s | %-12s |\n";
            String line = "+--------------------------------+--------------------+--------------------+--------------------+--------------------+--------------------+------------------------------+--------------+";

            System.out.println(line);
            System.out.printf(format, "Course Name", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Instructor", "Room");
            System.out.println(line);

            for (Course course : courses) {
                course.displayCourse();
            }

            System.out.println(line);
        }

        public void saveToFile(String filename) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                for (Course course : courses) {
                    course.saveToFile(writer);
                }
                System.out.println("Schedule saved to '" + filename + "' successfully.");
            } catch (IOException e) {
                System.err.println("❌ Error saving to file: " + e.getMessage());
            }
        }

        public void loadFromFile(String filename) {
            courses.clear();
            try (Scanner scanner = new Scanner(new File(filename))) {
                while (scanner.hasNextLine()) {
                    String[] parts = scanner.nextLine().split(",");
                    if (parts.length == 8) {
                        Course course = new Course(parts[0], parts[1], parts[2], parts[3],
                                parts[4], parts[5], parts[6], parts[7]);
                        courses.add(course);
                    }
                }
                System.out.println("Schedule loaded from '" + filename + "' successfully.");
            } catch (IOException e) {
                System.err.println("❌ Error loading from file: " + e.getMessage());
            }
        }

        public void editCourse(String courseName, Scanner input) {
            for (Course course : courses) {
                if (course.getCourseName().equalsIgnoreCase(courseName)) {
                    System.out.print("Enter new instructor: ");
                    course.setInstructor(input.nextLine());

                    System.out.print("Enter new room number: ");
                    course.setRoomNumber(input.nextLine());

                    for (int i = 0; i < 5; i++) {
                        System.out.print("Enter schedule for " + getDayName(i) + ": ");
                        course.setDaySchedule(i, input.nextLine());
                    }
                    System.out.println(" Course updated successfully.");
                    return;
                }
            }
            System.out.println("❌ Course not found.");
        }

        public void deleteCourse(String courseName) {
            if (courses.removeIf(course -> course.getCourseName().equalsIgnoreCase(courseName))) {
                System.out.println(" Course deleted successfully.");
            } else {
                System.out.println("❌ Course not found.");
            }
        }

        public boolean hasScheduleConflict(Course newCourse) {
            for (Course existing : courses) {
                for (int i = 0; i < 5; i++) {
                    String newTime = newCourse.getDaySchedule(i).trim();
                    String existingTime = existing.getDaySchedule(i).trim();

                    if (!newTime.isEmpty() && !existingTime.isEmpty() && newTime.equalsIgnoreCase(existingTime)) {
                        String day = getDayName(i);

                        if (existing.getCourseName().equalsIgnoreCase(newCourse.getCourseName())) {
                            System.out.printf("❌ Conflict: Course '%s' is already assigned at '%s' on %s to '%s'.\n",
                                    existing.getCourseName(), existingTime, day, existing.getInstructor());
                            return true;
                        }

                        if (existing.getInstructor().equalsIgnoreCase(newCourse.getInstructor())) {
                            System.out.printf("❌ Conflict: Instructor '%s' already has a class at '%s' on %s (%s).\n",
                                    existing.getInstructor(), existingTime, day, existing.getCourseName());
                            return true;
                        }

                        if (existing.getRoomNumber().equalsIgnoreCase(newCourse.getRoomNumber())) {
                            System.out.printf("❌ Conflict: Room '%s' already has '%s' at '%s' on %s.\n",
                                    newCourse.getRoomNumber(), existing.getCourseName(), existingTime, day);
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        private static String getDayName(int index) {
            return switch (index) {
                case 0 -> "Monday";
                case 1 -> "Tuesday";
                case 2 -> "Wednesday";
                case 3 -> "Thursday";
                case 4 -> "Friday";
                default -> "Unknown";
            };
        }
    }
}

// Main Class
public class CourseSchedule {
    public static void main(String[] args) {
        CourseBase.ScheduleManager manager = new CourseBase.ScheduleManager();
        loadPredefinedSchedule(manager); // Load predefined courses

        try (Scanner input = new Scanner(System.in)) {
            int choice;

            do {
                System.out.println("\n--- Wolkite University Class Schedule ---");
                System.out.println("1. Add Course");
                System.out.println("2. Edit Course");
                System.out.println("3. Delete Course");
                System.out.println("4. Display Schedule");
                System.out.println("5. Save Schedule to File");
                System.out.println("6. Load Schedule from File");
                System.out.println("7. Exit");
                System.out.print("Enter your choice: ");
                while (!input.hasNextInt()) {
                    System.out.print("Please enter a valid number: ");
                    input.next();
                }
                choice = input.nextInt();
                input.nextLine();

                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter course name: ");
                        String name = input.nextLine();
                        System.out.print("Enter instructor name: ");
                        String teacher = input.nextLine();
                        System.out.print("Enter room number: ");
                        String room = input.nextLine();
                        String[] days = new String[5];
                        for (int i = 0; i < 5; i++) {
                            System.out.print("Enter schedule for " + getDayName(i) + " (in time): ");
                            days[i] = input.nextLine();
                        }
                        CourseBase.Course newCourse = new CourseBase.Course(name, days[0], days[1], days[2], days[3], days[4], teacher, room);
                        if (manager.hasScheduleConflict(newCourse)) {
                            System.out.println("❌ Cannot add course due to conflict.\n");
                        } else {
                            manager.addCourse(newCourse);
                            System.out.println(" Course added successfully!\n");
                            manager.displaySchedule();
                        }
                    }
                    case 2 -> {
                        System.out.print("Enter course name to edit: ");
                        String courseName = input.nextLine();
                        manager.editCourse(courseName, input);
                        System.out.println();
                        manager.displaySchedule();
                    }
                    case 3 -> {
                        System.out.print("Enter course name to delete: ");
                        String courseName = input.nextLine();
                        manager.deleteCourse(courseName);
                        System.out.println();
                        manager.displaySchedule();
                    }
                    case 4 -> {
                        System.out.println();
                        manager.displaySchedule();
                    }
                    case 5 -> {
                        System.out.print("Enter filename to save schedule: ");
                        String filename = input.nextLine();
                        manager.saveToFile(filename);
                    }
                    case 6 -> {
                        System.out.print("Enter filename to load schedule: ");
                        String filename = input.nextLine();
                        manager.loadFromFile(filename);
                        System.out.println();
                        manager.displaySchedule();
                    }
                    case 7 -> System.out.println("Exiting... Goodbye!");
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } while (choice != 7);
        }
    }

    private static String getDayName(int index) {
        return switch (index) {
            case 0 -> "Monday";
            case 1 -> "Tuesday";
            case 2 -> "Wednesday";
            case 3 -> "Thursday";
            case 4 -> "Friday";
            default -> "Unknown";
        };
    }

    // Predefined Schedule
    private static void loadPredefinedSchedule(CourseBase.ScheduleManager manager) {
        manager.addCourse(new CourseBase.Course(
                "Data Structures and Algorithms", "4:30-6:20 (Lec)", "", "2:30-5:20 (Lab-17)", "", "", 
                "Mr. Abdo A. ", "CCI-12"));
        manager.addCourse(new CourseBase.Course(
                "Object Oriented Programming", "7:30-9:20 (Lec)", "", "7:30-10:30 (Lab-06)", "", "", 
                "Mr. Nigus O. ", "CCI-12"));
        manager.addCourse(new CourseBase.Course(
                "Advanced Database Systems", "", "", "", "2:30-4:20 (Lec)", "2:30-5:20 (Lab-17)", 
                "Mr. Teshome Y. ", "CCI-12"));
        manager.addCourse(new CourseBase.Course(
                "COA", "", "2:30-4:20 (Lec)", "", "", "2:30-5:20 (Lab-06)", 
                "Mr. Amanuel T.", "CCI-12"));
        manager.addCourse(new CourseBase.Course(
                "Computer Networks", "4:30-6:20 (Lec)", "4:30-6:20 (Lec)", "", "", "", 
                "Mr. Wondosen T.", "CCI-12"));
        manager.addCourse(new CourseBase.Course(
                "Ethical Issues in IT", "", "", "", "7:30-9:20 (Lec)", "", 
                "Mrs. Mimi A.", "CCI-12"));
        manager.addCourse(new CourseBase.Course(
                "Discrete Mathematics", "", "9:30-11:20 (Lec)", "", "4:30-6:20 (Lec)", "", 
                "Mr.Zenebe.", "CCI-12"));
        manager.addCourse(new CourseBase.Course(
                "Oracle Database", "9:30-11:20 (Lab-06)", "", "", "", "9:30-12:20 (Lab-06)", 
                "Mr.Teshome Y.", "Lab-06"));
        manager.addCourse(new CourseBase.Course(
                "DLS/LS/LMS", "", "", "", "", "7:30-9:20 (Lab-06)", 
                "TBA", "Lab-06"));

        System.out.println(" Predefined schedule loaded successfully.\n");
    }
}
