/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.rest.application.config;

import java.util.Set;
import javax.ws.rs.core.Application;
import javax.ws.rs.ApplicationPath;
/**
 *
 * @author marcelo.bhegedus
 */
@ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(Curtidas.CurtidasService.class);
        resources.add(com.mycompany.pi4.Amigos.FriendsService.class);
        resources.add(com.mycompany.pi4.Comentarios.CommentService.class);
        resources.add(com.mycompany.pi4.Historicos.PostsService.class);
        resources.add(com.mycompany.pi4.Login.LoginService.class);
        resources.add(com.mycompany.pi4.Usuarios.UsersService.class);
        resources.add(org.glassfish.jersey.server.wadl.internal.WadlResource.class);
    }
    
}
