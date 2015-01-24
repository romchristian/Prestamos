/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.persistencia;

import py.gestionpymes.prestamos.prestamos.persistencia.enums.Calificacion;
import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author ACER
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Categoria.TODOS, query = "select c from Categoria c")})
public class Categoria implements Serializable {

    public static final String TODOS = "package py.gestionpymes.clientes.persistencia.Categoria.TODOS";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Calificacion calificacion;
    private float ddm;
    private float promAtrazo;
    private float maxAtrazo;
    private int cantidadDePagos;//debe ser por lo menos mayor a 4

    public Categoria() {
    }

    public Categoria(float ddm, float promAtrazo, float maxAtrazo, int cantidadDePagos) {
        this.ddm = ddm;
        this.promAtrazo = promAtrazo;
        this.maxAtrazo = maxAtrazo;
        this.cantidadDePagos = cantidadDePagos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calificacion getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Calificacion calificacion) {
        this.calificacion = calificacion;
    }

    public float getDdm() {
        return ddm;
    }

    public void setDdm(float ddm) {
        this.ddm = ddm;
    }

    public float getPromAtrazo() {
        return promAtrazo;
    }

    public void setPromAtrazo(float promAtrazo) {
        this.promAtrazo = promAtrazo;
    }

    public float getMaxAtrazo() {
        return maxAtrazo;
    }

    public void setMaxAtrazo(float maxAtrazo) {
        this.maxAtrazo = maxAtrazo;
    }

    public int getCantidadDePagos() {
        return cantidadDePagos;
    }

    public void setCantidadDePagos(int cantidadDePagos) {
        this.cantidadDePagos = cantidadDePagos;
    }

    public void calcularCategoria(int cantpagos, float ddm, float max, float prom) {

        if (cantpagos < 4) {
            this.calificacion = Calificacion.NUEVO;
        } else if (ddm >= 0 && ddm <= 5 && max <= 5 && prom >= 0 && prom <= 5) {
            this.calificacion = Calificacion.EXCELENTE;
        } else if (ddm >= 6 && ddm <= 10 && max <= 10 && prom >= 6 && prom <= 10) {
            this.calificacion = Calificacion.MUY_BUENO;
        } else if (ddm >= 11 && ddm <= 15 && max <= 15 && prom >= 11 && prom <= 15) {
            this.calificacion = Calificacion.BUENO;
        } else if (ddm >= 16 && ddm <= 20 && max <= 20 && prom >= 16 && prom <= 20) {
            this.calificacion = Calificacion.REGULAR;
        } else if (ddm < 20 && max < 20 && prom < 20) {
            this.calificacion = Calificacion.REGULAR;
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Categoria)) {
            return false;
        }
        Categoria other = (Categoria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return calificacion.toString();
    }
}
