
package tn.esprit.autoloc.web.controller;

import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.service.IVehiculeService;
import tn.esprit.autoloc.web.dto.VehiculeDto;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    public VehiculeController(IVehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    @GetMapping
    public List<VehiculeDto> getAllVehicules() {
        return vehiculeService.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public VehiculeDto getVehiculeById(@PathVariable Long id) {
        return toDto(vehiculeService.findById(id));
    }

    @PostMapping
    public VehiculeDto createVehicule(@RequestBody VehiculeDto dto) {
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation(dto.getImmatriculation());
        vehicule.setMarque(dto.getMarque());
        vehicule.setModele(dto.getModele());
        vehicule.setCategorie(dto.getCategorie());
        vehicule.setTarifJournalier(dto.getTarifJournalier());
        vehicule.setStatut(dto.getStatut());

        Vehicule saved = vehiculeService.create(vehicule);
        return toDto(saved);
    }

    @PutMapping("/{id}")
    public VehiculeDto updateVehicule(
            @PathVariable Long id,
            @RequestBody VehiculeDto dto) {

        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation(dto.getImmatriculation());
        vehicule.setMarque(dto.getMarque());
        vehicule.setModele(dto.getModele());
        vehicule.setCategorie(dto.getCategorie());
        vehicule.setTarifJournalier(dto.getTarifJournalier());
        vehicule.setStatut(dto.getStatut());

        Vehicule updated = vehiculeService.update(id, vehicule);
        return toDto(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteVehicule(@PathVariable Long id) {
        vehiculeService.deleteById(id);
    }

    private VehiculeDto toDto(Vehicule vehicule) {
        VehiculeDto dto = new VehiculeDto();
        dto.setIdVehicule(vehicule.getIdVehicule());
        dto.setImmatriculation(vehicule.getImmatriculation());
        dto.setMarque(vehicule.getMarque());
        dto.setModele(vehicule.getModele());
        dto.setCategorie(vehicule.getCategorie());
        dto.setTarifJournalier(vehicule.getTarifJournalier());
        dto.setStatut(vehicule.getStatut());

        if (vehicule.getAgence() != null) {
            dto.setIdAgence(vehicule.getAgence().getIdAgence());
        }

        return dto;
    }
}
