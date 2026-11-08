<div align="center">

# 📚 Sistema de gestión de una biblioteca

## Trabajo Práctico Integrador - TP7 - Equipo (Jarascript)
<p align="center">
<img width="200" height="200" alt="biblioteca" src="https://github.com/user-attachments/assets/5dd98cd5-1f83-49d3-a669-99de07c95b97" />
</p>

</div>

## 👥 Integrantes
- Lezcano, Axel Antonio
- Ortiz, Santiago Tomas
- Jara Rey, Valentino
- López, Lautaro Hernan
- Martinez, Juan Esteban
- Isler Mendiaz, Magno Exequiel
---
### Índice 
- [Clases](TP7/Clases)
   - [Biblioteca](TP7/Clases/Biblioteca.java)
   - [Docente](TP7/Clases/Docente.java)
   - [Estudiante](TP7/Clases/Estudiante.java)
   - [Libro](TP7/Clases/Libro.java)
   -  [LibroNoPrestadoException](TP7/Clases/LibroNoPrestadoException.java)
   - [Prestamo](TP7/Clases/Prestamo.java)
   - [Socio](TP7/Clases/Socio.java)
- [Interfaces](TP7/Interfaz)
   - [Estilos](TP7/Interfaz/Estilos.java)
   - [GestionBibliotecaGUI](TP7/Interfaz/GestionBibliotecaGUI.java)
   - [Panel de Libros](TP7/Interfaz/PanelLibros.java)
   - [Panel de Prestamos](TP7/Interfaz/PanelPrestamos.java)
   - [Panel de Reportes](TP7/Interfaz/PanelReportes.java)
   - [Panel de Socios](TP7/Interfaz/PanelSocios.java)
- [Base de datos](TP7/datos)
   - [Conexión](TP7/datos/Conexion.java)
   - [LibroDAO](TP7/datos/LibroDAO.java)
   - [PrestamoDAO](TP7/datos/PrestamoDAO.java)
   - [SocioDAO](TP7/datos/SocioDAO.java)
---
### Link de trello
- https://trello.com/b/aqqgW54h/trello-integrador-poo

## Clase Biblioteca
- prestarLibro(`Calendar`, `Socio`, `Libro`): Crea el préstamo, y lo agrega en el libro y el socio.
- devolverLibro(`Libro`): Asigna la fecha de devolución del préstamo con la fecha actual. Si el libro no se
encuentra prestado lanzar una excepción de tipo LibroNoPrestadoException con el mensaje descriptivo “El
libro <<`titulo`>> no se puede devolver ya que se encuentra en la biblioteca”
- cantidadDeSociosPorTipo(`String`): devuelve la cantidad de socios del tipo pasado como parámetro.
- prestamosVencidos(): devuelve una colección con los préstamos vencidos al día de la fecha.
- docentesResponsables(): devuelve un colección con los docentes responsables.
- quienTieneElLibro(`Libro`): devuelve el nombre del Socio que tiene el libro con el título ingresado, y si no se
encuentra prestado lanza una excepción de tipo LibroNoPrestadoException con el mensaje descriptivo “El
libro se encuentra en la biblioteca”.
- buscarSocio(`int`): devuelve el Socio que tiene el dni pasado como parámetro, o null si no lo encuentra.
- listaDeSocios(): devuelve un String según formato1.
- listaDeLibros(): devuelve un String según formato2.
- listaDeTitulos(): devuelve un String con la lista de los títulos con los que cuenta la Biblioteca.
- listaDeDocentesResponsables(): devuelve un String según formato3.

## Clase Libro
- ultimoPrestamo(): retorna el último préstamo del libro.
- prestado(): devuelve true si el libro se encuentra prestado.
- toString(): devuelve el siguiente String: Titulo: <<`titulo`>>
 Ejemplo: Titulo: `JAVA. Como Programar`

## Clase Socio
- cantLibrosPrestados(): devuelve la cantidad de libros en poder del Socio.
- toString(): devuelve el siguiente String:
D.N.I.: <<`dni`>> || <<`nombre y apellido`>> (`[Docente|Estudiante]`)
Ejemplo:
D.N.I.: `14524782` || `Juan Perez (Docente)` || Libros Prestados: `6`
- puedePedir(): devuelve true si el socio no tiene ningún préstamo vencido.
## Clase Docente
- esResponsable(): devuelve true si el Docente nunca tuvo ni tiene un préstamo vencido.
- soyDeLaClase(): devuelve el String “`Docente`”.
- cambiarDiasDePrestamos(`int`): adiciona días de préstamo al docente. Es un premio a la responsabilidad.
## Clase Estudiante
- soyDeLaClase(): devuelve el String “`Estudiante`”.
- puedePedir(): devuelve true si el Estudiante no tiene ningún préstamo vencido y si no tiene más de 3 libros
prestados (en su poder).
## Clase Prestamo
- vencido(`Calendar`): devuelve true si la fecha pasada como parámetro es mayor que la fecha de vencimiento
(fecha de retiro más los días de préstamos asignados).
- toString(): devuelve el siguiente String:
Retiro: <<`fecha de retiro`>> - Devolución: <<`fecha de devolución`>>
 Libro: <<`título del libro`>>
 Socio: <<`nombre del socio`>>
Ejemplo:
Retiro: `2014/10/15` - Devolución: `2014/10/28`
Libro: `JAVA. Como Programar`
Socio: `Juan Perez`
## Formatos de listados
### Formato1: Listado de Socios
### Lista de Socios:
1) D.N.I.: <<`dni`>> || <<`nombre y apellido`>> (<<`tipo`>>) || Libros Prestados: <<`cant. prést. actuales` >>
2) D.N.I.: <<`dni`>> || <<`nombre y apellido`>> (<<`tipo`>>) || Libros Prestados: <<`cant. prést. actuales` >>
3) D.N.I.: <<`dni`>> || <<`nombre y apellido`>> (<<`tipo`>>) || Libros Prestados: <<`cant. prést. actuales` >>
### Ejemplo:
### Lista de Socios:
1) D.N.I.: `14524782` || `Juan Perez (Docente)` || Libros Prestados: `6`
2) D.N.I.: `17982110` || `Juan Fernández (Docente)` || Libros Prestados: `1`
3) D.N.I.: `10912002` || `María Alegre (Docente)` || Libros Prestados: `0`
4) D.N.I.: `28987498` || `Francisco Paenza (Estudiante)` || Libros Prestados: `2`
5) D.N.I.: `31987123` || `Cesar Milstein (Estudiante)` || Libros Prestados: `3`
**************************************
Cantidad de Socios del tipo Estudiante: 2  
Cantidad de Socios del tipo Docente: 3
**************************************
### Formato2: Listado de Libros
### Lista de Libros:
1) Titulo: <<`titulo`>> || Prestado: (<<`Si|No`>>)
2) Titulo: <<`titulo`>> || Prestado: (<<`Si|No`>>)
3) Titulo: <<`titulo`>> || Prestado: (<<`Si|No`>>)
### Ejemplo:
### Lista de Libros:
1) Titulo: `JAVA. Como Programar` || Prestado: `(No)`
2) Titulo: `Longman. Diccionario Pocket` || Prestado: `(Si)`
3) Titulo: `Vivir para contarla` || Prestado: `(No)`
---
### Formato3: Listado de Docentes Responsables
### Lista de Socios:
* D.N.I.: <<`dni`>> || <<`nombre y apellido`>> (<<`tipo`>>) || Libros Prestados: <<`cant. prést. actuales` >>
* D.N.I.: <<`dni`>> || <<`nombre y apellido`>> (<<`tipo`>>) || Libros Prestados: <<`cant. prést. actuales` >>
### Ejemplo:
### Lista de Docentes Responsables:
* D.N.I.: `14524782` || `Juan Perez (Docente)` || Libros Prestados: `6`  
* D.N.I.: `17982110` || `Juan Fernández (Docente)` || Libros Prestados: `1`
---
# Se solicita:
1. Implementar en java todas las clases del diseño provisto
2. Declarar la clase GestionBiblioteca, donde se deben instanciar objetos de las clases del diseño y
obtener los listados descriptos.
Usar los métodos apropiados para responder a las siguientes preguntas:
- ¿Qué cantidad de socios de tipo Estudiante hay?
- ¿Cuál es la lista de docentes que nunca han adeudado ni adeudan libros?
- ¿Cuál es la lista de libros? ¿Y la de socios?
- ¿Qué socio tiene prestado el libro “Programando con JAVA”?
NOTA:
- En todos los casos se deberá usar colecciones genéricas
- Todas las clases deben estar dentro del paquete “biblioteca”.
## 📂Estructura del repositorio

```
IntegradorPOO/
├── README.md
└── TP7/
   ├── Clases/   → Definición de clases
   ├── Interfaz/   → Intefaces gráficas
   └── datos/   → Implementación y conexión con una base de datos


