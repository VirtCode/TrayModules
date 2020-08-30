package ch.virt.traymodules.windowtracker;

import ch.virt.traymodules.windowtracker.data.Data;
import ch.virt.trayutils.Dialogs;
import ch.virt.trayutils.gui.helper.ComponentFactory;
import ch.virt.trayutils.modules.Module;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.$Gson$Preconditions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author VirtCode
 * @version 1.0
 */
public class Main extends Module {

    public static final String FILE = System.getenv("APPDATA") + "/TrayUtils/windowtrackerdata.json";
    private static final String TAG = "[WindowTracker] ";

    private int checkInterval = 60000; //Default: 1 Minute
    private int saveInterval = 60000;
    private boolean collectTitles = true;

    private Data data;
    private Timer timer;

    public Main() {
        super(3597, "Focus Tracker", "window.png", "This Module tracks how long which application is in your focus. Doing so you have precise analytics of how you are using your time on your computer. At the moment, there is no convenient way to view the collected data. But since this project is Open Source there will soon be.");
    }

    @Override
    public void create() {
        doLoad();

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                doSave();
            }
        }, saveInterval, saveInterval);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                doChecks();
            }
        }, checkInterval, checkInterval);
    }

    private void doChecks(){
        String process = Focus.getWindowProcess();
        String name = collectTitles ? Focus.getWindowText() : null;

        data.usedProcess(checkInterval / 1000, process, name);
    }

    private void doSave(){
        try {
            String json = new Gson().toJson(data);
            FileWriter fw = new FileWriter(new File(FILE));
            fw.write(json);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            System.err.println(TAG + "Failed to save new Data");
            e.printStackTrace();
        }
    }

    private void doLoad(){
        File file = new File(FILE);
        if (file.exists()){
            try {
                FileReader reader = new FileReader(file);
                data = new Gson().fromJson(reader, Data.class);
            } catch (FileNotFoundException e) {
                System.err.println(TAG + "Failed to load new Data");
                data = new Data();
                e.printStackTrace();
            }
        }else data = new Data();
    }

    @Override
    public JPanel settingsMenu() {
        JPanel panel = ComponentFactory.createGroup();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JCheckBox collectTitles = ComponentFactory.createCheckBox();
        collectTitles.setText("Save window Titles");
        collectTitles.setSelected(this.collectTitles);

        collectTitles.addActionListener(e -> {
            this.collectTitles = collectTitles.isSelected();
            eventBus.saveSettings();
        });

        JButton eraseData = ComponentFactory.createButton();
        eraseData.setText("Erase previously collected Data");
        eraseData.addActionListener(e -> {
            data = new Data();
            doSave();
        });

        JButton openFile = ComponentFactory.createButton();
        openFile.setText("Open Collected Data");
        openFile.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(new File(FILE));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        JButton showInExplorer = ComponentFactory.createButton();
        showInExplorer.setText("Show Data File in Explorer");
        showInExplorer.addActionListener(e -> Desktop.getDesktop().browseFileDirectory(new File(FILE)));

        panel.add(collectTitles);
        panel.add(openFile);
        panel.add(getPadding());
        panel.add(showInExplorer);
        panel.add(getPadding());
        panel.add(eraseData);

        return panel;
    }

    private JPanel getPadding(){
        JPanel padding = ComponentFactory.createGroup();
        padding.setMaximumSize(new Dimension(1, 6));
        padding.setMinimumSize(new Dimension(1, 6));
        return padding;
    }

    @Override
    public void fromSettings(JsonObject jsonObject) {
        if (jsonObject != null){
            if (jsonObject.has("check_interval")) checkInterval = jsonObject.get("check_interval").getAsInt();
            if (jsonObject.has("save_interval")) saveInterval = jsonObject.get("save_interval").getAsInt();
            if (jsonObject.has("collect_title")) collectTitles = jsonObject.get("collect_titles").getAsBoolean();
        }
    }

    @Override
    public JsonObject toSettings() {
        JsonObject object = new JsonObject();
        object.addProperty("check_interval", checkInterval);
        object.addProperty("save_interval", saveInterval);
        object.addProperty("collect_titles", collectTitles);
        return object;
    }
}
