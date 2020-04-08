package com.wq.ui.chatroom;

import com.wq.client.TCPClient;
import com.wq.client.bean.ServerInfo;
import com.wq.clink.Context;
import com.wq.clink.core.impl.MyProvider;
import com.wq.ui.utils.Constant;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Login extends JFrame {
    private static final long serialVersionUID = 1L;
    private int x0, y0, x1, y1;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        Login login = new Login();
        login.setVisible(true);

    }

    /**
     * Create the frame.
     */
    public Login() {
        setUndecorated(true);
        this.setTitle("致一聊天室");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(700, 300, 430, 480);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.requestFocus();
        addKeyListener(new KeyAdapter() {
            public void keyPressed(final KeyEvent e) {
                int ch = e.getKeyCode();
                System.out.println(ch + "dd");
            }
        });
        JLabel labe = new JLabel("欢迎来到致一聊天室");
        labe.setBounds(100, 30, 230, 30);
        labe.setFont(new Font("微软雅黑", Font.PLAIN, 23));
        labe.setForeground(Color.WHITE);
        contentPane.add(labe);
        JLabel sign_in = new JLabel("SIGN IN", JLabel.CENTER);
        sign_config(sign_in);
        sign_in.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Login.this.dispose();
                ChatRoom chat = new ChatRoom(setUp());
                chat.setVisible(true);


            }
        });


        sign_in.setBounds(68, 328, 291, 30);
        contentPane.add(sign_in);

        JLabel exit = new JLabel("EXIT", JLabel.CENTER);
        exit.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Login.this.dispose();
            }
        });
        sign_config(exit);
        exit.setBounds(68, 380, 291, 30);
        contentPane.add(exit);

        JLabel username = new JLabel("USERNAME");

        username.setForeground(Color.WHITE);
        username.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        username.setBounds(68, 150, 103, 30);
        contentPane.add(username);

        JLabel passwd = new JLabel("PASSWORD");
        passwd.setForeground(Color.WHITE);
        passwd.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        passwd.setBounds(68, 219, 115, 30);
        contentPane.add(passwd);

        JPasswordField passwd_input = new JPasswordField();

        input_config(passwd_input);
        passwd_input.setBounds(68, 255, 291, 30);
        contentPane.add(passwd_input);

        JTextField username_input = new JTextField();
        input_config(username_input);
        username_input.setBounds(68, 179, 291, 30);
        contentPane.add(username_input);
        ImageIcon imageIcon = new ImageIcon(Constant.class.getResource("login_bgc.jpg"));
        JLabel jLabel = new JLabel(imageIcon);

        jLabel.setBounds(0, 0, 430, 480);
        contentPane.add(jLabel);
        this.add_move_event();
        this.setResizable(false);
    }

    private TCPClient setUp(){
		try {
			Context.setup(new MyProvider());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
        ServerInfo info = new ServerInfo(8888, "120.79.87.220", UUID.randomUUID().toString());
		System.out.println("Server:" + info);
		TCPClient tcpClient = null;
		if (info != null) {
			try {
				tcpClient = TCPClient.startWith(info);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return tcpClient;
	}
    private void input_config(JTextField jTextField) {
        jTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    Login.this.dispose();
                    ChatRoom chat = new ChatRoom(setUp());
                    chat.setVisible(true);
                }
            }
        });
        jTextField.setOpaque(false);
        jTextField.setBorder(new LineBorder(Color.gray, 1));
        jTextField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        jTextField.setForeground(Color.WHITE);
    }

    private void add_move_event() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x0 = e.getXOnScreen();
                y0 = e.getYOnScreen();
            }
        });
        this.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                x1 = e.getXOnScreen();
                y1 = e.getYOnScreen();

                if (x1 != x0 || y1 != y0) {
                    Point p = Login.this.getLocation();
                    double px = p.getX();
                    double py = p.getY();
                    Login.this.setLocation((int) (px + (x1 - x0)),
                            (int) (py + (y1 - y0)));
                    x0 = x1;
                    y0 = y1;
                }
            }
        });
    }

    private void sign_config(JLabel jLabel) {

        jLabel.setCursor(Constant.HAND);
        jLabel.setForeground(Color.WHITE);
        jLabel.setEnabled(true);
        jLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        jLabel.setOpaque(true);
        jLabel.setBackground(Constant.SIGN_BEFORE);
        jLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                jLabel.setBackground(Constant.SIGN_AFTER);

            }

            @Override
            public void mouseExited(MouseEvent e) {
                jLabel.setBackground(Constant.SIGN_BEFORE);
            }
        });

    }
}
