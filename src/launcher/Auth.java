package launcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import net.kronos.mclaunch_util_lib.auth.YggdrasilRequester;
import net.kronos.mclaunch_util_lib.auth.model.YggdrasilAgent;
import net.kronos.mclaunch_util_lib.auth.model.YggdrasilAuthenticateRes;
import net.kronos.mclaunch_util_lib.auth.model.YggdrasilError;

/**
 * Mojang Account auth using mclaunch_util_lib
 */
public class Auth {

    public static String[] current_user;
    private final static String USER_DATA = "uuid: %s\nusername: %s\naccessToken: %s\n";

    public static String getName() {
        return current_user[1];
    }

    public static String getToken() {
        return current_user[2];
    }

    public static boolean isPaid() {
        return current_user.length > 3 ? Boolean.parseBoolean(current_user[3]) : true;
    }

    public static void setUser(String email, String password) throws Error {
        try {
            current_user = authenticate(email, password);
            save();
        } catch (Throwable e) {
            if (e.getMessage().contains("HTTP response code: 403"))
                throw new Error("Invalid Email and/or Password");
            throw new Error(e.getMessage());
        }
    }

    public static void setUser(File json) throws Exception {
        String[] data = new String[3];
        int i = 0;
        for (String s : Files.readAllLines(json.toPath())) {
            if (s.contains(":")) {
                data[i] = s.split(":")[1].trim();
                i++;
            }
        }
        current_user = data;
    }

    public static void save() throws IOException {
        File f = new File(Main.DIR, "user.dat");
        f.getParentFile().mkdirs();
        f.createNewFile();
        Files.write(f.toPath(), String.format(USER_DATA, current_user[0], current_user[1], current_user[2], current_user[3]).getBytes());
    }

    /**
     * @return String Array<br>
     *     [0] = playerUUID<br>
     *     [1] = playerName<br>
     *     [2] = authToken<br>
     *     [3] = isPaid<br>
     * @throws Throwable 
     */
    public static String[] authenticate(String username, String password) throws Throwable {
        YggdrasilRequester req = new YggdrasilRequester();
        YggdrasilAuthenticateRes res = null;

        try {
            res = req.authenticate(YggdrasilAgent.getMinecraftAgent(), username, password, "isaiahlaunch");
            System.out.println("Look what I got: " + res.getAccessToken());
        } catch (IOException | YggdrasilError e) {
            throw e;
        }

        return new String[] { res.getSelectedProfile().getId(), res.getSelectedProfile().getName(), res.getAccessToken(), "paid" };
    }

    public static void logout() {
        current_user = null;
        new File(Main.DIR, "user.dat").delete();
        Main.showLogin();
    }

}