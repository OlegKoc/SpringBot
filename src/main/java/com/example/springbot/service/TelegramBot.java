package com.example.springbot.service;

import com.example.springbot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScope;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;
    static final String HELP_TEXT = "This bot is created demonstrate Spring capabilities.\n\n" +
            "You can execute commandes from the main menu";

    public TelegramBot(BotConfig config) {

        this.config = config;
        List<BotCommand> listOfCommand = new ArrayList<>();
        listOfCommand.add(new BotCommand("/start", "get a welcome message"));
        listOfCommand.add(new BotCommand("/mydata", "get your data stored"));
        listOfCommand.add(new BotCommand("/deletedata", "delete my data"));
        listOfCommand.add(new BotCommand("/help", "info how to use this bot"));
        listOfCommand.add(new BotCommand("/settings", "set your preferences"));
        try {
            this.execute(new SetMyCommands(listOfCommand, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override           // здесь реализовывется то что пишет пользователь и возвращается ответ
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();  // идентификатор пользователя и его сообщение
            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    senMessage(chatId, HELP_TEXT);
                    break;
                default:
                    senMessage(chatId, "Sorry, command was not recognized");

            }

        }


    }

    private void startCommandReceived(long chatId, String name) {  // принимем два параметра т.к. чатов может быть много юзеров
        String answer = "Hi " + name + ", nice to meet you!";
        //log.info("Replied to user " + name);
        senMessage(chatId, answer);


    }

    private void senMessage(long chatId, String textToSend) {   // метод для отправки сообщений
        SendMessage message = new SendMessage();   // класс для отправки собщений, создаем объект
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
                                            //log.error("Error occured :" + e.getMessage());
        }

    }
}
