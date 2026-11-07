/*
* Esta clase representa la abstraccion de un estudiante la cual extiende de la superclase socio
*@autor Axel Antonio Lezcano
*/

import java.util.ArrayList;

public class Estudiante extends Socio{
    private String carrera;
    
    /**
     * Metodo contructor de la clase Estudiante
     * @param p_dniSocio
     * @param p_nombre
     * @param p_diasPrestamos
     * @param p_carrera */
    public Estudiante(int p_dniSocio, String p_nombre, int p_diasPrestamos, String p_carrera){
        super(p_dniSocio, p_nombre, p_diasPrestamos);
        this.setCarrera(p_carrera);
    }

    /**
     * Metodo contructor sobrecargado de la clase Estudiante
     * @param p_dniSocio
     * @param p_nombre
     * @param p_diasPrestamos
     * @param p_prestamos
     * @param p_carrera
     */
    public Estudiante(int p_dniSocio, String p_nombre, int p_diasPrestamos, ArrayList<Prestamo> p_prestamos, String p_carrera){
        super(p_dniSocio, p_nombre, p_diasPrestamos, p_prestamos);
        this.setCarrera(p_carrera);
    } 

    /**
     * Setter del atributo carrera
     * @param p_carrera
     */
    private void setCarrera(String p_carrera){
        this.carrera = p_carrera;
    }

    /**
     * Getter del atributo carrera
     * @return devuelve el valor del atributo carrera
    */
    public String getCarrera(){
        return this.carrera;
    }

    /**
     * Metodo que verifica si el Estudiante puede pedir otro prestamo
     * @return devuelve true si puede pedir, caso contrario devuelve false
     */
    public boolean puedePedir(){
      if(this.getPrestamos().size() < 3){
        return super.puedePedir();
      } else {
        return false;
      }
    }

    /**
     * Metodo que devuelve el tipo de la clase
     * @return devuelve el tipo de la clase como string (Estudiante)
     */
    public String soyDeLaClase(){
        return "Estudiante";
    }

}
