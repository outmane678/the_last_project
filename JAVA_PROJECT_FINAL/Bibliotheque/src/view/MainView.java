package view;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    public JButton btnGererEmprunt;
    public JButton btnGererLivre;
    public JButton btnGererUtilisateur;

    public MainView() {
        setTitle("Main View");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Boutons pour naviguer vers les modules
        btnGererEmprunt = new JButton("Gérer Emprunts");
        btnGererLivre = new JButton("Gérer Livres");
        btnGererUtilisateur = new JButton("Gérer Utilisateurs");

        // Panel pour ajouter les boutons
        JPanel panel = new JPanel();
        panel.add(btnGererLivre);
        panel.add(btnGererEmprunt);
        panel.add(btnGererUtilisateur);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }
}
