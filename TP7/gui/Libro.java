package gui;

import datos.LibroDAO;
import modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel gráfico (JPanel) que forma parte de la interfaz de usuario.
 * Se encarga de la sección de "Gestión de Libros", permitiendo registrar
 * nuevos ejemplares mediante un formulario y visualizar el catálogo actual en una tabla.
 */
public class PanelLibros extends JPanel {
    // Referencia al modelo principal de la biblioteca para la lógica de negocio en memoria
    private Biblioteca biblioteca;
    // Campos de texto para la introducción de los datos del libro
    private JTextField txtTitulo, txtEdicion, txtEditorial, txtAnio;
    // Componentes de la tabla para mostrar la lista de libros
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;
    // Objeto de acceso a datos (DAO) encargado de la persistencia (conexión con la base de datos)
    private LibroDAO libroDAO;

    /**
     * Constructor del panel. Inicializa los atributos principales, configura el diseño visual (layout),
     * construye los paneles internos (formulario y tabla) y carga los datos iniciales.
     * @param biblioteca Instancia compartida del modelo Biblioteca.
     */
    public PanelLibros(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        this.libroDAO = new LibroDAO();
        // Configura el diseño general del panel con un espacio horizontal y vertical de 15 píxeles
        setLayout(new BorderLayout(15, 15));
        setBackground(Estilos.MARFIL);

        // Título principal ubicado en la parte superior del panel
        JLabel lblTituloPanel = new JLabel("Gestión de Libros");
        lblTituloPanel.setFont(Estilos.FONT_TITULO);
        lblTituloPanel.setForeground(Estilos.AZUL_TINTA);
        add(lblTituloPanel, BorderLayout.NORTH);

        // Creación de las dos secciones principales: Formulario de registro y Tabla de catálogo
        JPanel panelFormulario = crearPanelFormulario();
        JPanel panelTabla = crearPanelTabla();

        // Panel contenedor central que distribuye el formulario y la tabla lado a lado (1 fila, 2 columnas)
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 15, 0));
        panelCentral.setOpaque(false);
        panelCentral.add(panelFormulario);
        panelCentral.add(panelTabla);

        add(panelCentral, BorderLayout.CENTER);

        // Carga inicial de los datos de libros en la tabla gráfica
        actualizarTabla();
    }

    /**
     * Crea, configura y devuelve el panel correspondiente al formulario de registro de libros.
     * Utiliza GridBagLayout para estructurar las etiquetas, campos de texto y el botón de guardado.
     * @return JPanel configurado con el formulario.
     */
    private JPanel crearPanelFormulario() {
        JPanel tarjeta = Estilos.crearTarjeta();
        tarjeta.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Márgenes internos alrededor de los componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Subtítulo del formulario
        JLabel lblSubtitulo = new JLabel("Registrar Nuevo Libro");
        lblSubtitulo.setFont(Estilos.FONT_SUBTITULO);
        lblSubtitulo.setForeground(Estilos.AZUL_TINTA);

        // Instanciación de los campos de texto
        txtTitulo = new JTextField();
        txtEdicion = new JTextField();
        txtEditorial = new JTextField();
        txtAnio = new JTextField();

        // Botón para procesar el guardado y su respectivo evento (ActionListener)
        JButton btnGuardar = new JButton("Agregar Libro");
        Estilos.estilizarBoton(btnGuardar, Estilos.AZUL_TINTA, Estilos.BLANCO_PAPEL);
        btnGuardar.addActionListener(e -> guardarLibro());

        // Posicionamiento de los elementos en el GridBagLayout
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

        // Espaciador vertical decorativo antes del botón
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        tarjeta.add(Box.createVerticalStrut(10), gbc);

        // Ubicación del botón de guardado en la parte inferior del formulario
        gbc.gridy = 6;
        tarjeta.add(btnGuardar, gbc);

        return tarjeta;
    }

    /**
     * Crea, configura y devuelve el panel que contiene la tabla gráfica (JTable)
     * para visualizar el catálogo de libros disponibles y prestados.
     * @return JPanel con la tabla y su barra de desplazamiento.
     */
    private JPanel crearPanelTabla() {
        JPanel tarjeta = Estilos.crearTarjeta();
        tarjeta.setLayout(new BorderLayout(10, 10));

        // Título de la sección de la tabla
        JLabel lblSubtitulo = new JLabel("Catálogo de Libros");
        lblSubtitulo.setFont(Estilos.FONT_SUBTITULO);
        lblSubtitulo.setForeground(Estilos.AZUL_TINTA);
        tarjeta.add(lblSubtitulo, BorderLayout.NORTH);

        // Nombres de las columnas que mostrará la tabla
        String[] columnas = {"Título", "Edición", "Editorial", "Año", "Estado"};
        
        // Creación del modelo de la tabla, sobrescribiendo el método para bloquear la edición directa en celdas
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Configuración estética de la tabla (fuentes, colores de cabecera y altura de filas)
        tablaLibros = new JTable(modeloTabla);
        tablaLibros.setFont(Estilos.FONT_BASE);
        tablaLibros.setRowHeight(25);
        tablaLibros.getTableHeader().setBackground(Estilos.AZUL_TINTA);
        tablaLibros.getTableHeader().setForeground(Estilos.BLANCO_PAPEL);
        tablaLibros.getTableHeader().setFont(Estilos.FONT_BOTON);

        // Se agrega la tabla dentro de un JScrollPane para permitir el desplazamiento vertical si hay muchos registros
        tarjeta.add(new JScrollPane(tablaLibros), BorderLayout.CENTER);

        return tarjeta;
    }

    /**
     * Método ejecutado al presionar el botón "Agregar Libro". Se encarga de:
     * 1. Capturar y validar los datos ingresados en los campos de texto.
     * 2. Comprobar que no existan campos vacíos ni duplicidad de títulos.
     * 3. Registrar el libro en el modelo de memoria (Biblioteca).
     * 4. Persistir el registro en la base de datos a través de LibroDAO.
     * 5. Limpiar los campos del formulario y actualizar la interfaz gráfica.
     */
    private void guardarLibro() {
        try {
            // Lectura y limpieza de espacios en blanco de las entradas de texto
            String titulo = txtTitulo.getText().trim();
            int edicion = Integer.parseInt(txtEdicion.getText().trim());
            String editorial = txtEditorial.getText().trim();
            int anio = Integer.parseInt(txtAnio.getText().trim());

            // Validación: Asegurar que los campos obligatorios de texto no estén vacíos
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

            // 2. Persistir en la base de datos (SQLite) la instancia recién creada
            Libro ultimoLibro = biblioteca.getLibros().get(biblioteca.getLibros().size() - 1);
            libroDAO.insertar(ultimoLibro);

            // Limpieza de los campos de texto del formulario tras un registro exitoso
            txtTitulo.setText("");
            txtEdicion.setText("");
            txtEditorial.setText("");
            txtAnio.setText("");

            // Actualización de la vista tabular y despliegue de mensaje de éxito
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Libro registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            // Manejo de excepciones en caso de que el usuario ingrese letras en campos numéricos (edición o año)
            JOptionPane.showMessageDialog(this, "Edición y Año deben ser números enteros válidos.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Actualiza los datos mostrados en la JTable.
     * Limpia el contenido actual del modelo y vuelve a recorrer la lista de libros
     * de la biblioteca, evaluando su estado (Prestado / Disponible) para llenar las filas.
     */
    public void actualizarTabla() {
        modeloTabla.setRowCount(0); // Elimina todas las filas actuales de la tabla
        ArrayList<Libro> libros = biblioteca.getLibros();

        // Itera sobre la lista de libros para estructurar las filas que se añadirán al modelo
        for (Libro libro : libros) {
            // Determina el estado del libro basándose en el método booleano de la clase Libro
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
