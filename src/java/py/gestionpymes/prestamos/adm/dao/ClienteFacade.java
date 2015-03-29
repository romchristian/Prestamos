/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.dao;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gestionpymes.prestamos.adm.web.ClienteFiltro;
import py.gestionpymes.prestamos.prestamos.dao.CuentaClienteDAO;
import py.gestionpymes.prestamos.prestamos.persistencia.Cliente;

/**
 *
 * @author Acer
 */
@Stateless
public class ClienteFacade extends AbstractFacade<Cliente> {

    @PersistenceContext(unitName = "PrestamosPU")
    private EntityManager em;
    @EJB
    private CuentaClienteDAO cuentaClienteDAO;

    public List<Cliente> findAll(ClienteFiltro filtro) {
        System.out.println("nro: " + filtro.getNroDoc());
        if (filtro.isBuscarPorDoc()) {
            System.out.println("busca por nro");
            return em.createQuery("SELECT c from Cliente c where c.nroDocumento = :cliente")
                    .setParameter("cliente", filtro.getNroDoc()).getResultList();
        } else {
            System.out.println("busca por apellido");
            return em.createQuery("SELECT c from Cliente c where UPPER(c.primerApellido) LIKE  CONCAT('%', :nombre, '%')")
                    .setParameter("nombre", filtro.getNombre().toUpperCase()).getResultList();
        }

    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClienteFacade() {
        super(Cliente.class);
    }

    @Override
    public void create(Cliente entity) {
        super.create(entity);
        cuentaClienteDAO.create(entity);
    }

}
