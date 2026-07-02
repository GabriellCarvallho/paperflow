//package com.system.paperflow.presentation.screen;
//
//import com.system.paperflow.application.usecase.paper.ListEventPapersUseCase;
//import com.system.paperflow.domain.entity.Event;
//import com.system.paperflow.domain.entity.Researcher;
//import com.system.paperflow.presentation.context.ScreenContext;
//import com.system.paperflow.presentation.ui.Screen;
//import com.system.paperflow.presentation.ui.ScreenUtils;
//import com.system.paperflow.presentation.ui.component.*;
//
//import javax.swing.*;
//import java.time.format.DateTimeFormatter;
//
//public class EventScreen implements Screen {
//
//    private final ScreenContext context;
//    private final ListEventPapersUseCase listEventPapersUseCase;
//
//    public EventScreen(ScreenContext context, ListEventPapersUseCase listEventPapersUseCase) {
//        this.context = context;
//        this.listEventPapersUseCase = listEventPapersUseCase;
//    }
//
//    @Override
//    public JComponent build() {
//        Researcher researcher = context.currentResearcher();
//        Event event = context.currentEvent();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        Row header = Row.create().spaceBetween();
//
//        Column title = Column.create().gap(6);
//        title.add(Text.title(event.getName()));
//        title.add(Text.subtitle("Mini-dashboard do evento selecionado."));
//        header.add(title);
//        header.add(Button.secondary("Voltar").onClick(() -> ScreenUtils.navigateTo("events")));
//
//        Grid metrics = Grid.columns(4);
////        metrics.add(metric("Status", event.getStatus().name()));
////        metrics.add(metric("Submissões", String.valueOf(listEventPapersUseCase.execute(event).size())));
////        metrics.add(metric("Revisões", String.valueOf(listEventAssignmentsUseCase.execute(event).size())));
////        metrics.add(metric("Prazo", event.getSubmissionDeadline().format(formatter)));
////
////        Card info = Card.create().gap(12);
////        info.add(Text.sectionTitle("Informações do evento"));
////        info.add(Text.body("Local: " + event.getCity()));
////        info.add(Text.body("Período: " + event.getStartDate().format(formatter) + " a " + event.getEndDate().format(formatter)));
////        info.add(Text.body("Áreas: Engenharia de Software, Sistemas de Informação, IA aplicada"));
////
////        Card actions = Card.create().gap(12);
////
////        if (researcher.isCoordinator() && event.isInPreparation()) {
////            actions.add(Text.sectionTitle("Preparação do evento"));
////            actions.add(Text.body("Antes de abrir submissões para autores, configure as áreas temáticas e convide revisores."));
////            actions.add(Button.primary("Iniciar evento").onClick(() -> ScreenUtils.navigateTo("topics")));
////        } else if (researcher.isCoordinator()) {
////            actions.add(Text.sectionTitle("Organização do evento"));
////            actions.add(Text.body("Evento aberto para operação. Acompanhe submissões, revisões e notificações."));
////            actions.add(Button.primary("Gerenciar áreas temáticas").onClick(() -> ScreenUtils.navigateTo("topics")));
////            actions.add(Button.primary("Convidar revisores").onClick(() -> ScreenUtils.navigateTo("reviewer-invitations")));
////            actions.add(Button.secondary("Dashboard").onClick(() -> ScreenUtils.navigateTo("dashboard")));
////        } else {
////            actions.add(Text.sectionTitle("Área do participante"));
////            actions.add(Text.body("Evento aberto para consulta. As ações de submissão e revisão não estão disponíveis nesta versão."));
////        }
//
//        Column content = Column.create().gap(24);
//        content.add(header);
//        content.add(metrics);
////        content.add(info);
////        content.add(actions);
//
//        return Page.create().add(content).build();
//    }
//
//    @Override
//    public String withTitle() {
//        return "Paper Flow - Evento";
//    }
//
//    private Card metric(String label, String value) {
//        Card card = Card.create().gap(6);
//        card.add(Text.caption(label));
//        card.add(Text.metric(value));
//        return card;
//    }
//}
