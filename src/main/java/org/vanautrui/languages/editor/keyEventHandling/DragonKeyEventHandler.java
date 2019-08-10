package org.vanautrui.languages.editor.keyEventHandling;

import org.vanautrui.languages.editor.gui.CustomHelpWindow;
import org.vanautrui.languages.editor.gui.DragonGUI_Editor;
import org.vanautrui.languages.editor.editorcore.DragonEditorWithImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;
import java.util.Scanner;

public class DragonKeyEventHandler {

    //https://stackoverflow.com/questions/30291437/java-keyevent-key-code-vs-extended-key-code

    //https://stackoverflow.com/questions/100123/application-wide-keyboard-shortcut-java-swing

    private DragonGUI_Editor master;

    public boolean ctrl_down=false;
    public boolean shift_down=false;
    public boolean alt_down=false;

    public DragonKeyEventHandler(DragonGUI_Editor master){
        this.master=master;
    }

    private void tryShareFile(){
        System.out.println("sharing the current buffer");
        //TODO: make server socket and such, until
        //a client has accepted, then close it again
        try(ServerSocket serverSocket = new ServerSocket(8888)){
            System.out.println("server running");
            try(Socket client = serverSocket.accept()){
                PrintWriter out = new PrintWriter(client.getOutputStream(),true);
                out.println(master.editorWithImage.get().getCurrentLine());
            }
        }catch (Exception ee){
            ee.printStackTrace();
        }
    }

    private void tryReceiveFile(){
        System.out.println("trying to recieve a file into current buffer");
        //TODO: make client socket and such
        //the client socket can be created right here
        try {
            Socket clientSocket = new Socket("127.0.0.1", 8888);
            Scanner in = new Scanner(clientSocket.getInputStream());
            String line=in.nextLine();
            System.out.println(line);
            master.editorWithImage.get().loadString(line);
        }catch (Exception ee){
            ee.printStackTrace();
        }
    }

    public KeyEventDispatcher getKeyEventDispatcherForKeyboardShortcuts(){
        KeyEventDispatcher dispatcher = new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                String key_event_type = e.paramString().split(",")[0];

                System.out.println("---------------");
                System.out.println(e.paramString());
                System.out.println();

                char realChar = (char)e.getExtendedKeyCode();
                System.out.println(realChar+"");

                switch (key_event_type){
                    case "KEY_PRESSED":
                        if(ctrl_down && realChar == 'O'){
                            System.out.println("CTRL + O ");
                            master.tryOpenFile();
                            return true;
                        }
                        if(ctrl_down && realChar == 'S'){
                            System.out.println("CTRL + S ");
                            master.trySaveFile();
                            return true;
                        }
                        if(ctrl_down && realChar == 'C'){
                            System.out.println("CTRL + C ");
                            System.exit(0);
                            return true;
                        }
                        if(ctrl_down && realChar == 'N'){
                            System.out.println("CTRL + N ");
                            return true;
                        }
                        if(ctrl_down && realChar == 'R'){
                            System.out.println("CTRL + R ");
                            return true;
                        }
                        if(e.getExtendedKeyCode()==112){
                            //pressed F1
                            System.out.println("   F1   ");
                            //show JFrame with help information,
                            //and return focus back to main window
                            if(master.helpWindow.isPresent()){
                                master.helpWindow.get().destroy();
                                master.helpWindow=Optional.empty();
                            }else {
                                master.helpWindow = Optional.of(new CustomHelpWindow(master));
                            }
                            return true;
                        }
                        if(e.getExtendedKeyCode()==113){
                            //pressed F2
                            tryShareFile();
                            return true;
                        }
                        if(e.getExtendedKeyCode()==114){
                            //pressed F3
                            tryReceiveFile();
                            return true;
                        }
                        break;
                    case "KEY_RELEASED":
                        break;
                    case "KEY_TYPED":
                        break;
                }

                ctrl_down=e.isControlDown();
                shift_down=e.isShiftDown();
                alt_down=e.isAltDown();
                return false;
            }
        };
        return dispatcher;
    }

    public KeyEventDispatcher getKeyEventDispatcher(){
        KeyEventDispatcher keyEventDispatcher = new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                //https://imagingexperts.typepad.com/imaging_experts/2005/10/adding_configur.html

                //prevent event from being processed
                //so we can do our own global event processing
                //that does not depend on the individual key listeners
                //on all the individual components

                //System.out.println("-----------------");
                //System.out.println(e.toString());
                //System.out.println(e.paramString());
                //System.out.println();


                KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(e);

                KeyStroke keyStrokeSave = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);

                if(keyStroke.equals(keyStrokeSave)){
                    System.out.println("SAVE FILE");
                }


                String key_event_type = e.paramString().split(",")[0];

                switch (key_event_type){
                    case "KEY_PRESSED":
                        switch (e.getKeyCode()){
                            case 8:
                                master.editorWithImage.get().pressBackSpace();
                                System.out.println("BACKSPACE");
                                break;
                            case 38:
                                //Arrow UP
                                master.editorWithImage.get().arrowUpChangeLine();
                                break;
                            case 40:
                                //Arrow Down
                                master.editorWithImage.get().arrowDownChangeLine();
                                break;
                            case 37:
                                //arrow left
                                master.editorWithImage.get().arrowLeftMoveCursor();
                                break;
                            case 39:
                                //arrow right
                                master.editorWithImage.get().arrowRightMoveCursor();
                                break;
                        }
                        break;
                    case "KEY_RELEASED":
                        if(e.getKeyCode()==(int)'p' && e.isControlDown()){
                            //Ctrl + p
                            //means switch the project view
                            System.out.println("TODO : switch project view");
                        }

                        switch(e.getKeyCode()){
                            case 10:
                                master.editorWithImage.get().pressEnter();
                                System.out.println("ENTER");
                                break;
                            case 9:
                                master.editorWithImage.get().pressTab();
                                System.out.println("TAB");
                                break;
                                /*
                            case 8:
                                master.editorWithImage.get().pressBackSpace();
                                System.out.println("BACKSPACE");
                                break;
                                 */
                                /*
                            case 38:
                                //Arrow UP
                                master.editorWithImage.get().arrowUpChangeLine();
                                break;
                            case 40:
                                //Arrow Down
                                master.editorWithImage.get().arrowDownChangeLine();
                                break;
                            case 37:
                                //arrow left
                                master.editorWithImage.get().arrowLeftMoveCursor();
                                break;
                            case 39:
                                //arrow right
                                master.editorWithImage.get().arrowRightMoveCursor();
                                break;
                            */
                            case 36:
                                master.editorWithImage.get().pressHome();
                                break;
                            case 35:
                                master.editorWithImage.get().pressEnd();
                                break;
                        }

                        break;
                    case "KEY_TYPED":
                        boolean is_allowed_char = e.getKeyChar() >= ' ' && e.getKeyChar() <= '~';
                        try {
                            if(is_allowed_char) {
                                master.editorWithImage.get().writeCharcter(e.getKeyChar());
                            }
                        }catch (Exception ee){
                            ee.printStackTrace();
                        }
                        break;
                }
                System.out.println(key_event_type);
                //extended keycode seems to represent the key we want
                System.out.println("extended keycode: "+e.getExtendedKeyCode());

                //keycode seems to r
                System.out.println("keycode: "+e.getKeyCode());
                System.out.println("key dispatch: "+e.getKeyChar());


                DragonEditorWithImage editor = master.editorWithImage.get();

                master.statusBar.get().updateLineCount(editor.getLineCount());
                master.statusBar.get().setCursorPos(editor.getCursorLineDisplay(),editor.getCursorColDisplay());

                return true;


            }
        };
        return keyEventDispatcher;
    }
}
