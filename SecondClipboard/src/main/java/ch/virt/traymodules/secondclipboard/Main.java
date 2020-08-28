package ch.virt.traymodules.secondclipboard;

import ch.virt.trayutils.Dialogs;
import ch.virt.trayutils.Utils;
import ch.virt.trayutils.gui.helper.ComponentFactory;
import ch.virt.trayutils.gui.helper.GroupFactory;
import ch.virt.trayutils.modules.KeyBind;
import ch.virt.trayutils.modules.Module;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;

/**
 * @author VirtCode
 * @version 1.0
 */
public class Main extends Module {
    private static final String TAG = "[SecondClipboardModule] ";

    private KeyBind copy;
    private KeyBind paste;
    private KeyBind cut;

    private Transferable content;

    private Clipboard clipboard;

    private Robot robot;

    private int delay = 100;


    public Main() {
        super(5673, "Second Clipboard", "clipboard.png", "This is a secondary clipboard. It works just like a normal clipboard but it has other key binds and can run beside the normal clipboard. There though can be some noticeable delays when using it.");
    }

    @Override
    public void create() {
        copy = new KeyBind(new int[]{29, 42, 46}, arg -> copy());
        paste = new KeyBind(new int[]{29, 42, 47}, arg -> paste());
        cut = new KeyBind(new int[]{29, 42, 45}, arg -> cut());

        inputBus.addKeyBind(copy);
        inputBus.addKeyBind(paste);
        inputBus.addKeyBind(cut);

        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            robot = new Robot();
        } catch (AWTException e) {
            Dialogs.showErrorDialog("Failed to setup secondary clipboard.");
            this.enabled = false;
            e.printStackTrace();
        }
    }

    @Override
    public JPanel settingsMenu() {
        JPanel panel = ComponentFactory.createGroup();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(GroupFactory.createChangeKeyBindModule(copy.getCodes(), "Copy", arg -> {
            copy.setCodes(Utils.toPrimitive(arg));
            eventBus.saveSettings();
        }, false));
        panel.add(GroupFactory.createChangeKeyBindModule(cut.getCodes(), "Cut", arg -> {
            cut.setCodes(Utils.toPrimitive(arg));
            eventBus.saveSettings();
        }, false));
        panel.add(GroupFactory.createChangeKeyBindModule(paste.getCodes(), "Paste", arg -> {
            paste.setCodes(Utils.toPrimitive(arg));
            eventBus.saveSettings();
        }, false));

        return panel;
    }

    @Override
    public void fromSettings(JsonObject s) {
        if (s.has("copy")){
            inputBus.removeKeyBind(copy);
            copy = new KeyBind(intFromJson(s.getAsJsonArray("copy")), arg -> copy());
            inputBus.addKeyBind(copy);
        }

        if (s.has("paste")){
            inputBus.removeKeyBind(paste);
            paste = new KeyBind(intFromJson(s.getAsJsonArray("paste")), arg -> paste());
            inputBus.addKeyBind(paste);
        }

        if (s.has("cut")){
            inputBus.removeKeyBind(cut);
            cut = new KeyBind(intFromJson(s.getAsJsonArray("cut")), arg -> cut());
            inputBus.addKeyBind(cut);
        }

        if (s.has("delay")) delay = s.get("delay").getAsInt();
    }

    private int[] intFromJson(JsonArray array){
        int[] result = new int[array.size()];
        for (int i = 0; i < array.size(); i++) {
            result[i] = array.get(i).getAsInt();
        }
        return result;
    }

    @Override
    public JsonObject toSettings() {
        JsonObject object = new JsonObject();
        object.addProperty("delay", delay);
        object.add("copy", new Gson().toJsonTree(copy.getCodes()));
        object.add("paste", new Gson().toJsonTree(paste.getCodes()));
        object.add("cut", new Gson().toJsonTree(cut.getCodes()));

        return object;
    }

    private void copy(){
        if (!enabled) return;
        System.out.println(TAG + "copying");

        release(copy.getCodes());
        Transferable oldContent = clipboard.getContents(null);
        sleep();
        keyCombo(KeyEvent.VK_C);
        sleep();
        content = clipboard.getContents(null);
        sleep();
        clipboard.setContents(oldContent, null);
    }

    private void cut(){
        if (!enabled) return;
        System.out.println(TAG + "cutting");

        release(cut.getCodes());
        Transferable oldContent = clipboard.getContents(null);
        sleep();
        keyCombo(KeyEvent.VK_X);
        sleep();
        content = clipboard.getContents(null);
        sleep();
        clipboard.setContents(oldContent, null);
    }

    private void paste(){
        if (!enabled) return;
        System.out.println(TAG + "pasting");

        release(paste.getCodes());
        Transferable oldContent = clipboard.getContents(null);
        sleep();
        clipboard.setContents(content, null);
        sleep();
        keyCombo(KeyEvent.VK_V);
        sleep();
        clipboard.setContents(oldContent, null);
    }

    private void keyCombo(int code){
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(code);
        robot.keyRelease(code);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    private void sleep(){
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void release(int[] codes){
        for (int code : codes) {
            robot.keyRelease(Utils.nativeToNormalKeyCode(code));
        }
    }
}
