import java.io.IOException;

public class MainMenu {

    private int choice;

    public void runMenu(User user) {
        while(true) {
            showMenu();
            getChoiceFromInput();
            if(choice < 1 || choice > 5) {
                System.out.println("Try again");
                continue;
            }
            runChoice(user);
            if(choice == 5) break;
        }
    }

    private void showMenu() {
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

    private void getChoiceFromInput() {

        String userInputStr;
        try {
             userInputStr = Client.input.readLine();
             choice = Integer.parseInt(userInputStr);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            choice = -1;
        }
    }

    public void runChoice(User user) {
        String request = makeRequest(choice, user);
        if(request.equals("Not enough money")) {
            System.out.println(request);
            return;
        }
        Client.sendRequest(request);
        if(choice == 5) {
            printGoodBye();
            return;
        }
        String response = Client.getResponse();
        responseProcess(response, user);
    }

    public String makeRequest(int choice, User user) {
        int amount = MainMenu.getAmount(choice);
        if(checkAmount(user, amount)) {

            String id = getId(choice);
            return user.getId() + "#" + choice + "#" + amount + "#" + id;
        }
        else
            return "Not enough money";

    }

    public static int getAmount(int choice) {
        if(choice == 1 || choice == 5) return 0;
        System.out.println("Enter amount: ");
        int amount = 0;
        try {
            amount = Integer.parseInt(Client.input.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            amount = -1;
        }
        return amount;
    }

    private boolean checkAmount(User user,  int amount) {
        if(choice == 2 || choice == 4)
            return amount >= 0 && user.getBalance() >= amount;
        else
            return amount >= 0;
    }

    private String getId(int choice) {
        if(choice != 4) return "null";

        else {
            System.out.println("Enter receiver id: ");
            String out = "";
            try {
                out = Client.input.readLine();
            } catch(IOException e) {
                e.printStackTrace();
            } return out;
        }
    }

    private void responseProcess(String response, User user) {

        String[] splitRes = response.split("#");
        if(splitRes.length != 2) {
            System.out.println(response);
            return;
        }
        System.out.println("Your balance: " + splitRes[1]);
        user.setBalance(Long.parseLong(splitRes[1]));
    }

    private void printGoodBye() {
        System.out.println("==================");
        System.out.println("Good bye!");
        System.out.println("==================");
    }
}
