package com.system.paperflow.presentation.screen;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.usecase.thematic.CreateThematicAreaUseCase;
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

public class TopicManagementScreen implements Screen {

    private final ScreenContext context;
    private final EventManager eventManager;
    private final CreateThematicAreaUseCase createThematicAreaUseCase;

    public TopicManagementScreen(
            ScreenContext context,
            EventManager eventManager,
            CreateThematicAreaUseCase createThematicAreaUseCase
    ) {
        this.context = context;
        this.eventManager = eventManager;
        this.createThematicAreaUseCase = createThematicAreaUseCase;
    }

    @Override
    public JComponent build() {
        Event event = eventManager.getCurrentEvent();
        TextField nameInput = TextField.create(TextFieldType.TEXT).fullWidth();
        StatusMessage status = StatusMessage.create();

        Column header = Column.create().gap(6);
        header.add(Text.title("Áreas temáticas"));
        header.add(Text.subtitle("RF03 - Cadastre as palavras-chave usadas nas submissões e na expertise dos revisores."));

        Grid fields = Grid.columns(1);
        fields.add(FormField.create("Nome da área temática", nameInput));

        Card form = Card.create().gap(16);
        form.add(Text.sectionTitle("Nova área"));
        form.add(Text.caption("Exemplos: IA, Machine Learning, Engenharia de Software, LLMs."));
        form.add(fields);
        form.add(Button.primary("Adicionar área").onClick(() -> {
            status.clear();
            try {
                createThematicAreaUseCase.execute(context.currentUser(), event.getId(), nameInput.text());
                ScreenUtils.navigateTo("topics");
            } catch (Exception exception) {
                status.error(exception.getMessage());
            }
        }));
        form.add(status);

        Grid topics = Grid.columns(3);
        for (ThematicArea area : event.getThematicAreas()) {
            Card areaCard = Card.create().gap(8);
            areaCard.add(Text.sectionTitle(area.name()));
            areaCard.add(Text.caption("Disponível para submissões e expertise."));
            topics.add(areaCard);
        }

        Card topicList = Card.create().gap(14);
        topicList.add(Text.sectionTitle("Áreas cadastradas"));
        topicList.add(event.getThematicAreas().isEmpty() ? Text.body("Nenhuma área cadastrada.") : topics);

        Row actions = Row.create().right().gap(12);
        actions.add(Button.secondary("Voltar ao evento").onClick(() -> ScreenUtils.navigateTo("event")));
        actions.add(Button.primary("Ir para comitê").onClick(() -> ScreenUtils.navigateTo("committee")));

        Column content = Column.create().gap(24);
        content.add(header);
        content.add(form);
        content.add(topicList);
        content.add(actions);

        return Page.create().add(content).build();
    }

    @Override
    public String withTitle() {
        return "Paper Flow - Áreas Temáticas";
    }
}
