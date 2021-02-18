public class CheckBalance {
    private String response;
    private User user;

    CheckBalance(User user) {
        this.user = user;
        Client.sendRequest(user.getId() + "#1#null#null");
        response = Client.getResponse();
        showBalance();
        user.setBalance(Long.valueOf(response));
    }

    private void showBalance() {
        System.out.println("=========================");
        System.out.println("Your balance is: " + response + "$");
    }

}
