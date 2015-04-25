/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import py.gestionpymes.prestamos.adm.dao.ABMService;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.dao.QueryParameter;
import py.gestionpymes.prestamos.adm.web.util.Credencial;
import py.gestionpymes.prestamos.contabilidad.persistencia.Pago;
import py.gestionpymes.prestamos.tesoreria.persisitencia.PuntoVenta;
import py.gestionpymes.prestamos.tesoreria.persisitencia.SesionTPV;
import py.gestionpymes.prestamos.tesoreria.persisitencia.Transaccion;
import py.gestionpymes.prestamos.tesoreria.web.TreeCierre;

/**
 *
 * @author christian
 */
@Stateless
public class TransaccionDAO extends AbstractDAO<Transaccion> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;
    @Inject
    private Credencial credencial;

    @Override
    public Transaccion create(Transaccion entity) {
        entity.setUsuarioLogeado(credencial.getUsuario().getNombre() + " " + credencial.getUsuario().getApellido());
        return abmService.create(entity);
    }

    @Override
    public Transaccion edit(Transaccion entity) {
        entity.setUsuarioLogeado(credencial.getUsuario().getNombre() + " " + credencial.getUsuario().getApellido());
        return abmService.update(entity);
    }

    @Override
    public void remove(Transaccion entity) {
        abmService.delete(entity);
    }

    @Override
    public Transaccion find(Object id) {
        return abmService.find(id, Transaccion.class);
    }

    @Override
    public List<Transaccion> findAll() {
        return abmService.getEM().createQuery("select obj from Transaccion obj").getResultList();
    }

    public BigDecimal findSumPorSesion(SesionTPV s) {
        return (BigDecimal) abmService.getEM().createNativeQuery("select (sum(entradas) - sum(salidas))::numeric(38,4) as saldo from \n"
                + "(select case when tipotransaccion = 'ENTRADA' then sum(monto)  else 0 end as entradas,\n"
                + "       case when tipotransaccion = 'SALIDA' then sum(monto) else 0 end as salidas\n"
                + " from transaccion\n"
                + " where sesiontpv_id = " + s.getId()
                + " group by tipotransaccion) x").getSingleResult();
    }

    public BigDecimal findSumTransaccion(SesionTPV s, String dtype, String tipoTransaccion) {
        return (BigDecimal) abmService.getEM().createNativeQuery("select sum(monto) "
                + " from transaccion where sesiontpv_id =  " + s.getId()
                + " and dtype = '" + dtype + "'"
                + " and tipotransaccion = '" + tipoTransaccion + "'").getSingleResult();
    }

    public BigDecimal findSumPago(SesionTPV s, String dtype, String tipoTransaccion, String PagoDtype) {
        return (BigDecimal) abmService.getEM().createNativeQuery("select sum(p.monto) from pago p \n"
                + " join transaccion t on p.transaccion_id = t.id \n"
                + " where t.sesiontpv_id = " + s.getId()
                + " and t.dtype = '" + dtype + "'"
                + " and p.dtype = '" + PagoDtype + "'"
                + " and t.tipotransaccion = '" + tipoTransaccion + "'").getSingleResult();
    }

    public List<Object[]> findSumPorBanco(SesionTPV s, String dtype, String tipoTransaccion, String PagoDtype) {
        List<Object[]> lista = abmService.getEM().createNativeQuery(
                " select b.nombre,sum(p.monto) from pago p \n"
                + " join transaccion t on p.transaccion_id = t.id \n"
                + " join banco b on b.id = p.banco_id "
                + " where t.sesiontpv_id = " + s.getId()
                + " and t.dtype = '" + dtype + "'"
                + " and p.dtype = '" + PagoDtype + "'"
                + " and t.tipotransaccion = '" + tipoTransaccion + "'"
                + " group by b.nombre ").getResultList();

        return lista;
    }

    
    public List<Pago> findChequesRecibidosTCC(SesionTPV s) {
        return abmService.getEM().createQuery("select c from ChequeRecibido c where c.transaccion.id  in (select tcc.id from TransaccionCobraCuota)\n"
                + "and c.transaccion.sesionTPV = ?1").setParameter(1, s).getResultList();
    }

    public List<Transaccion> findAllSesionTPV(SesionTPV s) {
        return abmService.getEM().createQuery("select obj from Transaccion obj WHERE OBJ.sesionTPV = :sesion")
                .setParameter("sesion", s)
                .getResultList();
    }

    public List<Transaccion> findAllPuntoVenta(PuntoVenta p) {
        return abmService.getEM().createQuery("select obj from Transaccion obj WHERE OBJ.puntoVenta = :tpv")
                .setParameter("tpv", p)
                .getResultList();
    }

    @Override
    public List<Transaccion> findAll(String query, QueryParameter params) {
        return abmService.findByQuery(query, params.parameters());
    }
}
