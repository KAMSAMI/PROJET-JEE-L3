package com.donsang.dao;

import com.donsang.model.Don;
import com.donsang.model.Don.StatutDon;
import com.donsang.model.Don.TypeDon;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * DAO pour la gestion des dons (implémentation en mémoire)
 */
public class DonDao implements IDao<Don> {
    
    private static DonDao instance;
    private final Map<Long, Don> dons;
    private final AtomicLong idGenerator;
    
    private DonDao() {
        this.dons = new ConcurrentHashMap<>();
        this.idGenerator = new AtomicLong(1);
    }
    
    /**
     * Pattern Singleton
     */
    public static synchronized DonDao getInstance() {
        if (instance == null) {
            instance = new DonDao();
        }
        return instance;
    }
    
    @Override
    public Don create(Don don) {
        if (don == null) {
            throw new IllegalArgumentException("Le don ne peut pas être null");
        }
        
        Long id = idGenerator.getAndIncrement();
        don.setId(id);
        dons.put(id, don);
        return don;
    }
    
    @Override
    public Optional<Don> findById(Long id) {
        return Optional.ofNullable(dons.get(id));
    }
    
    @Override
    public List<Don> findAll() {
        return new ArrayList<>(dons.values());
    }
    
    @Override
    public Don update(Don don) {
        if (don == null || don.getId() == null) {
            throw new IllegalArgumentException("Le don et son ID ne peuvent pas être null");
        }
        
        if (!dons.containsKey(don.getId())) {
            throw new IllegalArgumentException("Don avec l'ID " + don.getId() + " n'existe pas");
        }
        
        dons.put(don.getId(), don);
        return don;
    }
    
    @Override
    public boolean delete(Long id) {
        if (id == null) {
            return false;
        }
        return dons.remove(id) != null;
    }
    
    @Override
    public long count() {
        return dons.size();
    }
    
    /**
     * Recherche des dons par donneur
     */
    public List<Don> findByDonneur(Long donneurId) {
        if (donneurId == null) {
            return new ArrayList<>();
        }
        
        return dons.values().stream()
                .filter(d -> donneurId.equals(d.getDonneurId()))
                .sorted((d1, d2) -> d2.getDateDon().compareTo(d1.getDateDon()))
                .collect(Collectors.toList());
    }
    
    /**
     * Recherche des dons par receveur
     */
    public List<Don> findByReceveur(Long receveurId) {
        if (receveurId == null) {
            return new ArrayList<>();
        }
        
        return dons.values().stream()
                .filter(d -> receveurId.equals(d.getReceveurId()))
                .sorted((d1, d2) -> d2.getDateDon().compareTo(d1.getDateDon()))
                .collect(Collectors.toList());
    }
    
    /**
     * Recherche des dons par type
     */
    public List<Don> findByType(TypeDon typeDon) {
        if (typeDon == null) {
            return new ArrayList<>();
        }
        
        return dons.values().stream()
                .filter(d -> d.getTypeDon() == typeDon)
                .collect(Collectors.toList());
    }
    
    /**
     * Recherche des dons par statut
     */
    public List<Don> findByStatut(StatutDon statut) {
        if (statut == null) {
            return new ArrayList<>();
        }
        
        return dons.values().stream()
                .filter(d -> d.getStatut() == statut)
                .collect(Collectors.toList());
    }
    
    /**
     * Recherche des dons de sang disponibles
     */
    public List<Don> findDonsSangDisponibles() {
        return dons.values().stream()
                .filter(d -> d.getTypeDon().estDonSang())
                .filter(d -> d.getStatut() == StatutDon.EN_STOCK || 
                            d.getStatut() == StatutDon.COMPLETE)
                .filter(Don::estValide)
                .collect(Collectors.toList());
    }
    
    /**
     * Recherche des dons non attribués
     */
    public List<Don> findNonAttribues() {
        return dons.values().stream()
                .filter(d -> d.getReceveurId() == null)
                .filter(d -> d.getStatut() != StatutDon.ANNULE && 
                            d.getStatut() != StatutDon.UTILISE)
                .collect(Collectors.toList());
    }
    
    /**
     * Recherche des dons par période
     */
    public List<Don> findByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut == null || dateFin == null) {
            return new ArrayList<>();
        }
        
        return dons.values().stream()
                .filter(d -> d.getDateDon() != null)
                .filter(d -> !d.getDateDon().isBefore(dateDebut) && 
                            !d.getDateDon().isAfter(dateFin))
                .sorted((d1, d2) -> d2.getDateDon().compareTo(d1.getDateDon()))
                .collect(Collectors.toList());
    }
    
    /**
     * Recherche des dons du mois en cours
     */
    public List<Don> findDuMoisCourant() {
        LocalDate maintenant = LocalDate.now();
        LocalDate debutMois = maintenant.withDayOfMonth(1);
        LocalDate finMois = maintenant.withDayOfMonth(maintenant.lengthOfMonth());
        return findByPeriode(debutMois, finMois);
    }
    
    /**
     * Compte les dons par type
     */
    public Map<TypeDon, Long> countByType() {
        return dons.values().stream()
                .collect(Collectors.groupingBy(
                    Don::getTypeDon,
                    Collectors.counting()
                ));
    }
    
    /**
     * Compte les dons par statut
     */
    public Map<StatutDon, Long> countByStatut() {
        return dons.values().stream()
                .collect(Collectors.groupingBy(
                    Don::getStatut,
                    Collectors.counting()
                ));
    }
    
    /**
     * Calcule la quantité totale de sang disponible (en ml)
     */
    public double getQuantiteTotaleSangDisponible() {
        return findDonsSangDisponibles().stream()
                .mapToDouble(Don::getQuantite)
                .sum();
    }
    
    /**
     * Recherche un don par numéro de référence
     */
    public Optional<Don> findByNumeroReference(String numeroReference) {
        if (numeroReference == null || numeroReference.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return dons.values().stream()
                .filter(d -> numeroReference.equals(d.getNumeroReference()))
                .findFirst();
    }
    
    /**
     * Récupère les derniers dons (limite)
     */
    public List<Don> findRecents(int limit) {
        return dons.values().stream()
                .sorted((d1, d2) -> {
                    if (d1.getDateDon() == null) return 1;
                    if (d2.getDateDon() == null) return -1;
                    return d2.getDateDon().compareTo(d1.getDateDon());
                })
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * Nettoie toutes les données
     */
    public void clear() {
        dons.clear();
        idGenerator.set(1);
    }
}