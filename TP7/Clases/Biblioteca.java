import java.util.ArrayList;
import java.util.Calendar;

/**
 * Esta clase representa una biblioteca
 * @author Santiago Tomas Ortiz
 * @author Juan Esteban Martinez
 * @author Magno Ezequiel Isler Mendiaz
 */
public class Biblioteca{
    private String nombre;
    private ArrayList<Libro> libros;
    private ArrayList<Socio> socios;

    /**
     * Metodo Constructor de la clase Biblioteca
     * @param p_nombre
     */
    public Biblioteca(String p_nombre){
        this.setNombre(p_nombre);
        this.setLibros(new ArrayList<Libro>());
        this.setSocios(new ArrayList<Socio>());6
    }

    /**
     * Metodo Constructor Sobrecargado de la clase Biblioteca
     * @param p_nombre
     * @param p_libro
     * @param p_socios
     */
    public Biblioteca(String p_nombre, ArrayList<Libro> p_libro, ArrayList<Socio> p_socios){
        this.setNombre(p_nombre);
        this.setLibros(p_libro);
        this.setSocios(p_socios);
    }

    /**
     * Setter del atributo nombre
     * @param p_nombre
     */
    private void setNombre(String p_nombre){
        this.nombre = p_nombre;
    }

    /**
     * Setter del atributo libros
     * @param p_libros
     */
    private void setLibros(ArrayList<Libro> p_libros){
        this.libros = p_libros;
    }

    /**
     * Setter del atributo socios
     * @param p_socios
     */
    private void setSocios(ArrayList<Socio> p_socios){
        this.socios = p_socios;
    }


    /**
     * Getter del atributo nombre
     * @return devuelve el valor del atributo nombre
     */
    public String getNombre(){
        return this.nombre;
    }
    
    /**
     * Getter del atributo libros
     * @return devuelve el valor del atributo libros
     */
    public ArrayList<Libro> getLibros(){
        return this.libros;
    }

    /**
     * Getter del atributo socios
     * @return devuelve el valor del atributo socios
     */
    public ArrayList<Socio> getSocios(){
        return this.socios;
    } 

    /**
     * Metodo que agrega un libro en libros
     * @param p_libro
     */
    public void agregarLibro(Libro p_libro){
        this.getLibros().add(p_libro);
    }

    /**
     * Metodo que quita un libro de libros
     * @param p_libro
     * @return devuelve un true si quita el libro, sino false
     */
    public boolean quitarLibro(Libro p_libro){
        return this.getLibros().remove(p_libro);
    }

    /**
     * Metodo que agrega un socio en socios
     * @param p_socio
     */
    public void agregarSocio(Socio p_socio){
        this.getSocios().add(p_socio);
    }

    /**
     * Metodo que quita un socio de socios
     * @param p_socio
     * @return devuelve true si quita el socio, sino false
     */
    public boolean quitarSocio(Socio p_socio){
        return this.getSocios().remove(p_socio);
    }
    
    /**
     * Metodo que instancia y agrega un nuevo Libro dentro de libros
     * @param p_titulo
     * @param p_edicion
     * @param p_editorial
     * @param p_anio
     */
    public void nuevoLibro(String p_titulo, int p_edicion, String p_editorial, int p_anio){
        Libro libro = new Libro(p_titulo, p_edicion, p_editorial, p_anio);
        this.agregarLibro(libro);
    }

    /**
     * Metodo que instancia y agrega un nuevo Estudiante en socios
     * @param p_dniSocio
     * @param p_nombre
     * @param p_carrera
     */
    public void nuevoSocioEstudiante(int p_dniSocio, String p_nombre, String p_carrera){
        int diasPrestamos = 20;
        Socio estudiante = new Estudiante(p_dniSocio, p_nombre, diasPrestamos, p_carrera);
        this.agregarSocio(estudiante);
    }
    
    /**
     * Metodo que instancia y agrega un nuevoDocente en socios
     * @param p_dniSocio
     * @param p_nombre
     * @param p_area
     */
    public void nuevoSocioDocente(int p_dniSocio, String p_nombre, String p_area){
        int diasPrestamos = 5;
        Socio docente = new Docente(p_dniSocio, p_nombre, diasPrestamos , p_area);
        this.agregarSocio(docente);
    }

    public boolean prestarLibro(Calendar p_fechaRetiro, Socio p_socio, Libro p_libro){
      Prestamo prestamo = Prestamo(p_fechaRetiro, p_socio, p_libro);
      p_socio.agregarPrestamo(prestamo);
      p_libro.agregarPrestamo(prestamo);
      return true;
    }

    /**
     * Metodo que regresa un libro a la biblioteca y registra su fecha de devolucion
     * @param p_libro
     */
    public void devolverLibro(Libro p_libro){
     try{  
        Calendar fechaHoy = Calendar.getInstance();

         if(p_libro.ultimoPrestamo().getFechaDevolucion() != null){
           throw new LibroNoPrestadoException("El libro no se encuentra prestado");
         }

         p_libro.ultimoPrestamo().registrarFechaDevolucion(fechaHoy);

       } catch (LibroNoPrestadoException e){
          System.out.println("El libro " + p_libro.getTitulo() + " no se puede devolver ya que se encuentra en la biblioteca");
       }
    }
    
    /**
     * Cuenta la cantidad de socios del mismo tipo que su parametro
     * @param p_objeto
     * @return devuelve el total de socios de un determinado tipo
     */
    public int cantidadDeSociosPorTipo(String p_objeto){
        int totalPorTipo = 0;
        for(Socio socio : this.getSocios()){
            if(socio.soyDeLaClase().equalsIgnoreCase(p_objeto)){
                totalPorTipo++;
            }
        }
        return totalPorTipo;
    }

    /**
     * Crea una coleccion con los prestamos vencidos
     * @return devuelve los prestamos vencidos
     */
    public ArrayList<Prestamo> prestamosVencidos(){
        Calendar fechaHoy = Calendar.getInstance();
        ArrayList<Prestamo> prestamosVencidos = new ArrayList<Prestamo>();
        for(Libro libro : this.getLibros()){
            for(Prestamo prestamo : libro.getPrestamos()){
                if(prestamo.vencido(fechaHoy)){
                    prestamosVencidos.add(prestamo);
                }
            }
        }
        return prestamosVencidos;
    }

    /**
     * Metodo que crea un ArrayList con los docentes responables
     * @return ArrayList con los docente responsables
     */
    public ArrayList<Docente> docentesResponsables(){
    ArrayList<Docente> docentesResp = new ArrayList<Docente>();
    for(Socio socio : this.getSocios()){
        if(socio.soyDeLaClase().equalsIgnoreCase("Docente")){
            Docente docente = (Docente) socio;
            if(docente.esResponsable()){
                docentesResp.add(docente);
            }
        }
    }
    return docentesResp;
    }

  /**
   * Busca cual socio tiene el libro que llega por parametros
   * @param p_libro
   * @return devuelve el nombre del socio quien tiene el libro, sino lanza una excepcion
   */  
  public String quienTieneElLibro(Libro p_libro){
    try{
        if(!p_libro.prestado()){
            throw new LibroNoPrestadoException("El libro se encuentra en la biblioteca");
        }
        return p_libro.ultimoPrestamo().getSocio().getNombre();
    } 
    catch (LibroNoPrestadoException e){
        System.out.print(e.getMessage());
        return "";
    }
  }
/**
     * Metodo que devuelve un String con la informacion de todos los socios
     * @return String con el listado de socios
     */
    public String listaDeSocios() {
        String infoSocios = "";
        for (Socio socio : this.getSocios()) {
            infoSocios += socio.toString() + "\n";
        }
        return infoSocios;
    }

    /**
     * Metodo que busca y devuelve un socio segun su DNI
     * @param p_dni
     * @return el objeto Socio encontrado o null
     */
    public Socio buscarSocio(int p_dni) {
        Socio socioBuscado = null;
        for (Socio socio : this.getSocios()) {
            if (socio.getDniSocio() == p_dni) {
                socioBuscado = socio;
            }
        }
        return socioBuscado;
    }

    /**
     * Metodo que devuelve un String con los titulos o informacion de los libros
     * @return String con la lista de titulos
     */
    public String listaDeTitulos() {
        String titulos = "";
        for (Libro libro : this.getLibros()) {
            titulos += libro.toString() + "\n";
        }
        return titulos;
    }

    /**
     * Metodo que devuelve un String con el estado de prestamo de cada libro
     * @return String con el listado de libros y su estado
     */
    public String listaDeLibros() {
        String libros = "";
        for (Libro libro : this.getLibros()) {
            if (libro.prestado()) {
                libros += libro.toString() + " || Prestado: Si\n";
            } else {
                libros += libro.toString() + " || Prestado: No\n";
            }
        }
        return libros;
    }

    /**
     * Metodo que devuelve un String con la informacion de los docentes responsables
     * @return String con el listado de docentes responsables
     */
    public String listaDeDocentesResponsables() {
        String docentesResponsables = "";
        for (Docente docente : this.docentesResponsables()) {
            docentesResponsables += docente.toString() + "\n";
        }
        return docentesResponsables;
    }
