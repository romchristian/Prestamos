/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import org.joda.time.LocalDate;
import py.gestionpymes.prestamos.adm.dao.ABMService;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.dao.QueryParameter;
import py.gestionpymes.prestamos.adm.web.util.Credencial;
import py.gestionpymes.prestamos.contabilidad.persistencia.ChequeRecibido;
import py.gestionpymes.prestamos.contabilidad.persistencia.Pago;
import py.gestionpymes.prestamos.tesoreria.persisitencia.Banco;
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
                " select b.id, b.nombre,sum(p.monto), count(p.*) from pago p \n"
                + " join transaccion t on p.transaccion_id = t.id \n"
                + " join banco b on b.id = p.banco_id "
                + " where t.sesiontpv_id = " + s.getId()
                + " and t.dtype = '" + dtype + "'"
                + " and p.dtype = '" + PagoDtype + "'"
                + " and t.tipotransaccion = '" + tipoTransaccion + "'"
                + " group by b.id , b.nombre ").getResultList();
        
        return lista;
    }

    /**
     * findPagosDetalle
     *
     * @param s sesión actual
     * @param b banco seleccionado
     * @param tipoConsulta identificador del tipo de la consulta a devolver
     * @return lista de pagos
     */
    public List<Pago> findPagosDetalle(SesionTPV s, TreeCierre t) {
        
        List<Pago> R = new ArrayList<>();
        switch (t.getTipoConsulta()) {
            case TreeCierre.TCC:
                R = findTCC(s);
                break;
            case TreeCierre.TCC_EFE:
                R = findTccEfe(s);
                break;
            case TreeCierre.TCC_CH:
                R = findTccCh(s);
                break;
            case TreeCierre.TCC_BANCO:
                R = findTccBanco(s, t.getBancoId());
                break;
            case TreeCierre.EV:
                break;
            case TreeCierre.EV_EFE:
                break;
            case TreeCierre.DE:
                break;
            case TreeCierre.DE_EFE:
                break;
            case TreeCierre.SV:
                break;
            case TreeCierre.SV_EFE:
                break;
        }
        
        return R;
    }
    
    public List<Pago> findTCC(SesionTPV s) {
        System.out.println("Empieza_1: " + new Date());
        List<Pago> R = abmService
                .getEM()
                .createNativeQuery("select * from pago p "
                        + " join transaccion t on p.transaccion_id = t.id "
                        + " where t.sesiontpv_id = " + s.getId()
                        + " and t.dtype = 'TransaccionCobraCuota' " //+ " and p.dtype = '" + PagoDtype + "'"
                        , Pago.class).getResultList();
        
        System.out.println("Termina_1: " + new Date());
        return R;
    }
    
    public List<Pago> findTccEfe(SesionTPV s) {
        return abmService
                .getEM()
                .createNativeQuery("select * from pago p "
                        + " join transaccion t on p.transaccion_id = t.id "
                        + " where t.sesiontpv_id = " + s.getId()
                        + " and t.dtype = 'TransaccionCobraCuota' "
                        + " and p.dtype = 'Efectivo' ", Pago.class).getResultList();
    }
    
    public List<Pago> findTccCh(SesionTPV s) {
        return abmService
                .getEM()
                .createNativeQuery("select * from pago p "
                        + " join transaccion t on p.transaccion_id = t.id "
                        + " where t.sesiontpv_id = " + s.getId()
                        + " and t.dtype = 'TransaccionCobraCuota' "
                        + " and p.dtype = 'ChequeRecibido' ", Pago.class).getResultList();
    }
    
    public List<Pago> findTccBanco(SesionTPV s, Long bancoId) {
        return abmService
                .getEM()
                .createNativeQuery("select * from pago p "
                        + " join transaccion t on p.transaccion_id = t.id "
                        + " where t.sesiontpv_id = " + s.getId()
                        + " and t.dtype = 'TransaccionCobraCuota' "
                        + " and p.dtype = 'ChequeRecibido' "
                        + " and p.banco_id = " + bancoId, Pago.class).getResultList();
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
    
    public BigDecimal getTotalCobroCuotas(SesionTPV s) {
        return obtTotalTransaccion(s, "TransaccionCobraCuota", "ENTRADA");
    }
    
    public BigDecimal getTotalCobrosCuotasEfe(SesionTPV s) {
        return obtTotalPago(s, "TransaccionCobraCuota", "ENTRADA", "Efectivo");
    }
    
    public BigDecimal getTotalCobrosCuotasCh(SesionTPV s) {
        return obtTotalPago(s, "TransaccionCobraCuota", "ENTRADA", "ChequeRecibido");
    }
    
    public BigDecimal getTotalEntradasVarias(SesionTPV s) {
        return obtTotalTransaccion(s, "Transaccion", "ENTRADA");
    }
    
    public BigDecimal getTotalEntradasVariasEfe(SesionTPV s) {
        return obtTotalPago(s, "Transaccion", "ENTRADA", "Efectivo");
    }
    
    public BigDecimal getTotalSalidasVarias(SesionTPV s) {
        return obtTotalTransaccion(s, "Transaccion", "SALIDA");
    }
    
    public BigDecimal getTotalSalidasVariasEfe(SesionTPV s) {
        return obtTotalPago(s, "Transaccion", "SALIDA", "Efectivo");
    }
    
    public BigDecimal getTotalDesembolsos(SesionTPV s) {
        return obtTotalTransaccion(s, "TransaccionDesembolso", "SALIDA");
    }
    
    public BigDecimal getTotalDesembolsosEfe(SesionTPV s) {
        return obtTotalPago(s, "TransaccionDesembolso", "SALIDA", "Efectivo");
    }
    
    public BigDecimal obtTotalTransaccion(SesionTPV s, String dtype, String tipo) {
        return findSumTransaccion(s, dtype, tipo);
    }
    
    public BigDecimal obtTotalPago(SesionTPV s, String dtype, String tipo, String pagoDtype) {
        return findSumPago(s, dtype, tipo, pagoDtype);
    }
    
    public VistaGrafico obtVistaGrafico() {
        LocalDate date = new LocalDate();
        String sdate = date.getYear()+"-"+date.getMonthOfYear()+"-"+date.getDayOfMonth();
        
        Object[] obj = (Object[]) abmService.getEM().createNativeQuery("select (select sum(saldo) from puntoventa) as \"SALDO\", sum(case when tipotransaccion = 'ENTRADA' then monto else 0 end) as \"ENTRADA\",\n"
                + "sum(case when tipotransaccion = 'SALIDA' then monto else 0 end) as \"SALIDA\"\n"
                + "from transaccion where tipotransaccion in ('ENTRADA','SALIDA') and fecha::date =  '"+sdate+"'\n"
                + "group by 1").getSingleResult();
        
        VistaGrafico R = new VistaGrafico(obj);
        
        return R;
    }
}
