package it.polito.tdp.genes.model;

public class Adiacenza {
	private Integer c1;
	private Integer c2;
	private double peso;
	
	public Adiacenza(Integer c1, Integer c2, double peso) {
		super();
		this.c1 = c1;
		this.c2 = c2;
		this.peso = peso;
	}
	public Integer getC1() {
		return c1;
	}
	public void setC1(Integer c1) {
		this.c1 = c1;
	}
	public Integer getC2() {
		return c2;
	}
	public void setC2(Integer c2) {
		this.c2 = c2;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	
}
