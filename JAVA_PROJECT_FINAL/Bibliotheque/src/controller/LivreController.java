package controller;

import model.Livre;
import model.LivreModel;
import exceptions.LivreDejaExisteException;
import exceptions.LivreNonTrouveException;
import view.LivreView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class LivreController {
    private final LivreModel model;
    private final LivreView view;

    public LivreController() {
        this.model = new LivreModel("C:\\Users\\DELL\\Desktop\\livres.csv");
        this.view = new LivreView();

        // Ajouter les listeners aux boutons
        view.getAjouterButton().addActionListener(e -> ajouterLivre());
        view.getModifierButton().addActionListener(e -> modifierLivre());
        view.getSupprimerButton().addActionListener(e -> supprimerLivre());

        // Ajouter un DocumentListener au champ de recherche
        view.getRechercheTextField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                rechercherLivres();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                rechercherLivres();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                rechercherLivres();
            }
        });

        // Charger les livres dans la vue
        chargerLivres();
    }

    public LivreView getView() {
        return view;
    }

    private void chargerLivres() {
        List<Livre> livres = model.getLivres();
        DefaultTableModel tableModel = (DefaultTableModel) view.getLivresTable().getModel();
        tableModel.setRowCount(0); // Effacer les lignes actuelles
        for (Livre livre : livres) {
            tableModel.addRow(new Object[]{
                livre.getId(),
                livre.getTitre(),
                livre.getAuteur(),
                livre.getAnneePublication(),
                livre.getGenre()
            });
        }
    }

    private void ajouterLivre() {
        try {
            int id = Integer.parseInt(view.getIdTextField().getText());
            String titre = view.getTitreTextField().getText();
            String auteur = view.getAuteurTextField().getText();
            int annee = Integer.parseInt(view.getAnneeTextField().getText());
            String genre = view.getGenreTextField().getText();

            Livre livre = new Livre(id, titre, auteur, annee, genre);
            model.ajouterLivre(livre); // Peut lancer LivreDejaExisteException
            JOptionPane.showMessageDialog(view, "Livre ajouté avec succès !");
            chargerLivres();
            réinitialiserChamps();
        } catch (LivreDejaExisteException e) {
            JOptionPane.showMessageDialog(view, "Erreur : Un livre avec cet ID existe déjà !");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Erreur de saisie : ID et Année doivent être des entiers.");
        }
    }

    private void modifierLivre() {
        int selectedRow = view.getLivresTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Veuillez sélectionner un livre.");
            return;
        }

        try {
            int idSelectionne = (int) view.getLivresTable().getValueAt(selectedRow, 0);
            int idSaisi = Integer.parseInt(view.getIdTextField().getText());

            if (idSaisi != idSelectionne) {
                JOptionPane.showMessageDialog(view, "Erreur : Vous ne pouvez pas modifier l'ID du livre sélectionné.");
                return;
            }

            String titre = view.getTitreTextField().getText();
            String auteur = view.getAuteurTextField().getText();
            int annee = Integer.parseInt(view.getAnneeTextField().getText());
            String genre = view.getGenreTextField().getText();

            model.modifierLivre(idSelectionne, titre, auteur, annee, genre);
            JOptionPane.showMessageDialog(view, "Livre modifié avec succès !");
            chargerLivres();
            réinitialiserChamps();
        } catch (LivreNonTrouveException e) {
            JOptionPane.showMessageDialog(view, "Erreur : Livre non trouvé.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Erreur de saisie : ID et Année doivent être des entiers.");
        }
    }

    private void supprimerLivre() {
        int selectedRow = view.getLivresTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Veuillez sélectionner un livre à supprimer.");
            return;
        }

        try {
            int id = (int) view.getLivresTable().getValueAt(selectedRow, 0);
            model.supprimerLivre(id);
            JOptionPane.showMessageDialog(view, "Livre supprimé avec succès !");
            chargerLivres();
        } catch (LivreNonTrouveException e) {
            JOptionPane.showMessageDialog(view, "Erreur : Livre non trouvé.");
        }
    }

    // The current method is fine, but let's ensure it updates the table properly.
    private void rechercherLivres() {
        String query = view.getRechercheTextField().getText().trim(); // Trim any extra spaces
        List<Livre> resultats = model.rechercherLivres(query); // Filter based on query
    
        DefaultTableModel tableModel = (DefaultTableModel) view.getLivresTable().getModel();
        tableModel.setRowCount(0);  // Clear previous results
    
        // Debug: Afficher les résultats dans la console pour voir si la recherche fonctionne
        System.out.println("Livres trouvés : " + resultats.size());
    
        // Add the filtered results back to the table
        for (Livre livre : resultats) {
            tableModel.addRow(new Object[]{
                livre.getId(),
                livre.getTitre(),
                livre.getAuteur(),
                livre.getAnneePublication(),
                livre.getGenre()
            });
        }
    }
    
    


    private void réinitialiserChamps() {
        view.getIdTextField().setText("");
        view.getTitreTextField().setText("");
        view.getAuteurTextField().setText("");
        view.getAnneeTextField().setText("");
        view.getGenreTextField().setText("");
    }
}
