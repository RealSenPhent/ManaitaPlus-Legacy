package sen.manaita_plus_legacy.common.util;

import net.minecraft.ChatFormatting;

import java.util.Arrays;

public enum ManaitaPlusText {
    manaita_infinity(80.0D,
            ChatFormatting.RED, ChatFormatting.GOLD, ChatFormatting.YELLOW, ChatFormatting.GREEN,
            ChatFormatting.AQUA, ChatFormatting.BLUE, ChatFormatting.LIGHT_PURPLE),
    manaita_mode(120.0D,
            ChatFormatting.YELLOW, ChatFormatting.YELLOW, ChatFormatting.YELLOW, ChatFormatting.YELLOW, ChatFormatting.YELLOW,
            ChatFormatting.YELLOW, ChatFormatting.GOLD, ChatFormatting.RED, ChatFormatting.YELLOW, ChatFormatting.YELLOW, ChatFormatting.YELLOW,
            ChatFormatting.YELLOW, ChatFormatting.YELLOW, ChatFormatting.YELLOW, ChatFormatting.GOLD, ChatFormatting.RED),
    manaita_enchantment(120.0D,
            ChatFormatting.LIGHT_PURPLE, ChatFormatting.LIGHT_PURPLE, ChatFormatting.LIGHT_PURPLE, ChatFormatting.LIGHT_PURPLE,
            ChatFormatting.BLUE, ChatFormatting.DARK_PURPLE, ChatFormatting.LIGHT_PURPLE, ChatFormatting.LIGHT_PURPLE,
            ChatFormatting.LIGHT_PURPLE, ChatFormatting.LIGHT_PURPLE, ChatFormatting.AQUA, ChatFormatting.DARK_PURPLE);

    private final String[] chatFormattings;
    private final double delay;

    ManaitaPlusText(double delay,ChatFormatting... chatFormattings) {
        this.chatFormattings = Arrays.stream(chatFormattings).map(ChatFormatting::toString).toArray(String[]::new);
        if (delay <= 0.0D)
            delay = 0.001D;
        this.delay = delay;
    }

    public String formatting(String input) {
        String stripFormatting = ChatFormatting.stripFormatting(input);
        String[] colours = this.chatFormattings;
        StringBuilder sb = new StringBuilder(stripFormatting.length() * 3);
        int offset = (int)Math.floor((System.currentTimeMillis() & 0x3FFFL) / delay) % colours.length;
        for (int i = 0; i < stripFormatting.length(); i++) {
            char c = stripFormatting.charAt(i);
            int col = (i + colours.length - offset) % colours.length;
            sb.append(c);
            sb.append(colours[col]);
        }
        return sb.toString();
    }
}
