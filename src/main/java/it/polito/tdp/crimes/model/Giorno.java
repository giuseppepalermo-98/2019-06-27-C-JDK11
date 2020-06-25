package it.polito.tdp.crimes.model;



public class Giorno {

	private Integer giorno;
	private Integer mese;
	private Integer anno;

	public Giorno(Integer giorno, Integer mese, Integer anno) {
		super();
		this.giorno = giorno;
		this.mese = mese;
		this.anno = anno;
	}

	

	public Integer getGiorno() {
		return giorno;
	}



	public void setGiorno(Integer giorno) {
		this.giorno = giorno;
	}



	public Integer getMese() {
		return mese;
	}



	public void setMese(Integer mese) {
		this.mese = mese;
	}



	public Integer getAnno() {
		return anno;
	}



	public void setAnno(Integer anno) {
		this.anno = anno;
	}



	@Override
	public String toString() {
		return giorno+"/"+mese+"/"+anno;
	}
	
	
}
