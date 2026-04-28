public class Main {

    public static void main(String[] args) {
        System.out.println("Backend Validation Engine");
        DatabaseManager.connect();

        User sample = new User(1, "nimali_p", "ACTIVE");
        System.out.println("Loaded test user: " + sample.getUsername());
    }
}
