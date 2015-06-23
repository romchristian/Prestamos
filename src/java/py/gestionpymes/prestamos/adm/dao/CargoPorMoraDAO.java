/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.dao;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import py.gestionpymes.prestamos.adm.modelo.CargoPorMora;
import py.gestionpymes.prestamos.adm.modelo.Estado;
import py.gestionpymes.prestamos.adm.modelo.TipoCargo;
import py.gestionpymes.prestamos.prestamos.modelo.enums.PeriodoPago;

/**
 *
 * @author christian
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class CargoPorMoraDAO extends AbstractDAO<CargoPorMora> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;

    @Override
    public CargoPorMora create(CargoPorMora entity) {
        return abmService.create(entity);
    }

    @Override
    public CargoPorMora edit(CargoPorMora entity) {
        return abmService.update(entity);
    }

    @Override
    public void remove(CargoPorMora entity) {
        abmService.delete(entity);
    }

    @Override
    public CargoPorMora find(Object id) {
        return abmService.find(id, CargoPorMora.class);
    }

    @Override
    public List<CargoPorMora> findAll() {
        return abmService.getEM().createQuery("select obj from CargoPorMora obj").getResultList();
    }

    @Override
    public List<CargoPorMora> findAll(String query, QueryParameter params) {
        return abmService.findByQuery(query, params.parameters());
    }

    public CargoPorMora findCargo(int diasMora, PeriodoPago periodoPago) {
        TipoCargo tipo = null;
        switch (periodoPago) {
            case MENSUAL:
                tipo = TipoCargo.MENSUAL;
                break;
            case QUINCENAL:
                tipo = TipoCargo.QUINCENAL;
                break;
            case SEMANAL:
                tipo = TipoCargo.SEMANAL;
                break;
            case DIARIO:
                tipo = TipoCargo.DIARIO;
                break;
        }

        CargoPorMora R = null;
        try {

            System.out.println("diasMora: " + diasMora);
            System.out.println("tipo: " + tipo);
            if (diasMora > 0 && tipo == TipoCargo.MENSUAL) {
                R = (CargoPorMora) abmService.getEM().createQuery("SELECT c from CargoPorMora c where  c.tipo = :tipo and c.inicioTramo <= :diasMora and c.finTramo >= :diasMora")
                      
                        .setParameter("tipo", tipo)
                        .setParameter("diasMora", diasMora)
                        .getSingleResult();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return R;
    }

}
