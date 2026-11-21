package iu_digital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionTester extends JFrame {

    // Atributos encapsulados
    private JPanel mainPanel;
    private JTextField jdbcSqliteInventarioDbTextField;
    private JTextField textField1;
    private JPasswordField passwordField;
    private JButton probarConexiónButton;
    private JTextArea textArea1;

    // Constructor: inicializa el JFrame y registra los eventos
    public ConexionTester() {
        inicializarVentana();
        registrarEventos();
    }

    private void inicializarVentana() {
        setTitle("ConexionTester");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(400, 400);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void registrarEventos() {
        probarConexiónButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarConexion();
            }
        });
    }

    // Lógica de validación separada del evento (responsabilidad única)
    private void validarConexion() {
        String url = jdbcSqliteInventarioDbTextField.getText().trim();
        String usuario = textField1.getText().trim();
        String contrasena = new String(passwordField.getPassword());

        // try-with-resources: garantiza el cierre automático de Connection (conn.close())
        try (Connection conn = obtenerConexion(url, usuario, contrasena)) {
            if (conn != null && !conn.isClosed()) {
                mostrarMensaje("conexión exitosa", new Color(0, 128, 0));
            }
        } catch (ClassNotFoundException ex) {
            mostrarMensaje("fallo del driver: no se encontró el controlador JDBC.\n" + ex.getMessage(), Color.RED);
        } catch (SQLException ex) {
            mostrarMensaje("fallo de conexión\n" + ex.getMessage(), Color.RED);
        }
    }

    // Método auxiliar que carga el driver y establece la conexión
    private Connection obtenerConexion(String url, String usuario, String contrasena)
            throws ClassNotFoundException, SQLException {

        if (url.startsWith("jdbc:sqlite:")) {
            Class.forName("org.sqlite.JDBC");
        } else if (url.startsWith("jdbc:mysql:")) {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }

        if (usuario.isEmpty()) {
            return DriverManager.getConnection(url);
        }
        return DriverManager.getConnection(url, usuario, contrasena);
    }

    // Método auxiliar para actualizar la interfaz
    private void mostrarMensaje(String texto, Color color) {
        textArea1.setForeground(color);
        textArea1.setText(texto);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConexionTester app = new ConexionTester();
            app.setVisible(true);
        });
    }
}
