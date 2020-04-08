package com.wq.ui.chatroom;

import com.wq.client.TCPClient;
import com.wq.clink.Context;
import com.wq.ui.label.UserLabels;
import com.wq.ui.utils.Constant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class ChatRoom extends JFrame {

    private JPanel contentPane;
    public static final Color SIGN_BEFORE = new Color(0, 148, 230);
    public static final Color USER = new Color(235, 235, 236);
    private int x0, y0, x1, y1;
    private JLabel lblX;
    private JLabel lblNewLabel_2;
    private final TCPClient tcpClient;
    private JTextArea textArea;
    private final UiArriveMes uiArriveMes = new UiArriveMes() {
        @Override
        public void onArrive(String str) {
            textArea.append(str);
        }
    };
    public static void main(String[] args) {

        ChatRoom frame = new ChatRoom(null);
        frame.setVisible(true);
    }

    public ChatRoom(TCPClient tcpClient) {
        this.tcpClient = tcpClient;
        tcpClient.setUiArriveMes(uiArriveMes);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 747, 691);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        // ImageIcon icon=new ImageIcon("xxx/2.png"); //xxx代表图片存放路径，2.png图片名称及格式
        // this.setIconImage(icon.getImage());
        this.setTitle("致一聊天室");
        setUndecorated(true);
        JPanel panel = new JPanel();
        panel.setBounds(0, 61, 131, 631);
        contentPane.add(panel);
        panel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel panel_5 = new JPanel();
        panel_5.setBackground(new Color(250, 250, 250));
        scrollPane.setViewportView(panel_5);
        panel_5.setLayout(null);

        UserLabels user_1 = new UserLabels(Constant.class.getResource("head_small.png"),0);
        panel_5.add(user_1);

        UserLabels user_2 = new UserLabels(Constant.class.getResource("head_small.png"),1);
        panel_5.add(user_2);

        JLabel title = new JLabel("  魏阿魏小强");
        title.setFont(new Font("微软雅黑", Font.PLAIN, 17));
        title.setOpaque(true);

        title.setBackground(Color.WHITE);
        title.setBorder(new LineBorder(Color.gray));
        title.setBounds(130, 60, 631, 37);
        contentPane.add(title);

        JPanel panel_3 = new JPanel();
        panel_3.setBounds(130, 565, 619, 95);
        contentPane.add(panel_3);
        panel_3.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane_2 = new JScrollPane();
        scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane_2.setBorder(null);
        panel_3.add(scrollPane_2, BorderLayout.CENTER);

        JTextArea textArea_1 = new JTextArea();
        textArea_1.setLineWrap(true);
        scrollPane_2.setViewportView(textArea_1);
        JPanel panel_1 = new JPanel();

        panel_1.setBounds(130, 95, 619, 441);
        contentPane.add(panel_1);
        panel_1.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBorder(null);
        panel_1.add(scrollPane_1, BorderLayout.CENTER);

         textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
//        textArea.setEnabled(false);
        textArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        textArea.setForeground(Color.BLACK);

        scrollPane_1.setViewportView(textArea);
        scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane_1.setBorder(null);
        KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
        textArea_1.getInputMap().put(enter, "none");
        textArea_1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    String text = textArea_1.getText();
                    if (text==null || text.length()==0)
                        return;
                    textArea.append("你说:  "+text+"\n");
                    textArea_1.setText("");
                    tcpClient.send(text);
                }
            }
        });
        JPanel panel_2 = new JPanel();
        panel_2.setBorder(new LineBorder(Color.gray));
        panel_2.setBackground(Color.WHITE);
        panel_2.setBounds(130, 536, 620, 37);
        contentPane.add(panel_2);
        panel_2.setLayout(null);

        JLabel lblNewLabel_1 = new JLabel("历史记录");
        lblNewLabel_1.setOpaque(true);
        lblNewLabel_1.setBackground(Color.WHITE);
        lblNewLabel_1.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        lblNewLabel_1.setForeground(Color.gray);
        lblNewLabel_1.setBounds(538, 7, 72, 17);
        panel_2.add(lblNewLabel_1);

        JPanel panel_4 = new JPanel();

        panel_4.setBackground(Color.WHITE);
        panel_4.setBounds(130, 659, 631, 31);
        contentPane.add(panel_4);
        panel_4.setLayout(null);

        JLabel send = new JLabel("发送", JLabel.CENTER);
        send.setOpaque(true);
        send.setBackground(SIGN_BEFORE);
        send.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        send.setForeground(Color.WHITE);
        send.setBounds(540, 0, 64, 27);
        send.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                String text = textArea_1.getText();
                if (text==null || text.length()==0)
                    return;
                textArea.append("你说:  "+text+"\n");
                textArea_1.setText("");
                tcpClient.send(text);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
//                ChatRoom.this.lblNewLabel_2.setFont(new Font("微软雅黑", Font.PLAIN, 23));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
//                ChatRoom.this.lblNewLabel_2.setFont(new Font("微软雅黑", Font.PLAIN, 18));
            }

        });
        panel_4.add(send);
        ImageIcon img = new ImageIcon(Constant.class.getResource("bgc.jpg"));

        lblNewLabel_2 = new JLabel("—");
        lblNewLabel_2.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                ChatRoom.this.setExtendedState(JFrame.ICONIFIED);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
                ChatRoom.this.lblNewLabel_2.setFont(new Font("微软雅黑", Font.PLAIN, 23));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
                ChatRoom.this.lblNewLabel_2.setFont(new Font("微软雅黑", Font.PLAIN, 18));
            }

        });
        lblNewLabel_2.setForeground(Color.WHITE);
        lblNewLabel_2.setFont(new Font("微软雅黑", Font.BOLD, 18));
        lblNewLabel_2.setBounds(678, 15, 27, 30);
        contentPane.add(lblNewLabel_2);

        lblX = new JLabel("x");
        lblX.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                ChatRoom.this.dispose();
                tcpClient.exit();
                try {
                    Context.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
                ChatRoom.this.lblX.setFont(new Font("微软雅黑", Font.PLAIN, 35));

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
                ChatRoom.this.lblX.setFont(new Font("微软雅黑", Font.PLAIN, 31));
            }

        });
        lblX.setForeground(Color.WHITE);
        lblX.setFont(new Font("微软雅黑", Font.PLAIN, 31));
        lblX.setBounds(714, 15, 27, 30);
        contentPane.add(lblX);
        ImageIcon head = new ImageIcon(Constant.class.getResource("head.png"));
        JLabel lblNewLabel_3 = new JLabel(head);
        lblNewLabel_3.setOpaque(true);
        lblNewLabel_3.setBounds(36, 5, 48, 48);
        contentPane.add(lblNewLabel_3);
        JLabel lblNewLabel = new JLabel(img);
        lblNewLabel.setOpaque(true);
        lblNewLabel.setBounds(0, 0, 749, 63);
        contentPane.add(lblNewLabel);
        add_move_event(lblNewLabel);
    }

    private void add_move_event(JLabel jLabel) {
        jLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
                x0 = e.getXOnScreen();
                y0 = e.getYOnScreen();
            }


        });
        jLabel.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                // TODO Auto-generated method stub
                x1 = e.getXOnScreen();
                y1 = e.getYOnScreen();
                if (x1 != x0 || y1 != y0) {
                    Point p = ChatRoom.this.getLocation();
                    double px = p.getX();
                    double py = p.getY();
                    ChatRoom.this.setLocation((int) (px + (x1 - x0)), (int) (py + (y1 - y0)));
                    x0 = x1;
                    y0 = y1;
                }
            }
        });
    }
}
