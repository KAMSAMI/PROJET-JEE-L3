package com.donsang.util;

import com.donsang.model.*;
import com.donsang.service.*;
import com.donsang.util.ValidationException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.time.LocalDate;

@WebServlet(name = "InitDataServlet", loadOnStartup = 1)
public class InitDataServlet extends HttpServlet {
    
    @Override
    public void init() throws ServletException {
        try {
            DonneurService donneurService = DonneurService.getInstance();
            ReceveurService receveurService = ReceveurService.getInstance();
            DonService donService = DonService.getInstance();
            
            // Créer des donneurs
            Donneur d1 = new Donneur("Dupont", "Jean", "jean.dupont@email.com", 
                "0601020304", TypeDeSang.O_POSITIF, LocalDate.of(1985, 5, 15));
            d1.setVille("Paris");
            donneurService.creerDonneur(d1);
            
            Donneur d2 = new Donneur("Martin", "Marie", "marie.martin@email.com", 
                "0602030405", TypeDeSang.A_POSITIF, LocalDate.of(1990, 8, 20));
            d2.setVille("Lyon");
            donneurService.creerDonneur(d2);
            
            // Créer des receveurs
            Receveur r1 = new Receveur("Durand", "Pierre", "pierre.durand@email.com", 
                "0603040506", TypeDeSang.AB_POSITIF, 
                Receveur.TypeBesoin.SANG, Receveur.UrgenceNiveau.CRITIQUE);
            r1.setVille("Marseille");
            receveurService.creerReceveur(r1);
            
            // Créer des dons
            Don don1 = new Don(Don.TypeDon.SANG_TOTAL, LocalDate.now().minusDays(5), d1.getId());
            don1.setQuantite(450);
            don1.setStatut(Don.StatutDon.EN_STOCK);
            donService.creerDon(don1);
            
        } catch (ValidationException e) {
            System.err.println("Erreur lors de l'initialisation des données: " + e.getMessage());
        }
    }
}