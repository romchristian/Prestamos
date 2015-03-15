/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.dao;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import py.gestionpymes.prestamos.adm.dao.ABMService;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.dao.QueryParameter;
import py.gestionpymes.prestamos.tesoreria.persisitencia.PuntoVenta;
import py.gestionpymes.prestamos.tesoreria.persisitencia.SesionTPV;
import py.gestionpymes.prestamos.tesoreria.persisitencia.Transaccion;

/**
 *
 * @author christian
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class TransaccionDAO extends AbstractDAO<Transaccion> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;

    @Override
    public Transaccion create(Transaccion entity) {
        return abmService.create(entity);
    }

    @Override
    public Transaccion edit(Transaccion entity) {
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
