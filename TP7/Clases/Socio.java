import java.util.ArrayList;
/**
* Esta clase representa un Socio
* @author Valentino Jara Rey
*/
public abstract class Socio {
    private int dniSocio;
    private String nombre;
    private int diasPrestamo;
    private ArrayList<Prestamo> prestamos;

    /**
     * @param p_dniSocio
     * @param p_nombre
     * @param p_diasPrestamos
     * Metodo Constructor de la clase Socio
     */
    public Socio(int p_dniSocio, String p_nombre, int p_diasPrestamos){
      this.setDni(p_dniSocio);
      this.setNombre(p_nombre);
      this.setDiasPrestamos(p_diasPrestamos);
      this.setPrestamos(new ArrayList<Prestamo>());
    }
    
    /**
     * @param p_dniSocio
     * @param p_nombre
     * @param p_diasPrestamos
     * @param p_prestamos
     * Metodo Constructor sobrecargado de la clase socio
     */
    public Socio(int p_dniSocio, String p_nombre, int p_diasPrestamos, ArrayList<Prestamo> p_prestamos){
        this.setDni(p_dniSocio);
        this.setNombre(p_nombre);
        this.setDiasPrestamos(p_diasPrestamos);
        this.setPrestamos(p_prestamos);
    }

    /**
     * Metodo que permite agregar un prestamo a la vez a "prestamos"
     * @param p_prestamo
     */
    public void agregarPrestamo(Prestamo p_prestamo){
        this.getPrestamos().add(p_prestamo);
    }

    /**
     * Metodo que quita un prestamo de la prestamos
     * @param p_prestamo
     * @return devuelve verdadero si se quito un prestamo con exito, caso contrario falso
     */
    public boolean quitarPrestamo(Prestamo p_prestamo){
       return this.getPrestamos().remove(p_prestamo);
    }

    /**
     * Setter del atributo nombre
     * @param p_nombre
     */
    private void setNombre(String p_nombre) {
        this.nombre = p_nombre;
    }

    /**
     * Setter del atributo dniSocio
     * @param p_dniSocio
     */
    private void setDni(int p_dniSocio) {
        this.dniSocio = p_dniSocio;
    }

    /**
     * Setter del atributo diasPrestamo
     * @param p_dias
     */
    protected void setDiasPrestamos(int p_dias){
        this.diasPrestamo = p_dias;
    }

    /**
     * Setter del atributo prestamos
     * @param p_prestamos
     */
    private void setPrestamos(ArrayList<Prestamo> p_prestamos){
        this.prestamos = p_prestamos;
    }

    /**
     * Getter del atributo diasPrestamo
     * @return devuelve el atributo diasPrestamo
     */
    public int getDiasPrestamo() {
        return this.diasPrestamo;
    }

    /**
     * Getter del atributo dniSocio
     * @return devuelve el atributo dniSocio
     */
    public int getDniSocio() {
        return this.dniSocio;
    }

    /**
     * Getter del atributo prestamos
     * @return devuelve el atributo prestamos
     */
    public ArrayList<Prestamo> getPrestamos() {
        return this.prestamos;
    }

    /**
     * Getter del atributo nombre
     * @return devuelve el atributo nombre
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Metodo que calcula la cantidad
     * @return cantidad de libros prestados
     */
    public int cantLibrosPrestados(){
      return this.getPrestamos().size();
    }

    /**
     * Metodo que verifica si un socio puede pedir mas libros
     * @return devuelve false si hay almenos un prestamo vencido, caso contrario false
     */
    public boolean puedePedir(){
        boolean ningunoVencido = true;
        Calendar fechaHoy = Calendar.getInstance();
        for(Prestamo prestamo : this.getPrestamos()){
            if(prestamo.vencido(fechaHoy)){
                ningunoVencido = false;
            }
        }
        return ningunoVencido;
    }

    /**    
     * Metodo que concatena los datos comunes de un socio
     * @return devuelve una cadena de los datos generales de un socio
     */
    public String toString(){
        return "D.N.I.: " + this.getDniSocio() +  " || " + this.getNombre() + " || Libros prestados: " + this.cantLibrosPrestados();
    }

    public abstract String soyDeLaClase();
}
