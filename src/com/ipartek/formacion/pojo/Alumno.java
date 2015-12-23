package com.ipartek.formacion.pojo;

public class Alumno {
	
	private String nombre,	rol, dni, email;
	private int id;
	
	/**
	 * Se inicializan las variables para evitar NullPointerException
	 */
	public Alumno() {
		super();
		this.nombre = "";
		this.rol = "";
		this.dni = "";
		this.email = "";
	}

	public Alumno(String nombre, String rol, String dni, String email) {
		this();
		this.id = -1;
		this.nombre = nombre;
		this.rol = rol;
		this.dni = dni;
		this.email = email;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Alumno [nombre=" + nombre + ", rol=" + rol + ", dni=" + dni + ", email=" + email + "]";
	}
}
