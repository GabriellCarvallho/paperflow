package com.system.paperflow.presentation.screen;

import javax.swing.JComponent;

import com.system.paperflow.application.usecase.user.RegisterUserUseCase;
import com.system.paperflow.commons.StringUtils;
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

public class RegisterScreen implements Screen {

    private final RegisterUserUseCase registerUserUseCase;

    public RegisterScreen(RegisterUserUseCase registerUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
    }

    @Override
    public JComponent build() {
        TextField emailInput = TextField.create(TextFieldType.TEXT).fullWidth();
        TextField institutionInput = TextField.create(TextFieldType.TEXT).fullWidth();
        TextField passwordInput = TextField.create(TextFieldType.PASSWORD).fullWidth();

        StatusMessage statusMessage = StatusMessage.create();

        Column form = Column.create().center().gap(14);
        form.add(Text.title("Paper Flow"));
        form.add(Text.subtitle("Realize seu registro para continuar"));
        form.add(FormField.create("Instituição", institutionInput));
        form.add(FormField.create("E-mail", emailInput));
        form.add(FormField.create("Senha", passwordInput));
        form.add(Button.primary("Registrar").fullWidth().onClick(() -> {
            statusMessage.clear();

            try {
                registerUserUseCase.execute(emailInput.text(), passwordInput.password(), institutionInput.text());
                statusMessage.success("Usuário registrado com sucesso.");
            } catch (Exception exception) {
                statusMessage.error(exception.getMessage());
            }
        }));
        form.add(Button.secondary("Já tenho conta").fullWidth().onClick(() -> ScreenUtils.navigateTo("login")));
        form.add(statusMessage);

        return Center.create(form).build();
    }

    @Override
    public String withTitle() {
        return "Paper Flow - Registro";
    }
}
