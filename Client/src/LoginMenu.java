import java.io.IOException;

public class LoginMenu {
    private String login;
    private String password;
    public String response;

    LoginMenu() {
        while (true) {
            System.out.println("====================");
            System.out.println("Enter your login:");
            this.getLogin();
            System.out.println("Enter your password:");
            this.getPassword();
            if (tryToLogin())
                break;
        }
    }

    private void getLogin() {
        try {
            this.login = Client.input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getPassword() {
        try {
            this.password = Client.input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean tryToLogin() {

        try {
            Client.out.writeUTF(this.login + "#" + this.password);
            Client.out.flush();
            this.response = Client.in.readUTF();
            if(this.response.equals("Access denied"))
                return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
