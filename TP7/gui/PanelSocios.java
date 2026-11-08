package gui;

import datos.SocioDAO;
import modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Clase que representa el panel de la interfaz gráfica destinado a la gestión 
 * de los socios de la biblioteca. Permite el registro de nuevos socios, 
 * su visualización en una tabla y la aplicación de premios a docentes responsables.
 */
public class PanelSocios extends JPanel {
    
    /** Referencia al sistema principal de la biblioteca para gestionar datos en memoria */
    private Biblioteca biblioteca;
    
    /** Campo de texto para el ingreso del DNI del socio */
    private JTextField txtDni;
    /** Campo de texto para el ingreso del nombre completo del socio */
    private JTextField txtNombre;
    /** Campo de texto para el ingreso del dato específico (Carrera o Área) */
    private JTextField txtEspecifico;
    
    /** Selector desplegable para definir si el socio es Estudiante o Docente */
    private JComboBox<String> cbTipoSocio;
    /** Etiqueta dinámica que cambia su texto a "Carrera" o "Área" según el tipo de socio */
    private JLabel lblEspecifico;
    
    /** Tabla visual para listar a todos los socios registrados */
    private JTable tablaSocios;
    /** Modelo de datos asociado a la tabla de socios */
    private DefaultTableModel modeloTabla;
    
    /** Objeto encargado de manejar la persistencia de los socios en la base de datos */
    private SocioDAO socioDAO;

    /**
     * Constructor del panel de gestión de socios. Inicializa los componentes visuales, 
     * define la estructura del panel y carga los datos existentes.
     * 
     * @param biblioteca la instancia principal de la biblioteca con los datos del sistema
     */
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

    /**
     * Crea y configura el panel que contiene el formulario de registro 
     * para agregar nuevos socios al sistema.
     * 
     * @return JPanel con el diseño y los campos del formulario
     */
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

    /**
     * Crea y configura el panel que aloja la tabla con el listado de socios 
     * y los botones de acción relacionados con los registros.
     * 
     * @return JPanel con el diseño de tarjeta conteniendo la tabla y botones
     */
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

    /**
     * Extrae y valida los datos del formulario. Si la información es correcta 
     * y el DNI no está duplicado, registra al nuevo socio tanto en la memoria 
     * de la aplicación como en la base de datos.
     */
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

            if (biblioteca.buscarSocio(dni) != null) {
                JOptionPane.showMessageDialog(this, "Ya existe un socio registrado con el DNI " + dni + ".", "Error de Duplicidad", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ("Estudiante".equals(tipo)) {
                biblioteca.nuevoSocioEstudiante(dni, nombre, especifico);
            } else {
                biblioteca.nuevoSocioDocente(dni, nombre, especifico);
            }

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

    /**
     * Permite incrementar la cantidad de días de préstamo de un docente 
     * seleccionado en la tabla, verificando previamente que califique 
     * como responsable y actualizando el dato en la base de datos.
     */
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

    /**
     * Refresca los datos mostrados en la tabla consultando la lista actualizada 
     * de socios en la memoria del sistema de la biblioteca.
     */
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
