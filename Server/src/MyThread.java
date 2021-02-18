import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class MyThread extends Thread{

    private DataInputStream in;
    private DataOutputStream out;
    private Socket client;
    private BufferedReader logsNpass;
    private BufferedReader database;
    private HashMap<String, String> loginsAndPasswords;
    private Database db;

    public MyThread(Socket client, Database db) {
        this.client = client;
        this.db = db;
        initStreams();
        initMap();
        start();
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

    @Override
    public void run() {
        while(true) {
            String request = logIn();
            if(request.equals("Error")) {
                downService();
                Server.removeFromList(this);
                return;
            }
            String id = checkLoginAndGetId(request);
            if (id.equals("-1")) {
                sendResponse("Access denied");
                continue;
            }
            String response = makeResponse(id);
            sendResponse(response);
            break;
        }

        while(true) {
            String request = getRequest();
            if(request.equals("Error")) {
                downService();
                Server.removeFromList(this);
                break;
            }
            String[] reqArr = request.split("#");
            String response = makeResponse(reqArr[0], reqArr[1], reqArr[2], reqArr[3]);
            if(response.equals("break from loop")) break;
            sendResponse(response);
        }
    }

    private String logIn() {
        String request;
        try {
            request = in.readUTF();
        } catch (IOException e) {
//            e.printStackTrace();
            request = "Error";
        }
        return request;
    }

    private String checkLoginAndGetId(String request) {
        if(loginsAndPasswords.containsValue(request))
            for(String key : loginsAndPasswords.keySet()) {
                if(loginsAndPasswords.get(key).equals(request))
                    return key;
            }
        return "-1";
    }

    private String makeResponse(String id) {
        String response;
        response = id + "#" + db.getValue(id);
        return response;
    }

    private String makeResponse(String id, String request, String amount, String destinationId) {
        String value = db.getValue(id);
        String response = "";
        switch(request) {
            case "1": response =  value.substring(value.indexOf("#") + 1);
                break;
            case "2":
                response = changeBalance(value, amount, false);
                db.setValue(id, response);
                break;
            case "3":
                response = changeBalance(value, amount, true);
                db.setValue(id, response);
                break;
            case "4":
                response = transferMoney(value, amount, destinationId);
                db.setValue(id, response);
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
        return "break from loop";
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

    private String getRequest() {
        String request = "";
        try {
            request = in.readUTF();
        } catch (IOException e) {
            request = "Error";
        }
        return request;
    }

    private String changeBalance(String account, String amount, boolean plus) {
        String funds = account.substring(account.indexOf("#") + 1);
        int newBalance;
        if(plus)
            newBalance = Integer.valueOf(funds) + Integer.valueOf(amount);
        else
            newBalance = Integer.valueOf(funds) - Integer.valueOf(amount);
        return account.substring(0, account.indexOf("#")) + "#" + newBalance;
    }

    private String transferMoney(String sourceAccount, String amount, String destinationId) {
        String destinationAccount = db.getValue(destinationId);
        String newBalanceForSourceAccount = changeBalance(sourceAccount, amount, false);
        String newBalanceForDestAccount = changeBalance(destinationAccount, amount, true);
        db.setValue(destinationId, newBalanceForDestAccount);
        return newBalanceForSourceAccount;
    }
}
