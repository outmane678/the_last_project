package view;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;

public class EmpruntView extends JFrame {
    private JTextField idTextField;
    private JTextField livreIdTextField;
    private JTextField utilisateurIdTextField;
    private JTextField dateEmpruntTextField;
    private JTextField dateRetourTextField;
    private JTable empruntsTable;
    private JButton ajouterButton;
    private JButton modifierButton;
    private JTextField utilisateurRechercheField;  // Champ de recherche par utilisateur
    private JButton rechercherButton;  // Bouton de recherche

    public EmpruntView() {
        setTitle("Gestion des Emprunts");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        // Table des emprunts
        empruntsTable = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[][] {},
                new String[] {"ID Emprunt", "ID Livre", "ID Utilisateur", "Date Emprunt", "Date Retour"}
        );
        empruntsTable.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(empruntsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Formulaire d'ajout
        JPanel formulairePanel = new JPanel();
        formulairePanel.setLayout(new GridLayout(7, 2));  // 7 lignes pour inclure la recherche
        
        formulairePanel.add(new JLabel("ID Emprunt :"));
        idTextField = new JTextField();
        formulairePanel.add(idTextField);
        
        formulairePanel.add(new JLabel("ID Livre :"));
        livreIdTextField = new JTextField();
        formulairePanel.add(livreIdTextField);
        
        formulairePanel.add(new JLabel("ID Utilisateur :"));
        utilisateurIdTextField = new JTextField();
        formulairePanel.add(utilisateurIdTextField);
        
        formulairePanel.add(new JLabel("Date Emprunt (yyyy-MM-dd) :"));
        dateEmpruntTextField = new JTextField();
        formulairePanel.add(dateEmpruntTextField);
        
        formulairePanel.add(new JLabel("Date Retour (yyyy-MM-dd) :"));
        dateRetourTextField = new JTextField();
        formulairePanel.add(dateRetourTextField);
        
        // Recherche des emprunts par utilisateur
        formulairePanel.add(new JLabel("Rechercher par ID Utilisateur :"));
        utilisateurRechercheField = new JTextField();
        formulairePanel.add(utilisateurRechercheField);
        
        // Boutons
        ajouterButton = new JButton("Ajouter Emprunt");
        modifierButton = new JButton("Modifier Emprunt");
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(ajouterButton);
        buttonPanel.add(modifierButton);
        
        // Bouton de recherche
        rechercherButton = new JButton("Consulter l'historique");
        buttonPanel.add(rechercherButton);
        
        // Ajouter les panels à la fenêtre
        panel.add(formulairePanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(panel);
    }
    
    // Getters pour les champs de texte
    public JTextField getIdTextField() {
        return idTextField;
    }
    
    public JTextField getLivreIdTextField() {
        return livreIdTextField;
    }
    
    public JTextField getUtilisateurIdTextField() {
        return utilisateurIdTextField;
    }
    
    public JTextField getDateEmpruntTextField() {
        return dateEmpruntTextField;
    }
    
    public JTextField getDateRetourTextField() {
        return dateRetourTextField;
    }

    // Getter pour le tableau des emprunts
    public JTable getEmpruntsTable() {
        return empruntsTable;
    }
    
    // Getters pour les boutons
    public JButton getAjouterButton() {
        return ajouterButton;
    }
    
    public JButton getModifierButton() {
        return modifierButton;
    }

    // Getter pour le champ de recherche
    public JTextField getUtilisateurRechercheField() {
        return utilisateurRechercheField;
    }

    // Getter pour le bouton de recherche
    public JButton getRechercherButton() {
        return rechercherButton;
    }
}
