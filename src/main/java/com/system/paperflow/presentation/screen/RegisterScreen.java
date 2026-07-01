package com.system.paperflow.presentation.screen;

import javax.swing.JComponent;

import com.system.paperflow.presentation.ui.Screen;
import com.system.paperflow.presentation.ui.component.Button;
import com.system.paperflow.presentation.ui.component.Center;
import com.system.paperflow.presentation.ui.component.Column;
import com.system.paperflow.presentation.ui.component.FormField;
import com.system.paperflow.presentation.ui.component.Text;
import com.system.paperflow.presentation.ui.component.TextField;
import com.system.paperflow.presentation.ui.component.TextField.TextFieldType;

public class RegisterScreen implements Screen {

    private final Runnable onLoginClick;

    public RegisterScreen(Runnable onLoginClick) {
        this.onLoginClick = onLoginClick;
    }

    @Override
    public JComponent build() {
        TextField emailInput = TextField.create(TextFieldType.TEXT).fullWidth();
        TextField usernameInput = TextField.create(TextFieldType.TEXT).fullWidth();
        TextField institutionInput = TextField.create(TextFieldType.TEXT).fullWidth();
        TextField passwordInput = TextField.create(TextFieldType.PASSWORD).fullWidth();

        Column form = Column.create()
            .center()
            .gap(14)
            .children(

                Text.title("Paper Flow"),
                Text.subtitle("Realize seu registro para continuar"),

                FormField.create("Username", usernameInput),
                FormField.create("Instituição", institutionInput),
                FormField.create("E-mail", emailInput),
                FormField.create("Senha", passwordInput),

                Button.primary("Registrar").fullWidth(),
                Button.secondary("Já tenho conta").fullWidth().onClick(onLoginClick)
            );

        return Center.create(form).build();
    }

    @Override
    public String withTitle() {
        return "Paper Flow - Register";
    }
}
