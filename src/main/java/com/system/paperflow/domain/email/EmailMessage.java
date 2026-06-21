package com.system.paperflow.domain.email;

public class EmailMessage {

    private final String to, subject, body;
    private final EmailTemplate template;

    private EmailMessage(String to, String subject, String body, EmailTemplate template) {
        this.template = template;
        this.subject = subject;
        this.body = body;
        this.to = to;
    }

    public String getBody() {
        return body;
    }

    public String getSubject() {
        return subject;
    }

    public EmailTemplate getTemplate() {
        return template;
    }

    public String getTo() {
        return to;
    }

    public static EmailMessageBuilder builder() {
        return new EmailMessageBuilder();
    }

    public static class EmailMessageBuilder {

        private String to, subject, body;
        private EmailTemplate template;   

        public EmailMessageBuilder withRecipient(String recipient) {
            this.to = recipient;
            return this;
        }

        public EmailMessageBuilder withSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public EmailMessageBuilder withBody(String body) {
            this.body = body;
            return this;
        }

        public EmailMessageBuilder withTemplate(EmailTemplate template) {
            this.template = template;
            return this;
        }

        public EmailMessage build() {
            return new EmailMessage(to, subject, body, template);
        }
    }

}
