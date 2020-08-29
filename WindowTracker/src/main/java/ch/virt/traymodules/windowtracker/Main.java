package ch.virt.traymodules.windowtracker;

import ch.virt.trayutils.modules.Module;
import com.google.gson.JsonObject;

import javax.swing.*;

/**
 * @author VirtCode
 * @version 1.0
 */
public class Main extends Module {

    private int checkInterval = 60000; //Default: 1 Minute
    private boolean collectTiles = true;

    public Main() {
        super(3597, "Focus Tracker", "window.png", "This Module tracks how long which application is in your focus. Doing so you have precise analytics of how you are using your time on your computer.");
    }

    @Override
    public void create() {
        System.out.println(Focus.getWindowProcess());
    }

    private void doChecks(){
        String process = Focus.getWindowProcess();

    }

    @Override
    public JPanel settingsMenu() {
        return new JPanel();
    }

    @Override
    public void fromSettings(JsonObject jsonObject) {
        if (jsonObject != null){
            checkInterval = jsonObject.get("check_interval").getAsInt();
            collectTiles = jsonObject.get("collect_titles").getAsBoolean();
        }
    }

    @Override
    public JsonObject toSettings() {
        JsonObject object = new JsonObject();
        object.addProperty("check_interval", checkInterval);
        object.addProperty("collect_titles", collectTiles);
        return object;
    }
}
