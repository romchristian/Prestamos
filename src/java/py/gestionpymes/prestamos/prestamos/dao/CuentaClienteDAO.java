/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.dao;

import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gestionpymes.prestamos.adm.dao.AbstractFacade;
import py.gestionpymes.prestamos.prestamos.persistencia.Cliente;
import py.gestionpymes.prestamos.prestamos.persistencia.CuentaCliente;
import py.gestionpymes.prestamos.prestamos.persistencia.DetCuentaCliente;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.EstadoCuenta;

/**
 *
 * @author christian
 */
@Stateless
public class CuentaClienteDAO extends AbstractFacade<CuentaCliente> {

    @PersistenceContext(unitName = "PrestamosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CuentaClienteDAO() {
        super(CuentaCliente.class);
    }

    public CuentaCliente create(Cliente cliente) {
        CuentaCliente c = new CuentaCliente();
        c.setCliente(cliente);
        c.setDenominacion(cliente.getPrimerApellido() + ", " + cliente.getPrimerNombre());
        if (!cliente.getDirecciones().isEmpty() && cliente.getDirecciones().size() > 0) {
            c.setDireccion(cliente.getDirecciones().get(0).getDireccion());
        }
        c.setEstado(EstadoCuenta.ACTIVO);
        c.setFechaAlta(new Date());
        c.setNroCuenta(getSgteNumero());
        c.setTelefono("");
        create(c);
        return c;
    }

    public String getSgteNumero() {
        return (CuentaCliente.contador + 1) + "";
    }

    public CuentaCliente findPorCliente(Cliente c) {
        CuentaCliente R = null;
        try {

           R =  (CuentaCliente) em.createQuery("SELECT c FROM CuentaCliente c WHERE c.cliente = :cliente").setParameter("cliente", c).getSingleResult();
        } catch (Exception e) {
        }
        return R;
    }
    
    
    public List<DetCuentaCliente> findDetalles(CuentaCliente c){
        return em.createQuery("SELECT d FROM DetCuentaCliente d WHERE d.cuentaCliente = :cuenta").setParameter("cuenta", c).getResultList();
    }

}
