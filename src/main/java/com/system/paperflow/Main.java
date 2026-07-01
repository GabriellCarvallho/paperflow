package com.system.paperflow;

import java.util.function.Supplier;

import com.system.paperflow.presentation.screen.DashboardScreen;
import com.system.paperflow.presentation.screen.LoginScreen;
import com.system.paperflow.presentation.screen.RegisterScreen;
import com.system.paperflow.presentation.ui.Screen;
import com.system.paperflow.presentation.ui.ScreenUtils;

public class Main {

    public static void main(String[] args) {

        Runnable onNavigateToLogin = () -> ScreenUtils.navigateTo("login");
        Runnable onNavigateToRegister = () -> ScreenUtils.navigateTo("register");

        Supplier<Screen> loginSupplier = () -> new LoginScreen(onNavigateToRegister);
        Supplier<Screen> registerSupplier = () -> new RegisterScreen(onNavigateToLogin);

        ScreenUtils.register("login", loginSupplier);
        ScreenUtils.register("register", registerSupplier);
        ScreenUtils.register("dashboard", DashboardScreen::new);
        ScreenUtils.start("register");
        
    }
    
}
