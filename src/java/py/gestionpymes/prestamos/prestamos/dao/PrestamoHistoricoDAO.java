/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.dao;

import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gestionpymes.prestamos.adm.dao.AbstractFacade;
import py.gestionpymes.prestamos.adm.persistencia.Empresa;
import py.gestionpymes.prestamos.adm.persistencia.Sucursal;
import py.gestionpymes.prestamos.prestamos.persistencia.Cliente;
import py.gestionpymes.prestamos.prestamos.persistencia.CuentaCliente;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.EstadoPrestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.OperacionDesembolsoPrestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.Prestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.PrestamoHistorico;

/**
 *
 * @author christian
 */
@Stateless

public class PrestamoHistoricoDAO extends AbstractFacade<py.gestionpymes.prestamos.prestamos.persistencia.PrestamoHistorico> {

    @PersistenceContext(unitName = "PrestamosPU")
    private EntityManager em;
    @EJB
    private DetCuentaClienteDAO detCuentaClienteDAO;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PrestamoHistoricoDAO() {
        super(PrestamoHistorico.class);
    }

    public List<PrestamoHistorico> findAll(Cliente cliente) {
        return em.createQuery("SELECT p from PrestamoHistorico p where p.cliente = :cliente").setParameter("cliente", cliente).getResultList();
    }

    public List<PrestamoHistorico> findAllClienteEstado(Cliente cliente, EstadoPrestamo estado) {
        return em.createQuery("SELECT p from PrestamoHistorico p where p.cliente = :cliente and p.estado = :estado")
                .setParameter("cliente", cliente)
                .setParameter("estado", estado)
                .getResultList();
    }

    public List<PrestamoHistorico> findAllPorEmpresaFechaEstado(Empresa e, Sucursal s, EstadoPrestamo estado, Date inicio, Date fin) {

        System.out.println("Empresa: " + e.getRazonSocial());
        System.out.println("Sucursal: " + s.getNombre());
        System.out.println("Estado: " + estado);
        System.out.println("Inicio: " + inicio);
        System.out.println("Fin: " + fin);

        return em.createQuery("SELECT p from PrestamoHistorico p where p.empresa = :emp and p.sucursal = :suc and p.fechaInicioOperacion BETWEEN :inicio and :fin and p.estado = :estado")
                .setParameter("emp", e)
                .setParameter("suc", s)
                .setParameter("inicio", inicio)
                .setParameter("fin", fin)
                .setParameter("estado", estado)
                .getResultList();
    }

    public List<PrestamoHistorico> findAllPorEmpresaFechaEstadoCliente(Empresa e, Sucursal s, EstadoPrestamo estado, Date inicio, Date fin, Cliente cliente) {

        System.out.println("Empresa: " + e.getRazonSocial());
        System.out.println("Sucursal: " + s.getNombre());
        System.out.println("Estado: " + estado);
        System.out.println("Inicio: " + inicio);
        System.out.println("Fin: " + fin);

        return em.createQuery("SELECT p from PrestamoHistorico p where p.empresa = :emp and p.sucursal = :suc and p.fechaInicioOperacion BETWEEN :inicio and :fin and p.estado = :estado and p.cliente = :cliente")
                .setParameter("emp", e)
                .setParameter("suc", s)
                .setParameter("inicio", inicio)
                .setParameter("fin", fin)
                .setParameter("estado", estado)
                .setParameter("cliente", cliente)
                .getResultList();
    }

    public List<PrestamoHistorico> findAllPorEmpresa(Empresa e, Sucursal s, EstadoPrestamo estado) {
        return em.createQuery("SELECT p from PrestamoHistorico p where p.empresa = :emp and p.sucursal = :suc")
                .setParameter("emp", e)
                .setParameter("suc", s)
                .getResultList();
    }

    public List<PrestamoHistorico> findAllEstado(EstadoPrestamo estado) {
        return em.createQuery("SELECT p from PrestamoHistorico p where  p.estado = :estado")
                .setParameter("estado", estado)
                .getResultList();
    }

    public PrestamoHistorico desembolsa(PrestamoHistorico prestamo) {

        OperacionDesembolsoPrestamo op = new OperacionDesembolsoPrestamo(prestamo);

        CuentaCliente cc = (CuentaCliente) em.createQuery("select c from CuentaCliente c where c.cliente = :cliente")
                .setParameter("cliente", prestamo.getCliente()).getSingleResult();

        Integer ultmoid = 0;
        try {
            ultmoid = (Integer) em.createQuery("SELECT MAX(r.id) FROM Recibo r").getSingleResult();
        } catch (Exception e) {
        }

        op.setCuentaCliente(cc);
        //HACER: La fecha del desembolso debe ser igual a la fecha del prestamo
        op.setFecha(prestamo.getFecha());

        detCuentaClienteDAO.create(op);

        prestamo.setEstado(EstadoPrestamo.VIGENTE);
        edit(prestamo);

        return prestamo;
    }
    
   
    public PrestamoHistorico guardar(PrestamoHistorico p){
        PrestamoHistorico R = em.merge(p);
        
        return R;
    }
}
