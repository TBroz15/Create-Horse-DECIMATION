package dev.tuxebro.create_horse_decimation.utils;

import dev.tuxebro.create_horse_decimation.config.Config;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HorseCensorInator9000 {

    private static final Pattern HORSE_PATTERN = Pattern.compile(
            "h+o+r+s+e+|л+о+ш+а+д+ь*",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    );

    public static String CensorText(String text) {
        if (!canCensorHorse()) return text;

        if (text == null) return "";
        if (text.isEmpty()) return "";
        if (text.length() < 5) return text;

        if (!containsHorseLetters(text)) return text;

        Matcher matcher = HORSE_PATTERN.matcher(text);
        StringBuilder censoredTextBuilder = new StringBuilder();
        boolean found = false;

        while (matcher.find()) {
            found = true;

            String match = matcher.group();
            String lower = match.toLowerCase();

            int censorStart;
            int censorEnd;

            if (lower.contains("o")) {
                censorStart = lower.indexOf('o');
                censorEnd = lower.lastIndexOf('o');
            } else {
                censorStart = lower.indexOf('о');
                censorEnd = lower.lastIndexOf('о');
            }

            String prefix = match.substring(0, censorStart);
            String suffix = match.substring(censorEnd + 1);
            String censored = "*".repeat((censorEnd - censorStart) + 1);

            matcher.appendReplacement(
                    censoredTextBuilder,
                    Matcher.quoteReplacement(prefix + censored + suffix)
            );
        }

        if (!found) return text;

        matcher.appendTail(censoredTextBuilder);

        return censoredTextBuilder.toString();
    }

    public static MutableComponent CensorLiteral(String text) {
        return MutableComponent.create(
                PlainTextContents.create(CensorText(text))
        );
    }

    public static boolean containsHorseLetters(String input) {
        if (input == null) return false;

        int mask = 0;

        for (int i = 0; i < input.length(); i++) {
            char c = Character.toLowerCase(input.charAt(i));

            switch (c) {
                case 'h', 'л' -> mask |= 1;
                case 'o', 'о' -> mask |= 2;
                case 'r', 'ш' -> mask |= 4;
                case 's', 'а' -> mask |= 8;
                case 'e', 'д', 'ь' -> mask |= 16;
            }

            if (mask == 31) return true;
        }

        return false;
    }

    private static boolean canCensorHorse() {
        if (!Config.isLoaded) return false;
        return Config.client.censorHorseWord.get();
    }
}