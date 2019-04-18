package interfaces;

import java.util.Collection;
import classes.Incidente;
import services.EditException;

public interface IIncidenteService {

    public void addIncidente(Incidente incidente);
    public Collection<Incidente> getIncidentes();
    public Incidente getIncidente(Integer id);
    public Incidente editIncidente(Incidente incidente) throws EditException;
    public void deleteIncidente(Integer id);
    public boolean incidenteExists(Integer id);
}
