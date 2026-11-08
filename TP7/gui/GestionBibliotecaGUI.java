package gui;

import datos.Conexion;
import datos.LibroDAO;
import datos.PrestamoDAO;
import datos.SocioDAO;
import modelo.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Ventana principal de la interfaz grafica que administra la navegacion entre modulos,
 * la conexion con la base de datos y la inicializacion del sistema de gestion de biblioteca.
 */
public class GestionBibliotecaGUI extends JFrame {
    /** Instancia principal de la biblioteca que maneja la logica de negocio y colecciones */
    private Biblioteca biblioteca;
    /** Panel contenedor principal que utiliza CardLayout para cambiar entre las diferentes vistas */
    private JPanel panelContenido;
    /** Administrador de diseño tipo tarjeta para alternar entre paneles de modulos */
    private CardLayout cardLayout;

    /** Panel para la gestion y visualizacion de libros */
    private PanelLibros panelLibros;
    /** Panel para la gestion y visualizacion de socios */
    private PanelSocios panelSocios;
    /** Panel para la administracion de prestamos */
    private PanelPrestamos panelPrestamos;
    /** Panel para reportes y consultas del sistema */
    private PanelReportes panelReportes;

    /**
     * Constructor de la clase GestionBibliotecaGUI
     * Inicializa la base de datos, carga los datos desde SQLite, vincula prestamos
     * y configura la estructura visual y los paneles de la interfaz principal.
     */
    public GestionBibliotecaGUI() {
        // 1. Inicializar la tabla y estructura en SQLite (biblioteca.db) si no existen
        Conexion.inicializarBaseDeDatos();

        // 2. Instanciar los DAO
        SocioDAO socioDAO = new SocioDAO();
        LibroDAO libroDAO = new LibroDAO();
        PrestamoDAO prestamoDAO = new PrestamoDAO();

        // 3. Traer las listas directamente de SQLite
        ArrayList<Socio> sociosDB = socioDAO.obtenerTodos();
        ArrayList<Libro> librosDB = libroDAO.obtenerTodos();

        // 4. Instanciar la Biblioteca usando el constructor sobrecargado con las listas cargadas
        biblioteca = new Biblioteca("Biblioteca Central", librosDB, sociosDB);

        // 5. Vincular los préstamos existentes entre socios y libros
        prestamoDAO.obtenerTodos(biblioteca.getSocios(), biblioteca.getLibros());

        // 6. Configuración de la interfaz gráfica
        setTitle("Sistema de Gestión de Biblioteca");
        setSize(1050, 680);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel sidebar = crearSidebar();
        add(sidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.setBackground(Estilos.MARFIL);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Inicializar Paneles de Módulos con los datos ya cargados y vinculados
        panelLibros = new PanelLibros(biblioteca);
        panelSocios = new PanelSocios(biblioteca);
        panelPrestamos = new PanelPrestamos(biblioteca);
        panelReportes = new PanelReportes(biblioteca);

        panelContenido.add(crearPanelBienvenida(), "INICIO");
        panelContenido.add(panelLibros, "LIBROS");
        panelContenido.add(panelSocios, "SOCIOS");
        panelContenido.add(panelPrestamos, "PRESTAMOS");
        panelContenido.add(panelReportes, "REPORTES");

        add(panelContenido, BorderLayout.CENTER);
    }

    /**
     * Metodo que crea y configura el panel lateral (sidebar) con el titulo y los botones de navegacion
     * @return JPanel que representa la barra lateral de opciones
     */
    private JPanel crearSidebar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Estilos.AZUL_TINTA);
        panel.setPreferredSize(new Dimension(250, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));

        // Encabezado Lateral
        JLabel lblTitulo = new JLabel("<html><div style='text-align: left;'><b>GESTIÓN DE</b><br>BIBLIOTECA</div></html>");
        lblTitulo.setForeground(Estilos.ARENA);
        lblTitulo.setFont(Estilos.FONT_LOGO_BARRA);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 35)));

        // Botones del Menú Lateral
        JButton btnInicio = crearBotonNav("  Inicio / Estado");
        JButton btnLibros = crearBotonNav("  Gestión Libros");
        JButton btnSocios = crearBotonNav("  Gestión Socios");
        JButton btnPrestamos = crearBotonNav("  Préstamos");
        JButton btnReportes = crearBotonNav("  Reportes");

        btnInicio.addActionListener(e -> cardLayout.show(panelContenido, "INICIO"));

        btnLibros.addActionListener(e -> {
            panelLibros.actualizarTabla();
            cardLayout.show(panelContenido, "LIBROS");
        });

        btnSocios.addActionListener(e -> {
            panelSocios.actualizarTabla();
            cardLayout.show(panelContenido, "SOCIOS");
        });

        btnPrestamos.addActionListener(e -> {
            panelPrestamos.actualizarDesplegables();
            cardLayout.show(panelContenido, "PRESTAMOS");
        });

        btnReportes.addActionListener(e -> {
            panelReportes.actualizarReportes();
            cardLayout.show(panelContenido, "REPORTES");
        });

        panel.add(btnInicio);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(btnLibros);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(btnSocios);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(btnPrestamos);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(btnReportes);

        return panel;
    }

    /**
     * Metodo auxiliar para crear y estilizar los botones del menu de navegacion lateral
     * @param texto el texto que mostrara el boton
     * @return JButton configurado con el estilo de navegacion
     */
    private JButton crearBotonNav(String texto) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(210, 48));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        Estilos.estilitarBotonNav(btn);
        return btn;
    }

    /**
     * Metodo que crea el panel de bienvenida principal que se muestra al iniciar la aplicacion
     * @return JPanel con la tarjeta de bienvenida y logotipo
     */
    private JPanel crearPanelBienvenida() {
        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setBackground(Estilos.MARFIL);

        JPanel tarjetaLogotipo = Estilos.crearTarjeta();
        tarjetaLogotipo.setLayout(new BoxLayout(tarjetaLogotipo, BoxLayout.Y_AXIS));
        tarjetaLogotipo.setPreferredSize(new Dimension(550, 280));
        tarjetaLogotipo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblLogo = new JLabel("GESTIÓN DE BIBLIOTECA");
        lblLogo.setFont(Estilos.FONT_LOGO_GRANDE);
        lblLogo.setForeground(Estilos.AZUL_TINTA);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Bienvenido al Sistema de Gestión");
        lblSub.setFont(Estilos.FONT_SUBTITULO);
        lblSub.setForeground(Estilos.AMBAR_ADVERTENCIA);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel("<html><center>Seleccione una opción del menú lateral para administrar libros, socios, préstamos y consultas de manera eficiente.</center></html>");
        lblDesc.setFont(Estilos.FONT_BASE);
        lblDesc.setForeground(Color.DARK_GRAY);
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        tarjetaLogotipo.add(Box.createRigidArea(new Dimension(0, 25)));
        tarjetaLogotipo.add(lblLogo);
        tarjetaLogotipo.add(Box.createRigidArea(new Dimension(0, 10)));
        tarjetaLogotipo.add(lblSub);
        tarjetaLogotipo.add(Box.createRigidArea(new Dimension(0, 20)));
        tarjetaLogotipo.add(lblDesc);

        panelCentro.add(tarjetaLogotipo);
        return panelCentro;
    }

    /**
     * Metodo principal (main) para la ejecucion de la aplicacion de escritorio
     * @param args argumentos de la linea de comandos
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestionBibliotecaGUI gui = new GestionBibliotecaGUI();
            gui.setVisible(true);
        });
    }
}
