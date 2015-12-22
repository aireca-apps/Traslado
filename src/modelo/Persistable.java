package modelo;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz para dar la habilidad de que sean Persistables los DAOs.<br>
 * Se encarga de que se implementen las operaciones más básicas de CRUD.
 * 
 * @param
 * 			<P>
 *            Clase genérica la que luego sustituímos por nuestra clase
 *            concreta, por ejemplo <code>Persona<code>
 * 
 * @author Curso
 * 
 *
 */
public interface Persistable<P> {

	/**
	 * Listado de todos los objetos de la consulta ordenado por id descendente,
	 * limitado a 500
	 * 
	 * @return {@code List<P>} si existen datos, en caso contrario
	 *         {@code List<P>} vacía inicializada
	 * @throws SQLException 
	 */
	List<P> getAll() throws SQLException;

	/**
	 * Busca un objeto en la tabla por su identificador
	 * 
	 * @param id
	 *            {@code int} identificador del objeto
	 * @return Objeto genérico, {@code null} si no existe
	 * @throws SQLException 
	 */
	P getById(int id) throws SQLException;

	/**
	 * Elimina objeto de la tabla por su id
	 * 
	 * @param id
	 *            {@code int} identificador del objeto
	 * @return true si elimina, flase en caso contrario
	 * @throws SQLException 
	 */
	boolean delete(int id) throws SQLException;

	/**
	 * Modifica el objeto que localiza por su id, por el objeto que se pasa como
	 * segundo argumento
	 * 
	 * @param id
	 *            {@code int} identificador del objeto
	 * @param persistable
	 *            {@code P} objeto con valores a modificar
	 * @return true si modifica, flase en caso contrario
	 * @throws SQLException 
	 */
	boolean update(P persistable) throws SQLException;

	/**
	 * Inserta un nuevo objeto
	 * 
	 * @param persistable
	 *            {@code P} objeto a insertar
	 * @return {@code int} identificador del objeto insertado, -1 en caso
	 *         contrario
	 * 
	 */
	int insert(P persistable);
}
