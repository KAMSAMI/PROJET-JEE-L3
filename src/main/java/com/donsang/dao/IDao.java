package com.donsang.dao;

import java.util.List;
import java.util.Optional;

/**
 * Interface générique pour les opérations CRUD
 * @param <T> Type d'entité
 */
public interface IDao<T> {
    
    /**
     * Crée une nouvelle entité
     * @param entity L'entité à créer
     * @return L'entité créée avec son ID
     */
    T create(T entity);
    
    /**
     * Récupère une entité par son ID
     * @param id L'identifiant de l'entité
     * @return Optional contenant l'entité si trouvée
     */
    Optional<T> findById(Long id);
    
    /**
     * Récupère toutes les entités
     * @return Liste de toutes les entités
     */
    List<T> findAll();
    
    /**
     * Met à jour une entité existante
     * @param entity L'entité à mettre à jour
     * @return L'entité mise à jour
     */
    T update(T entity);
    
    /**
     * Supprime une entité par son ID
     * @param id L'identifiant de l'entité à supprimer
     * @return true si la suppression a réussi
     */
    boolean delete(Long id);
    
    /**
     * Compte le nombre total d'entités
     * @return Le nombre d'entités
     */
    long count();
}