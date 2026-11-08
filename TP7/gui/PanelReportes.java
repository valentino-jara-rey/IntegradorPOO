package gui;

import modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PanelReportes extends JPanel {
    private Biblioteca biblioteca;
    private JLabel lblCantEstudiantes, lblCantDocentes;
    private JTable tablaDocentesResponsables, tablaPrestamosVencidos;
    private DefaultTableModel modeloDocentes, modeloVencidos;

    public PanelReportes(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        setLayout(new BorderLayout(15, 15));
        setBackground(Estilos.MARFIL);

        JLabel lblTituloPanel = new JLabel("Reportes y Consultas");
        lblTituloPanel.setFont(Estilos.FONT_TITULO);
        lblTituloPanel.setForeground(Estilos.AZUL_TINTA);
        add(lblTituloPanel, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout(15, 15));
        panelCentral.setOpaque(false);

        panelCentral.add(crearPanelTarjetasMetricas(), BorderLayout.NORTH);

        JPanel panelTablas = new JPanel(new GridLayout(1, 2, 15, 0));
        panelTablas.setOpaque(false);
        panelTablas.add(crearTablaDocentesResponsables());
        panelTablas.add(crearTablaPrestamosVencidos());

        panelCentral.add(panelTablas, BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);

        actualizarReportes();
    }

    private JPanel crearPanelTarjetasMetricas() {
        JPanel panelMetricas = new JPanel(new GridLayout(1, 2, 15, 0));
        panelMetricas.setOpaque(false);

        JPanel tarjetaEstudiantes = Estilos.crearTarjeta();
        tarjetaEstudiantes.setLayout(new BorderLayout(5, 5));
        JLabel lblTituloEst = new JLabel("Socios de tipo Estudiante:");
        lblTituloEst.setFont(Estilos.FONT_SUBTITULO);
        lblTituloEst.setForeground(Estilos.AZUL_TINTA);

        lblCantEstudiantes = new JLabel("0");
        lblCantEstudiantes.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblCantEstudiantes.setForeground(Estilos.VERDE_EXITO);

        tarjetaEstudiantes.add(lblTituloEst, BorderLayout.NORTH);
        tarjetaEstudiantes.add(lblCantEstudiantes, BorderLayout.CENTER);

        JPanel tarjetaDocentes = Estilos.crearTarjeta();
        tarjetaDocentes.setLayout(new BorderLayout(5, 5));
        JLabel lblTituloDoc = new JLabel("Socios de tipo Docente:");
        lblTituloDoc.setFont(Estilos.FONT_SUBTITULO);
        lblTituloDoc.setForeground(Estilos.AZUL_TINTA);

        lblCantDocentes = new JLabel("0");
        lblCantDocentes.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblCantDocentes.setForeground(Estilos.AMBAR_ADVERTENCIA);

        tarjetaDocentes.add(lblTituloDoc, BorderLayout.NORTH);
        tarjetaDocentes.add(lblCantDocentes, BorderLayout.CENTER);

        panelMetricas.add(tarjetaEstudiantes);
        panelMetricas.add(tarjetaDocentes);

        return panelMetricas;
    }

    private JPanel crearTablaDocentesResponsables() {
        JPanel tarjeta = Estilos.crearTarjeta();
        tarjeta.setLayout(new BorderLayout(10, 10));

        JLabel lblSubtitulo = new JLabel("Docentes Responsables (Sin atrasos)");
        lblSubtitulo.setFont(Estilos.FONT_SUBTITULO);
        lblSubtitulo.setForeground(Estilos.AZUL_TINTA);
        tarjeta.add(lblSubtitulo, BorderLayout.NORTH);

        String[] columnas = {"DNI", "Nombre", "Área", "Días Préstamo"};
        modeloDocentes = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaDocentesResponsables = new JTable(modeloDocentes);
        tablaDocentesResponsables.setFont(Estilos.FONT_BASE);
        tablaDocentesResponsables.setRowHeight(25);
        tablaDocentesResponsables.getTableHeader().setBackground(Estilos.AZUL_TINTA);
        tablaDocentesResponsables.getTableHeader().setForeground(Estilos.BLANCO_PAPEL);
        tablaDocentesResponsables.getTableHeader().setFont(Estilos.FONT_BOTON);

        tarjeta.add(new JScrollPane(tablaDocentesResponsables), BorderLayout.CENTER);

        return tarjeta;
    }

    private JPanel crearTablaPrestamosVencidos() {
        JPanel tarjeta = Estilos.crearTarjeta();
        tarjeta.setLayout(new BorderLayout(10, 10));

        JLabel lblSubtitulo = new JLabel("Préstamos Vencidos a la Fecha");
        lblSubtitulo.setFont(Estilos.FONT_SUBTITULO);
        lblSubtitulo.setForeground(Estilos.TERRACOTA_ERROR);
        tarjeta.add(lblSubtitulo, BorderLayout.NORTH);

        String[] columnas = {"Libro", "Socio Poseedor", "Fecha Retiro"};
        modeloVencidos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaPrestamosVencidos = new JTable(modeloVencidos);
        tablaPrestamosVencidos.setFont(Estilos.FONT_BASE);
        tablaPrestamosVencidos.setRowHeight(25);
        tablaPrestamosVencidos.getTableHeader().setBackground(Estilos.TERRACOTA_ERROR);
        tablaPrestamosVencidos.getTableHeader().setForeground(Estilos.BLANCO_PAPEL);
        tablaPrestamosVencidos.getTableHeader().setFont(Estilos.FONT_BOTON);

        tarjeta.add(new JScrollPane(tablaPrestamosVencidos), BorderLayout.CENTER);

        return tarjeta;
    }

    public void actualizarReportes() {
        int totalEstudiantes = 0;
        int totalDocentes = 0;

        for (Socio socio : biblioteca.getSocios()) {
            if (socio instanceof Estudiante) {
                totalEstudiantes++;
            } else if (socio instanceof Docente) {
                totalDocentes++;
            }
        }

        lblCantEstudiantes.setText(String.valueOf(totalEstudiantes));
        lblCantDocentes.setText(String.valueOf(totalDocentes));

        // 1. Cargar lista de Docentes Responsables
        modeloDocentes.setRowCount(0);
        ArrayList<Docente> responsables = biblioteca.docentesResponsables();
        for (Docente doc : responsables) {
            Object[] fila = {
                    doc.getDniSocio(),
                    doc.getNombre(),
                    doc.getArea(),
                    doc.getDiasPrestamo()
            };
            modeloDocentes.addRow(fila);
        }

        // 2. Cargar lista de Préstamos Vencidos
        modeloVencidos.setRowCount(0);
        ArrayList<Prestamo> vencidos = biblioteca.prestamosVencidos();
        for (Prestamo p : vencidos) {
            String fechaRetiroStr = p.getFechaRetiro().get(java.util.Calendar.YEAR) + "/" +
                    (p.getFechaRetiro().get(java.util.Calendar.MONTH) + 1) + "/" +
                    p.getFechaRetiro().get(java.util.Calendar.DATE);
            Object[] fila = {
                    p.getLibro().getTitulo(),
                    p.getSocio().getNombre(),
                    fechaRetiroStr
            };
            modeloVencidos.addRow(fila);
        }
    }
}