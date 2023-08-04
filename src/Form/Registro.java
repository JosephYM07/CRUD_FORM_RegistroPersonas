package Form;
import Class.PersonaRegistro;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Registro {
    private JTextField IngresoCodigo;
    private JTextField ingresoCedulaTextField;
    private JTextField IngresoNombre;
    private JComboBox<String> SelccionarSigno;
    private JTextField IngresoFechaNacimiento;
    private JButton botonBuscarPorCódigo;
    private JButton botonBuscarPorNombre;
    private JButton botonBuscarPorSigno;
    private JButton borrarRegistroPresente;
    private JButton actualizarRegistroPresente;
    private JButton registrarElPresenteRegistro;
    private JButton limpiarFormulario;
    public JPanel PanelPrincipal;

    //Conexiones a la base de datos MySQL
    private Connection connection;
    private final String url = "jdbc:mysql://localhost:3306/registro_personas";
    private final String usuario = "root";
    private final String contrasena = "SoaD1725.";

    public Registro() {
        try {
            connection = DriverManager.getConnection(url, usuario, contrasena);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        registrarElPresenteRegistro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarPersona();
            }
        });

        actualizarRegistroPresente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarPersona();
            }
        });

        borrarRegistroPresente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrarPersona();
            }
        });

        botonBuscarPorCódigo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPorCodigo();
            }
        });

        botonBuscarPorNombre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPorNombre();
            }
        });

        botonBuscarPorSigno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPorSigno();
            }
        });

        limpiarFormulario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });
    }

    private void registrarPersona() {
        String codigo = IngresoCodigo.getText();
        String cedula = ingresoCedulaTextField.getText();
        String nombre = IngresoNombre.getText();
        String signo = (String) SelccionarSigno.getSelectedItem();
        String fechaNacimiento = IngresoFechaNacimiento.getText();

        PersonaRegistro persona = new PersonaRegistro(codigo, cedula, nombre, signo, fechaNacimiento);

        try {
            String sql = "INSERT INTO personas (codigo, cedula, nombre, signo, fecha_nacimiento) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, persona.getCodigo());
            statement.setString(2, persona.getCedula());
            statement.setString(3, persona.getNombre());
            statement.setString(4, persona.getSigno());
            statement.setString(5, persona.getFechaNacimiento());

            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Registro exitoso.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al registrar la persona.");
        }
    }

    private void actualizarPersona() {
        String codigo = IngresoCodigo.getText();
        String cedula = ingresoCedulaTextField.getText();
        String nombre = IngresoNombre.getText();
        String signo = (String) SelccionarSigno.getSelectedItem();
        String fechaNacimiento = IngresoFechaNacimiento.getText();

        PersonaRegistro persona = new PersonaRegistro(codigo, cedula, nombre, signo, fechaNacimiento);

        try {
            String sql = "UPDATE personas SET cedula=?, nombre=?, signo=?, fecha_nacimiento=? WHERE codigo=?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, persona.getCedula());
            statement.setString(2, persona.getNombre());
            statement.setString(3, persona.getSigno());
            statement.setString(4, persona.getFechaNacimiento());
            statement.setString(5, persona.getCodigo());

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Persona actualizada correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró una persona con ese código.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar la persona.");
        }
    }

    private void borrarPersona() {
        String codigo = IngresoCodigo.getText();

        try {
            String sql = "DELETE FROM personas WHERE codigo=?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, codigo);

            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Persona borrada correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró una persona con ese código.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al borrar la persona.");
        }
    }

    private void buscarPorCodigo() {
        String codigo = IngresoCodigo.getText();

        try {
            String sql = "SELECT * FROM personas WHERE codigo=?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, codigo);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                mostrarResultado(result);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró una persona con ese código.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al buscar la persona.");
        }
    }

    private void buscarPorNombre() {
        String nombre = IngresoNombre.getText();

        try {
            String sql = "SELECT * FROM personas WHERE nombre=?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, nombre);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                mostrarResultado(result);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró una persona con ese nombre.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al buscar la persona.");
        }
    }

    private void buscarPorSigno() {
        String signo = (String) SelccionarSigno.getSelectedItem();

        try {
            String sql = "SELECT * FROM personas WHERE signo=?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, signo);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                mostrarResultado(result);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron personas con ese signo.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al buscar personas.");
        }
    }

    private void limpiarFormulario() {
        IngresoCodigo.setText("");
        ingresoCedulaTextField.setText("");
        IngresoNombre.setText("");
        SelccionarSigno.setSelectedIndex(0);
        IngresoFechaNacimiento.setText("");
    }

    private void mostrarResultado(ResultSet result) throws SQLException {
        String codigo = result.getString("codigo");
        String cedula = result.getString("cedula");
        String nombre = result.getString("nombre");
        String signo = result.getString("signo");
        String fechaNacimiento = result.getString("fecha_nacimiento");

        PersonaRegistro persona = new PersonaRegistro(codigo, cedula, nombre, signo, fechaNacimiento);

        JOptionPane.showMessageDialog(null, persona.toString());
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Registro");
        frame.setContentPane(new Registro().PanelPrincipal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
