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

public class LoginScreen implements Screen {


    @Override
    public JComponent build() {
        TextField emailInput = TextField.create(TextFieldType.TEXT)
            .fullWidth();

        TextField passwordInput = TextField.create(TextFieldType.PASSWORD)
            .fullWidth();

        Column form = Column.create()
            .center()
            .gap(14)
            .children(

                Text.title("Paper Flow"),
                Text.subtitle("Entre para continuar"),

                FormField.create("E-mail", emailInput),
                FormField.create("Senha", passwordInput),

                Button.primary("Entrar").fullWidth()
            );

        return Center.create(form).build();
    }

    @Override
    public String withTitle() {
        return "Paper Flow - Login";
    }
}
