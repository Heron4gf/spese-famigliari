package it.unicam.cs.mpgc.jbudget125639.modules;

import it.unicam.cs.mpgc.jbudget125639.JBudgetLauncher;
import it.unicam.cs.mpgc.jbudget125639.MainController;
import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.model.FXTransactionService;
import it.unicam.cs.mpgc.jbudget125639.model.TransactionService;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.Module;
import it.unicam.cs.mpgc.jbudget125639.views.Global;
import it.unicam.cs.mpgc.jbudget125639.views.ViewsHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class FrontendModule extends Application implements Module {
    private ModulesHandler modulesManager;
    private Stage stage;

    public FrontendModule() {
        this.modulesManager = JBudgetLauncher.modulesManager;
        if (this.modulesManager != null) {
            this.modulesManager.addModule(this);
        }
    }

    @Override
    public String name() {
        return "Frontend";
    }

    @Override
    public void load() {}

    @Override
    public void unload() {
        if (stage != null && stage.isShowing()) {
            stage.close();
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        GlobalModule globalModule = modulesManager.getModule(GlobalModule.class);
        Objects.requireNonNull(globalModule, "GlobalModule dependency not loaded");

        ViewsHandler viewsHandler = globalModule.getGlobal();
        TransactionService transactionService = createTransactionService(viewsHandler);
        Parent root = getParent(viewsHandler, transactionService);
        setupAndShowStage(stage, root);
    }

    private Parent getParent(ViewsHandler handler, TransactionService service) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unicam/cs/mpgc/jbudget125639/main-view.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();
        controller.initModel(handler, service);
        return root;
    }

    private TransactionService createTransactionService(ViewsHandler viewsHandler) {
        TransactionService service = new FXTransactionService();
        Collection<User> users = viewsHandler.getViews().stream()
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .collect(Collectors.toList());
        service.setAllUsers(users);
        return service;
    }

    private void setupAndShowStage(Stage stage, Parent root) {
        Scene scene = new Scene(root, 1000, 700);
        stage.setTitle("JBudget");
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
}