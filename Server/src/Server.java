import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
    public static final int PORT = 4000;

    public static LinkedList<MyThread> threads = new LinkedList<>();

    public static void main(String[] args) {
        Database db = new Database();
        try {
            ServerSocket socket = new ServerSocket(PORT);


            while(true) {
                Socket client = socket.accept();

                threads.add(new MyThread(client, db));
                System.out.println(threads.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void removeFromList(MyThread thread) {
        threads.remove(thread);
    }
}
