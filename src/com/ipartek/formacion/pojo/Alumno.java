package com.ipartek.formacion.pojo;

import java.sql.Date;
import java.util.GregorianCalendar;

public class Alumno {
	
	private String nombre,	rol, dni, email;
	private int id;
	private Date fechaNacimiento;
	
	/**
	 * Se inicializan las variables para evitar NullPointerException
	 */
	public Alumno() {
		super();
		this.nombre = "";
		this.rol = "";
		this.dni = "";
		this.email = "";
		GregorianCalendar calendar = new GregorianCalendar(1900, 0, 1);
		this.fechaNacimiento = new Date(calendar.getTimeInMillis());
	}

	public Alumno(String nombre, String rol, String dni, String email, Date fechaNacimiento) {
		this();
		this.id = -1;
		this.nombre = nombre;
		this.rol = rol;
		this.dni = dni;
		this.email = email;
		this.fechaNacimiento = fechaNacimiento;
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
	
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	@Override
	public String toString() {
		return "Alumno [nombre=" + nombre + ", rol=" + rol + ", dni=" + dni + ", email=" + email + "]";
	}
}
