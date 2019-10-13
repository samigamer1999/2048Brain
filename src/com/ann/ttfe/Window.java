package com.ann.ttfe;

import javax.swing.*;

public class Window extends JFrame {

    public Window(){
        this.setTitle("Test");
        this.setLocation(0,0);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(new Panel());
        this.pack();
        this.setVisible(true);
    }
}