package gui;

import datos.PrestamoDAO;
import modelo.*;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class PanelPrestamos extends JPanel {
    private Biblioteca biblioteca;
    private JComboBox<String> cbSocios, cbLibrosPrestar, cbLibrosDevolver;
    private PrestamoDAO prestamoDAO;

    public PanelPrestamos(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        this.prestamoDAO = new PrestamoDAO();
        setLayout(new BorderLayout(15, 15));
        setBackground(Estilos.MARFIL);

        JLabel lblTituloPanel = new JLabel("Préstamos y Devoluciones");
        lblTituloPanel.setFont(Estilos.FONT_TITULO);
        lblTituloPanel.setForeground(Estilos.AZUL_TINTA);
        add(lblTituloPanel, BorderLayout.NORTH);

        JPanel panelIzquierdo = crearPanelPrestar();
        JPanel panelDerecho = crearPanelDevolver();

        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 15, 0));
        panelCentral.setOpaque(false);
        panelCentral.add(panelIzquierdo);
        panelCentral.add(panelDerecho);

        add(panelCentral, BorderLayout.CENTER);

        actualizarDesplegables();
    }

    private JPanel crearPanelPrestar() {
        JPanel tarjeta = Estilos.crearTarjeta();
        tarjeta.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblSubtitulo = new JLabel("Registrar Nuevo Préstamo");
        lblSubtitulo.setFont(Estilos.FONT_SUBTITULO);
        lblSubtitulo.setForeground(Estilos.AZUL_TINTA);

        cbSocios = new JComboBox<>();
        cbLibrosPrestar = new JComboBox<>();

        JButton btnPrestar = new JButton("Realizar Préstamo");
        Estilos.estilizarBoton(btnPrestar, Estilos.VERDE_EXITO, Estilos.BLANCO_PAPEL);
        btnPrestar.addActionListener(e -> realizarPrestamo());

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        tarjeta.add(lblSubtitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0; tarjeta.add(new JLabel("Seleccionar Socio:"), gbc);
        gbc.gridx = 1; tarjeta.add(cbSocios, gbc);

        gbc.gridy = 2; gbc.gridx = 0; tarjeta.add(new JLabel("Seleccionar Libro:"), gbc);
        gbc.gridx = 1; tarjeta.add(cbLibrosPrestar, gbc);

        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        tarjeta.add(Box.createVerticalStrut(15), gbc);

        gbc.gridy = 4;
        tarjeta.add(btnPrestar, gbc);

        return tarjeta;
    }

    private JPanel crearPanelDevolver() {
        JPanel tarjeta = Estilos.crearTarjeta();
        tarjeta.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblSubtitulo = new JLabel("Devolución y Consultas");
        lblSubtitulo.setFont(Estilos.FONT_SUBTITULO);
        lblSubtitulo.setForeground(Estilos.AZUL_TINTA);

        cbLibrosDevolver = new JComboBox<>();

        JButton btnDevolver = new JButton("Devolver Libro");
        Estilos.estilizarBoton(btnDevolver, Estilos.TERRACOTA_ERROR, Estilos.BLANCO_PAPEL);
        btnDevolver.addActionListener(e -> devolverLibro());

        JButton btnQuienLoTiene = new JButton("¿Quién tiene el libro?");
        Estilos.estilizarBoton(btnQuienLoTiene, Estilos.AMBAR_ADVERTENCIA, Estilos.BLANCO_PAPEL);
        btnQuienLoTiene.addActionListener(e -> consultarQuienTieneElLibro());

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

    private void realizarPrestamo() {
        if (cbSocios.getSelectedIndex() == -1 || cbLibrosPrestar.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un socio y un libro disponibles.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Socio socio = biblioteca.getSocios().get(cbSocios.getSelectedIndex());

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
            if (!socio.puedePedir()) {
                JOptionPane.showMessageDialog(this, "El socio no puede pedir más libros (Límite alcanzado o posee deudas).", "Préstamo Denegado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 1. Efectuar el préstamo en la instancia de Biblioteca
            biblioteca.prestarLibro(Calendar.getInstance(), socio, libroSeleccionado);

            // 2. Persistir el nuevo préstamo en SQLite
            Prestamo ultimoPrestamo = libroSeleccionado.ultimoPrestamo();
            if (ultimoPrestamo != null) {
                prestamoDAO.insertar(ultimoPrestamo);
            }

            actualizarDesplegables();
            JOptionPane.showMessageDialog(this, "Préstamo registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void devolverLibro() {
        if (cbLibrosDevolver.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "No hay libros prestados actualmente.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

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

            // 1. Efectuar la devolución en el modelo
            biblioteca.devolverLibro(libroSeleccionado);

            // 2. Actualizar la fecha de devolución en SQLite
            if (prestamoADevolver != null) {
                prestamoDAO.registrarDevolucion(prestamoADevolver);
            }

            actualizarDesplegables();
            JOptionPane.showMessageDialog(this, "Libro devuelto a la biblioteca.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void consultarQuienTieneElLibro() {
        if (cbLibrosDevolver.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro de la lista.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

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
            String responsable = biblioteca.quienTieneElLibro(libroSeleccionado);
            JOptionPane.showMessageDialog(this, "El libro '" + libroSeleccionado.getTitulo() + "' está en posesión de: " + responsable, "Consulta de Estado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void actualizarDesplegables() {
        cbSocios.removeAllItems();
        cbLibrosPrestar.removeAllItems();
        cbLibrosDevolver.removeAllItems();

        for (Socio s : biblioteca.getSocios()) {
            cbSocios.addItem(s.getNombre() + " (" + s.soyDeLaClase() + ")");
        }

        for (Libro l : biblioteca.getLibros()) {
            if (!l.prestado()) {
                cbLibrosPrestar.addItem(l.getTitulo());
            } else {
                cbLibrosDevolver.addItem(l.getTitulo());
            }
        }
    }
}