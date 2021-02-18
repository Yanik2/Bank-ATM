import java.io.IOException;

public class User {
    private int id;
    private String name;
    private long balance;
    private LoginMenu logMenu;


    public User() {
        logMenu = new LoginMenu();
        initUser();
    }
    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private void initUser() {
        String userData = logMenu.response;
        String[] arr = userData.split("#");
        id = Integer.valueOf(arr[0]);
        name = arr[1];
        balance = Integer.valueOf(arr[2]);
    }
}
