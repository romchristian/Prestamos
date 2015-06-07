/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import py.gestionpymes.prestamos.adm.dao.ABMService;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.dao.QueryParameter;
import py.gestionpymes.prestamos.adm.modelo.Moneda;
import py.gestionpymes.prestamos.adm.modelo.Usuario;
import py.gestionpymes.prestamos.tesoreria.modelo.PuntoVenta;
import py.gestionpymes.prestamos.tesoreria.modelo.TipoTransaccion;
import py.gestionpymes.prestamos.tesoreria.modelo.TipoTransaccionCaja;
import py.gestionpymes.prestamos.tesoreria.modelo.Transaccion;
import py.gestionpymes.prestamos.tesoreria.modelo.TransaccionCobraCuota;

/**
 *
 * @author christian
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class PuntoVentaDAO extends AbstractDAO<PuntoVenta> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;
    @EJB
    private TransaccionDAO transaccionDAO;

    @Override
    public PuntoVenta create(PuntoVenta entity) {
        return abmService.create(entity);
    }

    @Override
    public PuntoVenta edit(PuntoVenta entity) {
        return abmService.update(entity);
    }

    @Override
    public void remove(PuntoVenta entity) {
        abmService.delete(entity);
    }

    @Override
    public PuntoVenta find(Object id) {
        return abmService.find(id, PuntoVenta.class);
    }

    @Override
    public List<PuntoVenta> findAll() {
        return abmService.getEM().createQuery("SELECT p from PuntoVenta p").getResultList();
    }

    @Override
    public List<PuntoVenta> findAll(String query, QueryParameter params) {
        return abmService.findByQuery(query, params.parameters());
    }

    public PuntoVenta find(Usuario u) {
        PuntoVenta R = null;
        try {
            R = (PuntoVenta) abmService.getEM().createQuery("SELECT p FROM PuntoVenta p WHERE p.usuario = :u")
                    .setParameter("u", u)
                    .getSingleResult();
        } catch (Exception e) {
        }
        return R;
    }

    public void ajustaSaldo(PuntoVenta p, Moneda moneda, BigDecimal saldoAjustado) {
        BigDecimal saldoActual = null;
        try {
            saldoActual = (BigDecimal) abmService.getEM().createNativeQuery("select (sum(entradas) - sum(salidas))::numeric(38,4) as saldo from \n"
                    + "(select case when tipotransaccion = 'ENTRADA' then sum(monto)  else 0 end as entradas,\n"
                    + "       case when tipotransaccion = 'SALIDA' then sum(monto) else 0 end as salidas\n"
                    + " from transaccion\n"
                    + " where puntoventa_id = " + p.getId()
                    + " group by tipotransaccion) x").getSingleResult();
        } catch (Exception e) {
        }

        if (saldoActual == null) {
            saldoActual = new BigDecimal(BigInteger.ZERO);
        }
        BigDecimal diff = saldoAjustado.subtract(saldoActual);
        System.out.println("Saldo Actual: " + saldoActual);
        System.out.println("Saldo Ajustado: " + saldoAjustado);
        System.out.println("Diff: " + diff);

        TipoTransaccionCaja ttc = null;
        String param = null;
        try {

            if (diff.compareTo(new BigDecimal(BigInteger.ZERO)) > 0) {
                param = "AJUSTE DE SALDO POSITIVO";
            } else if (diff.compareTo(new BigDecimal(BigInteger.ZERO)) < 0) {
                param = "AJUSTE DE SALDO NEGATIVO";
                diff = diff.multiply(new BigDecimal(-1));
            }

            ttc = (TipoTransaccionCaja) abmService.getEM().createQuery("select t from TipoTransaccionCaja t where t.descripcion= ?1")
                    .setParameter(1, param).getSingleResult();
        } catch (Exception e) {
        }

        if (ttc != null) {
            Transaccion tr = new Transaccion();
            tr.setDescripcion(param);
            tr.setMoneda(moneda);
            tr.setFecha(new Date());
            tr.setTipoTransaccionCaja(ttc);
            tr.setPuntoVenta(p);
            tr.setMonto(diff);
            transaccionDAO.create(tr);
            //abmService.getEM().persist(tr);

            try {
                saldoActual = (BigDecimal) abmService.getEM().createNativeQuery("select (sum(entradas) - sum(salidas))::numeric(38,4) as saldo from \n"
                        + "(select case when tipotransaccion = 'ENTRADA' then sum(monto)  else 0 end as entradas,\n"
                        + "       case when tipotransaccion = 'SALIDA' then sum(monto) else 0 end as salidas\n"
                        + " from transaccion\n"
                        + " where puntoventa_id = " + p.getId()
                        + " group by tipotransaccion) x").getSingleResult();

                p.setSaldo(saldoActual);

                System.out.println("Saldo Modificado: " + p.getSaldo());
                abmService.getEM().merge(p);

            } catch (Exception e) {
            }
        }
    }
}
