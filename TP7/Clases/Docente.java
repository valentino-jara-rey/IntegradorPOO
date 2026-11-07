import java.util.ArrayList;
import java.util.Calendar;

/** 
* clase que representa la abtraccion de un docente
* @autor Lezcano Axel Antonio 
*/

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
    /**
    * Metodo que verifica que un Docente sea responsable
    * @return esResp Devuelve un valor Booleano (True o false)
    */
    public boolean esResponsable(){
      boolean esResp = true;
      Calendar fechaHoy = Calendar.getInstance();
      for(Prestamo prestamo: this.getPrestamos()){
        if(prestamo.getFechaDevolucion() != null){
          // se asigna true si el prestamo no estaba vencido al momento de la devolucion
          esResp = !prestamo.vencido(prestamo.getFechaDevolucion());
        }else{
          // se asigna true si el prestamo no esta vencido a dia de hoy
          esResp = !prestamo.vencido(fechaHoy);
        }
      }
      return esResp;
    }

    /**
    * Metodo que agrega dias de prestamos si es responsable
    * @param p_dias
    */    
    public void cambiarDiasDePrestamos(int p_dias){
        if(this.esResponsable()){
          this.setDiasPrestamos((this.getDiasPrestamo() + p_dias));
        }
    }

    /**
    *
    */
    public String soyDeLaClase(){
        return "Docente";
    }
}
