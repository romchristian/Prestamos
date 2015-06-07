/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.finanza.dao;

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
import py.gestionpymes.prestamos.finanza.modelo.BoletaDeposito;
import py.gestionpymes.prestamos.finanza.modelo.TransaccionDepositoEfectivo;
import py.gestionpymes.prestamos.finanza.modelo.BoletaDeposito;
import py.gestionpymes.prestamos.finanza.modelo.TransaccionReversionDepositoEfe;
import py.gestionpymes.prestamos.finanza.modelo.enums.EstadoBoletaDeposito;

/**
 *
 * @author christian
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class BoletaDepositoDAO extends AbstractDAO<BoletaDeposito> {
   
    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;
    @EJB
    private TransaccionBancariaDAO transaccionBancariaDAO;
    @Inject
    private Credencial credencial;

    @Override
    public BoletaDeposito create(BoletaDeposito entity) {
        return abmService.create(entity);
    }

    @Override
    public BoletaDeposito edit(BoletaDeposito entity) {
        return abmService.update(entity);
    }

    @Override
    public void remove(BoletaDeposito entity) {
        abmService.delete(entity);
    }

    @Override
    public BoletaDeposito find(Object id) {
        return abmService.find(id, BoletaDeposito.class);
    }

    @Override
    public List<BoletaDeposito> findAll() {
        return abmService.getEM().createQuery("select obj from BoletaDeposito obj").getResultList();
    }

    @Override
    public List<BoletaDeposito> findAll(String query, QueryParameter params) {
        return abmService.findByQuery(query, params.parameters());
    }
    
      public BoletaDeposito revertir(BoletaDeposito boleta) throws TransaccionBancariaNoCreadaException {
        

        if (boleta.getTotalEfectivo() != null) {

            try {
                TransaccionReversionDepositoEfe t = new TransaccionReversionDepositoEfe("ITAU-DEP-"+boleta.getNroComprobante(),
                        boleta.getNroComprobante(),
                        boleta.getCuentaBancaria(),
                        boleta.getMoneda(),
                        boleta.getCotizacion(),
                        credencial.getUsuario().getUsuario(),
                        boleta.getTotalEfectivo());

                transaccionBancariaDAO.create(t);
                boleta.setEstado(EstadoBoletaDeposito.PENDIENTE);
                
                edit(boleta);
            } catch (Exception e) {
                throw new TransaccionBancariaNoCreadaException(e.getMessage());
            }

        }
        return boleta;
    }
}
