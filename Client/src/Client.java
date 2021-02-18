import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Client {
    public User user;
    private static String host = "localhost";
    private static int port = 4000;
    private Socket socket;
    public static DataInputStream in;
    public static DataOutputStream out;
    public static BufferedReader input;

    public Client() {
        createSocket();
        createStreams();
        createReaderForUserInput();
        this.user = new User();
        runMainMenu();
        closeApp();
    }

    private void createSocket() {
        try {
            this.socket = new Socket(host, port);
        } catch (IOException e) {
            System.out.println("Server doesn't work");
            System.exit(0);
        }
    }

    private void createStreams() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createReaderForUserInput() {
        input = new BufferedReader(new InputStreamReader(System.in));
    }

    private void runMainMenu() {
        while(true) {
            MainMenu mainMenu = new MainMenu();
            mainMenu.showMenu();
            int choice = mainMenu.getChoiceFromInput();
            mainMenu.runChoice(choice, user);
            if(choice == 5) break;
        }
    }

    private void closeApp() {
        try {
            in.close();
            out.close();
            input.close();
            if(!socket.isClosed())
                socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendRequest(String request) {
        try {
            Client.out.writeUTF(request);
            Client.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getResponse() {
        String response = "";
        try {
            response = Client.in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
