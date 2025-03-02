package com.example.tgbot.Config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.properties")
public class BotConfig {

    @Value("${BotUserName}")
    String BotUserName;

    @Value("${BotToken}")
    String BotToken;

}
