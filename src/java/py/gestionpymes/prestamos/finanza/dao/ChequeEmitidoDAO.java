/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.finanza.dao;

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
import py.gestionpymes.prestamos.adm.dao.HistorialDAO;
import py.gestionpymes.prestamos.adm.dao.QueryParameter;
import py.gestionpymes.prestamos.adm.web.util.Credencial;
import py.gestionpymes.prestamos.finanza.modelo.ChequeEmitido;
import py.gestionpymes.prestamos.finanza.modelo.enums.EstadoChequeEmitido;
import py.gestionpymes.prestamos.finanza.modelo.HistorialCheque;
import py.gestionpymes.prestamos.finanza.modelo.TransaccionCobroCheque;
import py.gestionpymes.prestamos.finanza.modelo.TransaccionReversaCobroCheque;

/**
 *
 * @author christian
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ChequeEmitidoDAO extends AbstractDAO<ChequeEmitido> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;
    @EJB
    private HistorialDAO historialDAO;
    @Inject
    private Credencial credencial;
    @EJB
    private TransaccionBancariaDAO transaccionDAO;

    @Override
    public ChequeEmitido create(ChequeEmitido entity) {

        ChequeEmitido ch = abmService.create(entity);
        borrador(ch, "Creación del cheque");
        return ch;
    }

    @Override
    public ChequeEmitido edit(ChequeEmitido entity) {
        return abmService.update(entity);
    }

    @Override
    public void remove(ChequeEmitido entity) {
        abmService.delete(entity);
    }

    @Override
    public ChequeEmitido find(Object id) {
        return abmService.find(id, ChequeEmitido.class);
    }

    @Override
    public List<ChequeEmitido> findAll() {
        return abmService.getEM().createQuery("select obj from ChequeEmitido obj").getResultList();
    }

    @Override
    public List<ChequeEmitido> findAll(String query, QueryParameter params) {
        return abmService.findByQuery(query, params.parameters());
    }

    public ChequeEmitido borrador(ChequeEmitido ch, String descripcion) {
        HistorialCheque h = creaOperacion(ch, descripcion, EstadoChequeEmitido.BORRADOR.getOperacion());
        ch.setEstado(EstadoChequeEmitido.BORRADOR);

        if (ch.getHistorialCheques() == null) {
            List<HistorialCheque> historiales = new ArrayList<>();
            historiales.add(h);
            ch.setHistorialCheques(historiales);
        } else {
            ch.getHistorialCheques().add(h);
        }

        edit(ch);
        return ch;
    }

    public ChequeEmitido autorizar(ChequeEmitido ch, String descripcion) throws CambioEstadoChequeEmitidoException {
        if (ch.getEstado() != EstadoChequeEmitido.BORRADOR) {
            throw new CambioEstadoChequeEmitidoException("No puede autorizar un cheque que no este en el estado borrador");
        }

        HistorialCheque h = creaOperacion(ch, descripcion, EstadoChequeEmitido.AUTORIZADO.getOperacion());
        ch.setEstado(EstadoChequeEmitido.AUTORIZADO);

        if (ch.getHistorialCheques() == null) {
            List<HistorialCheque> historiales = new ArrayList<>();
            historiales.add(h);
            ch.setHistorialCheques(historiales);
        } else {
            ch.getHistorialCheques().add(h);
        }

        edit(ch);
        return ch;
    }

    public ChequeEmitido comprometer(ChequeEmitido ch, String descripcion) throws CambioEstadoChequeEmitidoException {

        if (ch.getEstado() != EstadoChequeEmitido.AUTORIZADO) {
            throw new CambioEstadoChequeEmitidoException("No puede comprometer un cheque no autorizado");
        }

        HistorialCheque h = creaOperacion(ch, descripcion, EstadoChequeEmitido.COMPROMETIDO.getOperacion());
        ch.setEstado(EstadoChequeEmitido.COMPROMETIDO);

        if (ch.getHistorialCheques() == null) {
            List<HistorialCheque> historiales = new ArrayList<>();
            historiales.add(h);
            ch.setHistorialCheques(historiales);
        } else {
            ch.getHistorialCheques().add(h);
        }

        edit(ch);
        return ch;
    }

    public ChequeEmitido cobrar(ChequeEmitido ch, String descripcion) throws CambioEstadoChequeEmitidoException {

        if (ch.getEstado() != EstadoChequeEmitido.COMPROMETIDO) {
            throw new CambioEstadoChequeEmitidoException("No puede cobrar un cheque no compremetido");
        }

        HistorialCheque h = creaOperacion(ch, descripcion, EstadoChequeEmitido.COBRADO.getOperacion());
        ch.setEstado(EstadoChequeEmitido.COBRADO);

        if (ch.getHistorialCheques() == null) {
            List<HistorialCheque> historiales = new ArrayList<>();
            historiales.add(h);
            ch.setHistorialCheques(historiales);
        } else {
            ch.getHistorialCheques().add(h);
        }

        edit(ch);

        TransaccionCobroCheque t = new TransaccionCobroCheque("CH-COBRADO",
                ch, ch.getCuentaBancaria(), ch.getMoneda(),
                null, credencial.getUsuario().getUsuario(), ch.getMonto());

        transaccionDAO.create(t);

        return ch;
    }

    public ChequeEmitido revesar(ChequeEmitido ch, String descripcion) throws CambioEstadoChequeEmitidoException {

        if (ch.getEstado() != EstadoChequeEmitido.COBRADO) {
            throw new CambioEstadoChequeEmitidoException("No puede reversar un cheque no cobrado");
        }

        HistorialCheque h = creaOperacion(ch, descripcion, "REVERSIÓN DE CHEQUE");
        ch.setEstado(EstadoChequeEmitido.BORRADOR);

        if (ch.getHistorialCheques() == null) {
            List<HistorialCheque> historiales = new ArrayList<>();
            historiales.add(h);
            ch.setHistorialCheques(historiales);
        } else {
            ch.getHistorialCheques().add(h);
        }

        edit(ch);

        TransaccionReversaCobroCheque t = new TransaccionReversaCobroCheque("CH-REVERSADO",
                ch, ch.getCuentaBancaria(), ch.getMoneda(),
                null, credencial.getUsuario().getUsuario(), ch.getMonto());

        transaccionDAO.create(t);

        return ch;
    }

    public ChequeEmitido anular(ChequeEmitido ch, String descripcion) throws CambioEstadoChequeEmitidoException {

        if (ch.getEstado() == EstadoChequeEmitido.COBRADO) {
            throw new CambioEstadoChequeEmitidoException("No puede anular un cheque cobrado: primero debe desconciliarlo");
        }

        HistorialCheque h = creaOperacion(ch, descripcion, EstadoChequeEmitido.ANULADO.getOperacion());
        ch.setEstado(EstadoChequeEmitido.ANULADO);

        if (ch.getHistorialCheques() == null) {
            List<HistorialCheque> historiales = new ArrayList<>();
            historiales.add(h);
            ch.setHistorialCheques(historiales);
        } else {
            ch.getHistorialCheques().add(h);
        }

        edit(ch);
        return ch;
    }

    public ChequeEmitido comentario(ChequeEmitido ch, String descripcion) throws CambioEstadoChequeEmitidoException {

        if (ch.getEstado() == EstadoChequeEmitido.COBRADO) {
            throw new CambioEstadoChequeEmitidoException("No puede comentar un cheque cobrado");
        }

        HistorialCheque h = creaOperacion(ch, descripcion, "COMENTARIO");

        if (ch.getHistorialCheques() == null) {
            List<HistorialCheque> historiales = new ArrayList<>();
            historiales.add(h);
            ch.setHistorialCheques(historiales);
        } else {
            ch.getHistorialCheques().add(h);
        }

        edit(ch);
        return ch;
    }

    private HistorialCheque creaOperacion(ChequeEmitido ch, String descripcion, String operacion) {
        HistorialCheque h = new HistorialCheque(ch, credencial.getUsuario(), operacion, descripcion);
        return (HistorialCheque) historialDAO.create(h);
    }

}
