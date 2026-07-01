package com.system.paperflow;

import com.system.paperflow.presentation.screen.DashboardScreen;
import com.system.paperflow.presentation.screen.LoginScreen;
import com.system.paperflow.presentation.screen.RegisterScreen;
import com.system.paperflow.presentation.ui.ScreenUtils;

public class Main {

    public static void main(String[] args) {

        ScreenUtils.register("login", () -> new LoginScreen(() -> ScreenUtils.navigateTo("register")));
        ScreenUtils.register("register", () -> new RegisterScreen(() -> ScreenUtils.navigateTo("login")));
        ScreenUtils.register("dashboard", DashboardScreen::new);
        ScreenUtils.start("register");
        
    }
    
}
