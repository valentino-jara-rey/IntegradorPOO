package datos;

import modelo.Docente;
import modelo.Estudiante;
import modelo.Socio;
import java.sql.*;
import java.util.ArrayList;

/**
 * Clase DAO (Data Access Object) encargada de gestionar el acceso y las operaciones CRUD
 * relacionadas con la entidad Socio en la base de datos SQLite.
 */
public class SocioDAO {

    /**
     * Inserta un nuevo socio (Estudiante o Docente) en la base de datos SQLite.
     * 
     * @param socio Objeto de tipo Socio a ser registrado en la base de datos.
     */
    public void insertar(Socio socio) {
        String sql = "INSERT INTO Socio (dni, nombre, tipo_socio, especifico, dias_prestamo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, socio.getDniSocio());
            pstmt.setString(2, socio.getNombre());
            pstmt.setString(3, socio.soyDeLaClase());

            String especifico = "";
            if (socio instanceof Estudiante) {
                especifico = ((Estudiante) socio).getCarrera();
            } else if (socio instanceof Docente) {
                especifico = ((Docente) socio).getArea();
            }

            pstmt.setString(4, especifico);
            pstmt.setInt(5, socio.getDiasPrestamo());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene y reconstruye la lista completa de socios registrados en la base de datos SQLite,
     * instanciándolos como objetos Estudiante o Docente según su tipo.
     * 
     * @return Lista de objetos de tipo Socio con todos los registros encontrados.
     */
    public ArrayList<Socio> obtenerTodos() {
        ArrayList<Socio> lista = new ArrayList<>();
        String sql = "SELECT dni, nombre, tipo_socio, especifico, dias_prestamo FROM Socio";

        try (Connection conn = Conexion.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int dni = rs.getInt("dni");
                String nombre = rs.getString("nombre");
                String tipo = rs.getString("tipo_socio");
                String especifico = rs.getString("especifico");
                int diasPrestamo = rs.getInt("dias_prestamo");

                if (tipo.contains("Estudiante")) {
                    lista.add(new Estudiante(dni, nombre, diasPrestamo, especifico));
                } else if (tipo.contains("Docente")) {
                    lista.add(new Docente(dni, nombre, diasPrestamo, especifico));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Actualiza la cantidad de días de préstamo otorgados a un docente en la base de datos SQLite.
     * 
     * @param docente Objeto de tipo Docente con los datos actualizados a persistir.
     */
    public void actualizarDiasPrestamoDocente(Docente docente) {
        String sql = "UPDATE Socio SET dias_prestamo = ? WHERE dni = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, docente.getDiasPrestamo());
            pstmt.setInt(2, docente.getDniSocio());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
