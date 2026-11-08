package datos;

import modelo.Libro;
import modelo.Prestamo;
import modelo.Socio;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PrestamoDAO {

    public void insertar(Prestamo prestamo) {
        String sql = "INSERT INTO Prestamo (dni_socio, titulo_libro, fecha_retiro, fecha_devolucion) VALUES (?, ?, ?, NULL)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, prestamo.getSocio().getDniSocio());
            pstmt.setString(2, prestamo.getLibro().getTitulo());

            int anio = prestamo.getFechaRetiro().get(Calendar.YEAR);
            int mes = prestamo.getFechaRetiro().get(Calendar.MONTH) + 1;
            int dia = prestamo.getFechaRetiro().get(Calendar.DAY_OF_MONTH);
            pstmt.setString(3, anio + "-" + mes + "-" + dia);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registrarDevolucion(Prestamo prestamo) {
        String sql = "UPDATE Prestamo SET fecha_devolucion = ? WHERE dni_socio = ? AND titulo_libro = ? AND fecha_devolucion IS NULL";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int anio = prestamo.getFechaDevolucion().get(Calendar.YEAR);
            int mes = prestamo.getFechaDevolucion().get(Calendar.MONTH) + 1;
            int dia = prestamo.getFechaDevolucion().get(Calendar.DAY_OF_MONTH);
            pstmt.setString(1, anio + "-" + mes + "-" + dia);

            pstmt.setInt(2, prestamo.getSocio().getDniSocio());
            pstmt.setString(3, prestamo.getLibro().getTitulo());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Prestamo> obtenerTodos(ArrayList<Socio> socios, ArrayList<Libro> libros) {
        ArrayList<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT dni_socio, titulo_libro, fecha_retiro, fecha_devolucion FROM Prestamo";

        try (Connection conn = Conexion.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int dniSocio = rs.getInt("dni_socio");
                String tituloLibro = rs.getString("titulo_libro");
                String fechaRetStr = rs.getString("fecha_retiro");
                String fechaDevStr = rs.getString("fecha_devolucion");

                Socio sBD = null;
                for (Socio s : socios) {
                    if (s.getDniSocio() == dniSocio) { sBD = s; break; }
                }

                Libro lBD = null;
                for (Libro l : libros) {
                    if (l.getTitulo().equals(tituloLibro)) { lBD = l; break; }
                }

                if (sBD != null && lBD != null) {
                    String[] fRet = fechaRetStr.split("-");
                    Calendar calRetiro = new GregorianCalendar(Integer.parseInt(fRet[0]), Integer.parseInt(fRet[1]) - 1, Integer.parseInt(fRet[2]));

                    Prestamo p = new Prestamo(calRetiro, sBD, lBD);

                    if (fechaDevStr != null && !fechaDevStr.isEmpty()) {
                        String[] fDev = fechaDevStr.split("-");
                        Calendar calDev = new GregorianCalendar(Integer.parseInt(fDev[0]), Integer.parseInt(fDev[1]) - 1, Integer.parseInt(fDev[2]));
                        p.registrarFechaDevolucion(calDev);
                    }

                    sBD.agregarPrestamo(p);
                    lBD.agregarPrestamo(p);
                    lista.add(p);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
