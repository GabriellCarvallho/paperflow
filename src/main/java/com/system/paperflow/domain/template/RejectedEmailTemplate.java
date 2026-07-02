package com.system.paperflow.domain.template;

import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.ReviewAssignment;

import java.util.List;

public class RejectedEmailTemplate implements EmailTemplate{

    private final Paper paper;
    private final List<ReviewAssignment> assignments;

    public RejectedEmailTemplate(Paper paper) {
        this(paper, List.of());
    }

    public RejectedEmailTemplate(Paper paper, List<ReviewAssignment> assignments) {
        this.paper = paper;
        this.assignments = assignments;
    }

    @Override
    public String template() {

        return """
                <html>
                <body style="font-family: Arial, Helvetica, sans-serif; color:#222; line-height:1.5;">

                    <h2 style="color:#cc0000;">Rejeição</h2>

                    <p>
                        Prezado Sr(a). <strong>%s</strong>:
                    </p>

                    <p>
                        Lamentamos informar que seu artigo de nº <strong>%s</strong>,
                        intitulado <strong>"%s"</strong>, não pôde ser aceito para o
                        <strong>%s</strong>.
                    </p>

                    <p>
                        Ao final deste e-mail seguem os pareceres dos revisores,
                        que esperamos que possam auxiliá-lo em futuras submissões.
                    </p>

                    <p>
                        Agradecemos sua submissão.
                    </p>

                    %s

                    %s

                </body>
                </html>
                """.formatted(
                paper.getAuthor().getUsername(),
                paper.getId(),
                paper.getTitle(),
                ReviewEmailFormatter.eventLabel(paper.getEvent()),
                ReviewEmailFormatter.signature(paper.getEvent()),
                ReviewEmailFormatter.renderReviews(assignments)
        );
    }
}
