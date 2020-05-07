package me.youlfey.rest.example.news.portal.domain.jms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LetterInstance implements JmsMessage {
    private String subject;
    private String content;
    private String[] destinations;

    @Override
    public Class getType() {
        return LetterInstance.class;
    }
}
