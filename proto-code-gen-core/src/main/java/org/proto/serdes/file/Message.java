package org.proto.serdes.file;

import org.proto.serdes.utils.MultiRowStrings;

import java.util.Comparator;
import java.util.List;

public class Message {
    private static final String INDENT = "    ";
    private final String name;
    private final List<MessageField> fields;
    public static final Comparator<Message> comparator = new Comparator<Message>() {
        @Override
        public int compare(Message o1, Message o2) {
            return o1.name.compareTo(o2.name);
        }
    };

    public Message(String name, List<MessageField> fields) {
        this.name = name;
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (name != null ? !name.equals(message.name) : message.name != null) return false;
        return fields != null ? fields.equals(message.fields) : message.fields == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (fields != null ? fields.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        MultiRowStrings rows = new MultiRowStrings("\n");
        rows.add("message " + name + " {");
        fields.forEach(field -> rows.add(INDENT + field.toString()));
        rows.add("}");
        return rows.toString();
    }

    static class MessageField {
        private final FieldSymbol fieldSymbol;
        private final String name;
        private final int index;

        MessageField(String name, FieldSymbol fieldSymbol, int index) {
            this.fieldSymbol = fieldSymbol;
            this.name = name;
            this.index = index;
        }

        @Override
        public String toString() {
            return fieldSymbol.getTypeString() + " " + name + " = " + index + ";";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MessageField that = (MessageField) o;

            if (index != that.index) return false;
            if (fieldSymbol != null ? !fieldSymbol.equals(that.fieldSymbol) : that.fieldSymbol != null) return false;
            return name != null ? name.equals(that.name) : that.name == null;
        }

        @Override
        public int hashCode() {
            int result = fieldSymbol != null ? fieldSymbol.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + index;
            return result;
        }
    }
}
