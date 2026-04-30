import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Backend Validation Engine");
        DatabaseManager.connect();

        List<User> users = DataLoader.loadUsers("data/test-data.csv");
        for (User user : users) {
            System.out.println("Loaded test user: " + user.getUsername());
        }
    }
}
