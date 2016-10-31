package com.itesm.mx

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class CambiosController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Cambios.list(params), model:[cambiosCount: Cambios.count()]
    }

    def show(Cambios cambios) {
        respond cambios
    }

    def create() {
        respond new Cambios(params)
    }

    @Transactional
    def save(Cambios cambios) {
        if (cambios == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (cambios.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond cambios.errors, view:'create'
            return
        }

        cambios.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'cambios.label', default: 'Cambios'), cambios.id])
                redirect cambios
            }
            '*' { respond cambios, [status: CREATED] }
        }
    }

    def edit(Cambios cambios) {
        respond cambios
    }

    @Transactional
    def update(Cambios cambios) {
        if (cambios == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (cambios.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond cambios.errors, view:'edit'
            return
        }

        cambios.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'cambios.label', default: 'Cambios'), cambios.id])
                redirect cambios
            }
            '*'{ respond cambios, [status: OK] }
        }
    }

    @Transactional
    def delete(Cambios cambios) {

        if (cambios == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        cambios.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'cambios.label', default: 'Cambios'), cambios.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'cambios.label', default: 'Cambios'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
