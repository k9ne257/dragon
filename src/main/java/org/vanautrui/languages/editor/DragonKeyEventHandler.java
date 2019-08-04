package org.vanautrui.languages.editor;

import javafx.scene.input.KeyCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class DragonKeyEventHandler {

    //https://stackoverflow.com/questions/100123/application-wide-keyboard-shortcut-java-swing

    private DragonGUI_Editor master;

    private Map<KeyStroke,Action> actionMap = new HashMap<>();

    public DragonKeyEventHandler(DragonGUI_Editor master){
        this.master=master;

        KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C,KeyEvent.CTRL_DOWN_MASK);

        actionMap.put(copy, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("CTRL+C (COPY) Pressed");
            }
        });
    }

    public KeyEventDispatcher getKeyEventDispatcher(){
        KeyEventDispatcher keyEventDispatcher = new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                //prevent event from being processed
                //so we can do our own global event processing
                //that does not depend on the individual key listeners
                //on all the individual components

                System.out.println("-----------------");
                //System.out.println(e.toString());
                //System.out.println(e.paramString());



                //TODO: this refers to the physical key. if you are using another layout, it doesnt work. fix it.
                KeyEvent myKeyEvent = new KeyEvent(master.contextArea.get().make(),0,0,e.getModifiers(),e.getExtendedKeyCode());

                KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(myKeyEvent);

                if ( actionMap.containsKey(keyStroke) ) {
                    final Action a = actionMap.get(keyStroke);
                    final ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), null );
                    SwingUtilities.invokeLater( new Runnable() {
                        @Override
                        public void run() {
                            a.actionPerformed(ae);
                        }
                    } );
                }



                String key_event_type = e.paramString().split(",")[0];

                switch (key_event_type){
                    case "KEY_PRESSED":
                        break;
                    case "KEY_RELEASED":
                        if(e.getKeyCode()==(int)'p' && e.isControlDown()){
                            //Ctrl + p
                            //means switch the project view
                            System.out.println("TODO : switch project view");
                        }

                        if(e.getKeyCode()==KeyCode.ENTER.ordinal()){
                            System.out.println("ENTER");
                        }
                        break;
                    case "KEY_TYPED":
                        break;
                }
                System.out.println(key_event_type);
                //extended keycode seems to represent the key we want
                System.out.println("extended keycode: "+e.getExtendedKeyCode());

                //keycode seems to r
                System.out.println("keycode: "+e.getKeyCode());
                System.out.println("key dispatch: "+e.getKeyChar());

                System.out.println("keycode for ctrl : "+KeyEvent.VK_CONTROL);

                //debug
                master.editorWithImage.get().appendLineTest();

                return true;


            }
        };
        return keyEventDispatcher;
    }
}