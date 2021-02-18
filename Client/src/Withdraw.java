/* make new name for method()
*/
public class Withdraw {

    private User user;
    private String response;

    public Withdraw(User user) {
        this.user = user;
        method();
        System.out.println("Your balance is " + user.getBalance());
    }

    private boolean checkAmount(int amount) {
        if(amount > user.getBalance()) {
            System.out.println("Not enough funds");
            return false;
        }
        if(amount <= 0) {
            System.out.println("Should be more than 0");
            return false;
        }
        else
            return true;
    }

    private void method() {
        int amount = MainMenu.getAmount();
        if(checkAmount(amount)) {
            Client.sendRequest(user.getId() + "#2#" + amount + "#null");
            response = Client.getResponse();
            user.setBalance(Long.valueOf(response.substring(response.indexOf("#") + 1)));
        }
    }

}
