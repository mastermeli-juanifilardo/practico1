package classes;

import java.util.ArrayList;
import java.util.Collection;

public class Proyecto {

    private Integer id;
    private String titulo;
    private Usuario propietario;
    private Collection incidentes;

    public Proyecto(Integer id, String titulo, Usuario propietario) {
        this.id = id;
        this.titulo = titulo;
        this.propietario = propietario;
        this.incidentes = new ArrayList();
    }

    public Proyecto(Integer id, String titulo, Usuario propietario, Collection incidentes) {
        this.id = id;
        this.titulo = titulo;
        this.propietario = propietario;
        this.incidentes = incidentes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Usuario getPropietario() {
        return propietario;
    }

    public void setPropietario(Usuario propietario) {
        this.propietario = propietario;
    }

    public Collection getIncidentes() {
        return incidentes;
    }

    public void setIncidentes(Collection incidentes) {
        this.incidentes = incidentes;
    }

    public void addIncidente(Incidente incidente) {
        this.incidentes.add(incidente);
    }

    public boolean hasIncidentes() {
        return ! this.incidentes.isEmpty();
    }
}

