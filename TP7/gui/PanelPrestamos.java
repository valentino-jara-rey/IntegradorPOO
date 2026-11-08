package gui;

import datos.PrestamoDAO;
import modelo.*;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

/**
 * Panel gráfico (JPanel) que gestiona el módulo de Préstamos y Devoluciones.
 * Permite registrar nuevos préstamos asociando un socio con un libro disponible,
 * así como gestionar la devolución de libros prestados y consultar quién posee un ejemplar actualmente.
 */
public class PanelPrestamos extends JPanel {
    // Referencia al modelo principal de la biblioteca para la lógica de negocio
    private Biblioteca biblioteca;
    // Componentes desplegables (ComboBox) para seleccionar socios y libros según su disponibilidad
    private JComboBox<String> cbSocios, cbLibrosPrestar, cbLibrosDevolver;
    // Objeto de acceso a datos (DAO) encargado de la persistencia de los préstamos en la base de datos
    private PrestamoDAO prestamoDAO;

    /**
     * Constructor del panel. Inicializa los atributos principales, configura el diseño visual (layout),
     * construye los paneles internos para prestar y devolver, y carga los datos iniciales en los desplegables.
     * @param biblioteca Instancia compartida del modelo Biblioteca.
     */
    public PanelPrestamos(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        this.prestamoDAO = new PrestamoDAO();
        // Configura el diseño general del panel con un espaciado horizontal y vertical de 15 píxeles
        setLayout(new BorderLayout(15, 15));
        setBackground(Estilos.MARFIL);

        // Título principal ubicado en la parte superior del panel
        JLabel lblTituloPanel = new JLabel("Préstamos y Devoluciones");
        lblTituloPanel.setFont(Estilos.FONT_TITULO);
        lblTituloPanel.setForeground(Estilos.AZUL_TINTA);
        add(lblTituloPanel, BorderLayout.NORTH);

        // Creación de las dos subsecciones: Panel izquierdo (Prestar) y Panel derecho (Devolver/Consultar)
        JPanel panelIzquierdo = crearPanelPrestar();
        JPanel panelDerecho = crearPanelDevolver();

        // Panel contenedor central que distribuye ambas secciones lado a lado (1 fila, 2 columnas)
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 15, 0));
        panelCentral.setOpaque(false);
        panelCentral.add(panelIzquierdo);
        panelCentral.add(panelDerecho);

        add(panelCentral, BorderLayout.CENTER);

        // Poblar los menús desplegables con la información actual de socios y libros
        actualizarDesplegables();
    }

    /**
     * Crea, configura y devuelve el panel correspondiente al registro de nuevos préstamos.
     * Utiliza GridBagLayout para organizar las etiquetas, los selectores y el botón de acción.
     * @return JPanel configurado para realizar préstamos.
     */
    private JPanel crearPanelPrestar() {
        JPanel tarjeta = Estilos.crearTarjeta();
        tarjeta.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Márgenes internos
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Subtítulo del panel de préstamos
        JLabel lblSubtitulo = new JLabel("Registrar Nuevo Préstamo");
        lblSubtitulo.setFont(Estilos.FONT_SUBTITULO);
        lblSubtitulo.setForeground(Estilos.AZUL_TINTA);

        // Inicialización de los desplegables para socios y libros disponibles
        cbSocios = new JComboBox<>();
        cbLibrosPrestar = new JComboBox<>();

        // Botón para procesar el préstamo con estilo visual de éxito (verde)
        JButton btnPrestar = new JButton("Realizar Préstamo");
        Estilos.estilizarBoton(btnPrestar, Estilos.VERDE_EXITO, Estilos.BLANCO_PAPEL);
        btnPrestar.addActionListener(e -> realizarPrestamo());

        // Posicionamiento de los elementos usando GridBagConstraints
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        tarjeta.add(lblSubtitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0; tarjeta.add(new JLabel("Seleccionar Socio:"), gbc);
        gbc.gridx = 1; tarjeta.add(cbSocios, gbc);

        gbc.gridy = 2; gbc.gridx = 0; tarjeta.add(new JLabel("Seleccionar Libro:"), gbc);
        gbc.gridx = 1; tarjeta.add(cbLibrosPrestar, gbc);

        // Espaciador vertical
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        tarjeta.add(Box.createVerticalStrut(15), gbc);

        // Botón de acción al fondo
        gbc.gridy = 4;
        tarjeta.add(btnPrestar, gbc);

        return tarjeta;
    }

    /**
     * Crea, configura y devuelve el panel correspondiente a las devoluciones y consultas de estado.
     * @return JPanel configurado para devolver libros y consultar responsabilidades.
     */
    private JPanel crearPanelDevolver() {
        JPanel tarjeta = Estilos.crearTarjeta();
        tarjeta.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Subtítulo del panel de devoluciones
        JLabel lblSubtitulo = new JLabel("Devolución y Consultas");
        lblSubtitulo.setFont(Estilos.FONT_SUBTITULO);
        lblSubtitulo.setForeground(Estilos.AZUL_TINTA);

        // Desplegable que contendrá exclusivamente los libros que se encuentran prestados
        cbLibrosDevolver = new JComboBox<>();

        // Botón para registrar la devolución (color terracota/error para denotar salida o cierre)
        JButton btnDevolver = new JButton("Devolver Libro");
        Estilos.estilizarBoton(btnDevolver, Estilos.TERRACOTA_ERROR, Estilos.BLANCO_PAPEL);
        btnDevolver.addActionListener(e -> devolverLibro());

        // Botón para consultar qué socio tiene un libro en posesión (color ámbar)
        JButton btnQuienLoTiene = new JButton("¿Quién tiene el libro?");
        Estilos.estilizarBoton(btnQuienLoTiene, Estilos.AMBAR_ADVERTENCIA, Estilos.BLANCO_PAPEL);
        btnQuienLoTiene.addActionListener(e -> consultarQuienTieneElLibro());

        // Posicionamiento de componentes en la rejilla
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        tarjeta.add(lblSubtitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0; tarjeta.add(new JLabel("Seleccionar Libro:"), gbc);
        gbc.gridx = 1; tarjeta.add(cbLibrosDevolver, gbc);

        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2;
        tarjeta.add(Box.createVerticalStrut(10), gbc);

        gbc.gridy = 3;
        tarjeta.add(btnDevolver, gbc);

        gbc.gridy = 4;
        tarjeta.add(btnQuienLoTiene, gbc);

        return tarjeta;
    }

    /**
     * Ejecuta la lógica para registrar un nuevo préstamo.
     * 1. Valida que se haya seleccionado un socio y un libro.
     * 2. Comprueba si el socio cumple con los requisitos para solicitar préstamos (límites/deudas).
     * 3. Registra el préstamo en memoria a través del modelo Biblioteca.
     * 4. Persiste el nuevo registro en la base de datos usando PrestamoDAO.
     * 5. Actualiza los menús desplegables de la interfaz.
     */
    private void realizarPrestamo() {
        // Validación: Asegurar que ambos desplegables tengan una selección activa
        if (cbSocios.getSelectedIndex() == -1 || cbLibrosPrestar.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un socio y un libro disponibles.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener la instancia del socio seleccionado
        Socio socio = biblioteca.getSocios().get(cbSocios.getSelectedIndex());

        // Buscar el objeto Libro correspondiente a la opción seleccionada en el combo de libros disponibles
        Libro libroSeleccionado = null;
        int indexAcceso = 0;
        for (Libro l : biblioteca.getLibros()) {
            if (!l.prestado()) {
                if (indexAcceso == cbLibrosPrestar.getSelectedIndex()) {
                    libroSeleccionado = l;
                    break;
                }
                indexAcceso++;
            }
        }

        if (libroSeleccionado != null) {
            // Validación de negocio: Verificar si el socio puede realizar más préstamos
            if (!socio.puedePedir()) {
                JOptionPane.showMessageDialog(this, "El socio no puede pedir más libros (Límite alcanzado o posee deudas).", "Préstamo Denegado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 1. Efectuar el préstamo en la instancia de Biblioteca (modelo en memoria) pasando la fecha actual
            biblioteca.prestarLibro(Calendar.getInstance(), socio, libroSeleccionado);

            // 2. Persistir el nuevo préstamo en SQLite utilizando su DAO correspondiente
            Prestamo ultimoPrestamo = libroSeleccionado.ultimoPrestamo();
            if (ultimoPrestamo != null) {
                prestamoDAO.insertar(ultimoPrestamo);
            }

            // Actualización de la interfaz y mensaje de éxito
            actualizarDesplegables();
            JOptionPane.showMessageDialog(this, "Préstamo registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Ejecuta la lógica para procesar la devolución de un libro prestado.
     * 1. Valida que exista al menos un libro en el desplegable de devolución.
     * 2. Localiza el libro y su respectivo préstamo activo.
     * 3. Actualiza el estado en la memoria de la biblioteca y registra la fecha de devolución en la base de datos.
     */
    private void devolverLibro() {
        // Validación: Verificar que haya libros prestados seleccionables
        if (cbLibrosDevolver.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "No hay libros prestados actualmente.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Identificar el libro seleccionado dentro de la lista de libros prestados
        Libro libroSeleccionado = null;
        int indexAcceso = 0;
        for (Libro l : biblioteca.getLibros()) {
            if (l.prestado()) {
                if (indexAcceso == cbLibrosDevolver.getSelectedIndex()) {
                    libroSeleccionado = l;
                    break;
                }
                indexAcceso++;
            }
        }

        if (libroSeleccionado != null) {
            Prestamo prestamoADevolver = libroSeleccionado.ultimoPrestamo();

            // 1. Efectuar la devolución en el modelo lógico de la biblioteca
            biblioteca.devolverLibro(libroSeleccionado);

            // 2. Actualizar la fecha de devolución en la base de datos (SQLite) a través del DAO
            if (prestamoADevolver != null) {
                prestamoDAO.registrarDevolucion(prestamoADevolver);
            }

            // Refrescar desplegables y mostrar confirmación
            actualizarDesplegables();
            JOptionPane.showMessageDialog(this, "Libro devuelto a la biblioteca.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Consulta y muestra en pantalla qué socio tiene actualmente en posesión un libro seleccionado.
     */
    private void consultarQuienTieneElLibro() {
        // Validación de selección previa
        if (cbLibrosDevolver.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro de la lista.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Localizar el libro prestado correspondiente al índice del combo
        Libro libroSeleccionado = null;
        int indexAcceso = 0;
        for (Libro l : biblioteca.getLibros()) {
            if (l.prestado()) {
                if (indexAcceso == cbLibrosDevolver.getSelectedIndex()) {
                    libroSeleccionado = l;
                    break;
                }
                indexAcceso++;
            }
        }

        if (libroSeleccionado != null) {
            // Consulta al modelo quién es el responsable del préstamo
            String responsable = biblioteca.quienTieneElLibro(libroSeleccionado);
            JOptionPane.showMessageDialog(this, "El libro '" + libroSeleccionado.getTitulo() + "' está en posesión de: " + responsable, "Consulta de Estado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Actualiza y sincroniza los elementos de todos los menús desplegables (JComboBox)
     * basándose en el estado actual de los socios y la disponibilidad de los libros en la biblioteca.
     */
    public void actualizarDesplegables() {
        // Limpiar los elementos actuales de los tres componentes
        cbSocios.removeAllItems();
        cbLibrosPrestar.removeAllItems();
        cbLibrosDevolver.removeAllItems();

        // Rellenar el desplegable de socios con su nombre y tipo de membresía/clase
        for (Socio s : biblioteca.getSocios()) {
            cbSocios.addItem(s.getNombre() + " (" + s.soyDeLaClase() + ")");
        }

        // Rellenar los desplegables de libros separándolos por su estado actual
        for (Libro l : biblioteca.getLibros()) {
            if (!l.prestado()) {
                cbLibrosPrestar.addItem(l.getTitulo()); // Libros disponibles para préstamo
            } else {
                cbLibrosDevolver.addItem(l.getTitulo()); // Libros prestados aptos para devolución/consulta
            }
        }
    }
}
