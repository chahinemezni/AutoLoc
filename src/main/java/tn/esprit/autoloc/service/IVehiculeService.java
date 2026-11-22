
package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Vehicule;
import java.util.List;

public interface IVehiculeService {

    Vehicule create(Vehicule vehicule);

    Vehicule findById(Long id);

    List<Vehicule> findAll();

    Vehicule update(Long id, Vehicule vehicule);

    void deleteById(Long id);
    Vehicule affecterVehicule(Long idVehicule, Long idAgence);
    Vehicule affecterEquipement(Long idVehicule, Long idEquipement);

    Vehicule desaffecterEquipement(Long idVehicule, Long idEquipement);
}
