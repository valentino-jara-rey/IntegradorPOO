import java.time.Year;
import java.util.Calendar;
public class Prestamo {
    private Calendar fechaRetiro;
    private Calendar fechaDevolucion;
    private Socio socio;
    private Libro libro;

    /**
     * Metodo constructor de la clase Prestamo
     * @param p_libro
     * @param p_socio
     * @param p_fechaRetiro
     */
    public Prestamo (Calendar p_fechaRetiro, Socio p_socio, Libro p_libro){
        this.setFechaRetiro(p_fechaRetiro);
        this.setSocio(p_socio);
        this.setLibro(p_libro);
    }

    /**
     * Setter de del atributo fechaRetiro
     * @param p_fechaRetiro
     */
     private void setFechaRetiro(Calendar p_fechaRetiro) {
       this.fechaRetiro =  p_fechaRetiro;
    }

    /**
     * Setter de del atributo fechaDevolucion
     * @param p_fechaDevolucion
     */
     private void setFechaDevolucion(Calendar p_fechaDevolucion) {
        this.fechaDevolucion = p_fechaDevolucion;
    }
    
    /**
     * Setter de del atributo socio
     * @param p_socio
     */
    private void setSocio(Socio p_socio) {
        this.socio = p_socio;
    }

    /**
     * Setter de del atributo libro
     * @param p_libro
     */
     private void setLibro(Libro p_libro) {
        this.libro = p_libro;
    }

    /**
     * Getter del atributo socio
     * @return devuelve el valor del atributo socio
     */
    public Socio getSocio() {
        return socio;
    }

    /**
     * Getter del atributo libro
     * @return devuelve el valor del atributo libro
    */
    public Libro getLibro() {
        return libro;
    }

    /**
     * Getter del atributo fechaRetiro
     * @return devuelve el valor del atributo fechaRetiro
     */
    public Calendar getFechaRetiro(){
        return this.fechaRetiro;
    }

    /**
     * Getter del atributo fechaDevolucion
     * @return devuelve el valor del atributo fechaDevolucion
     */
    public Calendar getFechaDevolucion(){
        return this.fechaDevolucion;
    }

    /**
     * Anota la fecha acordada de devolucion
     * @param p_fecha
     */
    public void registrarFechaDevolucion(Calendar p_fecha){
        this.setFechaDevolucion(p_fecha);
    }

    /**
     * Verifica si esta vencida la fecha de devolucion
     * @param p_fecha
     * @return devuelve true si sí o false si no
     */
    public boolean vencido(Calendar p_fecha){
        // se suma los dias prestados a la fecha retiro
        Calendar fechaVencimiento = (Calendar) this.getFechaRetiro().clone();
        fechaVencimiento.add(Calendar.DATE, this.getSocio().getDiasPrestamo());
        return p_fecha.after(fechaVencimiento);
    }

    /**    
     * Concatena los atributos de Prestamo
     * @return devuelve un cadena con datos concatenados
     */
    public String toString(){
        String formatoFechaRetiro = this.getFechaRetiro().get(Calendar.YEAR) + "/" 
        + this.getFechaRetiro().get(Calendar.MONTH) + "/"
        + this.getFechaRetiro().get(Calendar.DATE);
        
        String formatoFechaDevolucion = this.getFechaDevolucion().get(Calendar.YEAR) + "/" 
        + this.getFechaDevolucion().get(Calendar.MONTH) + "/"
        + this.getFechaDevolucion().get(Calendar.DATE);

        return "Retiro: " + formatoFechaRetiro + " - Devolucion: " + formatoFechaDevolucion + "\n"
        + "Libro: " + this.getLibro().getTitulo() + "\n"
        + "Socio: " + this.getSocio().getNombre();
    }



}
