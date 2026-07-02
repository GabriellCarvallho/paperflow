package com.system.paperflow.presentation.screen;

import javax.swing.JComponent;

import com.system.paperflow.application.usecase.user.LoginUserUseCase;
import com.system.paperflow.commons.StringUtils;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.presentation.context.ScreenContext;
import com.system.paperflow.presentation.ui.Screen;
import com.system.paperflow.presentation.ui.ScreenUtils;
import com.system.paperflow.presentation.ui.component.Button;
import com.system.paperflow.presentation.ui.component.Center;
import com.system.paperflow.presentation.ui.component.Column;
import com.system.paperflow.presentation.ui.component.FormField;
import com.system.paperflow.presentation.ui.component.StatusMessage;
import com.system.paperflow.presentation.ui.component.Text;
import com.system.paperflow.presentation.ui.component.TextField;
import com.system.paperflow.presentation.ui.component.TextField.TextFieldType;

public class LoginScreen implements Screen {

    private final LoginUserUseCase loginUserUseCase;
    private final ScreenContext context;

    public LoginScreen(LoginUserUseCase loginUserUseCase, ScreenContext context) {
        this.loginUserUseCase = loginUserUseCase;
        this.context = context;
    }

    @Override
    public JComponent build() {
        TextField emailInput = TextField.create(TextFieldType.TEXT).fullWidth();

        TextField passwordInput = TextField.create(TextFieldType.PASSWORD).fullWidth();

        StatusMessage statusMessage = StatusMessage.create();

        Column form = Column.create().center().gap(14);
        form.add(Text.title("Paper Flow"));
        form.add(Text.subtitle("Entre para continuar"));
        form.add(FormField.create("E-mail", emailInput));
        form.add(FormField.create("Senha", passwordInput));
        form.add(Button.primary("Entrar").fullWidth().onClick(() -> {
            statusMessage.clear();

            try {
                Researcher researcher = loginUserUseCase.execute(emailInput.text(), passwordInput.password());
                context.startSession(researcher);
                ScreenUtils.navigateTo("events");
            } catch (Exception exception) {
                statusMessage.error(exception.getMessage());
            }
        }));
        form.add(Button.secondary("Criar conta").fullWidth().onClick(() -> ScreenUtils.navigateTo("register")));
        form.add(statusMessage);

        return Center.create(form).build();
    }

    @Override
    public String withTitle() {
        return "Paper Flow - Login";
    }
}
