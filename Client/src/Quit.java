public class Quit {
    private User user;

    public Quit(User user) {
        Client.sendRequest(user.getId() + "#5#null#null");
        System.out.println("========================");
        System.out.println("Good Bye!");
        System.out.println("========================");
    }
}
