package com.itesm.mx

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class MonedaController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Moneda.list(params), model:[monedaCount: Moneda.count()]
    }

    def show(Moneda moneda) {
        respond moneda
    }

    def create() {
        respond new Moneda(params)
    }

    @Transactional
    def save(Moneda moneda) {

        if (moneda == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (moneda.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond moneda.errors, view:'create'
            return
        }

        moneda.save flush:true
        new Cambios(moneda:moneda, valorAnterior:moneda.cantidad).save flush:true     

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'moneda.label', default: 'Moneda'), moneda.id])
                redirect moneda
            }
            '*' { respond moneda, [status: CREATED] }
        }
    }

    def edit(Moneda moneda) {
        respond moneda
    }

    @Transactional
    def update(Moneda moneda) {
        if (moneda == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (moneda.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond moneda.errors, view:'edit'
            return
        }

        moneda.save flush:true

        Cambios c = new Cambios(valorAnterior:moneda.cantidad,moneda:moneda).save flush:true
        //moneda.cambios.add(c)
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'moneda.label', default: 'Moneda'), moneda.id])
                redirect moneda
            }
            '*'{ respond moneda, [status: OK] }
        }
    }

    @Transactional
    def delete(Moneda moneda) {

        if (moneda == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        moneda.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'moneda.label', default: 'Moneda'), moneda.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'moneda.label', default: 'Moneda'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
