package com.donsang.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe représentant un donneur de sang ou d'organe
 */
public class Donneur implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private TypeDeSang groupeSanguin;
    private LocalDate dateNaissance;
    private String adresse;
    private String ville;
    private String codePostal;
    private boolean actif;
    private LocalDate dateInscription;
    private List<Long> historiqueDonIds; // IDs des dons effectués
    private String observations;
    
    // Constructeur par défaut
    public Donneur() {
        this.historiqueDonIds = new ArrayList<>();
        this.dateInscription = LocalDate.now();
        this.actif = true;
    }
    
    // Constructeur avec paramètres essentiels
    public Donneur(String nom, String prenom, String email, String telephone, 
                   TypeDeSang groupeSanguin, LocalDate dateNaissance) {
        this();
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.groupeSanguin = groupeSanguin;
        this.dateNaissance = dateNaissance;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public TypeDeSang getGroupeSanguin() {
        return groupeSanguin;
    }
    
    public void setGroupeSanguin(TypeDeSang groupeSanguin) {
        this.groupeSanguin = groupeSanguin;
    }
    
    public LocalDate getDateNaissance() {
        return dateNaissance;
    }
    
    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
    
    public String getAdresse() {
        return adresse;
    }
    
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
    public String getVille() {
        return ville;
    }
    
    public void setVille(String ville) {
        this.ville = ville;
    }
    
    public String getCodePostal() {
        return codePostal;
    }
    
    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }
    
    public boolean isActif() {
        return actif;
    }
    
    public void setActif(boolean actif) {
        this.actif = actif;
    }
    
    public LocalDate getDateInscription() {
        return dateInscription;
    }
    
    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }
    
    public List<Long> getHistoriqueDonIds() {
        return historiqueDonIds;
    }
    
    public void setHistoriqueDonIds(List<Long> historiqueDonIds) {
        this.historiqueDonIds = historiqueDonIds;
    }
    
    public String getObservations() {
        return observations;
    }
    
    public void setObservations(String observations) {
        this.observations = observations;
    }
    
    // Méthodes utilitaires
    public String getNomComplet() {
        return prenom + " " + nom;
    }
    
    public int getAge() {
        if (dateNaissance != null) {
            return LocalDate.now().getYear() - dateNaissance.getYear();
        }
        return 0;
    }
    
    public void ajouterDon(Long donId) {
        if (!historiqueDonIds.contains(donId)) {
            historiqueDonIds.add(donId);
        }
    }
    
    public int getNombreDons() {
        return historiqueDonIds.size();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Donneur donneur = (Donneur) o;
        return Objects.equals(id, donneur.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Donneur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", groupeSanguin=" + groupeSanguin +
                ", nombreDons=" + getNombreDons() +
                '}';
    }
}