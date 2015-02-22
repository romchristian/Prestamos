/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author ACER
 */
@Entity

public class DetPlanGastos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private PlanGastos planGastos;
    @Column(scale = 8, precision = 38)
    private BigDecimal tasa;
    private Integer plazo;
    @Column(scale = 8, precision = 38)
    private BigDecimal porcentanjeGastos;
    @Column(scale = 8, precision = 38)
    private BigDecimal porcentanjeComision;

    public DetPlanGastos() {
    }

    public DetPlanGastos(PlanGastos planGastos) {
        this.planGastos = planGastos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlanGastos getPlanGastos() {
        return planGastos;
    }

    public void setPlanGastos(PlanGastos planGastos) {
        this.planGastos = planGastos;
    }

    public BigDecimal getTasa() {
        return tasa;
    }

    public void setTasa(BigDecimal tasa) {
        this.tasa = tasa;
    }

    public Integer getPlazo() {
        return plazo;
    }

    public void setPlazo(Integer plazo) {
        this.plazo = plazo;
    }

    public BigDecimal getPorcentanjeGastos() {
        if (porcentanjeGastos != null) {
            porcentanjeGastos.setScale(8, RoundingMode.HALF_EVEN);
        }
        return porcentanjeGastos;
    }

    public void setPorcentanjeGastos(BigDecimal porcentanjeGastos) {
        this.porcentanjeGastos = porcentanjeGastos;
    }

    public BigDecimal getPorcentanjeComision() {
        if (porcentanjeComision != null) {
            porcentanjeComision.setScale(8, RoundingMode.HALF_EVEN);
        }
        return porcentanjeComision;
    }

    public void setPorcentanjeComision(BigDecimal porcentanjeComision) {
        this.porcentanjeComision = porcentanjeComision;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DetPlanGastos other = (DetPlanGastos) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DetPlanGastos{" + "id=" + id + '}';
    }

}
