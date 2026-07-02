package com.system.paperflow.domain.template;

import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Review;
import com.system.paperflow.domain.entity.ReviewAssignment;

import java.util.List;

class ReviewEmailFormatter {

    private ReviewEmailFormatter() {
    }

    static String eventLabel(Event event) {
        return event.getName() + " - " + formatCategory(event.getCategory().name());
    }

    static String renderReviews(List<ReviewAssignment> assignments) {
        StringBuilder builder = new StringBuilder();
        int reviewerNumber = 1;

        for (ReviewAssignment assignment : assignments) {
            if (!assignment.isFinished()) {
                continue;
            }

            Review review = assignment.getReview();
            builder.append("""
                    <section style="margin-top: 24px;">
                        <h3>[Revisor %d]</h3>

                        <p><strong>Principal Contribuição ou pontos positivos</strong></p>
                        <hr/>
                        <p>%s</p>

                        <p><strong>Pontos negativos</strong></p>
                        <hr/>
                        <p>%s</p>
                    </section>
                    """.formatted(
                    reviewerNumber++,
                    preserveLines(review.getContribution()),
                    preserveLines(review.getCriticism())
            ));
        }

        return builder.toString();
    }

    static String signature(Event event) {
        return """
                <p>Atenciosamente,</p>

                <p>
                    <strong>Coordenação Paper Flow</strong><br/>
                    Coordenador(a) do Comitê de Programa do %s
                </p>
                """.formatted(event.getName());
    }

    private static String preserveLines(String value) {
        return escape(value).replace("\n", "<br/>");
    }

    private static String escape(String value) {
        if (value == null || value.isBlank()) {
            return "Nao informado.";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    private static String formatCategory(String category) {
        String[] parts = category.toLowerCase().split("_");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (!builder.isEmpty()) {
                builder.append(" ");
            }
            builder.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return builder.toString();
    }
}
