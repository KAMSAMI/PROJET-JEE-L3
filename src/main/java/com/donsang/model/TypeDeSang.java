package com.donsang.model;

/**
 * Énumération représentant les différents groupes sanguins
 */
public enum TypeDeSang {
    A_POSITIF("A+", "Peut donner à: A+, AB+", "Peut recevoir de: A+, A-, O+, O-"),
    A_NEGATIF("A-", "Peut donner à: A+, A-, AB+, AB-", "Peut recevoir de: A-, O-"),
    B_POSITIF("B+", "Peut donner à: B+, AB+", "Peut recevoir de: B+, B-, O+, O-"),
    B_NEGATIF("B-", "Peut donner à: B+, B-, AB+, AB-", "Peut recevoir de: B-, O-"),
    AB_POSITIF("AB+", "Peut donner à: AB+", "Peut recevoir de: Tous"),
    AB_NEGATIF("AB-", "Peut donner à: AB+, AB-", "Peut recevoir de: A-, B-, AB-, O-"),
    O_POSITIF("O+", "Peut donner à: A+, B+, AB+, O+", "Peut recevoir de: O+, O-"),
    O_NEGATIF("O-", "Peut donner à: Tous", "Peut recevoir de: O-");
    
    private final String designation;
    private final String compatibiliteDon;
    private final String compatibiliteReception;
    
    TypeDeSang(String designation, String compatibiliteDon, String compatibiliteReception) {
        this.designation = designation;
        this.compatibiliteDon = compatibiliteDon;
        this.compatibiliteReception = compatibiliteReception;
    }
    
    public String getDesignation() {
        return designation;
    }
    
    public String getCompatibiliteDon() {
        return compatibiliteDon;
    }
    
    public String getCompatibiliteReception() {
        return compatibiliteReception;
    }
    
    /**
     * Vérifie si ce groupe sanguin peut donner au groupe cible
     */
    public boolean peutDonnerA(TypeDeSang cible) {
        switch(this) {
            case O_NEGATIF:
                return true; // Donneur universel
            case O_POSITIF:
                return cible == A_POSITIF || cible == B_POSITIF || 
                       cible == AB_POSITIF || cible == O_POSITIF;
            case A_NEGATIF:
                return cible == A_POSITIF || cible == A_NEGATIF || 
                       cible == AB_POSITIF || cible == AB_NEGATIF;
            case A_POSITIF:
                return cible == A_POSITIF || cible == AB_POSITIF;
            case B_NEGATIF:
                return cible == B_POSITIF || cible == B_NEGATIF || 
                       cible == AB_POSITIF || cible == AB_NEGATIF;
            case B_POSITIF:
                return cible == B_POSITIF || cible == AB_POSITIF;
            case AB_NEGATIF:
                return cible == AB_POSITIF || cible == AB_NEGATIF;
            case AB_POSITIF:
                return cible == AB_POSITIF;
            default:
                return false;
        }
    }
    
    /**
     * Obtient un TypeDeSang à partir de sa désignation
     */
    public static TypeDeSang fromDesignation(String designation) {
        for (TypeDeSang type : values()) {
            if (type.designation.equalsIgnoreCase(designation)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Groupe sanguin invalide: " + designation);
    }
}