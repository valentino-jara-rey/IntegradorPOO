package gui;

import datos.SocioDAO;
import modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PanelSocios extends JPanel {
    private Biblioteca biblioteca;
    private JTextField txtDni, txtNombre, txtEspecifico;
    private JComboBox<String> cbTipoSocio;
    private JLabel lblEspecifico;
    private JTable tablaSocios;
    private DefaultTableModel modeloTabla;
    private SocioDAO socioDAO;

    public PanelSocios(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        this.socioDAO = new SocioDAO();
        setLayout(new BorderLayout(15, 15));
        setBackground(Estilos.MARFIL);

        JLabel lblTituloPanel = new JLabel("Gestión de Socios");
        lblTituloPanel.setFont(Estilos.FONT_TITULO);
        lblTituloPanel.setForeground(Estilos.AZUL_TINTA);
        add(lblTituloPanel, BorderLayout.NORTH);

        JPanel panelFormulario = crearPanelFormulario();
        JPanel panelTabla = crearPanelTabla();

        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 15, 0));
        panelCentral.setOpaque(false);
        panelCentral.add(panelFormulario);
        panelCentral.add(panelTabla);

        add(panelCentral, BorderLayout.CENTER);

        actualizarTabla();
    }

    private JPanel crearPanelFormulario() {
        JPanel tarjeta = Estilos.crearTarjeta();
        tarjeta.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblSubtitulo = new JLabel("Registrar Nuevo Socio");
        lblSubtitulo.setFont(Estilos.FONT_SUBTITULO);
        lblSubtitulo.setForeground(Estilos.AZUL_TINTA);

        txtDni = new JTextField();
        txtNombre = new JTextField();
        txtEspecifico = new JTextField();

        cbTipoSocio = new JComboBox<>(new String[]{"Estudiante", "Docente"});
        lblEspecifico = new JLabel("Carrera:");

        cbTipoSocio.addActionListener(e -> {
            if ("Estudiante".equals(cbTipoSocio.getSelectedItem())) {
                lblEspecifico.setText("Carrera:");
            } else {
                lblEspecifico.setText("Área:");
            }
        });

        JButton btnGuardar = new JButton("Registrar Socio");
        Estilos.estilizarBoton(btnGuardar, Estilos.AZUL_TINTA, Estilos.BLANCO_PAPEL);
        btnGuardar.addActionListener(e -> guardarSocio());

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        tarjeta.add(lblSubtitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0; tarjeta.add(new JLabel("Tipo de Socio:"), gbc);
        gbc.gridx = 1; tarjeta.add(cbTipoSocio, gbc);

        gbc.gridy = 2; gbc.gridx = 0; tarjeta.add(new JLabel("DNI:"), gbc);
        gbc.gridx = 1; tarjeta.add(txtDni, gbc);

        gbc.gridy = 3; gbc.gridx = 0; tarjeta.add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 1; tarjeta.add(txtNombre, gbc);

        gbc.gridy = 4; gbc.gridx = 0; tarjeta.add(lblEspecifico, gbc);
        gbc.gridx = 1; tarjeta.add(txtEspecifico, gbc);

        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        tarjeta.add(Box.createVerticalStrut(10), gbc);

        gbc.gridy = 6;
        tarjeta.add(btnGuardar, gbc);

        return tarjeta;
    }

    private JPanel crearPanelTabla() {
        JPanel tarjeta = Estilos.crearTarjeta();
        tarjeta.setLayout(new BorderLayout(10, 10));

        JLabel lblSubtitulo = new JLabel("Lista de Socios Registrados");
        lblSubtitulo.setFont(Estilos.FONT_SUBTITULO);
        lblSubtitulo.setForeground(Estilos.AZUL_TINTA);
        tarjeta.add(lblSubtitulo, BorderLayout.NORTH);

        String[] columnas = {"DNI", "Nombre", "Tipo", "Prestados", "Días Préstamo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaSocios = new JTable(modeloTabla);
        tablaSocios.setFont(Estilos.FONT_BASE);
        tablaSocios.setRowHeight(25);
        tablaSocios.getTableHeader().setBackground(Estilos.AZUL_TINTA);
        tablaSocios.getTableHeader().setForeground(Estilos.BLANCO_PAPEL);
        tablaSocios.getTableHeader().setFont(Estilos.FONT_BOTON);

        tarjeta.add(new JScrollPane(tablaSocios), BorderLayout.CENTER);

        JButton btnPremiarDocente = new JButton("Premiar Docente (+ Días)");
        Estilos.estilizarBoton(btnPremiarDocente, Estilos.AMBAR_ADVERTENCIA, Estilos.BLANCO_PAPEL);
        btnPremiarDocente.addActionListener(e -> premiarDocente());

        tarjeta.add(btnPremiarDocente, BorderLayout.SOUTH);

        return tarjeta;
    }

    private void guardarSocio() {
        try {
            int dni = Integer.parseInt(txtDni.getText().trim());
            String nombre = txtNombre.getText().trim();
            String especifico = txtEspecifico.getText().trim();
            String tipo = (String) cbTipoSocio.getSelectedItem();

            if (nombre.isEmpty() || especifico.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // VALIDACIÓN: Verificar si ya existe un socio con ese DNI en la biblioteca
            if (biblioteca.buscarSocio(dni) != null) {
                JOptionPane.showMessageDialog(this, "Ya existe un socio registrado con el DNI " + dni + ".", "Error de Duplicidad", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 1. Agregar a la memoria RAM de Biblioteca
            if ("Estudiante".equals(tipo)) {
                biblioteca.nuevoSocioEstudiante(dni, nombre, especifico);
            } else {
                biblioteca.nuevoSocioDocente(dni, nombre, especifico);
            }

            // 2. Persistir en SQLite
            Socio ultimoSocio = biblioteca.getSocios().get(biblioteca.getSocios().size() - 1);
            socioDAO.insertar(ultimoSocio);

            txtDni.setText("");
            txtNombre.setText("");
            txtEspecifico.setText("");

            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Socio registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El DNI debe ser un número entero válido.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void premiarDocente() {
        int filaSeleccionada = tablaSocios.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un docente de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Socio socio = biblioteca.getSocios().get(filaSeleccionada);
        if (socio instanceof Docente) {
            Docente docente = (Docente) socio;
            if (docente.esResponsable()) {
                String input = JOptionPane.showInputDialog(this, "Ingrese la cantidad de días extra a adicionar:", "Premiar Docente", JOptionPane.QUESTION_MESSAGE);
                if (input != null && !input.trim().isEmpty()) {
                    try {
                        int diasExtra = Integer.parseInt(input.trim());
                        docente.cambiarDiasDePrestamos(diasExtra);

                        // Actualizar en SQLite los días modificados del docente
                        socioDAO.actualizarDiasPrestamoDocente(docente);

                        actualizarTabla();
                        JOptionPane.showMessageDialog(this, "Se han agregado " + diasExtra + " días al docente " + docente.getNombre(), "Premio Otorgado", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Ingrese un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "El docente no califica como responsable (posee devoluciones vencidas).", "Acción no permitida", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "El socio seleccionado es un Estudiante. Esta función es solo para Docentes.", "Atención", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void actualizarTabla() {
        modeloTabla.setRowCount(0);
        ArrayList<Socio> socios = biblioteca.getSocios();

        for (Socio socio : socios) {
            Object[] fila = {
                    socio.getDniSocio(),
                    socio.getNombre(),
                    socio.soyDeLaClase(),
                    socio.cantLibrosPrestados(),
                    socio.getDiasPrestamo()
            };
            modeloTabla.addRow(fila);
        }
    }
}