package dream_team.easy_travel.mainApp;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Objects;
import javax.swing.*;

import dream_team.easy_travel.DatabaseConnection.ManageDatabase;
import dream_team.easy_travel.Easy_Travel;
import dream_team.easy_travel.swing.Button;
import dream_team.easy_travel.swing.MyPasswordField;
import dream_team.easy_travel.swing.MyTextField;
import net.miginfocom.swing.MigLayout;


public class PanelLoginAndRegister extends javax.swing.JLayeredPane {
    private boolean isPasswordVisible = false;
    public PanelLoginAndRegister() {
        initComponents();
        initRegister();
        initLogin();
        login.setVisible(false);
        register.setVisible(true);
    }

    private void initRegister() {
        register.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel("Create Account");
        label.setFont(new Font("sansserif", Font.BOLD, 30));
        label.setForeground(new Color(0, 0, 0));
        register.add(label);
        MyTextField txtUser = new MyTextField();
        txtUser.setPrefixIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/user.png"), "Image not found: /com/raven/icon/user.png")));
        txtUser.setHint("Name");
        register.add(txtUser, "w 60%");
        MyTextField txtEmail = new MyTextField();
        txtEmail.setPrefixIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/mail.png"), "Image not found: /com/raven/icon/mail.png")));
        txtEmail.setHint("Email");
        register.add(txtEmail, "w 60%");
        MyPasswordField txtPass = new MyPasswordField();
        txtPass.setPrefixIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/pass.png"), "Image not found: /com/raven/icon/pass.png")));
        txtPass.setHint("Password");
        register.add(txtPass, "w 60%");

        Button cmd = new Button();
        cmd.setBackground(new Color(0, 0, 0));
        cmd.setForeground(new Color(250, 250, 250));
        cmd.setText("SIGN UP");
        register.add(cmd, "w 40%, h 40");
        cmd.addActionListener(e -> {
            String name = txtUser.getText();
            String username = txtEmail.getText();
            String password = new String(txtPass.getPassword());
            if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields");
                return;
            }
            ManageDatabase db = new ManageDatabase();
            try {
                if (db.getUserByUsername(username) != null) {
                    JOptionPane.showMessageDialog(this, "Username already exists");
                    return;
                }
                db.addNewUser(name, username, password);
                JOptionPane.showMessageDialog(this, "User created successfully, Please login");
                login.setVisible(true);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        });

    }

    private void initLogin() {
        login.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel("Sign In");
        label.setFont(new Font("SansSerif", Font.BOLD, 30));
        label.setForeground(new Color(9, 9, 9));
        login.add(label);

        MyTextField txtEmail = new MyTextField();
        txtEmail.setPrefixIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/mail.png"), "Image not found: /mail.png")));
        txtEmail.setHint("Email");
        login.add(txtEmail, "w 60%");

        JPanel passwordPanel = new JPanel(new MigLayout("insets 0", "[grow,fill][]", "[]"));
        passwordPanel.setBackground(Color.WHITE);

        MyPasswordField txtPass = new MyPasswordField();
        txtPass.setPrefixIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/pass.png"), "Image not found: /pass.png")));
        txtPass.setHint("Password");
        passwordPanel.add(txtPass, "w 90%");

        ImageIcon eyeOpenIcon = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/eyeOpen.png"), "Image not found: /eyeOpen.png")).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH));
        ImageIcon eyeClosedIcon = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/eyeClosed.png"), "Image not found: /eyeClosed.png")).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH));
        JButton btn = new JButton(eyeClosedIcon);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        passwordPanel.add(btn, "w 10%");

        login.add(passwordPanel, "w 60%");

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isPasswordVisible = !isPasswordVisible;
                if (isPasswordVisible) {
                    txtPass.setEchoChar((char) 0);
                    btn.setIcon(eyeOpenIcon);
                } else {
                    txtPass.setEchoChar('•');
                    btn.setIcon(eyeClosedIcon);
                }
            }
        });

        JButton cmdForget = new JButton("Forgot your password ?");
        cmdForget.setForeground(new Color(100, 100, 100));
        cmdForget.setFont(new Font("SansSerif", Font.BOLD, 12));
        cmdForget.setContentAreaFilled(false);
        cmdForget.setCursor(new Cursor(Cursor.HAND_CURSOR));
        login.add(cmdForget);

        Button cmd = new Button();
        cmd.setBackground(new Color(0, 0, 0));
        cmd.setForeground(new Color(250, 250, 250));
        cmd.setText("SIGN IN");
        login.add(cmd, "w 40%, h 40");

        cmd.addActionListener(e -> {
            String username = txtEmail.getText();
            String password = new String(txtPass.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields");
            } else {
                try {
                    ManageDatabase db = new ManageDatabase();
                    User user = db.getUserByUsername(username);
                    if (user != null && user.getPassword().equals(password)) {
                        JOptionPane.showMessageDialog(null, "Login Successful");
                        Easy_Travel app = new Easy_Travel();
                        app.setLoggedInUser(user);
                        app.showPanel("Home");
                        app.updateFrameTitle("Home");
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Username or Password");
                    }
                } catch (Exception ex) {
                    System.err.println(ex);
                }
            }
        });
}

public void showRegister(boolean show) {
        if (show) {
            register.setVisible(true);
            login.setVisible(false);
        } else {
            register.setVisible(false);
            login.setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        login = new javax.swing.JPanel();
        register = new javax.swing.JPanel();

        setLayout(new java.awt.CardLayout());

        login.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout loginLayout = new javax.swing.GroupLayout(login);
        login.setLayout(loginLayout);
        loginLayout.setHorizontalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );
        loginLayout.setVerticalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(login, "card3");

        register.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout registerLayout = new javax.swing.GroupLayout(register);
        register.setLayout(registerLayout);
        registerLayout.setHorizontalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );
        registerLayout.setVerticalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(register, "card2");
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel login;
    private javax.swing.JPanel register;
    // End of variables declaration//GEN-END:variables
}
