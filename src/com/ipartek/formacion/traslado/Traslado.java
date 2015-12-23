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

	private static File archivo, ruta, informe;
	private static final String RUTA = "C:\\desarrollo\\personas", ARCHIVO = "\\personas.txt",
			INFORME = "\\informe.txt";
	private static FileReader frFichero;
	private static BufferedReader brFichero;
	private static DbConnection conn;
	private static PreparedStatement pst;
	private static int errores, insertados;
	private static FileWriter fwFichero;
	private static PrintWriter pwFichero;
	private static ArrayList<String> errorAlumnos = new ArrayList<String>();

	public static void main(String[] args) {
		try {
			iniciarConexion();
			leer();
			escribir();
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
		ruta = new File(RUTA);
		archivo = new File(ruta + ARCHIVO);
		String linea = null;
		frFichero = new FileReader(archivo);
		brFichero = new BufferedReader(frFichero);
		int i = -1;
		while ((linea = brFichero.readLine()) != null) {
			String[] sAlumno = linea.split(",");
			if (sAlumno.length == 7) {
				try {
					insert(new Alumno(sAlumno[0] + " " + sAlumno[1] + " " + sAlumno[2], sAlumno[6], sAlumno[5],
							sAlumno[4]));
					insertados++;
				} catch (Exception e) {
					errores++;
					errorAlumnos.add(linea);
				}
			} else {
				errores++;
				errorAlumnos.add(linea);
			}
			if (++i % 100 == 0)
				System.out.println("");
			System.out.print(".");
		}
	}

	/**
	 * Se inserta el alumno en la base de datos
	 * @param {@code Alumno}. El {@code Alumno} que se desea insertar en la bbdd
	 * @throws Exception
	 */
	public static void insert(Alumno persistable) throws Exception {
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
	private static void escribir() throws IOException {
		informe = new File(ruta + INFORME);
		if (!informe.exists())
			informe.createNewFile();
		fwFichero = new FileWriter(informe);
		pwFichero = new PrintWriter(fwFichero, true); // Autoflush: true
		pwFichero
				.println("Se han insertado " + insertados + " Alumnos, de un total de " + (errores + insertados) + ".");
		pwFichero.println("\nLos Alumnos no insertados han sido los siguientes:\n");
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