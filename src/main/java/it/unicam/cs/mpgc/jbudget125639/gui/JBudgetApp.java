package it.unicam.cs.mpgc.jbudget125639.gui;

import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.gui.components.BalanceBox;
import it.unicam.cs.mpgc.jbudget125639.gui.components.HeaderBar;
import it.unicam.cs.mpgc.jbudget125639.gui.components.NavigationBar;
import it.unicam.cs.mpgc.jbudget125639.gui.screens.HomeScreen;
import it.unicam.cs.mpgc.jbudget125639.gui.screens.ScreenManager;
import it.unicam.cs.mpgc.jbudget125639.gui.screens.StatsScreen;
import it.unicam.cs.mpgc.jbudget125639.gui.services.ServiceFactory;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import it.unicam.cs.mpgc.jbudget125639.money.Currency;
import it.unicam.cs.mpgc.jbudget125639.money.MoneyAmount;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;


public class JBudgetApp extends Application {

    @Setter
    private static ModulesManager modulesManager;

    private StackPane mainContentStack;
    private ScreenManager screenManager;
    private ServiceFactory.ServiceBundle services;
    private User currentUser;
    
    private HeaderBar headerBar;
    private BalanceBox balanceBox;
    private NavigationBar navigationBar;
    private HomeScreen homeScreen;
    private StatsScreen statsScreen;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Applicazione JBudget");

        initializeServices();
        setupCurrentUser();
        setupUI(primaryStage);
        initializeScreens();
        
        // Avvia con la schermata Home
        screenManager.switchToScreen(HomeScreen.SCREEN_ID);
        updateData();
    }
    
    private void setupUI(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        headerBar = new HeaderBar(services, this::onUserChanged, this::onFiltersChanged);
        root.setTop(headerBar.getNode());

        mainContentStack = new StackPane();
        VBox centerLayout = new VBox(20);
        centerLayout.setPadding(new Insets(20, 40, 40, 40));
        centerLayout.setAlignment(Pos.TOP_CENTER);

        balanceBox = new BalanceBox();
        
        // Inizializza il ScreenManager
        StackPane screensContainer = new StackPane();
        screenManager = new ScreenManager(screensContainer);
        navigationBar = new NavigationBar(screenManager);
        
        VBox.setVgrow(screensContainer, Priority.ALWAYS);
        centerLayout.getChildren().addAll(
                balanceBox.getNode(),
                navigationBar.getNode(),
                screensContainer
        );

        mainContentStack.getChildren().add(centerLayout);

        ScrollPane scrollPane = new ScrollPane(mainContentStack);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 1100, 850);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void initializeScreens() {
        homeScreen = new HomeScreen(services, this::showDialog);
        statsScreen = new StatsScreen(services);
        
        screenManager.registerScreen(homeScreen);
        screenManager.registerScreen(statsScreen);
        
        // Imposta l'utente corrente per tutte le schermate
        if (currentUser != null) {
            screenManager.setCurrentUser(currentUser);
        }
    }

    private void initializeServices() {
        ServiceFactory serviceFactory = new ServiceFactory(modulesManager);
        this.services = serviceFactory.createServiceBundle();
    }

    private void setupCurrentUser() {
        var users = services.viewService.getViewNames();
        if (!users.isEmpty()) {
            String firstUserName = users.get(0);
            services.viewService.setCurrentView(firstUserName);
            if (services.viewService.isCurrentViewUser()) {
                currentUser = services.viewService.getCurrentViewAsUser();
            }
        }
    }

    private void onUserChanged(String userName) {
        if (userName != null && !userName.equals("Aggiungi Utente...")) {
            services.viewService.setCurrentView(userName);
            if (services.viewService.isCurrentViewUser()) {
                currentUser = services.viewService.getCurrentViewAsUser();
                screenManager.setCurrentUser(currentUser);
            }
            updateData();
        } else if ("Aggiungi Utente...".equals(userName)) {
            showAddUserDialog();
        }
    }

    private void onFiltersChanged() {
        updateData();
    }

    private void updateData() {
        if (currentUser != null) {
            var filters = headerBar.getCurrentFilters();
            var filteredTransactions = currentUser.getFiltered(filters.toArray(new it.unicam.cs.mpgc.jbudget125639.filters.IFilter[0]));
            
            // Aggiorna il balance box
            double balance = filteredTransactions.stream()
                    .mapToDouble(t -> t.getDirection() == TransactionDirection.IN ? 
                            t.getAmount().getValue() : -t.getAmount().getValue())
                    .sum();
            MoneyAmount totalBalance = new MoneyAmount(balance, Currency.EUR);
            balanceBox.updateBalance(totalBalance);
            
            // Aggiorna le schermate con i dati filtrati
            homeScreen.updateTransactions(filteredTransactions);
            statsScreen.updateStats(filteredTransactions);
            
            headerBar.setCurrentUser(currentUser.getName());
        }
    }

    private void showAddUserDialog() {
        var result = services.dialogService.showNewUserDialog();
        if (result.isPresent()) {
            try {
                User newUser = services.userService.createAndAddUser(result.get());
                headerBar.refreshUsers();
                currentUser = newUser;
                screenManager.setCurrentUser(currentUser);
                updateData();
            } catch (Exception e) {
                services.dialogService.showError("Errore durante la creazione dell'utente: " + e.getMessage());
            }
        }
    }
    
    /**
     * Metodo per mostrare dialoghi modali. Utilizzato dalle schermate per mostrare overlay.
     * 
     * @param overlay l'overlay da mostrare
     */
    private void showDialog(StackPane overlay) {
        mainContentStack.getChildren().add(overlay);
    }
}
