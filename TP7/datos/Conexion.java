package datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {
    
    /**
     * URL de conexion para la base de datos SQLite de la biblioteca
     */
    private static final String URL = "jdbc:sqlite:biblioteca.db";

    /**
     * Metodo que establece y devuelve una conexion con la base de datos SQLite
     * @return Connection objeto de conexion activo
     * @throws SQLException si ocurre algun error al conectar con la base de datos
     */
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /**
     * Metodo que inicializa la base de datos creando las tablas necesarias si no existen (Libro, Socio y Prestamo)
     */
    public static void inicializarBaseDeDatos() {
        String sqlLibros = "CREATE TABLE IF NOT EXISTS Libro (" +
                "id_libro INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT NOT NULL, " +
                "edicion INTEGER NOT NULL, " +
                "editorial TEXT NOT NULL, " +
                "anio INTEGER NOT NULL);";

        String sqlSocios = "CREATE TABLE IF NOT EXISTS Socio (" +
                "dni INTEGER PRIMARY KEY, " +
                "nombre TEXT NOT NULL, " +
                "tipo_socio TEXT NOT NULL, " +
                "especifico TEXT NOT NULL, " +
                "dias_prestamo INTEGER NOT NULL);";

        String sqlPrestamos = "CREATE TABLE IF NOT EXISTS Prestamo (" +
                "id_prestamo INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "dni_socio INTEGER NOT NULL, " +
                "titulo_libro TEXT NOT NULL, " +
                "fecha_retiro TEXT NOT NULL, " +
                "fecha_devolucion TEXT, " +
                "FOREIGN KEY (dni_socio) REFERENCES Socio(dni));";

        try (Connection conn = getConexion();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlLibros);
            stmt.execute(sqlSocios);
            stmt.execute(sqlPrestamos);
        } catch (SQLException e) {
            System.err.println("Error al inicializar BD: " + e.getMessage());
        }
    }
}
