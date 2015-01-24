/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.persistencia;

import java.io.Serializable;
import javax.persistence.*;


/**
 *
 * @author christian
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Moneda.TODOS, query = "select m from Moneda m"),
    @NamedQuery(name = Moneda.POR_NOMBRE, query = "select m from Moneda m where m.nombre = :nombre"),
    @NamedQuery(name = Moneda.POR_ESTADO, query = "select m from Moneda m where m.estado = :estado")})
public class Moneda implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TODOS = "package py.gestionpymes.jpa.adm.Moneda.TODOS";
    public static final String POR_NOMBRE = "package py.gestionpymes.jpa.adm.Moneda.POR_NOMBRE";
    public static final String POR_ESTADO = "package py.gestionpymes.jpa.adm.Moneda.POR_ESTADO";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Version
    private Long version;
    private String nombre;
    private String abreviacion;
    private int decimales;
    private boolean monedaLocal;
    @Enumerated(EnumType.STRING)
    private Estado estado;

    public Moneda() {
        this.estado = Estado.ACTIVO;
    }

    public Moneda(String nombre, String abreviacion, int decimales, boolean monedaLocal) {
        this();
        this.nombre = nombre;
        this.abreviacion = abreviacion;
        this.decimales = decimales;
        this.monedaLocal = monedaLocal;
    }



    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAbreviacion() {
        return abreviacion;
    }

    public void setAbreviacion(String abreviacion) {
        this.abreviacion = abreviacion;
    }

    public int getDecimales() {
        return decimales;
    }

    public void setDecimales(int decimales) {
        this.decimales = decimales;
    }

    public boolean isMonedaLocal() {
        return monedaLocal;
    }

    public void setMonedaLocal(boolean monedaLocal) {
        this.monedaLocal = monedaLocal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        if (!(object instanceof Moneda)) {
            return false;
        }
        Moneda other = (Moneda) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
