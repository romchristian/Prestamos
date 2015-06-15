/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gestionpymes.prestamos.adm.dao.ABMService;
import py.gestionpymes.prestamos.adm.dao.AbstractFacade;
import py.gestionpymes.prestamos.adm.modelo.Empresa;
import py.gestionpymes.prestamos.adm.modelo.Sucursal;
import py.gestionpymes.prestamos.contabilidad.modelo.FacturaVenta;
import py.gestionpymes.prestamos.prestamos.modelo.Cliente;
import py.gestionpymes.prestamos.prestamos.modelo.CuentaCliente;
import py.gestionpymes.prestamos.prestamos.modelo.DetPrestamo;
import py.gestionpymes.prestamos.prestamos.modelo.enums.EstadoPrestamo;
import py.gestionpymes.prestamos.prestamos.modelo.OperacionDesembolsoPrestamo;
import py.gestionpymes.prestamos.prestamos.modelo.Prestamo;
import py.gestionpymes.prestamos.prestamos.modelo.enums.EstadoDetPrestamo;
import py.gestionpymes.prestamos.tesoreria.dao.TransaccionDAO;
import py.gestionpymes.prestamos.tesoreria.modelo.Secuencia;
import py.gestionpymes.prestamos.tesoreria.modelo.SesionTPV;
import py.gestionpymes.prestamos.tesoreria.modelo.TipoTransaccionCaja;
import py.gestionpymes.prestamos.tesoreria.modelo.Transaccion;
import py.gestionpymes.prestamos.tesoreria.modelo.TransaccionDesembolso;

/**
 *
 * @author christian
 */
@Stateless

public class PrestamoDAO extends AbstractFacade<py.gestionpymes.prestamos.prestamos.modelo.Prestamo> {

    @PersistenceContext(unitName = "PrestamosPU")
    private EntityManager em;
    @EJB
    private DetCuentaClienteDAO detCuentaClienteDAO;
    @EJB
    private TransaccionDAO transaccionDAO;
    private ABMService abmService;

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
    
    public List<ListadoMora> listadoMora() {

        String consuta = "select c.nrodocumento, c.primernombre, "
                + "c.segundonombre, c.primerapellido, c.segundoapellido,"
                + " p.id as operacionnro, dp.nrocuota, p.fechainiciooperacion, "
                + "dp.fechavencimiento, dp.diasmora, dp.montomora, dp.montocuota, "
                + "(dp.moramoratorio + dp.morapunitorio) as moracuota, "
                + "(dp.montocuota + dp.moramoratorio + dp.morapunitorio) as cuotaconmora "
                + "from persona as c, prestamo as p, detprestamo as dp "
                + "where p.id = dp.prestamo_id and p.cliente_id = c.id and "
                + "diasmora > 0 order by c.id, p.id, dp.nrocuota";
                

        List<Object[]> lista = abmService.getEM().createNativeQuery(consuta).getResultList();

        List<ListadoMora> R = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {

            R.add(new ListadoMora(lista.get(i)));
        }
        return R;
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

    
    public List<DetPrestamo> findCuotasPendientes(Prestamo p){
        return em.createQuery("SELECT dp from DetPrestamo dp WHERE dp.prestamo = :prestamo and dp.estado = :estado ORDER BY dp.nroCuota")
                .setParameter("prestamo", p)
                .setParameter("estado", EstadoDetPrestamo.PENDIENTE)
                .getResultList();
    }
}
