package info.itsthesky.disky.tools;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;

public class Webhooktest {

    public Webhooktest() {
        WebhookClientBuilder builder = new WebhookClientBuilder("https://discord.com/api/webhooks/819639477223424080/IBJ9RNmIJTKselmtjnVzEgWiPdFzcEAwYvrA8ofBxyqn9uAKQRUOwE-7u0cQlrOg_SCa"); // or id, token
        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Hello");
            thread.setDaemon(true);
            return thread;
        });
        builder.setWait(true);
        WebhookClient client = builder.build();

        client.send("Hello World");
        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(0xFF00EE)
                .setDescription("Hello World")
                .build();

        client.send(embed)
                .thenAccept((message) -> System.out.printf("Message with embed has been sent [%s]%n", message.getId()));
        WebhookMessageBuilder builder2 = new WebhookMessageBuilder();
        builder2.setUsername("Minn");
        builder2.setAvatarUrl("avatarUrl");
        builder2.setContent("Hello World");
        client.send(builder2.build());
    }

}
