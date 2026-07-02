package com.system.paperflow.presentation.screen;

import com.system.paperflow.presentation.ui.Screen;
import com.system.paperflow.presentation.ui.ScreenUtils;
import com.system.paperflow.presentation.ui.component.Button;
import com.system.paperflow.presentation.ui.component.Card;
import com.system.paperflow.presentation.ui.component.Column;
import com.system.paperflow.presentation.ui.component.Page;
import com.system.paperflow.presentation.ui.component.Row;
import com.system.paperflow.presentation.ui.component.Text;

import javax.swing.JComponent;

public class RequirementTenScreen implements Screen {

    @Override
    public JComponent build() {
        Column header = Column.create().gap(6);
        header.add(Text.title("RF10 - Requisito adicional"));
        header.add(Text.subtitle("Espaço reservado para o novo padrão que a equipe ainda vai definir."));

        Card card = Card.create().gap(12);
        card.add(Text.sectionTitle("Sugestão para implementar depois"));
        card.add(Text.body("Checklist de preparação do evento antes da distribuição automática."));
        card.add(Text.body("Padrão sugerido: Chain of Responsibility."));
        card.add(Text.body("Fluxo possível: evento criado -> áreas cadastradas -> comitê formado -> submissões recebidas -> distribuição liberada."));

        Row actions = Row.create().right().gap(12);
        actions.add(Button.secondary("Voltar ao evento").onClick(() -> ScreenUtils.navigateTo("event")));

        Column content = Column.create().gap(24);
        content.add(header);
        content.add(card);
        content.add(actions);

        return Page.create().add(content).build();
    }

    @Override
    public String withTitle() {
        return "Paper Flow - RF10";
    }
}
