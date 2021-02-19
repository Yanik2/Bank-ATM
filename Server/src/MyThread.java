import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class MyThread extends Thread{

    private DataInputStream in;
    private DataOutputStream out;
    private final Socket client;
    private BufferedReader logsNpass;
    private HashMap<String, String> loginsAndPasswords;
    private final Database db;

    public MyThread(Socket client, Database db) {
        this.client = client;
        this.db = db;
        initStreams();
        initMap();
        start();
    }

    @Override
    public void run() {
        while(true) {
            String request = getRequest();
            if(request.equals("Error")) {
                downService();
                Server.removeFromList(this);
                return;
            }
            String checkedRequest = checkLoginAndGetId(request);
            if (checkedRequest.equals("-1")) {
                sendResponse("Access denied");
                continue;
            }
            String[] reqArr = checkedRequest.split("#");
            String response = makeResponse(reqArr[0], reqArr[1], reqArr[2], reqArr[3]);
            if(response.equals("user quits")) {
                db.updateDatabase();
                break;
            }
            sendResponse(response);
        }
    }

    private String getRequest() {
        String request;
        try {
            request = in.readUTF();
        } catch (IOException e) {
            request = "Error";
        }
        return request;
    }

    private String checkLoginAndGetId(String request) {
        if(request.startsWith("login")) {
            int index = request.indexOf("#");
            String loginRequest = request.substring(index + 1);
            if (loginsAndPasswords.containsValue(loginRequest))
                for (String key : loginsAndPasswords.keySet()) {
                    if (loginsAndPasswords.get(key).equals(loginRequest))
                        return key + "#0" + "#null" + "#null";
                }
            return "-1";
        }
        return request;
    }

    private String makeResponse(String id, String request, String amount, String destinationId) {
        String response = "";
        switch(request) {
            case "0":
                response = id + "#" + db.getValue(id);
                break;
            case "1":
                response =  db.getValue(id);
                break;
            case "2":
                response = changeBalance(id, amount, false);
                break;
            case "3":
                response = changeBalance(id, amount, true);
                break;
            case "4":
                if(!db.checkForDestinationId(destinationId)) {
                    response = "There is no user with that id";
                    break;
                }
                response = transferMoney(id, amount, destinationId);
                break;
            case "5":
                response = downService();
                Server.removeFromList(this);
                break;
        }
        return response;
    }

    private void sendResponse(String response) {
        try {
            out.writeUTF(response);
            out.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private String transferMoney(String sourceAccount, String amount, String destinationId) {
        changeBalance(destinationId, amount, true);
        return changeBalance(sourceAccount, amount, false);
    }

    private String changeBalance(String id, String amount, boolean plus) {
        String account = db.getValue(id);
        String funds = account.substring(account.indexOf("#") + 1);
        int newBalance;
        if(plus)
            newBalance = Integer.parseInt(funds) + Integer.parseInt(amount);
        else
            newBalance = Integer.parseInt(funds) - Integer.parseInt(amount);
        String result = account.substring(0, account.indexOf("#")) + "#" + newBalance;
        db.setValue(id, result);
        return result;
    }

    private String downService() {
        try {
            if(!client.isClosed()) {
                in.close();
                out.close();
                logsNpass.close();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return "user quits";
    }

    private void initStreams() {
        try {
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
            logsNpass = new BufferedReader(new InputStreamReader(new FileInputStream("database/logsAndPass")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMap() {
        loginsAndPasswords = new HashMap<>();
        String key;
        String value;
        try {
            do {
                key = logsNpass.readLine();
                value = logsNpass.readLine();
                if(key != null && value != null) {
                    loginsAndPasswords.put(key, value);
                }
            } while(key != null && value != null);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
