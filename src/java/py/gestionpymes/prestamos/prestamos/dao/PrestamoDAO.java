/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.dao;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gestionpymes.prestamos.adm.dao.AbstractFacade;
import py.gestionpymes.prestamos.prestamos.persistencia.Cliente;
import py.gestionpymes.prestamos.prestamos.persistencia.CuentaCliente;
import py.gestionpymes.prestamos.prestamos.persistencia.EstadoPrestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.OperacionDesembolsoPrestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.Prestamo;

/**
 *
 * @author christian
 */
@Stateless

public class PrestamoDAO extends AbstractFacade<py.gestionpymes.prestamos.prestamos.persistencia.Prestamo> {

    @PersistenceContext(unitName = "PrestamosPU")
    private EntityManager em;
    @EJB
    private DetCuentaClienteDAO detCuentaClienteDAO;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PrestamoDAO() {
        super(Prestamo.class);
    }

    public List<Prestamo> findAll(Cliente cliente) {
        return em.createQuery("SELECT p from Prestamo p where p.cliente = :cliente").setParameter("cliente", cliente).getResultList();
    }

    public Prestamo desembolsa(Prestamo prestamo) {

        OperacionDesembolsoPrestamo op = new OperacionDesembolsoPrestamo(prestamo);

        CuentaCliente cc = (CuentaCliente) em.createQuery("select c from CuentaCliente c where c.cliente = :cliente")
                .setParameter("cliente", prestamo.getCliente()).getSingleResult();

        op.setCuentaCliente(cc);
        //HACER: La fecha del desembolso debe ser igual a la fecha del prestamo
        op.setFecha(prestamo.getFecha());

        detCuentaClienteDAO.create(op);

        prestamo.setEstado(EstadoPrestamo.DESEMBOLSADO);
        edit(prestamo);

        return prestamo;
    }

}
