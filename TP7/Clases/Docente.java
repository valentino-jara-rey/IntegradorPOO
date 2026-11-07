import java.util.ArrayList;
import java.util.Calendar;

public class Docente extends Socio{
    private String area;

  //constructores
    /**
     * Metodo contructor de la clase Docente
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
     * Metodo contructor sobrecargado de la clase Docente
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

  //accesores
    private void setArea(String p_area){
        this.area = p_area;
    }

    public String getArea(){
        return this.area;
    }

  //metodos
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

    public void cambiarDiasDePrestamos(int p_dias){
        if(this.esResponsable()){
          this.setDiasPrestamos((this.getDiasPrestamo() + p_dias));
        }
    }

    public String soyDeLaClase(){
        return "Docente";
    }
}
