package com.system.paperflow.presentation.screen;

import com.system.paperflow.application.usecase.review.SubmitReviewUseCase;
import com.system.paperflow.domain.enums.Verdict;
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

public class SubmitReviewScreen implements Screen {

    private final ScreenContext context;
    private final SubmitReviewUseCase submitReviewUseCase;

    public SubmitReviewScreen(ScreenContext context, SubmitReviewUseCase submitReviewUseCase) {
        this.context = context;
        this.submitReviewUseCase = submitReviewUseCase;
    }

    @Override
    public JComponent build() {
        TextField contributionInput = TextField.create(TextFieldType.TEXT).fullWidth();
        TextField criticismInput = TextField.create(TextFieldType.TEXT).fullWidth();
        TextField verdictInput = TextField.create(TextFieldType.TEXT).fullWidth();
        StatusMessage status = StatusMessage.create();

        Column header = Column.create().gap(6);
        header.add(Text.title("Concluir revisão"));
        header.add(Text.subtitle("Informe contribuições, críticas e veredito do artigo."));

        Grid fields = Grid.columns(1);
        fields.add(FormField.create("Contribuições ou pontos positivos", contributionInput));
        fields.add(FormField.create("Pontos de crítica", criticismInput));
        fields.add(FormField.create("Veredito: REJECTED, WEAKLY_REJECTED, WEAKLY_ACCEPTED ou ACCEPTED", verdictInput));

        Card form = Card.create().gap(16);
        form.add(Text.sectionTitle("Parecer"));
        form.add(Text.caption("O resultado ficará disponível ao autor quando o artigo for aceito/rejeitado."));
        form.add(fields);
        form.add(Button.primary("Enviar revisão").onClick(() -> {
            status.clear();
            try {
                submitReviewUseCase.execute(
                        context.selectedPaperId(),
                        context.currentUser(),
                        contributionInput.text(),
                        criticismInput.text(),
                        parseVerdict(verdictInput.text())
                );
                context.clearSelectedPaper();
                ScreenUtils.navigateTo("reviewer-assignments");
            } catch (Exception exception) {
                status.error(exception.getMessage());
            }
        }));
        form.add(status);

        Row actions = Row.create().right().gap(12);
        actions.add(Button.secondary("Voltar às revisões").onClick(() -> ScreenUtils.navigateTo("reviewer-assignments")));

        Column content = Column.create().gap(24);
        content.add(header);
        content.add(form);
        content.add(actions);

        return Page.create().add(content).build();
    }

    @Override
    public String withTitle() {
        return "Paper Flow - Concluir Revisão";
    }

    private Verdict parseVerdict(String value) {
        if (value == null || value.isBlank()) {
            return Verdict.WEAKLY_ACCEPTED;
        }

        return Verdict.valueOf(value.trim().toUpperCase().replace(" ", "_"));
    }
}
