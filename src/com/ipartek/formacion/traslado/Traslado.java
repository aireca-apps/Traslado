package com.ipartek.formacion.traslado;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ipartek.formacion.modelo.*;
import com.ipartek.formacion.pojo.Alumno;

public class Traslado {
	
	private static final String RUTA = "C:\\desarrollo\\personas", 
			ARCHIVO = "\\personas.txt",
			INFORME = "\\informe.txt";
	private static File archivo, ruta, informe;
	private static FileReader frFichero;
	private static BufferedReader brFichero;
	private static DbConnection conn;
	private static PreparedStatement pst;
	private static int errores, insertados, i = -1;
	private static FileWriter fwFichero;
	private static PrintWriter pwFichero;
	private static ArrayList<String> errorAlumnos = new ArrayList<String>();

	public static void main(String[] args) {
		try {
			iniciarConexion();
			leer();
			crearInforme();
			desconectar();
		} catch (FileNotFoundException e) {
			System.err.println("No se ha encontrado el archivo de lectura " + archivo);
		} catch (IOException e) {
			System.err.println("Error con el archivo de lectura o escritura");
		} catch (SQLException e) {
			System.err.println("Error con SQL");
		}
		System.out.println("\nSe han creado " + insertados + " alumnos con " + errores + " errores");
	}

	/**
	 * Inicia la conexión con la base de datos y prapara la consulta
	 * @throws SQLException en caso de que ocurra algún error SQL
	 */
	private static void iniciarConexion() throws SQLException {
		conn = new DbConnection();
		String sql = "INSERT INTO `persona` (`dni`, `pass`, `fecha_nacimiento`, `observaciones`, `email`, `nombre`) VALUES (?,?,?,?,?,?)";
		pst = conn.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
	}

	/**
	 * Lee el archivo personas.txt facilitado, llama al método que inserta los datos en la bbdd, <br> 
	 * crea un ArrayList con las líneas que no se han insertado y contabiliza los alumnos insertados y los errores.
	 * @throws IOException en caso de haber algún problema con el archivo de lectura.
	 */
	private static void leer() throws IOException {
		//Se crean los Files que apuntan al archivo
		ruta = new File(RUTA);
		archivo = new File(ruta + ARCHIVO);
		//Se crea el flujo de datos de lectura
		frFichero = new FileReader(archivo);
		brFichero = new BufferedReader(frFichero);
		
		String linea = null;		
		
		//bucle de lectura
		while ((linea = brFichero.readLine()) != null) {
			try {
			String[] sAlumno = linea.split(",");
			if (sAlumno.length == 7) {				
					insert(new Alumno(sAlumno[0] + " " + sAlumno[1] + " " + sAlumno[2], sAlumno[6], sAlumno[5],
							sAlumno[4]));
					insertados++;				
			} else {
				errores++;
				errorAlumnos.add(linea);
				errorAlumnos.add("\tError: Faltan apartados");
			}
			} catch (Exception e) {
				errores++;
				errorAlumnos.add(linea);
				errorAlumnos.add("\tError: " + e.getMessage());
			}
			if (++i % 100 == 0 && i != 0)
				System.out.println(" " + i);
			System.out.print(".");
		}
	}

	/**
	 * Se inserta el alumno en la base de datos
	 * @param {@code Alumno}. El {@code Alumno} que se desea insertar en la bbdd
	 * @throws Exception
	 */
	public static void insert(Alumno persistable) throws Exception {
		if (persistable.getDni().length() != 9)
			throw new Exception("DNI incorrecto");
		if (!persistable.getEmail().contains("@") || persistable.getEmail().contains(" "))
			throw new Exception("Email incorrecto");
		pst.setString(1, persistable.getDni());
		pst.setString(2, null);
		pst.setDate(3, null);
		pst.setString(4, persistable.getRol());
		pst.setString(5, persistable.getEmail());
		pst.setString(6, persistable.getNombre());
		if (pst.executeUpdate() != 1)
			throw new SQLException("No se ha insertado el dato");
	}

	/**
	 * Genera el informe
	 * @throws IOException en caso de haber algún problema con el archivo de escritura.
	 */
	private static void crearInforme() throws IOException {
		informe = new File(ruta + INFORME);
		//Si hay un informe previo lo borra y crea uno nuevo
		if (informe.exists())
			informe.delete();
		informe.createNewFile();
		//Se crea el flujo de escritura
		fwFichero = new FileWriter(informe);
		pwFichero = new PrintWriter(fwFichero, true); // Autoflush: true
		//Se escribe en el informe
		pwFichero.println("Se han insertado " + insertados + " Alumnos, de un total de " 
						+ i + ".");
		pwFichero.println("");
		pwFichero.println("Los Alumnos no insertados han sido los siguientes:");
		pwFichero.println("");
		for (String alumno : errorAlumnos)
			pwFichero.println(alumno);
	}

	/**
	 * Cierra los flujos de datos y la conexión
	 * @throws SQLException en caso de que ocurra algún error SQL
	 * @throws IOException en caso de haber algún problema con el archivo de escritura o lectura.
	 */
	private static void desconectar() throws SQLException, IOException {
		// Se cierra el flujo de escritura
		pwFichero.close();
		fwFichero.close();
		// Se cierra el flujo de lectura
		brFichero.close();
		frFichero.close();
		//Se cierra la conexión con la bbdd
		pst.close();
		conn.desconectar();
	}
}