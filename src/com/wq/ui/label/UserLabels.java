package com.wq.ui.label;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class UserLabels extends JLabel {

    private final int LEN = 50;

    public UserLabels(URL path, int sum) {
        super();
        setLayout(null);
        setBounds(0, sum * LEN, 129, 50);
//        ImageIcon head_small = new ImageIcon("C:\\Users\\28080\\Desktop\\head_small.png");
        ImageIcon head_small = new ImageIcon(path);
        JLabel label = new JLabel(head_small);
        label.setOpaque(true);
        label.setBounds(10, 10, 30, 30);
        add(label);
        JLabel label_1 = new JLabel(" 魏小强二");
        label_1.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        label_1.setBounds(50, 10, 69, 28);
        add(label_1);
    }

}
