/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.dao;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import py.gestionpymes.prestamos.adm.dao.ABMService;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.dao.QueryParameter;
import py.gestionpymes.prestamos.adm.persistencia.Usuario;
import py.gestionpymes.prestamos.adm.web.util.Credencial;
import py.gestionpymes.prestamos.adm.web.util.MisSesiones;
import py.gestionpymes.prestamos.adm.web.util.UsuarioLogueado;
import py.gestionpymes.prestamos.tesoreria.persisitencia.PuntoVenta;
import py.gestionpymes.prestamos.tesoreria.persisitencia.SesionTPV;

/**
 *
 * @author christian
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class SesionTPVDAO extends AbstractDAO<SesionTPV> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;
    @Inject
    private Credencial credencial;

    public List<ResumenTransaccion> resumenTransaccion(SesionTPV sesionTPV) {

        String consuta = "select operacion,tipo ,\n"
                + "case when tipo = 'SALIDA' then sum(egreso)*-1 else sum(ingreso) end as monto,\n"
                + "count(*) as cantidaTransaccion \n"
                + "from\n"
                + "(select t.fecha, tt.descripcion as operacion,\n"
                + " t.descripcion as descripcion, \n"
                + " t.tipotransaccion as tipo,\n"
                + " case when t.tipotransaccion = 'ENTRADA' then t.monto else 0 end as ingreso,\n"
                + " case when t.tipotransaccion = 'SALIDA' then t.monto else 0 end as egreso\n"
                + " from transaccion t \n"
                + "join tipotransaccioncaja tt on t.tipotransaccioncaja_id = tt.id where t.sesiontpv_id = " + sesionTPV.getId() + ") v\n"
                + "group by operacion,tipo";

        List<Object[]> lista = abmService.getEM().createNativeQuery(consuta).getResultList();

        List<ResumenTransaccion> R = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {

            R.add(new ResumenTransaccion(lista.get(i)));
        }
        return R;
    }

    @Override
    public SesionTPV create(SesionTPV entity) {
        return abmService.create(entity);
    }

    @Override
    public SesionTPV edit(SesionTPV entity) {
        return abmService.update(entity);
    }

    public SesionTPV cierre(SesionTPV entity) {
        PuntoVenta p = entity.getPuntoVenta();
        p.setSaldo(entity.getSaldoCierre());
        abmService.getEM().merge(p);
        return abmService.update(entity);
    }

    @Override
    public void remove(SesionTPV entity) {
        abmService.delete(entity);
    }

    @Override
    public SesionTPV find(Object id) {
        return abmService.find(id, SesionTPV.class);
    }

    @Produces
    @Named(value = "misSesiones")
    @MisSesiones
    @Override
    public List<SesionTPV> findAll() {
        System.out.println("Usuario loguedo: " + credencial.getUsuario());
        return abmService.getEM().createQuery("select obj from SesionTPV obj where obj.usuario = :u ")
                .setParameter("u", credencial.getUsuario())
                .getResultList();
    }

    @Override
    public List<SesionTPV> findAll(String query, QueryParameter params) {
        return abmService.findByQuery(query, params.parameters());
    }

}
