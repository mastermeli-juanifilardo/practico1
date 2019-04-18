package services;

import java.util.Collection;
import java.util.HashMap;

import services.EditException;
import classes.Incidente;
import interfaces.IIncidenteService;

public class IncidenteServiceMap implements IIncidenteService {

    private HashMap<Integer, Incidente> incidenteMap;

    public IncidenteServiceMap() {
        this.incidenteMap = new HashMap<Integer, Incidente>();
    }

    public IncidenteServiceMap(HashMap<Integer, Incidente> incidenteMap) {
        this.incidenteMap = incidenteMap;
    }

    @Override
    public void addIncidente(Incidente incidente) {
        incidenteMap.put(incidente.getId(), incidente);
    }

    @Override
    public Collection<Incidente> getIncidentes() {
        return incidenteMap.values();
    }

    @Override
    public Incidente getIncidente(Integer id) {
        return incidenteMap.get(id);
    }

    @Override
    public Incidente editIncidente(Incidente incidente) throws EditException {
        try {
            if (incidente.getId() == null) {
                throw new EditException("El id no puede ser nulo");
            }
            Incidente incidenteEditar = incidenteMap.get(incidente.getId());
            if (incidente.getDescripcion() != null) {
                incidenteEditar.setDescripcion(incidente.getDescripcion());
            }
            if (incidente.getReportador() != null) {
                incidenteEditar.setReportador(incidente.getReportador());
            }
            return incidenteEditar;
        } catch (Exception exception) {
            throw new EditException(exception.getMessage());
        }
    }

    @Override
    public void deleteIncidente(Integer id) {
        incidenteMap.remove(id);
    }

    @Override
    public boolean incidenteExists(Integer id) {
        return incidenteMap.containsKey(id);
    }
}
