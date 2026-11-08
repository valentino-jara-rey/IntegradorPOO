package datos;

import modelo.Libro;
import java.sql.*;
import java.util.ArrayList;

/**
 * Clase DAO encargada de gestionar las operaciones de persistencia para la entidad Libro
 */
public class LibroDAO {

    /**
     * Metodo que inserta un nuevo registro de libro en la base de datos
     * @param libro el objeto Libro que se desea registrar
     */
    public void insertar(Libro libro) {
        String sql = "INSERT INTO Libro (titulo, edicion, editorial, anio) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, libro.getTitulo());
            pstmt.setInt(2, libro.getEdicion());
            pstmt.setString(3, libro.getEditorial());
            pstmt.setInt(4, libro.getAnio());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que obtiene una coleccion con todos los libros almacenados en la base de datos
     * @return un ArrayList con todos los objetos Libro encontrados
     */
    public ArrayList<Libro> obtenerTodos() {
        ArrayList<Libro> lista = new ArrayList<>();
        String sql = "SELECT titulo, edicion, editorial, anio FROM Libro";

        try (Connection conn = Conexion.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Libro libro = new Libro(
                        rs.getString("titulo"),
                        rs.getInt("edicion"),
                        rs.getString("editorial"),
                        rs.getInt("anio")
                );
                lista.add(libro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
