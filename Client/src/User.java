public class User {
    private int id;
    private String name;
    private long balance;
    private final LoginMenu logMenu;


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

    public int getId() {
        return id;
    }

    private void initUser() {
        String userData = logMenu.response;
        String[] arr = userData.split("#");
        id = Integer.parseInt(arr[0]);
        name = arr[1];
        balance = Integer.parseInt(arr[2]);
    }
}
