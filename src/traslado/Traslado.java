package traslado;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import Alumno.Alumno;
import modelo.*;

public class Traslado {

	private static File archivo, ruta;
	private static final String RUTA = "C:\\desarrollo\\personas", ARCHIVO = "\\personas.txt";
	private static FileReader frFichero;
	private static BufferedReader brFichero;
	private static DbConnection db;
	private static Connection conn;
	private static AlumnoDAO dao;

	public static void main(String[] args) {
		ruta = new File(RUTA);
		archivo = new File(ruta + ARCHIVO);
		//sdb = new DbConnection();		
		//conn.setAutoCommit(false);
		dao = new AlumnoDAO();
		ArrayList<Alumno> arrayAlumnos = new ArrayList<Alumno>();
		int errores = 0;
		if (ruta.exists() || archivo.exists()) {
			try {
				frFichero = new FileReader(archivo);
				brFichero = new BufferedReader(frFichero);
				// Bucle de lectura
				String linea = null;
				
				Alumno alumno = null;
				
				while ((linea = brFichero.readLine()) != null) {
					String[] sAlumno = linea.split(",");
							if ( sAlumno.length == 7){
						alumno = new Alumno(sAlumno[0] + " " + sAlumno[1] + " " + sAlumno[2], sAlumno[6], sAlumno[5], sAlumno[4]);
						arrayAlumnos.add(alumno);
					}else
						errores++;
				}
				System.out.println("Se han creado " + arrayAlumnos.size() + " alumnos con " + errores + " errores");
				insertarAlumnos(arrayAlumnos);
			} catch (Exception e) {
				errores++;
			}
						
			try {
				conn.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			db = null;
			dao = null;
		}

	}

	private static void insertarAlumnos(ArrayList<Alumno> arrayAlumnos) {
		for (Alumno alum : arrayAlumnos){
			//conn = db.getConnection();
			dao.insert(alum);
			//db.desconectar();
		}
		
	}
}
