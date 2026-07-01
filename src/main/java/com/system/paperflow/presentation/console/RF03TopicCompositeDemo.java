package com.system.paperflow.presentation.console;

import com.system.paperflow.application.persistence.TopicPersistence;
import com.system.paperflow.application.persistence.UserPersistence;
import com.system.paperflow.application.usecase.topic.CreateTopicTreeUseCase;
import com.system.paperflow.application.usecase.topic.ListTopicTreeUseCase;
import com.system.paperflow.application.usecase.user.EnsureDefaultCoordinatorUseCase;
import com.system.paperflow.domain.entity.Topic;
import com.system.paperflow.domain.entity.TopicComponent;
import com.system.paperflow.domain.entity.TopicGroup;
import com.system.paperflow.infrastructure.sqlite.SQLiteTopicAdapter;

import java.nio.file.Path;
import java.util.List;

public class RF03TopicCompositeDemo {

    private RF03TopicCompositeDemo() {
        // Classe utilitaria para demonstracao no Main.
    }

    public static void run(UserPersistence userPersistence) {
        System.out.println("=== RF03 - Cadastro de areas tematicas ===");
        System.out.println("Padrao usado: Composite");
        System.out.println();

        Path databasePath = Path.of("data", "paperflow.db");
        TopicPersistence topicPersistence = new SQLiteTopicAdapter(databasePath);

        CreateTopicTreeUseCase createTopicTreeUseCase = new CreateTopicTreeUseCase(
                topicPersistence,
                userPersistence
        );
        ListTopicTreeUseCase listTopicTreeUseCase = new ListTopicTreeUseCase(topicPersistence);

        TopicGroup artificialIntelligence = buildArtificialIntelligenceTree();
        TopicGroup softwareEngineering = buildSoftwareEngineeringTree();

        createTopicTreeUseCase.execute(EnsureDefaultCoordinatorUseCase.DEFAULT_EMAIL, artificialIntelligence);
        createTopicTreeUseCase.execute(EnsureDefaultCoordinatorUseCase.DEFAULT_EMAIL, softwareEngineering);

        List<TopicComponent> registeredTopics = listTopicTreeUseCase.execute();

        System.out.println("Areas cadastradas pelo coordenador padrao:");
        for (TopicComponent component : registeredTopics) {
            printTopicTree(component, "");
        }

        System.out.println();
        System.out.println("Palavras-chave disponiveis para artigos e revisores:");
        for (TopicComponent component : registeredTopics) {
            for (Topic keyword : component.getKeywords()) {
                System.out.println("- " + keyword.getName());
            }
        }

        System.out.println();
        System.out.println("RF03 finalizado para simulacao inicial.");
    }

    private static TopicGroup buildArtificialIntelligenceTree() {
        TopicGroup artificialIntelligence = new TopicGroup(
                "Inteligencia Artificial",
                "Grupo de areas relacionadas a tecnicas de IA."
        );

        artificialIntelligence.addChild(new Topic(
                "Machine Learning",
                "Palavra-chave para artigos sobre aprendizado de maquina."
        ));
        artificialIntelligence.addChild(new Topic(
                "Visao Computacional",
                "Palavra-chave para artigos sobre processamento e interpretacao de imagens."
        ));

        TopicGroup naturalLanguageProcessing = new TopicGroup(
                "Processamento de Linguagem Natural",
                "Subgrupo de IA relacionado a linguagem humana."
        );
        naturalLanguageProcessing.addChild(new Topic(
                "LLMs",
                "Palavra-chave para artigos sobre grandes modelos de linguagem."
        ));
        naturalLanguageProcessing.addChild(new Topic(
                "Mineracao de Texto",
                "Palavra-chave para artigos sobre extracao de informacao textual."
        ));

        artificialIntelligence.addChild(naturalLanguageProcessing);
        return artificialIntelligence;
    }

    private static TopicGroup buildSoftwareEngineeringTree() {
        TopicGroup softwareEngineering = new TopicGroup(
                "Engenharia de Software",
                "Grupo de areas relacionadas ao desenvolvimento e qualidade de software."
        );

        softwareEngineering.addChild(new Topic(
                "Design de Software",
                "Palavra-chave para artigos sobre desenho e arquitetura de sistemas."
        ));
        softwareEngineering.addChild(new Topic(
                "Padroes de Projeto",
                "Palavra-chave para artigos sobre aplicacao de design patterns."
        ));
        softwareEngineering.addChild(new Topic(
                "Testes de Software",
                "Palavra-chave para artigos sobre validacao e verificacao de sistemas."
        ));

        return softwareEngineering;
    }

    private static void printTopicTree(TopicComponent component, String indentation) {
        System.out.println(indentation + "- " + component.getName());

        if (component instanceof TopicGroup group) {
            for (TopicComponent child : group.getChildren()) {
                printTopicTree(child, indentation + "  ");
            }
        }
    }
}