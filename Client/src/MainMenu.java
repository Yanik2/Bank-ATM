import java.io.IOException;

public class MainMenu {

    public void showMenu() {
        System.out.println("===============================");
        System.out.println("Choose your option:");

        System.out.println("1. Check Balance");
        System.out.println("2. Withdraw");
        System.out.println("3. Put money");
        System.out.println("4. Transfer money");
        System.out.println("5. Quit");
        System.out.println("===============================");
        System.out.print(": ");
    }

    public int getChoiceFromInput() {
        int userInput = 0;
        String userInputStr = "";
        try {
             userInputStr = Client.input.readLine();
             userInput = Integer.valueOf(userInputStr);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            userInput = -1;
        }
        return userInput;
    }

    public void runChoice(int choice, User user) {
        switch(choice) {
            case 1:
                new CheckBalance(user);
                break;
            case 2:
                new Withdraw(user);
                break;
            case 3:
                new PutMoney(user);
                break;
            case 4:
                new TransferMoney(user);
                break;
            case 5:
                new Quit(user);
                break;
        }
    }

    public static int getAmount() {
        int amount = 0;
        try {
            amount = Integer.valueOf(Client.input.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return amount;
    }
}
