/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gestionpymes.prestamos.adm.modelo.Usuario;
import py.gestionpymes.prestamos.prestamos.modelo.Categoria;

/**
 *
 * @author Acer
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "PrestamosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }

    public Usuario find(String login) {
        Usuario R = null;
        
        try {
            R = (Usuario) em.createQuery("SELECT u FROM Usuario u WHERE u.usuario = :login ")
                    .setParameter("login", login).getSingleResult();
        } catch (Exception e) {
        }
        
        return R;
    }

}
