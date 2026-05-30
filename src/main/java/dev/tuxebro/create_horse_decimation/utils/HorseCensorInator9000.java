package dev.tuxebro.create_horse_decimation.utils;

import dev.tuxebro.create_horse_decimation.config.Config;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HorseCensorInator9000 {
    private static final Pattern HORSE_PATTERN = Pattern.compile("h+o+r+s+e+", Pattern.CASE_INSENSITIVE);

    public static String CensorText(String text) {
        if (!canCensorHorse()) return text;

        if (text == null) return "";
        if (text.isEmpty()) return "";
        if (text.length() < 5) return text; // ofc "h*rse" is not a 4 letter word (or less)

        if (!containsHorseLetters(text)) return text;

        Matcher matcher = HORSE_PATTERN.matcher(text);
        StringBuilder censoredTextBuilder = new StringBuilder();
        boolean found = false;

        while (matcher.find()) {
            found = true;
            String match = matcher.group();

            int firstO = match.toLowerCase().indexOf('o');
            int lastO = match.toLowerCase().lastIndexOf('o');

            String prefix = match.substring(0, firstO);
            String suffix = match.substring(lastO + 1);
            String censored = "*".repeat((lastO - firstO) + 1);

            matcher.appendReplacement(censoredTextBuilder, prefix + censored + suffix);
        }

        if (!found) return text;
        matcher.appendTail(censoredTextBuilder);

        return censoredTextBuilder.toString();
    }

    public static MutableComponent CensorLiteral(String text) {
        return MutableComponent.create(PlainTextContents.create(CensorText(text)));
    }

    // tbh i let gemini ai do a silly optimization that checks letters
    // instead of spamming string.contains() five times
    // so it can prevent running regex replacement
    //
    // now i know how bitmasking works
    // which in my understanding, its like storing multiple booleans into one integer
    // this is why i pull a socratic method
    // and learn from ai instead of copying code out of the blue -tbroz15
    public static boolean containsHorseLetters(String input) {
        if (input == null) return false;

        int mask = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c >= 'A' && c <= 'Z') {
                c |= 32; // makes the character lowercased
            }

            // check the char if it has those letters
            switch (c) {
                case 'h' -> mask |= 1;
                case 'o' -> mask |= 2;
                case 'r' -> mask |= 4;
                case 's' -> mask |= 8;
                case 'e' -> mask |= 16;
            }

            if (mask == 31) return true; // because 1+2+4+8+16
        }

        return false;
    }

    private static boolean canCensorHorse() {
        if (!Config.isLoaded) return false; // prevent it from crashing, dont remove this line fr
        return Config.client.censorHorseWord.get();
    }
}
