package com.system.paperflow.presentation.screen;

import javax.swing.JComponent;

import com.system.paperflow.presentation.controller.ControllerResult;
import com.system.paperflow.presentation.controller.LoginController;
import com.system.paperflow.presentation.ui.Screen;
import com.system.paperflow.presentation.ui.component.Button;
import com.system.paperflow.presentation.ui.component.Center;
import com.system.paperflow.presentation.ui.component.Column;
import com.system.paperflow.presentation.ui.component.FormField;
import com.system.paperflow.presentation.ui.component.StatusMessage;
import com.system.paperflow.presentation.ui.component.Text;
import com.system.paperflow.presentation.ui.component.TextField;
import com.system.paperflow.presentation.ui.component.TextField.TextFieldType;

public class LoginScreen implements Screen {

    private final LoginController controller;
    private final Runnable onRegisterClick;
    private final Runnable onLoginSuccess;

    public LoginScreen(LoginController controller, Runnable onRegisterClick, Runnable onLoginSuccess) {
        this.controller = controller;
        this.onRegisterClick = onRegisterClick;
        this.onLoginSuccess = onLoginSuccess;
    }

    @Override
    public JComponent build() {
        TextField emailInput = TextField.create(TextFieldType.TEXT).fullWidth();

        TextField passwordInput = TextField.create(TextFieldType.PASSWORD).fullWidth();

        StatusMessage statusMessage = StatusMessage.create();

        Column form = Column.create()
            .center()
            .gap(14)
            .children(

                Text.title("Paper Flow"),
                Text.subtitle("Entre para continuar"),

                FormField.create("E-mail", emailInput),
                FormField.create("Senha", passwordInput),

                Button.primary("Entrar").fullWidth()
                    .onClick(() -> handleLogin(emailInput, passwordInput, statusMessage)),
                Button.secondary("Criar conta").fullWidth().onClick(onRegisterClick),
                statusMessage
            );

        return Center.create(form).build();
    }

    @Override
    public String withTitle() {
        return "Paper Flow - Login";
    }

    private void handleLogin(TextField emailInput, TextField passwordInput, StatusMessage statusMessage) {
        ControllerResult result = controller.execute(emailInput.text(), passwordInput.password());

        if (result.success()) {
            statusMessage.success(result.message());
            onLoginSuccess.run();
        } else {
            statusMessage.error(result.message());
        }
    }
}
