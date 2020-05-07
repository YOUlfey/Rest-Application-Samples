package me.youlfey.rest.example.news.portal.config.email;

import com.google.common.collect.ImmutableMap;
import org.stringtemplate.v4.ST;

import java.util.HashMap;
import java.util.Map;

import static me.youlfey.rest.example.news.portal.config.email.EmailTemplates.TemplateParameters.*;

public class EmailTemplates {
    public static String CREATE_TOPIC = "create topic";
    public static String REMOVE_TOPIC = "remove topic";
    public static String UPDATE_TOPIC = "update topic";
    public static String CREATE_NEWS = "create news";
    public static String REMOVE_NEWS = "remove news";
    public static String UPDATE_NEWS = "update news";

    public static class TemplateParameters {
        public static final String ID = "id";
        public static final String DATE = "date";
        public static final String NAME = "name";
        public static final String MESSAGE = "message";
        public static final String TOPIC = "topic";
        public static final String TOPIC_ID = "topicId";
        public static final String TOPIC_NAME = "topicName";
        public static final String MESSAGE_CONDITION = "messageCondition";
        public static final String TOPIC_CONDITION = "topicCondition";
    }

    private static final String CREATE_TOPIC_MESSAGE = "Topic <" + ID + "> with name '<" + NAME + ">' has been created";
    private static final String REMOVE_TOPIC_MESSAGE = "Topic <" + ID + "> with name '<" + NAME + ">' has been removed";
    private static final String UPDATE_TOPIC_MESSAGE = "Topic <" + ID + "> was changed at <" + DATE + ">." +
            " New name of topic is '<" + NAME + ">'";
    private static final String CREATE_NEWS_MESSAGE = "News <" + ID + "> with message '<" + MESSAGE + ">' " +
            "has been created in the topic <" + TOPIC_ID + ">, which called '<" + TOPIC_NAME + ">'";
    private static final String REMOVE_NEWS_MESSAGE = "News <" + ID + "> with message '<" + MESSAGE + ">' " +
            "has been removed from the topic <" + TOPIC_ID + ">, which called '<" + TOPIC_NAME + ">'";
    private static final String UPDATE_NEWS_MESSAGE = "News <" + ID + "> has been changed. " +
            "<if(" + MESSAGE_CONDITION + ")> message changed to <" + MESSAGE + "> .<endif>" +
            " <if(" + TOPIC_CONDITION + ")> topic changed to <" + TOPIC_ID + "> .<endif>";

    public static Map<String, ST> templates = ImmutableMap.copyOf(
            new HashMap<String, ST>() {{
                put(CREATE_TOPIC, new ST(CREATE_TOPIC_MESSAGE));
                put(REMOVE_TOPIC, new ST(REMOVE_TOPIC_MESSAGE));
                put(UPDATE_TOPIC, new ST(UPDATE_TOPIC_MESSAGE));
                put(CREATE_NEWS, new ST(CREATE_NEWS_MESSAGE));
                put(REMOVE_NEWS, new ST(REMOVE_NEWS_MESSAGE));
                put(UPDATE_NEWS, new ST(UPDATE_NEWS_MESSAGE));
            }}
    );
}
