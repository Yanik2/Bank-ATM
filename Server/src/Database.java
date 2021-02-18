import java.io.*;
import java.util.HashMap;

public class Database {
    private HashMap<String, String> map;

    public Database() {
        map = new HashMap<>();
        initMap();
    }

    private void initMap() {
        try(BufferedReader data = new BufferedReader(new InputStreamReader(new FileInputStream("database/database")))) {
            String account;
            do {
                account = data.readLine();
                if (account != null) {
                    String key = account.substring(0, account.indexOf("#"));
                    String value = account.substring(account.indexOf("#") + 1);
                    map.put(key, value);
                }
            } while (account != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized String getValue(String key) {
        return map.get(key);
    }

    public synchronized void setValue(String key, String value) {
        if(map.containsKey(key)) {
            map.replace(key, value);
        } else {
            map.put(key, value);
        }
    }

    public synchronized void updateDatabase() {
        try(DataOutputStream out = new DataOutputStream(new FileOutputStream("database/database"))) {
            for(String key : map.keySet()) {
                out.writeUTF(key + map.get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
