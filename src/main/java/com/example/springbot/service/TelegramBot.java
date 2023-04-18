package com.example.springbot.service;

import com.example.springbot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;

    public TelegramBot(BotConfig config) {

        this.config = config;
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
                default:
                    senMessage(chatId, "Sorry, ......");
            }

        }


    }

    private void startCommandReceived(long chatId, String name) {  // принимем два параметра т.к. чатов может быть много юзеров
        String answer = "Hi " + name + ", nice to meet you!";
        senMessage(chatId, answer);


    }

    private void senMessage(long chatId, String textToSend) {   // метод для отправки сообщений
        SendMessage message = new SendMessage();   // класс для отправки собщений, создаем объект
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
}
