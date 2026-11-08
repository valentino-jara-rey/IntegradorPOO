package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Clase de utilidad encargada de centralizar los estilos visuales, colores, fuentes y componentes personalizados de la interfaz grafica.
 */
public class Estilos {
    
    /** Color base azul tinta para fondos o elementos principales */
    public static final Color AZUL_TINTA = Color.decode("#1B2A38");
    /** Color marfil para fondos claros o contrastes suaves */
    public static final Color MARFIL = Color.decode("#F5F2EB");
    /** Color blanco papel para paneles y contenedores */
    public static final Color BLANCO_PAPEL = Color.decode("#FFFFFF");
    /** Color arena para bordes o detalles secundarios */
    public static final Color ARENA = Color.decode("#E2D7C5");

    /** Color verde institucional para indicadores de exito */
    public static final Color VERDE_EXITO = Color.decode("#2E7D32");
    /** Color ambar para avisos o advertencias */
    public static final Color AMBAR_ADVERTENCIA = Color.decode("#D97706");
    /** Color terracota para errores o alertas criticas */
    public static final Color TERRACOTA_ERROR = Color.decode("#C62828");

    /** Fuente de tipo Georgia negrita en tamaño 32 para el logo grande */
    public static final Font FONT_LOGO_GRANDE = new Font("Georgia", Font.BOLD, 32);
    /** Fuente de tipo Georgia negrita en tamaño 18 para el logo de la barra */
    public static final Font FONT_LOGO_BARRA = new Font("Georgia", Font.BOLD, 18);
    /** Fuente Segoe UI negrita en tamaño 22 para titulos principales */
    public static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 22);
    /** Fuente Segoe UI negrita en tamaño 15 para subtitulos */
    public static final Font FONT_SUBTITULO = new Font("Segoe UI", Font.BOLD, 15);
    /** Fuente Segoe UI normal en tamaño 14 para textos base */
    public static final Font FONT_BASE = new Font("Segoe UI", Font.PLAIN, 14);
    /** Fuente Segoe UI negrita en tamaño 14 para textos de botones */
    public static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 14);

    /**
     * Crea y devuelve un panel configurado con estilo de tarjeta con bordes y padding
     * @return JPanel con el diseño de tarjeta aplicado
     */
    public static JPanel crearTarjeta() {
        JPanel panel = new JPanel();
        panel.setBackground(BLANCO_PAPEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ARENA, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        return panel;
    }

    /**
     * Aplica estilo de tarjeta moderna con transparencia, bordes redondeados,
     * relieve sutil y efecto hover iluminado a los botones del menú lateral.
     * @param boton el JButton al cual se le aplicara el estilo de navegacion
     */
    public static void estilitarBotonNav(JButton boton) {
        boton.setFont(FONT_BOTON);
        boton.setForeground(ARENA); // <-- CAMBIADO: Texto en tono arena
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setBorder(new EmptyBorder(10, 20, 10, 20));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.putClientProperty("hover", true);
                boton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.putClientProperty("hover", false);
                boton.repaint();
            }
        });

        boton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = c.getWidth();
                int height = c.getHeight();

                boolean isHover = Boolean.TRUE.equals(c.getClientProperty("hover"));

                Color colorArriba = isHover ? new Color(255, 255, 255, 60) : new Color(255, 255, 255, 25);
                Color colorAbajo  = isHover ? new Color(255, 255, 255, 25) : new Color(255, 255, 255, 8);

                GradientPaint gpFondo = new GradientPaint(0, 0, colorArriba, 0, height, colorAbajo);
                g2.setPaint(gpFondo);
                g2.fillRoundRect(0, 0, width - 1, height - 1, 14, 14);

                Color colorBorde = isHover ? new Color(255, 255, 255, 120) : new Color(255, 255, 255, 50);
                g2.setColor(colorBorde);
                g2.drawRoundRect(0, 0, width - 1, height - 1, 14, 14);

                g2.setColor(new Color(255, 255, 255, isHover ? 160 : 90));
                g2.drawLine(7, 1, width - 8, 1);

                g2.dispose();
                super.paint(g, c);
            }
        });
    }

    /**
     * Aplica un estilo personalizado a un boton estandar con colores de fondo y texto definidos
     * @param boton el JButton a modificar
     * @param colorFondo el color de fondo para el boton
     * @param colorTexto el color del texto para el boton
     */
    public static void estilizarBoton(JButton boton, Color colorFondo, Color colorTexto) {
        boton.setBackground(colorFondo);
        boton.setForeground(colorTexto);
        boton.setFont(FONT_BOTON);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorFondo.darker(), 1),
                new EmptyBorder(10, 18, 10, 18)
        ));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Metodo alternativo alias de estilizarBoton para compatibilidad con nombres similares
     * @param boton el JButton a modificar
     * @param colorFondo el color de fondo para el boton
     * @param colorTexto el color del texto para el boton
     */
    public static void estilizaBoton(JButton boton, Color colorFondo, Color colorTexto) {
        estilizarBoton(boton, colorFondo, colorTexto);
    }
}
