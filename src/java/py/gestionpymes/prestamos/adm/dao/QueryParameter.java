/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.dao;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cromero
 */
public class QueryParameter {
    private Map<String,Object> parameters = null;

    public QueryParameter() {
        this.parameters = new HashMap<String, Object>();
    }
    
    
    private QueryParameter(String name, Object value){
        this();
        this.parameters.put(name, value);
    }
    
    public static QueryParameter where(String name, Object value){
        return new QueryParameter(name, value);
    }
    
    public QueryParameter and(String name, Object value){
        this.parameters.put(name, value);
        return this;
    }
    public Map<String,Object> parameters(){
        return this.parameters;
    }
}
