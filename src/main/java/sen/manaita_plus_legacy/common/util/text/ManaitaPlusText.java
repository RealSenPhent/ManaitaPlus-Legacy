package sen.manaita_plus_legacy.common.util.text;

import net.minecraft.ChatFormatting;

import javax.annotation.Nonnull;
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
        this.delay = delay;
    }

    public String formatting(@Nonnull String input) {
        input = ChatFormatting.stripFormatting(input);
        assert input != null;
        String[] colours = this.chatFormattings;
        StringBuilder sb = new StringBuilder(input.length() * 3);
        int offset = (int)Math.floor((System.currentTimeMillis() & 0x3FFFL) / delay) % colours.length;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            int col = (i + colours.length - offset) % colours.length;
            sb.append(colours[col]);
            sb.append(c);
        }
        return sb.toString();
    }
}
