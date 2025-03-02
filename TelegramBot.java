package com.example.tgbot.servise;

import com.example.tgbot.Config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;

    static final String Help_text = """
            Цей бот було створено для здачі роботи по практиці.\n
            Загалом у цього бота немає певної мети створення, та чіткої реалізації,він створений для демонтрації своїх вмінь та за для цікавості в написані ботів на джаві.\n
            Якщо у вас є ідеї щодо реалізації цього бота ви можете написати свій варіант за командою "/idea"
            Завчасно дякую!)\n
            """;

    static final String IdeaForBot = """
            Якщо ви вибрати команду /Idea отже у вас є та сама ІДЕЯ як можна реалізувати цього бота, та як би він міг допомогати або надати щось користувачу.\n
            Прошу написати її в цьому боті, та можлива саме ваша ідея буде реалізована в ньому)\n
            ОБОВ'ЯЗКОВО повідомлення з вашою думкою потрібно починати з слів IDEA
            """;
    public TelegramBot(BotConfig config) {
        super(config.getBotToken());
        this.config = config;
        setBotCommands();
    }

    private void setBotCommands() {
        List<BotCommand> listofcommands = new ArrayList<>();
        listofcommands.add(new BotCommand("/start", "Start using the bot"));
        listofcommands.add(new BotCommand("/help", "Get information about the bot"));
        listofcommands.add(new BotCommand("/idea", "Write how can you realize this bot"));

        try {
            this.execute(new SetMyCommands(listofcommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("ERROR setting bot's command list: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotUserName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    sendMessage(chatId, Help_text);
                    log.info("The user has executed the /help command");
                    break;
                case "/idea":
                    sendMessage(chatId, IdeaForBot);
                    log.info("The user has executed the /idea command");
                    break;
                default:
                    if (messageText.startsWith("IDEA ")) {
                        String userIdea = messageText.substring(5).trim();
                        sendMessage(chatId, "Thank you for your idea: \"" + userIdea + "\"\nWith respect, your bot.");
                        log.info("The user has submitted an idea: " + userIdea);
                    } else {
                        sendMessage(chatId, "Thank you for contacting this bot!");
                    }
            }
        }
    }


    private  void startCommandReceived(long chatId, String name) {
        String answer = "Hello! Welcome " + name + "! How are you doing?";
        log.info("Replied to user " + name);

        sendMessage(chatId, answer);

    }

    private void sendMessage(long chatId, String textToSend) {
        try {
            execute(new org.telegram.telegrambots.meta.api.methods.send.SendMessage(String.valueOf(chatId), textToSend));
        } catch (TelegramApiException e) {
            log.error("Error sending message: " + e.getMessage());
        }
    }
}