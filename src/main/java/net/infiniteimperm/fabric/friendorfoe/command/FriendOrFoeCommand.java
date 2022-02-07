package net.infiniteimperm.fabric.friendorfoe.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.infiniteimperm.fabric.friendorfoe.FriendOrFoe;
import net.minecraft.text.LiteralText;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;

public record FriendOrFoeCommand(
        Function<String, Boolean> function) implements com.mojang.brigadier.Command<FabricClientCommandSource> {

    @Override
    public int run(CommandContext<FabricClientCommandSource> context) {
        String playerName = StringArgumentType.getString(context, "playerName");
        if (playerName == null || "".equals(playerName)) {
            context.getSource().sendError(new LiteralText("§cplayerName was null or empty"));
            return 1;
        }

        final CompletableFuture<String> completableFuture;
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest
                    .newBuilder()
                    .GET()
                    .uri(new URI("https://api.mojang.com/users/profiles/minecraft/" + playerName))
                    .timeout(Duration.ofSeconds(2))
                    .build();
            completableFuture = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body);
        } catch (URISyntaxException e) {
            context.getSource().sendError(new LiteralText("§cerror on calling namemc. was the playerName correct? see log for more informations."));
            FriendOrFoe.LOGGER.error(e);
            return 1;
        }

        if (completableFuture != null) {
            context.getSource().sendFeedback(new LiteralText("waiting for namemc response"));
            ForkJoinPool.commonPool().execute(() -> {
                String response = completableFuture.join();
                FriendOrFoe.LOGGER.info("received from namemc: " + response);
                String[] split = response.split(",");
                if (split.length != 2) {
                    context.getSource().sendError(new LiteralText("§cresponse had the wrong format. see log for more informations."));
                    FriendOrFoe.LOGGER.error("response had the wrong format: expected 2 json entries but received: " + split.length);
                    return;
                }
                split = split[1].split(":");
                if (split.length != 2) {
                    context.getSource().sendError(new LiteralText("§cresponse had the wrong format. see log for more informations."));
                    FriendOrFoe.LOGGER.error("response had the wrong format: expected 2 json entries but received: " + split.length);
                    return;
                }
                String uuid = split[1].replace("\"", "").replace("}", "").replace("{", "");
                FriendOrFoe.LOGGER.info("player " + playerName + " translated to UUID " + uuid);
                uuid = uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20);
                if (function.apply(uuid)) {
                    context.getSource().sendFeedback(new LiteralText("§aplayer " + playerName + " with UUID " + uuid + " processed"));
                } else {
                    context.getSource().sendError(new LiteralText("§cerror on processing player " + playerName + " with UUID " + uuid));
                }
            });
            return 0;
        }

        context.getSource().sendError(new LiteralText("§ccouldn't call namemc"));
        return 1;
    }

}
