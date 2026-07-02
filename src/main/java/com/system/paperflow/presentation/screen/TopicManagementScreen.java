//package com.system.paperflow.presentation.screen;
//
//import javax.swing.JComponent;
//
//import com.system.paperflow.application.usecase.thematic.CreateThematicAreaUseCase;
//import com.system.paperflow.application.usecase.thematic.ListTopicTreeUseCase;
//import com.system.paperflow.domain.entity.Topic;
//import com.system.paperflow.domain.entity.TopicComponent;
//import com.system.paperflow.presentation.context.ScreenContext;
//import com.system.paperflow.presentation.ui.Screen;
//import com.system.paperflow.presentation.ui.ScreenUtils;
//import com.system.paperflow.presentation.ui.component.Button;
//import com.system.paperflow.presentation.ui.component.Card;
//import com.system.paperflow.presentation.ui.component.Column;
//import com.system.paperflow.presentation.ui.component.FormField;
//import com.system.paperflow.presentation.ui.component.Grid;
//import com.system.paperflow.presentation.ui.component.Page;
//import com.system.paperflow.presentation.ui.component.Row;
//import com.system.paperflow.presentation.ui.component.StatusMessage;
//import com.system.paperflow.presentation.ui.component.Text;
//import com.system.paperflow.presentation.ui.component.TextField;
//import com.system.paperflow.presentation.ui.component.TextField.TextFieldType;
//
//public class TopicManagementScreen implements Screen {
//
//    private final ScreenContext context;
//    private final CreateThematicAreaUseCase createThematicAreaUseCase;
//
//    public TopicManagementScreen(ScreenContext context, CreateThematicAreaUseCase createThematicAreaUseCase) {
//        this.context = context;
//        this.createThematicAreaUseCase = createThematicAreaUseCase;
//        this.listTopicTreeUseCase = listTopicTreeUseCase;
//    }
//
//    @Override
//    public JComponent build() {
//        TextField nameInput = TextField.create(TextFieldType.TEXT).fullWidth();
//        TextField descriptionInput = TextField.create(TextFieldType.TEXT).fullWidth();
//        StatusMessage status = StatusMessage.create();
//
//        Column header = Column.create().gap(6);
//        header.add(Text.title("Áreas temáticas"));
//        header.add(Text.subtitle(context.currentEvent().isInPreparation()
//            ? "Etapa 1 de 2 - Cadastre os temas que orientam submissões e revisões."
//            : "Gerencie os temas usados nas submissões e revisões do evento."));
//
//        Grid fields = Grid.columns(2);
//        fields.add(FormField.create("Nome da área", nameInput));
//        fields.add(FormField.create("Descrição curta", descriptionInput));
//
//        Card form = Card.create().gap(16);
//        form.add(Text.sectionTitle("Nova área"));
//        form.add(Text.caption("Use nomes objetivos, como Inteligência Artificial, Engenharia de Software ou Sistemas de Informação."));
//        form.add(fields);
//        form.add(Button.primary("Adicionar área").onClick(() -> {
//            try {
//                createThematicAreaUseCase.execute(context.currentResearcher().getEmail(), new Topic(nameInput.text(), descriptionInput.text()));
//                status.success("Área adicionada.");
//                ScreenUtils.navigateTo("topics");
//            } catch (Exception exception) {
//                status.error(exception.getMessage());
//            }
//        }));
//        form.add(status);
//
//        Grid topics = Grid.columns(3);
//        for (TopicComponent topic : listTopicTreeUseCase.execute()) {
//            topics.add(Card.create().gap(8).add(Text.sectionTitle(topic.getName())).add(Text.caption(topic.getDescription())));
//        }
//
//        Card topicList = Card.create().gap(14);
//        topicList.add(Text.sectionTitle("Áreas cadastradas"));
//        topicList.add(listTopicTreeUseCase.execute().isEmpty() ? Text.body("Nenhuma área cadastrada.") : topics);
//
//        Row actions = Row.create().right().gap(12);
//        actions.add(Button.secondary("Voltar ao evento").onClick(() -> ScreenUtils.navigateTo("event")));
//        if (context.currentEvent().isInPreparation()) {
//            actions.add(Button.primary("Próximo").onClick(() -> ScreenUtils.navigateTo("reviewer-invitations")));
//        }
//
//        Column content = Column.create().gap(24);
//        content.add(header);
//        content.add(form);
//        content.add(topicList);
//        content.add(actions);
//
//        return Page.create().add(content).build();
//    }
//
//    @Override
//    public String withTitle() {
//        return "Paper Flow - Áreas Temáticas";
//    }
//}
