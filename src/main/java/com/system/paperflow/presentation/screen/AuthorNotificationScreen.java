package com.system.paperflow.presentation.screen;

import com.system.paperflow.application.usecase.notification.NotifyAuthorsUseCase;
import com.system.paperflow.domain.entity.EmailMessage;
import com.system.paperflow.infrastructure.memory.InMemoryEmailGateway;
import com.system.paperflow.presentation.ui.Screen;
import com.system.paperflow.presentation.ui.ScreenUtils;
import com.system.paperflow.presentation.ui.component.Button;
import com.system.paperflow.presentation.ui.component.Card;
import com.system.paperflow.presentation.ui.component.Column;
import com.system.paperflow.presentation.ui.component.Grid;
import com.system.paperflow.presentation.ui.component.Page;
import com.system.paperflow.presentation.ui.component.Row;
import com.system.paperflow.presentation.ui.component.StatusMessage;
import com.system.paperflow.presentation.ui.component.Text;

import javax.swing.JComponent;
import java.util.List;

public class AuthorNotificationScreen implements Screen {

    private final NotifyAuthorsUseCase notifyAuthorsUseCase;
    private final InMemoryEmailGateway emailGateway;

    public AuthorNotificationScreen(NotifyAuthorsUseCase notifyAuthorsUseCase, InMemoryEmailGateway emailGateway) {
        this.notifyAuthorsUseCase = notifyAuthorsUseCase;
        this.emailGateway = emailGateway;
    }

    @Override
    public JComponent build() {
        StatusMessage status = StatusMessage.create();
        List<EmailMessage> messages = emailGateway.sentMessages();

        Column header = Column.create().gap(6);
        header.add(Text.title("Notificação aos autores"));
        header.add(Text.subtitle("RF09 - Envio das mensagens de resultado aos autores."));

        Card action = Card.create().gap(14);
        action.add(Text.sectionTitle("Enviar notificações"));
        action.add(Text.body("Esta tela usa o gateway de e-mail configurado no projeto. Na versão em memória, as mensagens ficam registradas para conferência."));
        action.add(Button.primary("Enviar para autores").onClick(() -> {
            status.clear();
            try {
                int sent = notifyAuthorsUseCase.execute();
                status.success(sent + " mensagem(ns) enviada(s)/registrada(s).");
                ScreenUtils.navigateTo("notifications");
            } catch (Exception exception) {
                status.error(exception.getMessage());
            }
        }));
        action.add(status);

        Grid grid = Grid.columns(2);
        for (EmailMessage message : messages) {
            Card card = Card.create().gap(8);
            card.add(Text.caption("Para: " + message.getTo()));
            card.add(Text.sectionTitle(message.getSubject()));
            card.add(Text.body("Corpo: " + summarize(message.getBody())));
            grid.add(card);
        }

        Card sent = Card.create().gap(14);
        sent.add(Text.sectionTitle("Mensagens registradas"));
        sent.add(messages.isEmpty() ? Text.body("Nenhuma mensagem enviada ainda.") : grid);

        Row actions = Row.create().right().gap(12);
        actions.add(Button.secondary("Voltar ao evento").onClick(() -> ScreenUtils.navigateTo("event")));

        Column content = Column.create().gap(24);
        content.add(header);
        content.add(action);
        content.add(sent);
        content.add(actions);

        return Page.create().add(content).build();
    }

    @Override
    public String withTitle() {
        return "Paper Flow - Notificações";
    }

    private String summarize(String body) {
        if (body == null) {
            return "";
        }

        return body.length() <= 180 ? body : body.substring(0, 180) + "...";
    }
}
