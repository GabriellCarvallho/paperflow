package com.system.paperflow.domain.template;

import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.ReviewAssignment;

import java.util.List;

public class AcceptedEmailTemplate implements EmailTemplate {

    private final Paper paper;
    private final List<ReviewAssignment> assignments;

    public AcceptedEmailTemplate(Paper paper) {
        this(paper, List.of());
    }

    public AcceptedEmailTemplate(Paper paper, List<ReviewAssignment> assignments) {
        this.paper = paper;
        this.assignments = assignments;
    }

    @Override
    public String template() {
        return """
                <html>
                <body style="font-family: Arial, Helvetica, sans-serif; color:#222; line-height:1.5;">

                    <h2 style="color:#0033cc;">Aceitação</h2>

                    <p>
                        Prezado Sr(a). <strong>%s</strong>:
                    </p>

                    <p>
                        Parabéns! Sua submissão de id: <strong>%s</strong>,
                        intitulada <strong>"%s"</strong>, para o
                        <strong>%s</strong>, foi aceita.
                    </p>

                    <p>
                        As avaliações estão disponíveis ao final do e-mail.
                    </p>

                    %s

                    %s

                </body>
                </html>
                """.formatted(
                paper.getAuthor().getUsername(),
                paper.getId().toString(),
                paper.getTitle(),
                ReviewEmailFormatter.eventLabel(paper.getEvent()),
                ReviewEmailFormatter.signature(paper.getEvent()),
                ReviewEmailFormatter.renderReviews(assignments)
        );
    }
}
