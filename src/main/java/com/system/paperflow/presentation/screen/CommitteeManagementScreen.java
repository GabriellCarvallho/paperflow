package com.system.paperflow.presentation.screen;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.usecase.committee.AddReviewerUseCase;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Researcher;
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

public class CommitteeManagementScreen implements Screen {

    private final ScreenContext context;
    private final EventManager eventManager;
    private final AddReviewerUseCase addReviewerUseCase;

    public CommitteeManagementScreen(ScreenContext context, EventManager eventManager, AddReviewerUseCase addReviewerUseCase) {
        this.context = context;
        this.eventManager = eventManager;
        this.addReviewerUseCase = addReviewerUseCase;
    }

    @Override
    public JComponent build() {
        Event event = eventManager.getCurrentEvent();
        TextField emailInput = TextField.create(TextFieldType.TEXT).fullWidth();
        TextField areasInput = TextField.create(TextFieldType.TEXT).fullWidth();
        StatusMessage status = StatusMessage.create();

        Column header = Column.create().gap(6);
        header.add(Text.title("Comitê técnico"));
        header.add(Text.subtitle("RF04 - O coordenador adiciona revisores cadastrados e informa suas expertises."));

        Card availableAreas = Card.create().gap(8);
        availableAreas.add(Text.sectionTitle("Áreas disponíveis"));
        availableAreas.add(Text.body(event.getThematicAreas().isEmpty()
                ? "Nenhuma área cadastrada. Cadastre áreas antes de adicionar revisores."
                : event.getThematicAreas().stream().map(ThematicArea::name).toList().toString()));

        Grid fields = Grid.columns(2);
        fields.add(FormField.create("E-mail do usuário cadastrado", emailInput));
        fields.add(FormField.create("Expertises separadas por vírgula", areasInput));

        Card form = Card.create().gap(16);
        form.add(Text.sectionTitle("Adicionar revisor"));
        form.add(Text.caption("O usuário precisa ter sido cadastrado antes pelo RF02."));
        form.add(fields);
        form.add(Button.primary("Adicionar ao comitê").onClick(() -> {
            status.clear();
            try {
                addReviewerUseCase.execute(context.currentUser(), emailInput.text(), parseList(areasInput.text()));
                ScreenUtils.navigateTo("committee");
            } catch (Exception exception) {
                status.error(exception.getMessage());
            }
        }));
        form.add(status);

        Grid reviewers = Grid.columns(3);
        for (Researcher reviewer : event.getCommittee()) {
            Card reviewerCard = Card.create().gap(8);
            reviewerCard.add(Text.sectionTitle(reviewer.getUsername()));
            reviewerCard.add(Text.caption(reviewer.getEmail()));
            reviewerCard.add(Text.body("Expertises: " + reviewer.getAreas().stream().map(ThematicArea::name).toList()));
            reviewers.add(reviewerCard);
        }

        Card list = Card.create().gap(14);
        list.add(Text.sectionTitle("Revisores do comitê"));
        list.add(event.getCommittee().isEmpty() ? Text.body("Nenhum revisor adicionado.") : reviewers);

        Row actions = Row.create().right().gap(12);
        actions.add(Button.secondary("Voltar ao evento").onClick(() -> ScreenUtils.navigateTo("event")));

        Column content = Column.create().gap(24);
        content.add(header);
        content.add(availableAreas);
        content.add(form);
        content.add(list);
        content.add(actions);

        return Page.create().add(content).build();
    }

    @Override
    public String withTitle() {
        return "Paper Flow - Comitê Técnico";
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
