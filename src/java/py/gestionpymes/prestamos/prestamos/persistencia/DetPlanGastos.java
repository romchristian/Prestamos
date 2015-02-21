/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import py.gestionpymes.prestamos.adm.persistencia.Empresa;

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
    private PlanGastos  planGastos;
    private BigDecimal tasa;
    private Integer plazo;
    private BigDecimal porcentanjeGastos;
    private BigDecimal porcentanjeComision;
    
    
    

    public DetPlanGastos() {
    }
   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
}
