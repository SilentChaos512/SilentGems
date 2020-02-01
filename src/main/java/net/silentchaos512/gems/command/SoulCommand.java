package net.silentchaos512.gems.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.silentchaos512.gems.item.SoulGemItem;
import net.silentchaos512.gems.lib.soul.Soul;

import java.util.Collection;
import java.util.stream.Collectors;

public final class SoulCommand {
    private static final SuggestionProvider<CommandSource> SOUL_ID_SUGGESTIONS = (context, builder) ->
            ISuggestionProvider.func_212476_a(Soul.getValues().stream().map(Soul::getId), builder);

    private SoulCommand() {}

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("sg_soul")
                .requires(source -> source.hasPermissionLevel(2));

        builder.then(Commands.literal("give_gem")
                .then(Commands.argument("players", EntityArgument.players())
                        .then(Commands.argument("soulID", ResourceLocationArgument.resourceLocation())
                                .suggests(SOUL_ID_SUGGESTIONS)
                                .executes(context -> giveSoulGem(context,
                                        EntityArgument.getPlayers(context, "players"),
                                        ResourceLocationArgument.getResourceLocation(context, "soulID"),
                                        1
                                ))
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes(context -> giveSoulGem(context,
                                                EntityArgument.getPlayers(context, "players"),
                                                ResourceLocationArgument.getResourceLocation(context, "soulID"),
                                                IntegerArgumentType.getInteger(context, "amount")
                                        ))
                                )
                        )
                )
        );

        builder.then(Commands.literal("list")
                .executes(context -> {
                    String line = Soul.getValues().stream()
                            .map(soul -> soul.getId().toString())
                            .collect(Collectors.joining(", "));
                    context.getSource().sendFeedback(new StringTextComponent(line), true);
                    return 1;
                })
        );

        dispatcher.register(builder);
    }

    private static int giveSoulGem(CommandContext<CommandSource> context, Collection<ServerPlayerEntity> players, ResourceLocation soulId, int count) {
        Soul soul = Soul.from(soulId);
        if (soul == null) {
            context.getSource().sendErrorMessage(new TranslationTextComponent("command.silentgems.soul.invalid"));
            return 0;
        }

        for (ServerPlayerEntity player : players) {
            int amountLeft = count;
            while (amountLeft > 0) {
                int stackSize = Math.min(64, amountLeft);
                amountLeft -= stackSize;
                ItemStack stack = SoulGemItem.getStack(soul, stackSize);

                // This part copied from GiveCommand
                boolean itemAdded = player.inventory.addItemStackToInventory(stack);
                if (itemAdded && stack.isEmpty()) {
                    stack.setCount(1);
                    ItemEntity itemEntity = player.dropItem(stack, false);
                    if (itemEntity != null) {
                        itemEntity.makeFakeItem();
                    }

                    player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    player.container.detectAndSendChanges();
                } else {
                    ItemEntity itemEntity = player.dropItem(stack, false);
                    if (itemEntity != null) {
                        itemEntity.setNoPickupDelay();
                        itemEntity.setOwnerId(player.getUniqueID());
                    }
                }
            }
        }

        return count;
    }
}
