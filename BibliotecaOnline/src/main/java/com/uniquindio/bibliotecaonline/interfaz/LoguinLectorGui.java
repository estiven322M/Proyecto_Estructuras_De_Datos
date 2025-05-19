package com.uniquindio.bibliotecaonline.interfaz;

import com.uniquindio.bibliotecaonline.logica.GestorLectores;
import com.uniquindio.bibliotecaonline.modelo.Lector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoguinLectorGui extends JFrame implements ActionListener {

    private BibliotecaGUI bibliotecaGUI;
    private GestorLectores gestorLectores;

    // Datos correctos del administrador
    private final String ADMIN_USUARIO = "JhonMendez";
    private final String ADMIN_CONTRASENA = "1094948414";

    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JButton btnEntrar, btnAtras;

    public LoguinLectorGui(BibliotecaGUI bibliotecaGUI) {
        this.bibliotecaGUI = bibliotecaGUI;
        this.gestorLectores = new GestorLectores();

        setTitle("Login Lector");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelLogin = new JPanel(new GridLayout(3, 2, 10, 10));
        panelLogin.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelLogin.add(new JLabel("Usuario (ID):"));
        campoUsuario = new JTextField();
        panelLogin.add(campoUsuario);

        panelLogin.add(new JLabel("Contraseña:"));
        campoContrasena = new JPasswordField();
        panelLogin.add(campoContrasena);

        btnEntrar = new JButton("Entrar");
        btnAtras = new JButton("Atrás");

        btnEntrar.setActionCommand("ENTRAR");
        btnAtras.setActionCommand("ATRAS");

        btnEntrar.addActionListener(this);
        btnAtras.addActionListener(this);

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnEntrar);
        panelBotones.add(btnAtras);

        add(panelLogin, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if (comando.equals("ENTRAR")) {
            String usuario = campoUsuario.getText().trim();
            String contrasena = new String(campoContrasena.getPassword()).trim();

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Usuario y contraseña son requeridos", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (gestorLectores.validarLoginDesdeArchivo(usuario, contrasena)) {
                
                // Buscar el lector en la lista cargada en memoria
                Lector lector = gestorLectores.buscarLector(usuario, false); // false = buscar por ID
                if(lector != null){
                    JOptionPane.showMessageDialog(this, "Bienvenido, " + lector.getNombre());
                    new LectorGui(bibliotecaGUI,lector);
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(this, "Error interno: lector no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (comando.equals("ATRAS")) {
            bibliotecaGUI.setVisible(true);
            dispose();
        }
    }
}
