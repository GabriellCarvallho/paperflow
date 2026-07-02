//package com.system.paperflow.presentation.screen;
//
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//import javax.swing.JComponent;
//
//import com.system.paperflow.domain.entity.Event;
//import com.system.paperflow.domain.entity.Researcher;
//import com.system.paperflow.presentation.context.ScreenContext;
//import com.system.paperflow.presentation.ui.Screen;
//import com.system.paperflow.presentation.ui.ScreenUtils;
//import com.system.paperflow.presentation.ui.View;
//import com.system.paperflow.presentation.ui.component.ClickableCard;
//import com.system.paperflow.presentation.ui.component.Column;
//import com.system.paperflow.presentation.ui.component.Grid;
//import com.system.paperflow.presentation.ui.component.Page;
//import com.system.paperflow.presentation.ui.component.Button;
//import com.system.paperflow.presentation.ui.component.Center;
//import com.system.paperflow.presentation.ui.component.Row;
//import com.system.paperflow.presentation.ui.component.Text;
//
//public class EventsScreen implements Screen {
//
//    private final ScreenContext context;
//
//    public EventsScreen(ScreenContext context) {
//        this.context = context;
//    }
//
//    @Override
//    public JComponent build() {
//        Researcher researcher = context.currentResearcher();
////        List<Event> eventList = listEventsUseCase.execute().stream()
////            .filter(event -> researcher.isCoordinator() || event.isOpenForSubmissions())
////            .toList();
//
//        Column greeting = Column.create().gap(6);
//        greeting.add(Text.title("Olá, " + researcher.getUsername()));
//        greeting.add(Text.subtitle("Estes são os eventos disponíveis no Paper Flow."));
//
//        Row headerRow = Row.create().spaceBetween();
//        headerRow.add(greeting);
//
//        if (researcher.isCoordinator()) {
//            Row actions = Row.create().gap(12);
//            actions.add(Button.primary("Criar evento").onClick(() -> ScreenUtils.navigateTo("event-create")));
//            actions.add(Button.secondary("Sair").onClick(() -> {
//                context.endSession();
//                ScreenUtils.navigateTo("login");
//            }));
//            headerRow.add(actions);
//        } else {
//            headerRow.add(Button.secondary("Sair").onClick(() -> {
//                context.endSession();
//                ScreenUtils.navigateTo("login");
//            }));
//        }
//
//        Grid events = Grid.columns(3);
//
////        for (Event event : eventList) {
////            events.add(eventCard(event));
////        }
////
////        Column content = Column.create().gap(24);
////        content.add(headerRow);
////
////        if (eventList.isEmpty()) {
////            content.add(Center.create(Text.sectionTitle("Não possui nenhum registro de evento registrado!")).withMinHeight(520));
////        } else {
////            content.add(events);
////        }
//
//        return Page.create().build();
//    }
//
//    @Override
//    public String withTitle() {
//        return "Paper Flow - Eventos";
//    }
//
////    private View eventCard(Event event) {
////        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
////        String status = event.getStatus().name();
////        String period = event.getStartDate().format(formatter) + " a " + event.getEndDate().format(formatter);
////        String deadline = "Submissões até " + event.getSubmissionDeadline().format(formatter);
////        String description = switch (event.getStatus()) {
////            case EM_PREPARACAO -> "Evento em preparação pela coordenação";
////            case ABERTO -> event.isOpenForSubmissions() ? "Evento atual recebendo submissões" : "Prazo de submissão encerrado";
////            case FECHADO -> "Evento anterior encerrado";
////        };
////
////        ClickableCard card = ClickableCard.create()
////            .onClick(() -> {
////                context.selectEvent(event);
////                ScreenUtils.navigateTo("event");
////            })
////            .withMinHeight(240)
////            .gap(10);
////
////        card.add(Text.caption(status));
////        card.add(Text.sectionTitle(event.getName()));
////        card.add(Text.body(event.getCity()));
////        card.add(Text.caption(period));
////        card.add(Text.caption(deadline));
////        card.add(Text.body(description).bold());
////
////        return card;
////    }
//}
