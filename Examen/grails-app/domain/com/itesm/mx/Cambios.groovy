package com.itesm.mx

class Cambios {

	String valorAnterior

	static belongsTo = [moneda: Moneda]
    static constraints = {
    }
    String toString(){
    	return moneda.pais
    }
}
