package modelo;

import java.util.ArrayList;
import java.util.Calendar;

/** 
* clase que representa la abtraccion de un docente
* @autor Lezcano Axel Antonio 
*/
public class Docente extends Socio{
    private String area;

    //metodos
    /**
     * Metodo contructor de la clase modelo.Docente
     * @param p_dniSocio
     * @param p_nombre
     * @param p_diasPrestamos
     * @param p_area
     * */
    public Docente(int p_dniSocio, String p_nombre, int p_diasPrestamos, String p_area){
        super(p_dniSocio, p_nombre, p_diasPrestamos);
        this.setArea(p_area);
    }

    /**
     * Metodo contructor sobrecargado de la clase modelo.Docente
     * @param p_dniSocio
     * @param p_nombre
     * @param p_diasPrestamos
     * @param p_prestamos
     * @param p_area
     */
    public Docente(int p_dniSocio, String p_nombre, int p_diasPrestamos, ArrayList<Prestamo> p_prestamos, String p_area){
        super(p_dniSocio, p_nombre, p_diasPrestamos, p_prestamos);
        this.setArea(p_area);
    }

    //accesors
    private void setArea(String p_area){
        this.area = p_area;
    }

    public String getArea(){
        return this.area;
    }

    //metodos
    /**
    * Cuenta la cantidad de libros prestados
    * @return librosPrestados
    */
    @Override
    public int cantLibrosPrestados(){
        int librosPrestados = 0;
        for(Prestamo prestamo: this.getPrestamos()){
            if(prestamo.getFechaDevolucion() == null){
                librosPrestados++;
            }
        }
        return librosPrestados;
    }

    /**
    * Metodo que pregunta si es responsable un docente
    * @return esResponsable
    */
    public boolean esResponsable(){
        boolean esResponsable = true;
        Calendar fechaHoy = Calendar.getInstance();
        for(Prestamo prestamo: this.getPrestamos()){
            if(prestamo.getFechaDevolucion() != null){
                // se asigna true si el prestamo no estaba vencido al momento de la devolucion
                esResponsable = !prestamo.vencido(prestamo.getFechaDevolucion());
            }else{
                // se asigna true si el prestamo no esta vencido a dia de hoy
                esResponsable = !prestamo.vencido(fechaHoy);
            }
        }
        return esResponsable;
    }

    /**
    * Metodo encargado de adicionar dias a los dias de prestamo
    * @param p_dias
    */
    public void cambiarDiasDePrestamos(int p_dias){
        if(this.esResponsable()){
            this.setDiasPrestamos((this.getDiasPrestamo() + p_dias));
        }
    }

    /**
    * Pregunta si es que puede Pedir
    * @return puedePedir
    */
    @Override
    public boolean puedePedir(){
        boolean puedePedir = true;
        Calendar fechaHoy = Calendar.getInstance();
        for(Prestamo prestamo: this.getPrestamos()){
            //El prestamo no fue devuelto aun
            boolean esPrestamoActual = prestamo.getFechaDevolucion() == null;
            //el prestamo esta vencido
            boolean esPrestamoVencido = prestamo.vencido(fechaHoy);
            if(esPrestamoActual && esPrestamoVencido){
                puedePedir = false;
            }
        }

        return puedePedir;
    }

    /**
    * Devuelve el nombre de su clase
    * @return "modelo.Docente"
    */
    public String soyDeLaClase(){
        return "modelo.Docente";
    }
}
