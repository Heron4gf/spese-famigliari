package it.unicam.cs.mpgc.jbudget125639.gui;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.filters.IFilter;
import it.unicam.cs.mpgc.jbudget125639.gui.components.BalanceBox;
import it.unicam.cs.mpgc.jbudget125639.gui.components.HeaderBar;
import it.unicam.cs.mpgc.jbudget125639.gui.components.NavigationBar;
import it.unicam.cs.mpgc.jbudget125639.gui.screens.HomeScreen;
import it.unicam.cs.mpgc.jbudget125639.gui.screens.ScreenManager;
import it.unicam.cs.mpgc.jbudget125639.gui.screens.StatsScreen;
import it.unicam.cs.mpgc.jbudget125639.gui.services.ServiceFactory;
import it.unicam.cs.mpgc.jbudget125639.modules.GlobalModule;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import it.unicam.cs.mpgc.jbudget125639.money.Currency;
import it.unicam.cs.mpgc.jbudget125639.money.MoneyAmount;
import it.unicam.cs.mpgc.jbudget125639.views.View;
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
import lombok.NonNull;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class JBudgetApp extends Application {

    @Setter // Facciamo injection del ModulesManager perchè non possiamo passarlo con un costruttore
    private static ModulesManager modulesManager;

    private StackPane mainContentStack;
    private ScreenManager screenManager;
    private ServiceFactory.ServiceBundle services;

    private View currentView;
    
    private HeaderBar headerBar;
    private BalanceBox balanceBox;
    private HomeScreen homeScreen;
    private StatsScreen statsScreen;

    @Override
    public void start(@NonNull Stage primaryStage) {
        primaryStage.setTitle("Applicazione JBudget");

        initializeServices();
        setupUI(primaryStage);
        initializeScreens();

        screenManager.switchToScreen(HomeScreen.SCREEN_ID);
        updateData();
    }

    private void setupUI(@NonNull Stage primaryStage) {
        BorderPane root = createRootLayout();
        Scene scene = new Scene(root, 1100, 850);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane createRootLayout() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        headerBar = new HeaderBar(this::onUserChanged, this::onFiltersChanged, modulesManager);
        root.setTop(headerBar.getContainer());

        ScrollPane scrollPane = new ScrollPane(createMainContent());
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        root.setCenter(scrollPane);

        return root;
    }

    private StackPane createMainContent() {
        mainContentStack = new StackPane();
        VBox centerLayout = new VBox(20);
        centerLayout.setPadding(new Insets(20, 40, 40, 40));
        centerLayout.setAlignment(Pos.TOP_CENTER);

        balanceBox = new BalanceBox();

        StackPane screensContainer = new StackPane();
        screenManager = new ScreenManager(screensContainer);
        NavigationBar navigationBar = new NavigationBar(screenManager);

        VBox.setVgrow(screensContainer, Priority.ALWAYS);
        centerLayout.getChildren().addAll(
                balanceBox.getNode(),
                navigationBar.getNode(),
                screensContainer
        );

        mainContentStack.getChildren().add(centerLayout);
        return mainContentStack;
    }
    
    private void initializeScreens() {
        homeScreen = new HomeScreen(services, this::showDialog);
        statsScreen = new StatsScreen(services);
        
        screenManager.registerScreen(homeScreen);
        screenManager.registerScreen(statsScreen);

        currentView = modulesManager.getModule(GlobalModule.class).getGlobal();
        screenManager.setViewer(currentView);
        
        // Initialize the header with current users
        refreshHeaderUsers();
    }

    private void initializeServices() {
        ServiceFactory serviceFactory = new ServiceFactory(modulesManager);
        this.services = serviceFactory.createServiceBundle();
    }


    private void onUserChanged(String userName) {
        if (userName == null) {
            return;
        }

        if (HeaderBar.LabelKey.ADD_USER.label().equals(userName)) {
            showAddUserDialog();
            return;
        }

        GlobalModule globalModule = modulesManager.getModule(GlobalModule.class);
        currentView = globalModule.getGlobal().getView(userName);
        screenManager.setViewer(currentView);
        updateData();
    }

    private void onFiltersChanged() {
        updateData();
    }

    private void updateData() {
        if (currentView != null) {
            List<IFilter> filters = headerBar.getCurrentFilters();
            IFilter[] filterArray = filters.toArray(new IFilter[0]);

            Collection<Transaction> filteredTransactions = currentView.getFiltered(filterArray);
            double balance = currentView.total(filterArray);

            balanceBox.updateBalance(new MoneyAmount(balance, Currency.EUR));
            homeScreen.updateTransactions(filteredTransactions);
            statsScreen.updateStats(filteredTransactions);
            headerBar.setCurrentUser(currentView.getName());
        }
    }

    private void showAddUserDialog() {
        Optional<String> userNameResult = services.dialogService.showNewUserDialog();

        userNameResult.ifPresent(userName -> {
            try {
                User newUser = services.userService.createAndAddUser(userName);
                handleNewUserCreated(newUser);
            } catch (Exception e) {
                String errorMessage = "Errore durante la creazione dell'utente: " + e.getMessage();
                services.dialogService.showError(errorMessage);
            }
        });
    }

    private void handleNewUserCreated(User newUser) {
        refreshHeaderUsers();
        currentView = newUser;
        screenManager.setViewer(currentView);
        updateData();
    }
    
    /**
     * Aggiorna la lista degli utenti nella HeaderBar.
     */
    private void refreshHeaderUsers() {
        var global = modulesManager.getModule(GlobalModule.class).getGlobal();
        var viewNames = global.getViews().stream()
                .map(View::getName)
                .filter(name -> !name.equals("global")) // Exclude the global view itself
                .toList();
        headerBar.updateUserList(viewNames);
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
