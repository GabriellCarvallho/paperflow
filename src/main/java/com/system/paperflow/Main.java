package com.system.paperflow;

import java.util.function.Supplier;

import com.system.paperflow.application.factory.ResearcherCreator;
import com.system.paperflow.application.persistence.UserPersistence;
import com.system.paperflow.application.usecase.user.RegisterUserUseCase;
import com.system.paperflow.infrastructure.sqlite.SQLiteUserAdapter;
import com.system.paperflow.presentation.controller.LoginController;
import com.system.paperflow.presentation.controller.RegisterController;
import com.system.paperflow.presentation.screen.BlankScreen;
import com.system.paperflow.presentation.screen.LoginScreen;
import com.system.paperflow.presentation.screen.RegisterScreen;
import com.system.paperflow.presentation.ui.Screen;
import com.system.paperflow.presentation.ui.ScreenUtils;

public class Main {

    public static void main(String[] args) {
        UserPersistence userPersistence = new SQLiteUserAdapter("data/paper-flow.db");
        RegisterUserUseCase registerUserUseCase = new RegisterUserUseCase(userPersistence, new ResearcherCreator());
        RegisterController registerController = new RegisterController(registerUserUseCase);
        LoginController loginController = new LoginController(userPersistence);

        Runnable onNavigateToLogin = () -> ScreenUtils.navigateTo("login");
        Runnable onNavigateToRegister = () -> ScreenUtils.navigateTo("register");
        Runnable onNavigateToBlank = () -> ScreenUtils.navigateTo("blank");

        Supplier<Screen> loginSupplier = () -> new LoginScreen(loginController, onNavigateToRegister, onNavigateToBlank);
        Supplier<Screen> registerSupplier = () -> new RegisterScreen(registerController, onNavigateToLogin);

        ScreenUtils.register("login", loginSupplier);
        ScreenUtils.register("register", registerSupplier);
        ScreenUtils.register("blank", BlankScreen::new);
        ScreenUtils.start("register");
        
    }
    
}
