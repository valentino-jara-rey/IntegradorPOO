import java.util.ArrayList;
/**
 * Esta clase represemta un Libro
 * @author Lautaro Hernan López
 */
public class Libro{
    private String titulo;
    private int edicion;
    private String editorial;
    private int anio;
    private ArrayList<Prestamo> prestamos;

    /**
     * Metodo constructor de la clase Libro
     * @param p_titulo
     * @param p_edicion
     * @param p_editorial
     * @param p_anio
     */
    public Libro(String p_titulo, int p_edicion, String p_editorial, int p_anio){
        this.setTitulo(p_titulo);
        this.setEdicion(p_edicion);
        this.setEditorial(p_editorial);
        this.setAnio(p_anio);
        this.setPrestamos(new ArrayList<Prestamo>());
    }

    /**
     * Metodo constructor sobrecargado de la clase Libro
     * @param p_titulo
     * @param p_edicion
     * @param p_editorial
     * @param p_anio
     * @param p_prestamos
     */
    public Libro(String p_titulo, int p_edicion, String p_editorial, int p_anio, ArrayList<Prestamo> p_prestamos){
        this.setTitulo(p_titulo);
        this.setEdicion(p_edicion);
        this.setEditorial(p_editorial);
        this.setAnio(p_anio);
        this.setPrestamos(p_prestamos);
    }

    /**
     * Setter del atributo titulo
     * @param p_titulo
     */
    private void setTitulo(String p_titulo){
        this.titulo = p_titulo;
    }

    /**
     * Setter del atributo edicion
     * @param p_edicion
     */
    private void setEdicion(int p_edicion){
        this.edicion = p_edicion;
    }

    /**
     * Setter del atributo editorial
     * @param p_editorial
     */
    private void setEditorial(String p_editorial){
        this.editorial = p_editorial;
    }

    /**
     * Setter del atributo anio
     * @param p_anio
     */
    private void setAnio(int p_anio){
        this.anio = p_anio;
    }

    /**
     * Setter del atributo prestamos
     * @param p_prestamos
     */
    private void setPrestamos(ArrayList<Prestamo> p_prestamos){
        this.prestamos = p_prestamos;
    }

    /**
     * Getter del atributo titulo
     * @return devuelve el valor del atributo titulo
     */
    public String getTitulo(){
        return  this.titulo;
    }

    /**
     * Getter del atributo edicion
     * @return devuelve el valor del atributo edicion
     */
    public int getEdicion(){
        return this.edicion;
    }

    /**
     * Getter del atributo editorial
     * @return devuelve el valor del atributo editorial
     */
    public String getEditorial(){
        return this.editorial;
    }

    /**
     * Getter del atributo anio
     * @return devuelve el valor del atributo anio
     */
    public int getAnio(){
        return this.anio;
    }

    /**
     * Getter del atributo prestamos
     * @return devuelve el valor del atributo prestamos
     */
    public ArrayList<Prestamo> getPrestamos(){
        return this.prestamos;
    }

    /**
     * Metodo que verifica si el libro esta prestado
     * @return devuelve un booleano dependiendo de si el libro esta prestado o no
     */
    public boolean prestado(){
        return this.ultimoPrestamo().getFechaDevolucion() == null;
    }

    /**
     * Metodo que devuelve un prestamo
     * @return devuelve el ultimo prestamo del libro
     */
    public Prestamo ultimoPrestamo(){
        return this.getPrestamos().get(this.getPrestamos().size() - 1);
    }

    /**
     * Metodo que formatea el titulo
     * @return devuelve el titulo formateado
     */
    public String toString(){
        return "Titulo: " + this.getTitulo();
    }
}
