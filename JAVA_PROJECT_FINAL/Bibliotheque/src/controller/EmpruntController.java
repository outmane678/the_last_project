package controller;

import exceptions.EmpruntExisteException;
import exceptions.EmpruntNonTrouveException;
import model.Emprunt;
import model.EmpruntModel;
import model.Livre;
import model.LivreModel;
import model.Utilisateur;
import model.UtilisateurModel;
import view.EmpruntView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EmpruntController {
    private EmpruntModel model;
    private EmpruntView view;
    private LivreModel livreModel;
    private UtilisateurModel utilisateurModel;

    public EmpruntController() {
        // Initialisation des modèles
        this.livreModel = new LivreModel("C:\\Users\\DELL\\Desktop\\livres.csv");
        this.utilisateurModel = new UtilisateurModel("C:\\Users\\DELL\\Desktop\\utilisateurs.csv");
        this.model = new EmpruntModel("C:\\Users\\DELL\\Desktop\\emprunts.csv", utilisateurModel.getUtilisateurs(), livreModel.getLivres());
        this.view = new EmpruntView();

        // Ajouter les listeners aux boutons
        view.getAjouterButton().addActionListener(e -> ajouterEmprunt());
        view.getModifierButton().addActionListener(e -> modifierEmprunt());
        view.getRechercherButton().addActionListener(e -> rechercherEmpruntsParUtilisateur());

        // Charger les emprunts dans la vue
        chargerEmprunts();

        // Afficher la vue
        view.setVisible(true);
    }

    private void chargerEmprunts() {
        List<Emprunt> emprunts = model.getEmprunts();
        DefaultTableModel tableModel = (DefaultTableModel) view.getEmpruntsTable().getModel();
        tableModel.setRowCount(0); // Effacer les lignes actuelles
        for (Emprunt emprunt : emprunts) {
            tableModel.addRow(new Object[]{
                    emprunt.getId(),
                    emprunt.getLivre().getId(),
                    emprunt.getUtilisateur().getId(),
                    emprunt.getDateEmprunt(),
                    emprunt.getDateRetour()
            });
        }
    }

    private void ajouterEmprunt() {
        try {
            int id = Integer.parseInt(view.getIdTextField().getText());
            int livreId = Integer.parseInt(view.getLivreIdTextField().getText());
            int utilisateurId = Integer.parseInt(view.getUtilisateurIdTextField().getText());
            LocalDate dateEmprunt = LocalDate.parse(view.getDateEmpruntTextField().getText());
            LocalDate dateRetour = LocalDate.parse(view.getDateRetourTextField().getText());
    
            LocalDate today = LocalDate.now();
    
            // Vérification des dates
            if (!dateEmprunt.equals(today)) {
                JOptionPane.showMessageDialog(view, "La date d'emprunt doit être la date d'aujourd'hui : " + today);
                return;
            }
    
            if (!dateRetour.isAfter(today)) {
                JOptionPane.showMessageDialog(view, "La date de retour doit être strictement supérieure à la date d'aujourd'hui :" + today);
                return;
            }
    
            // Vérification si l'ID existe déjà
            if (model.getEmprunts().stream().anyMatch(e -> e.getId() == id)) {
                throw new EmpruntExisteException("Erreur : Un emprunt avec cet ID existe déjà !");
            }
    
            // Vérification si le livre et l'utilisateur existent
            Livre livre = livreModel.getLivres().stream()
                    .filter(l -> l.getId() == livreId)
                    .findFirst()
                    .orElseThrow(() -> new EmpruntNonTrouveException("Erreur : Livre introuvable avec cet ID !"));
    
            Utilisateur utilisateur = utilisateurModel.getUtilisateurs().stream()
                    .filter(u -> u.getId() == utilisateurId)
                    .findFirst()
                    .orElseThrow(() -> new EmpruntNonTrouveException("Erreur : Utilisateur introuvable avec cet ID !"));
    
            // Ajout de l'emprunt
            Emprunt emprunt = new Emprunt(id, livre, utilisateur, dateEmprunt, dateRetour);
            model.ajouterEmprunt(emprunt);
            JOptionPane.showMessageDialog(view, "Emprunt ajouté avec succès !");
            chargerEmprunts();
            reinitialiserChamps();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Erreur de saisie : ID, Livre ID, Utilisateur ID doivent être des entiers.");
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(view, "Erreur de format de date : Utilisez le format yyyy-MM-dd.");
        } catch (EmpruntExisteException | EmpruntNonTrouveException e) {
            JOptionPane.showMessageDialog(view, e.getMessage());
        }
    }
    
    private void modifierEmprunt() {
        int selectedRow = view.getEmpruntsTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Veuillez sélectionner un emprunt.");
            return;
        }
    
        try {
            int id = (int) view.getEmpruntsTable().getValueAt(selectedRow, 0);
            String newDateRetour = JOptionPane.showInputDialog(view, "Nouvelle date de retour (yyyy-MM-dd) :");
    
            LocalDate dateRetour = LocalDate.parse(newDateRetour);
            LocalDate today = LocalDate.now();
    
            // Validation stricte : La nouvelle date de retour doit être strictement supérieure à aujourd'hui
            if (!dateRetour.isAfter(today)) {
                JOptionPane.showMessageDialog(view, "Erreur : La date de retour doit être strictement supérieure à la date d'aujourd'hui :"+today);
                return;
            }
    
            model.modifierEmprunt(id, dateRetour);
            JOptionPane.showMessageDialog(view, "Emprunt modifié avec succès !");
            chargerEmprunts();
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(view, "Erreur de format de date : Utilisez le format yyyy-MM-dd.");
        } catch (EmpruntNonTrouveException e) {
            JOptionPane.showMessageDialog(view, e.getMessage());
        }
    }
    
    
    // rechercher 
    private void rechercherEmpruntsParUtilisateur() {
        try {
            String utilisateurIdText = view.getUtilisateurRechercheField().getText();  // Champs de recherche dans la vue
    
            // Si l'ID est vide, afficher tous les emprunts
            if (utilisateurIdText.isEmpty()) {
                // Appeler la méthode du modèle pour récupérer tous les emprunts
                List<Emprunt> filteredEmprunts = model.getEmpruntsParUtilisateur(null);
    
                // Mettre à jour le tableau avec tous les emprunts
                DefaultTableModel tableModel = (DefaultTableModel) view.getEmpruntsTable().getModel();
                tableModel.setRowCount(0); // Effacer les lignes actuelles
    
                // Ajouter les résultats dans le tableau
                for (Emprunt emprunt : filteredEmprunts) {
                    tableModel.addRow(new Object[]{
                            emprunt.getId(),
                            emprunt.getLivre().getId(),
                            emprunt.getUtilisateur().getId(),
                            emprunt.getDateEmprunt(),
                            emprunt.getDateRetour()
                    });
                }
                return;
            }
    
            // Si l'ID n'est pas vide, tenter de le convertir en entier
            int utilisateurId = Integer.parseInt(utilisateurIdText);
    
            // Appeler la méthode du modèle pour récupérer les emprunts de l'utilisateur
            List<Emprunt> filteredEmprunts = model.getEmpruntsParUtilisateur(utilisateurId);
    
            DefaultTableModel tableModel = (DefaultTableModel) view.getEmpruntsTable().getModel();
            tableModel.setRowCount(0); // Effacer les lignes actuelles
    
            // Ajouter les résultats dans le tableau
            for (Emprunt emprunt : filteredEmprunts) {
                tableModel.addRow(new Object[]{
                        emprunt.getId(),
                        emprunt.getLivre().getId(),
                        emprunt.getUtilisateur().getId(),
                        emprunt.getDateEmprunt(),
                        emprunt.getDateRetour()
                });
            }
    
        } catch (NumberFormatException e) {
            // Si le format de l'ID est incorrect, afficher un message d'erreur
            JOptionPane.showMessageDialog(view, "Veuillez entrer un ID utilisateur valide.");
        }
    }

    
    

    private void reinitialiserChamps() {
        view.getIdTextField().setText("");
        view.getLivreIdTextField().setText("");
        view.getUtilisateurIdTextField().setText("");
        view.getDateEmpruntTextField().setText("");
        view.getDateRetourTextField().setText("");
    }

    public EmpruntView getView() {
        return view;
    }
}
