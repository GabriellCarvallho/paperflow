package com.system.paperflow.domain.template;

import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.Review;

public class AcceptedEmailTemplate implements EmailTemplate {

    private final Paper paper;

    public AcceptedEmailTemplate(Paper paper) {
        this.paper = paper;
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

                </body>
                </html>
                """.formatted(
                paper.getAuthor().getUsername(),
                paper.getId().toString(),
                paper.getTitle(),
                paper.getEvent().getName() + " - " + paper.getEvent().getCategory()
        );
    }
}
