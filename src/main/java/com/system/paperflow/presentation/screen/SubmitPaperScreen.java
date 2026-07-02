package com.system.paperflow.presentation.screen;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.usecase.paper.SubmitPaperUseCase;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.ThematicArea;
import com.system.paperflow.presentation.context.ScreenContext;
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

import javax.swing.JComponent;
import java.util.Arrays;
import java.util.List;

public class SubmitPaperScreen implements Screen {

    private final ScreenContext context;
    private final EventManager eventManager;
    private final SubmitPaperUseCase submitPaperUseCase;

    public SubmitPaperScreen(ScreenContext context, EventManager eventManager, SubmitPaperUseCase submitPaperUseCase) {
        this.context = context;
        this.eventManager = eventManager;
        this.submitPaperUseCase = submitPaperUseCase;
    }

    @Override
    public JComponent build() {
        Event event = eventManager.getCurrentEvent();
        TextField titleInput = TextField.create(TextFieldType.TEXT).fullWidth();
        TextField summaryInput = TextField.create(TextFieldType.TEXT).fullWidth();
        TextField areasInput = TextField.create(TextFieldType.TEXT).fullWidth();
        TextField collaboratorsInput = TextField.create(TextFieldType.TEXT).fullWidth();
        StatusMessage status = StatusMessage.create();

        Column header = Column.create().gap(6);
        header.add(Text.title("Submeter artigo"));
        header.add(Text.subtitle("RF05 - Informe título, resumo, coautores cadastrados e áreas temáticas."));

        Card availableAreas = Card.create().gap(8);
        availableAreas.add(Text.sectionTitle("Áreas disponíveis"));
        availableAreas.add(Text.body(event.getThematicAreas().stream().map(ThematicArea::name).toList().toString()));

        Grid fields = Grid.columns(2);
        fields.add(FormField.create("Título do artigo", titleInput));
        fields.add(FormField.create("Áreas separadas por vírgula", areasInput));
        fields.add(FormField.create("Resumo", summaryInput));
        fields.add(FormField.create("E-mails dos coautores, separados por vírgula", collaboratorsInput));

        Card form = Card.create().gap(16);
        form.add(Text.sectionTitle("Dados da submissão"));
        form.add(Text.caption(event.isOpenForSubmission()
                ? "O evento está aberto para submissões."
                : "O evento não está aberto para submissões."));
        form.add(fields);
        form.add(Button.primary("Submeter artigo").onClick(() -> {
            status.clear();
            try {
                submitPaperUseCase.execute(
                        context.currentUser(),
                        event.getId(),
                        titleInput.text(),
                        summaryInput.text(),
                        parseList(areasInput.text()),
                        parseList(collaboratorsInput.text())
                );
                ScreenUtils.navigateTo("my-papers");
            } catch (Exception exception) {
                status.error(exception.getMessage());
            }
        }));
        form.add(status);

        Row actions = Row.create().right().gap(12);
        actions.add(Button.secondary("Voltar ao evento").onClick(() -> ScreenUtils.navigateTo("event")));

        Column content = Column.create().gap(24);
        content.add(header);
        content.add(availableAreas);
        content.add(form);
        content.add(actions);

        return Page.create().add(content).build();
    }

    @Override
    public String withTitle() {
        return "Paper Flow - Submissão";
    }

    private List<String> parseList(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }

        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(text -> !text.isBlank())
                .toList();
    }
}
