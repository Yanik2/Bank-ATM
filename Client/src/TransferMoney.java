import java.io.IOException;

public class TransferMoney {

    private User user;
    private String response;

    public TransferMoney(User user) {
        this.user = user;
        System.out.println("===========================");
        method();
        System.out.println("Your balance is: " + user.getBalance());
    }

    private String getDestName() {
        String name = "";
        try {
            name = Client.input.readLine();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return name;
    }

    private boolean transfer(int amount, String destName) {


        return true;
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
        System.out.println("Enter amount you want to transfer: ");
        int amount = MainMenu.getAmount();
        if(checkAmount(amount)) {
            System.out.println("Transfer money to(id): ");
            String destId = getDestName();
            Client.sendRequest(user.getId() + "#4#" + amount + "#" + destId);
            response = Client.getResponse();
            user.setBalance(Long.valueOf(response.substring(response.indexOf("#") + 1)));
            System.out.print("Success!");
        }
    }
}
