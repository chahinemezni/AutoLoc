package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.web.dto.ContratDto;
import tn.esprit.autoloc.web.dto.PaiementDto;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/contrats")
@RequiredArgsConstructor
@Tag(name = "Contrats", description = "Gestion des contrats de location")
public class ContratController {

    private final IContratService contratService;

    @Operation(summary = "Lister tous les contrats")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des contrats récupérée")
    })
    @GetMapping
    public List<ContratDto> findAll() {
        return contratService.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Operation(summary = "Trouver un contrat par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrat trouvé"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    @GetMapping("/{id}")
    public ContratDto findById(
            @Parameter(description = "Identifiant du contrat")
            @PathVariable Long id) {
        return toDto(contratService.findById(id));
    }

    @Operation(summary = "Créer un contrat avec ses paiements")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contrat créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContratDto create(@RequestBody ContratDto dto) {
        Contrat contrat = toEntity(dto);
        return toDto(contratService.create(contrat));
    }

    @Operation(summary = "Modifier un contrat")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrat modifié"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    @PutMapping("/{id}")
    public ContratDto update(
            @Parameter(description = "Identifiant du contrat")
            @PathVariable Long id,
            @RequestBody ContratDto dto) {
        Contrat contrat = toEntity(dto);
        return toDto(contratService.update(id, contrat));
    }

    @Operation(summary = "Supprimer un contrat")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contrat supprimé"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "Identifiant du contrat")
            @PathVariable Long id) {
        contratService.deleteById(id);
    }

    private ContratDto toDto(Contrat contrat) {
        ContratDto dto = new ContratDto();
        dto.setIdContrat(contrat.getIdContrat());
        dto.setDateSignature(contrat.getDateSignature());
        dto.setMontantTotal(contrat.getMontantTotal());
        dto.setValide(contrat.isValide());

        if (contrat.getPaiements() != null) {
            List<PaiementDto> paiements = contrat.getPaiements()
                    .stream()
                    .map(paiement -> {
                        PaiementDto paiementDto = new PaiementDto();
                        paiementDto.setIdPaiement(paiement.getIdPaiement());
                        paiementDto.setMontant(paiement.getMontant());
                        paiementDto.setDatePaiement(paiement.getDatePaiement());
                        paiementDto.setModePaiement(paiement.getModePaiement());
                        return paiementDto;
                    })
                    .toList();

            dto.setPaiements(paiements);
        }

        return dto;
    }

    private Contrat toEntity(ContratDto dto) {
        Contrat contrat = new Contrat();
        contrat.setIdContrat(dto.getIdContrat());
        contrat.setDateSignature(dto.getDateSignature());
        contrat.setMontantTotal(dto.getMontantTotal());
        contrat.setValide(dto.isValide());

        if (dto.getPaiements() != null) {
            List<Paiement> paiements = new ArrayList<>();

            for (PaiementDto paiementDto : dto.getPaiements()) {
                Paiement paiement = new Paiement();
                paiement.setIdPaiement(paiementDto.getIdPaiement());
                paiement.setMontant(paiementDto.getMontant());
                paiement.setDatePaiement(paiementDto.getDatePaiement());
                paiement.setModePaiement(paiementDto.getModePaiement());
                paiement.setContrat(contrat);
                paiements.add(paiement);
            }

            contrat.setPaiements(paiements);
        }

        return contrat;
    }
}