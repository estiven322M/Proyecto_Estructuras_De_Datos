
package com.uniquindio.bibliotecaonline.interfaz;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class LoguinAdminGui extends JFrame implements ActionListener{
    
    private BibliotecaGUI bibliotecaGUI; // Referencia a la interfaz principal
    
    // Datos correctos del administrador
    private final String ADMIN_USUARIO = "JhonMendez";
    private final String ADMIN_CONTRASENA = "1094948414";

    // Componentes gráficos
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JButton btnEntrar, btnAtras;
    
    public LoguinAdminGui(BibliotecaGUI bibliotecaGUI) {
        this.bibliotecaGUI = bibliotecaGUI;

        setTitle("Login Administrador");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel principal
        JPanel panelLogin = new JPanel();
        panelLogin.setLayout(new GridLayout(3, 2, 10, 10));
        panelLogin.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Etiquetas y campos de entrada
        panelLogin.add(new JLabel("Usuario:"));
        campoUsuario = new JTextField();
        panelLogin.add(campoUsuario);

        panelLogin.add(new JLabel("Contraseña:"));
        campoContrasena = new JPasswordField();
        panelLogin.add(campoContrasena);

        // Botones
        btnEntrar = new JButton("Entrar");
        btnAtras = new JButton("Atrás");

        btnEntrar.setActionCommand("ENTRAR");
        btnAtras.setActionCommand("ATRAS");

        btnEntrar.addActionListener(this);
        btnAtras.addActionListener(this);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnEntrar);
        panelBotones.add(btnAtras);

        // Agregar paneles al frame
        add(panelLogin, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if (comando.equals("ENTRAR")) {
            String usuarioIngresado = campoUsuario.getText();
            String contrasenaIngresada = new String(campoContrasena.getPassword());

            if (usuarioIngresado.equals(ADMIN_USUARIO) && contrasenaIngresada.equals(ADMIN_CONTRASENA)) {
                JOptionPane.showMessageDialog(this, "Acceso concedido. Bienvenido, " + ADMIN_USUARIO);
                new AdministradorGui(bibliotecaGUI);  // Abre la interfaz de Administrador
                dispose(); // Cierra la ventana de login
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (comando.equals("ATRAS")) {
            bibliotecaGUI.setVisible(true); // Regresa a la interfaz principal
            dispose();
        }
    }
    
}
