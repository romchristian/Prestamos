/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gestionpymes.prestamos.adm.dao.AbstractFacade;
import py.gestionpymes.prestamos.adm.persistencia.Empresa;
import py.gestionpymes.prestamos.adm.persistencia.Sucursal;
import py.gestionpymes.prestamos.contabilidad.persistencia.FacturaVenta;
import py.gestionpymes.prestamos.prestamos.persistencia.Cliente;
import py.gestionpymes.prestamos.prestamos.persistencia.CuentaCliente;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.EstadoPrestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.OperacionDesembolsoPrestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.Prestamo;
import py.gestionpymes.prestamos.tesoreria.dao.TransaccionDAO;
import py.gestionpymes.prestamos.tesoreria.persisitencia.Secuencia;
import py.gestionpymes.prestamos.tesoreria.persisitencia.SesionTPV;
import py.gestionpymes.prestamos.tesoreria.persisitencia.TipoTransaccion;
import py.gestionpymes.prestamos.tesoreria.persisitencia.TipoTransaccionCaja;
import py.gestionpymes.prestamos.tesoreria.persisitencia.Transaccion;
import py.gestionpymes.prestamos.tesoreria.persisitencia.TransaccionDesembolso;

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
    @EJB
    private TransaccionDAO transaccionDAO;

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

    public List<Prestamo> findAllClienteEstado(Cliente cliente, EstadoPrestamo estado) {
        return em.createQuery("SELECT p from Prestamo p where p.cliente = :cliente and p.estado = :estado")
                .setParameter("cliente", cliente)
                .setParameter("estado", estado)
                .getResultList();
    }

    public List<Prestamo> findAllPorEmpresaFechaEstado(Empresa e, Sucursal s, EstadoPrestamo estado, Date inicio, Date fin) {

        return em.createQuery("SELECT p from Prestamo p where p.empresa = :emp and p.sucursal = :suc and p.fechaInicioOperacion BETWEEN :inicio and :fin and p.estado = :estado")
                .setParameter("emp", e)
                .setParameter("suc", s)
                .setParameter("inicio", inicio)
                .setParameter("fin", fin)
                .setParameter("estado", estado)
                .getResultList();
    }

    public List<Prestamo> findAllPorEmpresaFechaEstadoCliente(Empresa e, Sucursal s, EstadoPrestamo estado, Date inicio, Date fin, Cliente cliente) {

        return em.createQuery("SELECT p from Prestamo p where p.empresa = :emp and p.sucursal = :suc and p.fechaInicioOperacion BETWEEN :inicio and :fin and p.estado = :estado and p.cliente = :cliente")
                .setParameter("emp", e)
                .setParameter("suc", s)
                .setParameter("inicio", inicio)
                .setParameter("fin", fin)
                .setParameter("estado", estado)
                .setParameter("cliente", cliente)
                .getResultList();
    }

    public List<Prestamo> findAllPorEmpresa(Empresa e, Sucursal s, EstadoPrestamo estado) {
        return em.createQuery("SELECT p from Prestamo p where p.empresa = :emp and p.sucursal = :suc")
                .setParameter("emp", e)
                .setParameter("suc", s)
                .getResultList();
    }

    public List<Prestamo> findAllEstado(EstadoPrestamo estado) {
        return em.createQuery("SELECT p from Prestamo p where  p.estado = :estado")
                .setParameter("estado", estado)
                .getResultList();
    }

    public Prestamo desembolsa(Prestamo prestamo) {

        OperacionDesembolsoPrestamo op = new OperacionDesembolsoPrestamo(prestamo);

        CuentaCliente cc = (CuentaCliente) em.createQuery("select c from CuentaCliente c where c.cliente = :cliente")
                .setParameter("cliente", prestamo.getCliente()).getSingleResult();

        Integer ultmoid = 0;
        try {
            ultmoid = (Integer) em.createQuery("SELECT MAX(r.id) FROM Recibo r").getSingleResult();
        } catch (Exception e) {
        }

        op.setCuentaCliente(cc);
        //HACER: La fecha del desembolso debe ser igual a la fechade inicio del prestamo
        op.setFecha(prestamo.getFechaInicioOperacion());

        detCuentaClienteDAO.create(op);

        prestamo.setEstado(EstadoPrestamo.VIGENTE);
        edit(prestamo);

        return prestamo;
    }

    public Prestamo desembolsa(Prestamo prestamo, FacturaVenta f, SesionTPV s) throws NumeroInvalidoException {

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
        op.setFecha(prestamo.getFechaInicioOperacion());

        detCuentaClienteDAO.create(op);

        prestamo.setEstado(EstadoPrestamo.VIGENTE);
        edit(prestamo);

        em.persist(f);
        
        Long numeroFactura = null;
        try {
            numeroFactura = Long.parseLong(f.getNumero());
        } catch (Exception e) {
            throw new NumeroInvalidoException("El numero de la factura es invalido");
        }
        
        
        Secuencia secuencia = s.getPuntoVenta().getSecuencia();
        secuencia.setUltimoNumero(numeroFactura);        
        em.merge(secuencia);
        
        TipoTransaccionCaja ttc = null;
        try {
            ttc = (TipoTransaccionCaja) em.createQuery("select t from TipoTransaccionCaja t where t.descripcion= ?1")
                    .setParameter(1, "DESEMBOLSO PRESTAMO").getSingleResult();
        } catch (Exception e) {
        }

        Transaccion t = new TransaccionDesembolso(f, prestamo, s,
                "Desembolso de  prestamo: Prestamo #" + prestamo.getId()+" - "+prestamo.getCliente().devuelveNombreCompleto(), prestamo.getMontoPrestamo(),
                f.getMoneda());

        if (ttc != null) {
            t.setTipoTransaccionCaja(ttc);
        }

        transaccionDAO.create(t);
        //em.persist(t);

        return prestamo;
    }

}
