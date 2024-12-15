package module.discord;


import chrome.ChromeDriverToolFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import s3.service.S3UploaderService;

import java.awt.*;
import java.io.IOException;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Component
public class DiscordBot extends ListenerAdapter {


    private final DiscordMessageProcessor discordMessageProcessor;

    private ChromeDriverToolFactory chromeDriverToolFactory;

    private S3UploaderService s3UploaderService;
    private JDA jda;

    @Value("${discord.bot.token}")
    private String discordBotToken;

    @PostConstruct
    public void init() throws IOException {
        jda = JDABuilder.createDefault(discordBotToken)
                .setActivity(Activity.playing("서버 실행중"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(this)
                .build();

        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<TextChannel> textChannels = jda.getTextChannels();

    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);

        User user = event.getAuthor();

        //로봇이 보낸건 무시.
        if (user.isBot() && !event.getMessage().toString().startsWith("!")) {
            return;
        }

        TextChannel textChannel = event.getChannel().asTextChannel();

        Long channelId = textChannel.getIdLong();

        String returnMessage = null;
        if (channelId.equals(DiscordString.ALL_CATEGORIES_CHANNEL)) {
            returnMessage = discordMessageProcessor.responseServerRunningOrNull("all_categories", event.getMessage().getContentDisplay(), chromeDriverToolFactory.getChromeDriverTool("all_categories"));
        } else if (channelId.equals(DiscordString.DOUBLE_F_DISCOUNT_CHANNEL) || channelId.equals(DiscordString.DOUBLE_F_NEW_PRODUCT_CHANNEL)) {
            returnMessage = discordMessageProcessor.responseServerRunningOrNull("doublef", event.getMessage().getContentDisplay(), chromeDriverToolFactory.getChromeDriverTool("doublef"));
        } else if (channelId.equals(DiscordString.BIFFI_DISCOUNT_CHANNEL) || channelId.equals(DiscordString.BIFFI_NEW_PRODUCT_CHANNEL)) {
            returnMessage = discordMessageProcessor.responseServerRunningOrNull("biffi", event.getMessage().getContentDisplay(), chromeDriverToolFactory.getChromeDriverTool("biffi"));
        } else if (channelId.equals(DiscordString.STYLE_NEW_PRODUCT_CHANNEL) || channelId.equals(DiscordString.STYLE_DISCOUNT_CHANNEL)) {
            returnMessage = discordMessageProcessor.responseServerRunningOrNull("style", event.getMessage().getContentDisplay(), chromeDriverToolFactory.getChromeDriverTool("style"));
        } else if(channelId.equals(DiscordString.EIC_NEW_PRODUCT_CHANNEL) || channelId.equals(DiscordString.EIC_DISCOUNT_CHANNEL)) {
            returnMessage = discordMessageProcessor.responseServerRunningOrNull("eic", event.getMessage().getContentDisplay(), chromeDriverToolFactory.getChromeDriverTool("eic"));
        }
        else if (channelId.equals(DiscordString.GEBENE_NEW_PRODUCT_CHANNEL)) {
            if (event.getMessage().getContentDisplay().contains("!upload")) {
                returnMessage = discordMessageProcessor.responseServerRunningS3ServiceOrNull(event.getMessage().getContentDisplay(), s3UploaderService);
            } else {
                returnMessage = discordMessageProcessor.responseServerRunningOrNull("gebe", event.getMessage().getContentDisplay(), chromeDriverToolFactory.getChromeDriverTool("gebe"));
            }
        } else if (channelId.equals(DiscordString.EXCHANGE_CHANNEL)) {
            returnMessage = discordMessageProcessor.responseExchangeFee(event.getMessage().getContentDisplay());
        }

        if (returnMessage != null) {
            textChannel.sendMessage(returnMessage).queue();
        }
    }

    public void sendMessage(Long channelId, String message) {
        final TextChannel textChannel = jda.getTextChannelById(channelId);
        assert textChannel != null;
        textChannel.sendMessage(message).queue(); // 품번도 같이 전송
    }

    public void sendNewProductInfoCommon(Long channelId, String description, String productLink, String imageUrlOrNull, String[] skuInfo) {
        final TextChannel textChannel = jda.getTextChannelById(channelId);
        assert (textChannel != null);

        // Embed 생성
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("새 상품 알림!");
        embed.setDescription(description);
        embed.setColor(Color.GREEN);

        embed.addField("사이트 상품 바로가기", "[상세페이지 바로가기](" + productLink + ")", false); // false는 필드가 인라인으로 표시되지 않도록 설정합니다.
        if (imageUrlOrNull != null) {
            embed.setImage(imageUrlOrNull); // 웹 이미지 사용
        }
        String skuInfoToString = String.join(" ", skuInfo);
        textChannel.sendMessageEmbeds(embed.build()).queue();
        textChannel.sendMessage(skuInfoToString).queue(); // 품번도 같이 전송
    }

    public void sendDiscountChangeInfoCommon(Long channelId, String description, String productLink, String imageUrlOrNull, String[] skuInfo) {
        final TextChannel textChannel = jda.getTextChannelById(channelId);
        assert (textChannel != null);

        // Embed 생성
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("할인율 바뀌었습니다!!!!");
        embed.setDescription(description);
        embed.setColor(Color.BLUE);

        embed.addField("사이트 상품 바로가기", "[상세페이지 바로가기](" + productLink + ")", false); // false는 필드가 인라인으로 표시되지 않도록 설정합니다.
        if (imageUrlOrNull != null) {
            embed.setImage(imageUrlOrNull); // 웹 이미지 사용
        }
        String skuInfoToString = String.join(" ", skuInfo);
        textChannel.sendMessageEmbeds(embed.build()).queue();
        textChannel.sendMessage(skuInfoToString).queue(); // 품번도 같이 전송
    }

    public void setS3UploaderService(S3UploaderService s3UploaderService) {
        this.s3UploaderService = s3UploaderService;
    }

    public void setChromeDriverTool(ChromeDriverToolFactory chromeDriverToolFactory) {
        this.chromeDriverToolFactory = chromeDriverToolFactory;
    }


}
