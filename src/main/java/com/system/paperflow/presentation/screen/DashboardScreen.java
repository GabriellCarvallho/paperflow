package com.system.paperflow.presentation.screen;

import javax.swing.JComponent;

import com.system.paperflow.presentation.ui.Screen;
import com.system.paperflow.presentation.ui.View;
import com.system.paperflow.presentation.ui.component.Card;
import com.system.paperflow.presentation.ui.component.Column;
import com.system.paperflow.presentation.ui.component.Grid;
import com.system.paperflow.presentation.ui.component.Page;
import com.system.paperflow.presentation.ui.component.Text;

public class DashboardScreen implements Screen {

    @Override
    public JComponent build() {
        Column content = Column.create()
            .gap(24)
            .children(
                Column.create()
                    .gap(6)
                    .children(
                        Text.title("Dashboard"),
                        Text.subtitle("Resumo do evento Paper Flow")
                    ),
                Grid.columns(4).children(
                    metricCard("Artigos submetidos", "128", "18 aguardando revisão"),
                    metricCard("Revisores ativos", "42", "9 disponíveis hoje"),
                    metricCard("Aceitos", "36", "28% das submissões"),
                    metricCard("Prazo final", "12 dias", "para encerrar avaliações")
                ),
                Grid.columns(2).children(
                    reviewCard(),
                    scheduleCard()
                )
            );

        return Page.create(content).build();
    }

    @Override
    public String withTitle() {
        return "Paper Flow - Dashboard";
    }

    private View metricCard(String title, String value, String caption) {
        return Card.create()
            .withMinHeight(132)
            .gap(8)
            .children(
                Text.caption(title),
                Text.metric(value),
                Text.caption(caption)
            );
    }

    private View reviewCard() {
        return Card.create()
            .withMinHeight(230)
            .gap(14)
            .children(
                Text.sectionTitle("Fila de revisão"),
                activity("Deep Learning aplicado à triagem clínica", "Aguardando 2 pareceres"),
                activity("Arquiteturas distribuídas para eventos científicos", "Parecer técnico recebido"),
                activity("Modelos de recomendação para revisores", "Revisor convidado ontem")
            );
    }

    private View scheduleCard() {
        return Card.create()
            .withMinHeight(230)
            .gap(14)
            .children(
                Text.sectionTitle("Próximas ações"),
                activity("Enviar lembrete para revisores pendentes", "Hoje, 17:00"),
                activity("Publicar lista preliminar de aceitos", "Sexta-feira"),
                activity("Fechar etapa de contestação", "Em 5 dias")
            );
    }

    private View activity(String title, String caption) {
        return Column.create()
            .gap(4)
            .children(
                Text.body(title).bold(),
                Text.caption(caption)
            );
    }
}
