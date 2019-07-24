package net.silentchaos512.gems.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.silentchaos512.gems.chaos.ChaosEvents;
import net.silentchaos512.gems.chaos.ChaosSourceCapability;

public final class ChaosCommand {
    private static final SuggestionProvider<CommandSource> EVENT_ID_SUGGESTIONS = (ctx, builder) ->
            ISuggestionProvider.func_212476_a(ChaosEvents.getEventIds().stream(), builder);

    private ChaosCommand() {}

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("sg_chaos").requires(source ->
                source.hasPermissionLevel(2));

        // get
        builder
                .then(Commands.literal("get")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(
                                        ChaosCommand::runGet
                                )
                        )
                        .then(Commands.literal("world")
                                .executes(
                                        ChaosCommand::runGetWorld
                                )
                        )
                        .executes(
                                context -> runGetSingle(context, context.getSource().asPlayer())
                        )
                );
        // set
        builder
                .then(Commands.literal("set")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                        .executes(
                                                ChaosCommand::runSet
                                        )
                                )
                        )
                        .then(Commands.literal("world")
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                        .executes(
                                                ChaosCommand::runSetWorld
                                        )
                                )
                        )
                );
        // trigger event
        builder.then(Commands.literal("trigger_event")
                .then(Commands.argument("target", EntityArgument.player())
                        .then(Commands.argument("event", ResourceLocationArgument.resourceLocation())
                                .suggests(EVENT_ID_SUGGESTIONS)
                                .executes(context -> {
                                    ChaosEvents.triggerEvent(
                                            ResourceLocationArgument.getResourceLocation(context, "event"),
                                            EntityArgument.getPlayer(context, "target"));
                                    return 1;
                                })
                        )
                )
        );

        dispatcher.register(builder);
    }

    private static int runGet(CommandContext<CommandSource> context) throws CommandSyntaxException {
        for (ServerPlayerEntity player : EntityArgument.getPlayers(context, "targets")) {
            runGetSingle(context, player);
        }
        return 1;
    }

    private static int runGetSingle(CommandContext<CommandSource> context, ServerPlayerEntity player) {
        player.getCapability(ChaosSourceCapability.INSTANCE).ifPresent(source -> {
            String format = String.format("%,d", source.getChaos());
            ITextComponent text = translate("chaos.get", player.getName(), format);
            context.getSource().sendFeedback(text, true);
        });
        return 1;
    }

    private static int runGetWorld(CommandContext<CommandSource> context) {
        ServerWorld world = context.getSource().getWorld();
        world.getCapability(ChaosSourceCapability.INSTANCE).ifPresent(source -> {
            String format = String.format("%,d", source.getChaos());
            ITextComponent text = translate("chaos.get", "world", format);
            context.getSource().sendFeedback(text, true);
        });
        return 1;
    }

    private static int runSet(CommandContext<CommandSource> context) throws CommandSyntaxException {
        int amount = IntegerArgumentType.getInteger(context, "amount");
        for (ServerPlayerEntity player : EntityArgument.getPlayers(context, "targets")) {
            player.getCapability(ChaosSourceCapability.INSTANCE).ifPresent(source -> {
                source.setChaos(amount);
                String format = String.format("%,d", source.getChaos());
                ITextComponent text = translate("chaos.set", player.getName(), format);
                context.getSource().sendFeedback(text, true);
            });
        }
        return 1;
    }

    private static int runSetWorld(CommandContext<CommandSource> context) {
        int amount = IntegerArgumentType.getInteger(context, "amount");
        ServerWorld world = context.getSource().getWorld();
        world.getCapability(ChaosSourceCapability.INSTANCE).ifPresent(source -> {
            source.setChaos(amount);
            String format = String.format("%,d", source.getChaos());
            ITextComponent text = translate("chaos.set", "world", format);
            context.getSource().sendFeedback(text, true);
        });
        return 1;
    }

    private static ITextComponent translate(String key, Object... params) {
        return new TranslationTextComponent("command.silentgems." + key, params);
    }
}
