package it.unicam.cs.mpgc.jbudget125639.gui.services;

import it.unicam.cs.mpgc.jbudget125639.gui.services.validation.ValidationService;
import it.unicam.cs.mpgc.jbudget125639.gui.services.create.TransactionService;
import it.unicam.cs.mpgc.jbudget125639.gui.services.create.UserService;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class ServiceFactory {
    
    @NonNull
    private final ModulesManager modulesManager;
    
    private final Map<Class<?>, Object> services = new ConcurrentHashMap<>();
    
    @SuppressWarnings("unchecked")
    private <T> T getOrCreate(Class<T> serviceClass, Supplier<T> factory) {
        return (T) services.computeIfAbsent(serviceClass, k -> factory.get());
    }
    
    public ValidationService getValidationService() {
        return getOrCreate(ValidationService.class, ValidationService::new);
    }
    
    public DialogService getDialogService() {
        return getOrCreate(DialogService.class, DialogService::new);
    }
    
    public TransactionService getTransactionService() {
        return getOrCreate(TransactionService.class, () -> new TransactionService(getValidationService()));
    }
    
    public UserService getUserService() {
        return getOrCreate(UserService.class, () -> new UserService(getValidationService(), modulesManager));
    }
    
    public ServiceBundle createServiceBundle() {
        return new ServiceBundle(getDialogService(), getTransactionService(), getUserService());
    }
    
    @RequiredArgsConstructor
    public static class ServiceBundle {
        @NonNull public final DialogService dialogService;
        @NonNull public final TransactionService transactionService;
        @NonNull public final UserService userService;
    }
}
