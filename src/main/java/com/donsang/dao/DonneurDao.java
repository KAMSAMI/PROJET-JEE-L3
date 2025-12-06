package com.donsang.dao;

import com.donsang.model.Donneur;
import com.donsang.model.TypeDeSang;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * DAO pour la gestion des donneurs (implémentation en mémoire)
 */
public class DonneurDao implements IDao<Donneur> {
    
    private static DonneurDao instance;
    private final Map<Long, Donneur> donneurs;
    private final AtomicLong idGenerator;
    
    private DonneurDao() {
        this.donneurs = new ConcurrentHashMap<>();
        this.idGenerator = new AtomicLong(1);
    }
    
    /**
     * Pattern Singleton pour avoir une seule instance
     */
    public static synchronized DonneurDao getInstance() {
        if (instance == null) {
            instance = new DonneurDao();
        }
        return instance;
    }
    
    @Override
    public Donneur create(Donneur donneur) {
        if (donneur == null) {
            throw new IllegalArgumentException("Le donneur ne peut pas être null");
        }
        
        Long id = idGenerator.getAndIncrement();
        donneur.setId(id);
        donneurs.put(id, donneur);
        return donneur;
    }
    
    @Override
    public Optional<Donneur> findById(Long id) {
        return Optional.ofNullable(donneurs.get(id));
    }
    
    @Override
    public List<Donneur> findAll() {
        return new ArrayList<>(donneurs.values());
    }
    
    @Override
    public Donneur update(Donneur donneur) {
        if (donneur == null || donneur.getId() == null) {
            throw new IllegalArgumentException("Le donneur et son ID ne peuvent pas être null");
        }
        
        if (!donneurs.containsKey(donneur.getId())) {
            throw new IllegalArgumentException("Donneur avec l'ID " + donneur.getId() + " n'existe pas");
        }
        
        donneurs.put(donneur.getId(), donneur);
        return donneur;
    }
    
    @Override
    public boolean delete(Long id) {
        if (id == null) {
            return false;
        }
        return donneurs.remove(id) != null;
    }
    
    @Override
    public long count() {
        return donneurs.size();
    }
    
    /**
     * Recherche des donneurs par nom (recherche partielle, insensible à la casse)
     */
    public List<Donneur> findByNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String nomRecherche = nom.toLowerCase().trim();
        return donneurs.values().stream()
                .filter(d -> d.getNom().toLowerCase().contains(nomRecherche) ||
                            d.getPrenom().toLowerCase().contains(nomRecherche))
                .collect(Collectors.toList());
    }
    
    /**
     * Recherche des donneurs par groupe sanguin
     */
    public List<Donneur> findByGroupeSanguin(TypeDeSang groupeSanguin) {
        if (groupeSanguin == null) {
            return new ArrayList<>();
        }
        
        return donneurs.values().stream()
                .filter(d -> d.getGroupeSanguin() == groupeSanguin)
                .collect(Collectors.toList());
    }
    
    /**
     * Recherche des donneurs actifs
     */
    public List<Donneur> findActifs() {
        return donneurs.values().stream()
                .filter(Donneur::isActif)
                .collect(Collectors.toList());
    }
    
    /**
     * Recherche un donneur par email
     */
    public Optional<Donneur> findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return donneurs.values().stream()
                .filter(d -> email.equalsIgnoreCase(d.getEmail()))
                .findFirst();
    }
    
    /**
     * Compte les donneurs par groupe sanguin
     */
    public Map<TypeDeSang, Long> countByGroupeSanguin() {
        return donneurs.values().stream()
                .collect(Collectors.groupingBy(
                    Donneur::getGroupeSanguin,
                    Collectors.counting()
                ));
    }
    
    /**
     * Récupère les donneurs les plus actifs (par nombre de dons)
     */
    public List<Donneur> findTopDonneurs(int limit) {
        return donneurs.values().stream()
                .sorted((d1, d2) -> Integer.compare(d2.getNombreDons(), d1.getNombreDons()))
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * Vérifie si un email existe déjà
     */
    public boolean emailExists(String email) {
        return findByEmail(email).isPresent();
    }
    
    /**
     * Nettoie toutes les données (utile pour les tests)
     */
    public void clear() {
        donneurs.clear();
        idGenerator.set(1);
    }
}