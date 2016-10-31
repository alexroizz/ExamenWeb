package com.itesm.mx

class Moneda {

	String pais
	Double cantidad
	String simbolo

	static fetchMode = [cambios: 'eager']

	static hasMany = [cambios: Cambios]

    static constraints = {
    }
    String toString(){
    	return pais +" "+ cantidad +" "+ simbolo
    }
}
