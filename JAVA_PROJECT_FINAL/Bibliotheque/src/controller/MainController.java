package controller;

import view.MainView;

public class MainController {
    private MainView mainView;

    public MainController(MainView mainView) {
        this.mainView = mainView;

        // Action pour ouvrir le contrôleur Emprunt
        mainView.btnGererEmprunt.addActionListener(e -> new EmpruntController());

        // Action pour ouvrir le contrôleur Livre
        mainView.btnGererLivre.addActionListener(e -> new LivreController());

        // Action pour ouvrir le contrôleur Utilisateur
        mainView.btnGererUtilisateur.addActionListener(e -> new UtilisateurController());
    }
}
