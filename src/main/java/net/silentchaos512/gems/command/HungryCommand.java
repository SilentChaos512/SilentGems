package net.silentchaos512.gems.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Just here to make testing foods easier.
 */
public final class HungryCommand {
    private HungryCommand() {}

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("mesohungry").requires(source ->
                source.hasPermissionLevel(2));
        builder.then(
                Commands.argument("target", EntityArgument.singlePlayer()).executes(context -> {
                    EntityPlayerMP player = EntityArgument.getOnePlayer(context, "target");
                    player.getFoodStats().setFoodLevel(2);
                    return 1;
                })
        );
        dispatcher.register(builder);
    }
}
