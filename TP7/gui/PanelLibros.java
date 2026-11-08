package gui;

import datos.LibroDAO;
import modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PanelLibros extends JPanel {
    private Biblioteca biblioteca;
    private JTextField txtTitulo, txtEdicion, txtEditorial, txtAnio;
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;
    private LibroDAO libroDAO;

    public PanelLibros(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        this.libroDAO = new LibroDAO();
        setLayout(new BorderLayout(15, 15));
        setBackground(Estilos.MARFIL);

        // Título del Panel
        JLabel lblTituloPanel = new JLabel("Gestión de Libros");
        lblTituloPanel.setFont(Estilos.FONT_TITULO);
        lblTituloPanel.setForeground(Estilos.AZUL_TINTA);
        add(lblTituloPanel, BorderLayout.NORTH);

        // Formulario + Tabla
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

        JLabel lblSubtitulo = new JLabel("Registrar Nuevo Libro");
        lblSubtitulo.setFont(Estilos.FONT_SUBTITULO);
        lblSubtitulo.setForeground(Estilos.AZUL_TINTA);

        txtTitulo = new JTextField();
        txtEdicion = new JTextField();
        txtEditorial = new JTextField();
        txtAnio = new JTextField();

        JButton btnGuardar = new JButton("Agregar Libro");
        Estilos.estilizarBoton(btnGuardar, Estilos.AZUL_TINTA, Estilos.BLANCO_PAPEL);
        btnGuardar.addActionListener(e -> guardarLibro());

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        tarjeta.add(lblSubtitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0; tarjeta.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; tarjeta.add(txtTitulo, gbc);

        gbc.gridy = 2; gbc.gridx = 0; tarjeta.add(new JLabel("Edición:"), gbc);
        gbc.gridx = 1; tarjeta.add(txtEdicion, gbc);

        gbc.gridy = 3; gbc.gridx = 0; tarjeta.add(new JLabel("Editorial:"), gbc);
        gbc.gridx = 1; tarjeta.add(txtEditorial, gbc);

        gbc.gridy = 4; gbc.gridx = 0; tarjeta.add(new JLabel("Año:"), gbc);
        gbc.gridx = 1; tarjeta.add(txtAnio, gbc);

        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        tarjeta.add(Box.createVerticalStrut(10), gbc);

        gbc.gridy = 6;
        tarjeta.add(btnGuardar, gbc);

        return tarjeta;
    }

    private JPanel crearPanelTabla() {
        JPanel tarjeta = Estilos.crearTarjeta();
        tarjeta.setLayout(new BorderLayout(10, 10));

        JLabel lblSubtitulo = new JLabel("Catálogo de Libros");
        lblSubtitulo.setFont(Estilos.FONT_SUBTITULO);
        lblSubtitulo.setForeground(Estilos.AZUL_TINTA);
        tarjeta.add(lblSubtitulo, BorderLayout.NORTH);

        String[] columnas = {"Título", "Edición", "Editorial", "Año", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaLibros = new JTable(modeloTabla);
        tablaLibros.setFont(Estilos.FONT_BASE);
        tablaLibros.setRowHeight(25);
        tablaLibros.getTableHeader().setBackground(Estilos.AZUL_TINTA);
        tablaLibros.getTableHeader().setForeground(Estilos.BLANCO_PAPEL);
        tablaLibros.getTableHeader().setFont(Estilos.FONT_BOTON);

        tarjeta.add(new JScrollPane(tablaLibros), BorderLayout.CENTER);

        return tarjeta;
    }

    private void guardarLibro() {
        try {
            String titulo = txtTitulo.getText().trim();
            int edicion = Integer.parseInt(txtEdicion.getText().trim());
            String editorial = txtEditorial.getText().trim();
            int anio = Integer.parseInt(txtAnio.getText().trim());

            if (titulo.isEmpty() || editorial.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // VALIDACIÓN: Verificar si ya existe un libro con el mismo título (ignorando mayúsculas/minúsculas)
            for (Libro l : biblioteca.getLibros()) {
                if (l.getTitulo().equalsIgnoreCase(titulo)) {
                    JOptionPane.showMessageDialog(this, "Ya existe un libro registrado con el título '" + titulo + "'.", "Error de Duplicidad", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // 1. Agregar a la memoria de Biblioteca usando el método de tu modelo
            biblioteca.nuevoLibro(titulo, edicion, editorial, anio);

            // 2. Persistir en SQLite la instancia creada
            Libro ultimoLibro = biblioteca.getLibros().get(biblioteca.getLibros().size() - 1);
            libroDAO.insertar(ultimoLibro);

            txtTitulo.setText("");
            txtEdicion.setText("");
            txtEditorial.setText("");
            txtAnio.setText("");

            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Libro registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Edición y Año deben ser números enteros válidos.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarTabla() {
        modeloTabla.setRowCount(0);
        ArrayList<Libro> libros = biblioteca.getLibros();

        for (Libro libro : libros) {
            String estado = libro.prestado() ? "Prestado" : "Disponible";
            Object[] fila = {
                    libro.getTitulo(),
                    libro.getEdicion(),
                    libro.getEditorial(),
                    libro.getAnio(),
                    estado
            };
            modeloTabla.addRow(fila);
        }
    }
}