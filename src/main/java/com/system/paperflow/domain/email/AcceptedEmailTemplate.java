package com.system.paperflow.domain.email;

public class AcceptedEmailTemplate implements EmailTemplate {

    @Override
    public String template() {
        return """
                <p style="margin:0 0 20px 0;font-size:16px;color:#333333;">
                    Prezado Sr. Alex Cunha,
                </p>

                <p style="margin:0 0 20px 0;font-size:16px;line-height:1.6;color:#333333;">
                    <strong>Parabéns!</strong> Sua submissão de nº
                    <strong>249227</strong>, intitulada
                    <em>"Bem-te-vejo: Conectando pessoas e profissionais de saúde mental para promover o cuidado diante de sinais de depressão"</em>,
                    para o <strong>SBSI 2026 - Full Papers</strong>, foi aceita.
                </p>

                <p style="margin:0 0 20px 0;font-size:16px;line-height:1.6;color:#333333;">
                    As avaliações estão disponíveis ao final deste e-mail.
                </p>

                <p style="margin:30px 0 0 0;font-size:16px;color:#333333;">
                    Atenciosamente,
                </p>

                <p style="margin:10px 0 30px 0;font-size:16px;color:#333333;">
                    <strong>Damires Souza</strong><br>
                    Coordenadora do Comitê de Programa do SBSI 2026
                </p>

                <hr style="border:none;border-top:1px solid #dddddd;margin:30px 0;">

                <h3 style="margin:0 0 15px 0;color:#333333;">
                    [Revisor 1]
                </h3>

                <p style="margin:0 0 10px 0;font-size:15px;color:#333333;">
                    <strong>Principal Contribuição ou pontos positivos</strong>
                </p>

                <p style="margin:0 0 25px 0;font-size:15px;color:#555555;">
                    &lt;usar o mesmo formato mostrado para o caso de rejeição&gt;
                </p>

                <h3 style="margin:0 0 15px 0;color:#333333;">
                    [Revisor 2]
                </h3>

                <p style="margin:0 0 10px 0;font-size:15px;color:#333333;">
                    <strong>Principal Contribuição ou pontos positivos</strong>
                </p>

                <p style="margin:0;font-size:15px;color:#555555;">
                    &lt;usar o mesmo formato mostrado para o caso de rejeição&gt;
                </p>
                """;
    }

}
