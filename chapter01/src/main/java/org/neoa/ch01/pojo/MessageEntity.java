package org.neoa.ch01.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {
    private Long id;
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageEntity)) return false;
        MessageEntity message = (MessageEntity) o;
        return Objects.equals(getId(), message.getId())
                && Objects.equals(getText(), message.getText());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getText());
    }
    @Override
    public String toString() {
        return String.format("MessageEntity{id=%d,text='%s'}",
                getId(),
                getText());
    }
}
