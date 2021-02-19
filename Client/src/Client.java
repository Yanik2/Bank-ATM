import java.io.*;
import java.net.Socket;

public class Client {
    public User user;
    private final static String HOST = "localhost";
    private final static int PORT = 4000;
    private Socket socket;
    public static DataInputStream in;
    public static DataOutputStream out;
    public static BufferedReader input;

    public Client() {
        createSocket();
        createStreams();
        createReaderForUserInput();
        this.user = new User();
        MainMenu mainMenu = new MainMenu();
        mainMenu.runMenu(user);
        closeApp();
    }

    private void createSocket() {
        try {
            this.socket = new Socket(HOST, PORT);
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
            System.out.println("Server is not available now");
        }
        return response;
    }
}
