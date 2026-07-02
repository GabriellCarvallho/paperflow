package com.system.paperflow.presentation.screen;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import javax.swing.JComponent;

import com.system.paperflow.application.usecase.event.CreateEventUseCase;
import com.system.paperflow.presentation.ui.Screen;
import com.system.paperflow.presentation.ui.ScreenUtils;
import com.system.paperflow.presentation.ui.component.Button;
import com.system.paperflow.presentation.ui.component.Card;
import com.system.paperflow.presentation.ui.component.Column;
import com.system.paperflow.presentation.ui.component.FormField;
import com.system.paperflow.presentation.ui.component.Grid;
import com.system.paperflow.presentation.ui.component.Page;
import com.system.paperflow.presentation.ui.component.Row;
import com.system.paperflow.presentation.ui.component.StatusMessage;
import com.system.paperflow.presentation.ui.component.Text;
import com.system.paperflow.presentation.ui.component.TextField;
import com.system.paperflow.presentation.ui.component.TextField.TextFieldType;

public class CreateEventScreen implements Screen {

    private static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
        .appendPattern("dd/MM/uuuu")
        .toFormatter()
        .withResolverStyle(ResolverStyle.STRICT);

    private final CreateEventUseCase createEventUseCase;

    public CreateEventScreen(CreateEventUseCase createEventUseCase) {
        this.createEventUseCase = createEventUseCase;
    }

    @Override
    public JComponent build() {
        TextField nameInput = TextField.create(TextFieldType.TEXT).fullWidth();
        TextField cityInput = TextField.create(TextFieldType.TEXT).fullWidth();
        TextField endDateInput = TextField.create(TextFieldType.DATE).fullWidth();
        TextField deadlineInput = TextField.create(TextFieldType.DATE).fullWidth();
        StatusMessage statusMessage = StatusMessage.create();

        Column header = Column.create().gap(6);
        header.add(Text.title("Criar evento"));
        header.add(Text.subtitle("Abra um novo ciclo de submissões para pesquisadores e revisores."));

        Grid identityFields = Grid.columns(2);
        identityFields.add(FormField.create("Nome do evento", nameInput));
        identityFields.add(FormField.create("Cidade", cityInput));

        Grid dateFields = Grid.columns(2);
        dateFields.add(FormField.create("Data final (dd/MM/yyyy)", endDateInput));
        dateFields.add(FormField.create("Prazo de submissão (dd/MM/yyyy)", deadlineInput));

        Row actions = Row.create().right().gap(12);
        actions.add(Button.secondary("Voltar para eventos").onClick(() -> ScreenUtils.navigateTo("events")));
        actions.add(Button.primary("Criar evento").onClick(() -> {
            try {

                //Felipe integra o caso de uso aqui.

                ScreenUtils.navigateTo("events");
            } catch (DateTimeParseException exception) {
                statusMessage.error("Use datas validas no formato dd/MM/yyyy.");
            } catch (Exception exception) {
                statusMessage.error(exception.getMessage());
            }
        }));

        Card form = Card.create().gap(18);
        form.add(Text.sectionTitle("Dados do evento"));
        form.add(Text.caption("O ciclo começa automaticamente hoje, em " + LocalDate.now().format(DATE_FORMATTER) + "."));
        form.add(identityFields);
        form.add(dateFields);
        form.add(actions);
        form.add(statusMessage);

        Column content = Column.create().gap(24);
        content.add(header);
        content.add(form);

        return Page.create().add(content).build();
    }

    @Override
    public String withTitle() {
        return "Paper Flow - Criar Evento";
    }

    private LocalDate parseDate(String date) {
        return LocalDate.parse(date, DATE_FORMATTER);
    }
}
