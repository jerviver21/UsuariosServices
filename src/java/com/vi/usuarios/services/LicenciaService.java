/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vi.usuarios.services;

import com.vi.usuarios.dominio.Licencia;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jerviver21
 */
@Stateless
@LocalBean
public class LicenciaService {
    
    @PersistenceContext(unitName = "UsuariosPU")
    private EntityManager em;
    

    public Licencia create(Licencia licencia){
        licencia = em.merge(licencia);
        licencia.setNoLicencia(String.format("%05d", licencia.getId()));
        return licencia;
    }
    
    
    public Licencia findByNo(String no){
        List<Licencia> licencia = em.createNamedQuery("Licencia.findByNo").setParameter("no", no).getResultList();
        if(licencia.isEmpty()){
            return null;
        }
        return licencia.get(0);
    }
}
