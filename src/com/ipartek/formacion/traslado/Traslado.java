package com.ipartek.formacion.traslado;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import com.ipartek.formacion.modelo.*;
import com.ipartek.formacion.pojo.Alumno;

public class Traslado {

	private static final String RUTA = ".\\data", ARCHIVO = "\\personas.txt", INFORME = "\\informe.txt";
	private static File archivo, ruta, informe;
	private static FileReader frFichero;
	private static BufferedReader brFichero;
	private static DbConnection conn;
	private static PreparedStatement pst;
	private static int errores, insertados, i = -1;
	private static FileWriter fwFichero;
	private static PrintWriter pwFichero;
	private static ArrayList<String> errorAlumnos = new ArrayList<String>();
	public static final Pattern EMAIL_EXPRESION = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE),
			DNI_EXPRESION = Pattern.compile("^((([A-Z]|[a-z])\\d{8})|(\\d{8}([A-Z]|[a-z])))$");

	public static void main(String[] args) {
		iniciarConexion();
		leer();
		crearInforme();
		desconectar();
		System.out.println("\nSe han insertado " + insertados + " alumnos.\nSe han encontado " + errores
				+ " alumnos con errores que no han sido insertados." + "\nInforme creado en: " + RUTA + INFORME);
	}

	/**
	 * Inicia la conexión con la base de datos y prapara la consulta <br>
	 * En caso de no poder conectarse con la bbdd, lanza un excepción y finaliza
	 * la ejecución.
	 */
	private static void iniciarConexion() {
		try {
			conn = new DbConnection();
			String sql = "INSERT INTO `persona` (`dni`, `pass`, `fecha_nacimiento`, `observaciones`, `email`, `nombre`) VALUES (?,?,?,?,?,?)";
			pst = conn.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			System.err.println("No se ha podido establecer conexión con la bbdd");
			e.printStackTrace();
		}
	}

	/**
	 * Lee el archivo personas.txt facilitado, llama al método que inserta los
	 * datos en la bbdd, <br>
	 * crea un {@code ArrayList} con las líneas que no se han insertado y contabiliza
	 * los {@code Alumnos} insertados y los errores. <br>
	 * En caso de no poder localizar o leer el archivo de lectura, lanza un excepción y finaliza la
	 * ejecución.
	 */
	private static void leer() {
		try {
			// Se crean los Files que apuntan al archivo
			ruta = new File(RUTA);
			archivo = new File(ruta + ARCHIVO);
			// Se crea el flujo de datos de lectura
			frFichero = new FileReader(archivo);
			brFichero = new BufferedReader(frFichero);

			String linea = null;

			// bucle de lectura
			while ((linea = brFichero.readLine()) != null) {
				try {
					String[] sAlumno = linea.split(",");
					if (sAlumno.length == 7) {
						insert(createAlumno(sAlumno[0] + " " + sAlumno[1] + " " + sAlumno[2], sAlumno[6], sAlumno[5],
								sAlumno[4], sAlumno[3]));
						insertados++;
					} else {
						addError(linea, "Error: Faltan apartados");
					}
				} catch (Exception e) {
					addError(linea, e.getMessage());
				} finally {
					// Muestra el progreso de lectura en la pantala
					if (++i % 100 == 0 && i != 0)
						System.out.println(" " + i);
					System.out.print(".");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param linea {@code String} representa un alumno leído del fichero
	 * @param error {@code String} representa el error por el que no se ha generado el alumno
	 */	
	private static void addError(String linea, String error) {
		errorAlumnos.add(++errores + ") Alumno: " + linea + "\r\n\t" + error);
	}

	/**
	 * Genera los alumnos desde los String proporcionados
	 * 
	 * @param {@code nombreApelidos}. Nombre y apellidos del {@code Alumno}.
	 * @param {@code rol}. Rol del {@code Alumno}.
	 * @param {@code dni}. DNI del {@code Alumno}. En caso de no corresponderse
	 *            con un {@code dni}, lanza una excepción.
	 * @param {@code email}. Email del {@code Alumno}. En caso de no
	 *            corresponderse con un {@code email}, lanza una excepción.
	 * @param {@code edadAlumno}. Edad del {@code Alumno}. En caso de no poder
	 *            pasarlo a {@code int} o ser una edad incorrecta, lanza una
	 *            excepción.
	 * @return {@code Alumno}. Devuelve el {@code Alumno} en caso de que se haya
	 *         creado
	 * @throws Exception
	 */
	private static Alumno createAlumno(String nombreApelidos, String rol, String dni, String email, String edadAlumno)
			throws Exception {
		// Deetcta errores en el DNI
		if (!DNI_EXPRESION.matcher(dni).find())
			throw new Exception("DNI incorrecto");
		// Detecta errores en el email
		if (!EMAIL_EXPRESION.matcher(email).find())
			throw new Exception("Email incorrecto");

		// Se calcula la fecha de nacimiento, aun solo teniendo el dato del año.
		// Se pone un mes y un día genéricos
		int edad = 0;
		try {
			edad = Integer.parseInt(edadAlumno);
		} catch (Exception e) {
			throw new Exception("La edad no es correcta");
		}
		if (edad <= 0)
			throw new Exception("La edad no es correcta");
		Calendar cal = Calendar.getInstance();
		GregorianCalendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR) - edad, 0, 1);

		return new Alumno(nombreApelidos, rol, dni, email, new Date(calendar.getTimeInMillis()));
	}

	/**
	 * Se inserta el alumno en la base de datos
	 * 
	 * @param {@code Alumno}. El {@code Alumno} que se desea insertar en la bbdd
	 * @throws Exception
	 *             en caso de que no se haya insertado el dato.
	 */
	public static void insert(Alumno persistable) throws Exception {
		pst.setString(1, persistable.getDni());
		pst.setString(2, "123456");
		pst.setDate(3, persistable.getFechaNacimiento());
		pst.setString(4, persistable.getRol());
		pst.setString(5, persistable.getEmail());
		pst.setString(6, persistable.getNombre());
		if (pst.executeUpdate() != 1)
			throw new Exception("Error SQL: No se ha insertado el dato");
	}

	/**
	 * Genera el informe. <br>
	 * En caso de no poder crear el informe, lanza un excepción y finaliza la
	 * ejecución.
	 * 
	 */
	private static void crearInforme() {
		try {
			informe = new File(ruta + INFORME);
			// Si hay un informe previo lo borra y crea uno nuevo
			if (informe.exists())
				informe.delete();
			informe.createNewFile();
			// Se crea el flujo de escritura
			fwFichero = new FileWriter(informe);
			pwFichero = new PrintWriter(fwFichero, true); // Autoflush: true
			// Se escribe en el informe
			pwFichero.println("\nSe han insertado " + insertados + " alumnos.\nSe han encontado " + errores
					+ " alumnos con errores que no han sido insertados.\r\n"
					+ "\r\nLos Alumnos no insertados han sido los siguientes:\r\n");
			for (String errorAlumno : errorAlumnos)
				pwFichero.println(errorAlumno);
		} catch (IOException e) {
			System.err.println("No se ha generado el informe");
			e.printStackTrace();
		}
	}

	/**
	 * Cierra los flujos de datos y la conexión <br>
	 * En caso de no poder desconectar el flujo de datos o la conexión a la
	 * bbdd, lanza un excepción y finaliza la ejecución.
	 */
	private static void desconectar() {
		try {
			// Se cierra el flujo de escritura
			pwFichero.close();
			fwFichero.close();
			// Se cierra el flujo de lectura
			brFichero.close();
			frFichero.close();
			// Se cierra la conexión con la bbdd
			pst.close();
			conn.desconectar();
		} catch (Exception e) {
			System.err.println("No se ha podido cerrar los flujos de datos a la bbdd o con los ficheros");
			e.printStackTrace();
		}
	}
}