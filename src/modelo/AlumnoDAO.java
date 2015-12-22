package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Alumno.Alumno;
import java.sql.PreparedStatement;

public class AlumnoDAO implements Persistable<Alumno> {

	@Override
	public List<Alumno> getAll() throws SQLException {
		// Se abre conexión
		DbConnection conn = new DbConnection();
		// Consulta
		String sql = "select * from persona order by id desc limit 500";
		PreparedStatement consulta = conn.getConnection().prepareStatement(sql);
		// ejecutar la consulta
		ResultSet res = consulta.executeQuery();

		// ArrayList de Alumnos
		ArrayList<Alumno> lista = new ArrayList<Alumno>();

		// iterar sobre los resultados
		while (res.next())
			lista.add(mapeo(res));
		res.close();
		consulta.close();
		conn.desconectar();
		return lista;
	}

	@Override
	public Alumno getById(int id) throws SQLException {
		// Se abre conexión
		DbConnection conn = new DbConnection();
		// nombre de la clase y ctrl + espacio
		String sql = "select * from persona where id = ?";
		PreparedStatement consulta = conn.getConnection().prepareStatement(sql);
		consulta.setInt(1, id);
		// ejecutar la consulta
		ResultSet res = consulta.executeQuery();

		// iterar sobre los resultados
		res.next();
		Alumno p = mapeo(res);

		res.close();
		consulta.close();
		conn.desconectar();
		return p;
	}

	@Override
	public boolean delete(int id) throws SQLException {
		DbConnection conn = new DbConnection();
		boolean resul = false;
		String sql = "delete from `persona` where `id` = ?;";
		PreparedStatement pst = conn.getConnection().prepareStatement(sql);
		pst.setInt(1, id);
		if (pst.executeUpdate() == 1)// Si solo afecta a una línea
			resul = true;
		return resul;
	}

	@Override
	public boolean update(Alumno persistable) throws SQLException {
		boolean resul = false;
		if (persistable != null) {
			DbConnection conn = new DbConnection();
			String sql = "update `persona` set dni = ?, observaciones = ?, email = ?, nombre = ? where `id` = ? ;";
			PreparedStatement pst = conn.getConnection().prepareStatement(sql);
			pst.setString(1, persistable.getDni());
			pst.setString(2, persistable.getRol());
			pst.setString(3, persistable.getEmail());
			pst.setString(4, persistable.getNombre());
			pst.setInt(5, persistable.getId());
			if (pst.executeUpdate() == 1)// Si solo afecta a una línea
				resul = true;
		}
		return resul;
	}

	@Override
	public int insert(Alumno persistable) {
		int i;
		// Se abre conexión
		DbConnection conn = new DbConnection();
		try {
			String sql = "INSERT INTO `persona` (`dni`, `pass`, `fecha_nacimiento`, `observaciones`, `email`, `nombre`) VALUES (?,?,?,?,?,?)";
			PreparedStatement pst = conn.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

			pst.setString(1, persistable.getDni());
			pst.setString(2, null);
			pst.setDate(3, null);
			pst.setString(4, persistable.getRol());
			pst.setString(5, persistable.getEmail());
			pst.setString(6, persistable.getNombre());
			// ejecutar la consulta. Si no afecta a una línea, lanzamos la
			// excepción
			if (pst.executeUpdate() != 1)
				throw new SQLException("No se ha insertado el dato");

			ResultSet generatedKeys = pst.getGeneratedKeys();
			generatedKeys.next();
			i = generatedKeys.getInt(1);
			persistable.setId(i);
			pst.close();

		} catch (Exception e) {
			i = -1;
			e.printStackTrace();
		}
		conn.desconectar();
		return i;
	}

	private Alumno mapeo(ResultSet res) throws SQLException {
		Alumno p = new Alumno();
		p.setId(res.getInt("id"));
		p.setNombre(res.getString("nombre"));
		p.setDni(res.getString("dni"));
		p.setRol(res.getString("observaciones"));
		p.setEmail(res.getString("email"));
		return p;
	}
}