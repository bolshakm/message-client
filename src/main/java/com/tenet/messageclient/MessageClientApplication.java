package com.tenet.messageclient;

import com.tenet.messageclient.config.RequestConfiguration;
import com.tenet.messageclient.service.GetMessageRunner;
import com.tenet.messageclient.service.MessageService;
import com.tenet.messageclient.service.TopicService;
import com.tenet.messageclient.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class MessageClientApplication implements CommandLineRunner {

    private final UserService userService;
    private final MessageService messageService;
    private final TopicService topicService;

    public MessageClientApplication(final UserService userService,
                                    final MessageService messageService,
                                    final TopicService topicService) {
        this.userService = userService;
        this.messageService = messageService;
        this.topicService = topicService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MessageClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        RequestConfiguration configuration = new RequestConfiguration();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Welcome! Enter IP with port ot URL address of messaging server (type 'quit' to exit): ");

        healthCheck(configuration, scanner);

        System.out.println("Please enter login and password. If user does not exist it will be created, other-vice validated.");
        System.out.println("Below the list of existing users:");
        userService.getUserLogins(configuration)
                .forEach(System.out::println);

        validateUser(configuration, scanner);

        System.out.println("Choose topic for discussion:");

        validateTopic(configuration, scanner);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new GetMessageRunner(configuration), 0, 1, TimeUnit.SECONDS);

        while (true){
            String input = getInput(scanner);
            if (input.equalsIgnoreCase("-topic")){
                System.out.println("Choose topic for discussion:");
                validateTopic(configuration, scanner);
            }
            messageService.sendMessage(configuration, input);
        }
    }

    private void validateTopic(RequestConfiguration configuration, Scanner scanner) {
        List<String> allTopics = topicService.getAllTopics(configuration);
        allTopics.forEach(System.out::println);
        boolean isTopicNameWrong = true;
        while (isTopicNameWrong) {
            String selectedTopicName = getInput(scanner);
            isTopicNameWrong = !allTopics.contains(selectedTopicName);
            if (isTopicNameWrong) {
                System.out.println("Seems like we dont have this topic. Please reenter the name.");
                allTopics.forEach(System.out::println);
            } else {
                System.out.println("Topic defined: " + selectedTopicName);
            }
            configuration.setTopic(selectedTopicName);
        }
    }

    private void validateUser(RequestConfiguration configuration, Scanner scanner) {
        boolean userValidationFailed = true;
        while (userValidationFailed) {
            System.out.println("Enter login.");
            configuration.setLogin(getInput(scanner));
            System.out.println("Enter password.");
            configuration.setPassword(getInput(scanner));
            userValidationFailed = userService.isUserDataIncorrect(configuration);
        }
    }

    private void healthCheck(RequestConfiguration configuration, Scanner scanner) {
        boolean healthcheckFailed = true;
        while (healthcheckFailed) {
            configuration.setUrl(getInput(scanner));
            healthcheckFailed = messageService.isHealthcheckInvalid(configuration);
        }
    }

    private String getInput(Scanner scanner) {
        String input = scanner.nextLine();
        if ("quit".equalsIgnoreCase(input)) {
            System.out.println("Exiting...");
            scanner.close();
            System.exit(0);
        }
        return input;
    }
}
