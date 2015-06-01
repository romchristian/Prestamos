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
import py.gestionpymes.prestamos.finanza.persistencia.CuentaBancaria;
import py.gestionpymes.prestamos.finanza.persistencia.TransaccionDepositoEfectivo;
import py.gestionpymes.prestamos.finanza.persistencia.BoletaDeposito;

/**
 *
 * @author christian
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class CuentaBancariaDAO extends AbstractDAO<CuentaBancaria> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;
    @EJB
    private TransaccionBancariaDAO transaccionBancariaDAO;
    @Inject
    private Credencial credencial;

    @Override
    public CuentaBancaria create(CuentaBancaria entity) {
        return abmService.create(entity);
    }

    @Override
    public CuentaBancaria edit(CuentaBancaria entity) {
        return abmService.update(entity);
    }

    @Override
    public void remove(CuentaBancaria entity) {
        abmService.delete(entity);
    }

    @Override
    public CuentaBancaria find(Object id) {
        return abmService.find(id, CuentaBancaria.class);
    }

    @Override
    public List<CuentaBancaria> findAll() {
        return abmService.getEM().createQuery("select obj from CuentaBancaria obj").getResultList();
    }

    @Override
    public List<CuentaBancaria> findAll(String query, QueryParameter params) {
        return abmService.findByQuery(query, params.parameters());
    }

    public boolean deposita(BoletaDeposito boleta) throws TransaccionBancariaNoCreadaException {
        boolean R = false;

        if (boleta.getTotalEfectivo() != null) {

            try {
                TransaccionDepositoEfectivo t = new TransaccionDepositoEfectivo("ITAU-EFE-01",
                        boleta.getNroComprobante(),
                        boleta.getFecha(),
                        boleta.getCuentaBancaria(),
                        boleta.getMoneda(),
                        boleta.getCotizacion(),
                        credencial.getUsuario().getUsuario(),
                        boleta.getTotalEfectivo());

                transaccionBancariaDAO.create(t);
                R = true;
            } catch (Exception e) {
                throw new TransaccionBancariaNoCreadaException(e.getMessage());
            }

        }
        return R;
    }
}
