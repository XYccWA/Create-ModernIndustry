package org.xyccwa.create_modern_industry.command;

import java.util.Optional;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.xyccwa.create_modern_industry.CreateModernIndustry;
import org.xyccwa.create_modern_industry.compat.CompatFlags;
import org.xyccwa.create_modern_industry.compat.aeronautics.AeronauticsCompat;
import org.xyccwa.create_modern_industry.compat.sable.SableCompat;

/**
 * {@code /cmi} 调试命令。
 *
 * <p>基础阶段最有用的一条：{@code /cmi status} —— 一次性打印
 * 本模组版本、兼容模组探测结果、以及玩家当前是否站在某块航空载具（Sable 子层级）上。
 * 之后做兼容内容时，这条命令可以用来确认「子层级查询这条路通不通」。
 */
public final class CMICommands {

    private CMICommands() {
    }

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("cmi")
                .then(Commands.literal("status").executes(context -> status(context.getSource())))
                .executes(context -> status(context.getSource())));
    }

    private static int status(CommandSourceStack source) {
        source.sendSuccess(() -> Component.literal("== " + CreateModernIndustry.MOD_NAME + " ==")
                .withStyle(ChatFormatting.GOLD), false);

        source.sendSuccess(() -> line("version", CompatFlags.versionOf(CreateModernIndustry.MOD_ID)), false);
        source.sendSuccess(() -> line("create", CompatFlags.versionOf(CompatFlags.CREATE)), false);
        source.sendSuccess(() -> line("sable", CompatFlags.versionOf(CompatFlags.SABLE)), false);
        source.sendSuccess(() -> line("simulated", CompatFlags.versionOf(CompatFlags.SIMULATED)), false);
        source.sendSuccess(() -> line("aeronautics", CompatFlags.versionOf(CompatFlags.AERONAUTICS)), false);
        source.sendSuccess(() -> line("offroad", CompatFlags.versionOf(CompatFlags.OFFROAD)), false);
        source.sendSuccess(() -> line("jei", CompatFlags.versionOf(CompatFlags.JEI)), false);

        source.sendSuccess(() -> line("aeronauticsSuite", AeronauticsCompat.isAvailable() ? "ready" : "incomplete"), false);
        source.sendSuccess(() -> line("sableApi", (SableCompat.isAvailable() ? "usable" : "disabled")), false);

        if (source.getEntity() instanceof ServerPlayer player) {
            Optional<String> subLevel = SableCompat.subLevelNameAt(player.serverLevel(), player.position());
            source.sendSuccess(() -> line("subLevel", subLevel.orElse("<不在子层级中>")), false);
        }

        return 1;
    }

    private static Component line(String key, String value) {
        return Component.literal(" " + key + ": ").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(value).withStyle(ChatFormatting.WHITE));
    }
}
