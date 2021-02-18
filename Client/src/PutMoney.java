public class PutMoney {

    private String response;

    public PutMoney(User user) {
        int amount = MainMenu.getAmount();
        Client.sendRequest(user.getId() + "#3#" + amount + "#null");
        response = Client.getResponse();
        System.out.println(response);
        user.setBalance(Long.valueOf(response.substring(response.indexOf("#") + 1)));
        System.out.println("Your balance is " + user.getBalance());
    }
}
