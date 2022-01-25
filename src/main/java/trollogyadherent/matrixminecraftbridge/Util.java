package trollogyadherent.matrixminecraftbridge;

import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static Util instance;

    // Pattern for recognizing a URL, based off RFC 3986
    private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    private boolean isUrl(String text) {
        Matcher matcher = urlPattern.matcher(text);
        while (matcher.find()) {
            return true;
        }
        return false;
    }

    private ArrayList getSplitUrls(String str) {
        ArrayList<String> text_and_urls = new ArrayList<>();
        Matcher matcher = urlPattern.matcher(str);
        int lastIndex = 0;
        while (matcher.find()) {
            int matchStart = matcher.start(1);
            int matchEnd = matcher.end();
            //System.out.println("Found url in message, start_index: " + matchStart + ", end_index: " + matchEnd);
            text_and_urls.add(str.substring(lastIndex, matchStart));
            text_and_urls.add(str.substring(matchStart, matchEnd - 1));
            lastIndex = matchEnd - 1;
        }
        text_and_urls.add(str.substring(lastIndex));
        return text_and_urls;
    }

    public ChatComponentText getChatCompWithLinks(String str) {
        ArrayList<String> splitUrls = getSplitUrls(str);
        ChatComponentText res = new ChatComponentText("");
        for (String elem : splitUrls) {
            if (isUrl(elem)) {
                res.appendSibling(new ChatComponentText(elem).setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, elem)).setColor(EnumChatFormatting.BLUE)));
            } else {
                res.appendText(elem);
            }
        }
        return res;
    }

    public static Util getInstance() {
        if (instance == null) {
            instance = new Util();
        }
        return instance;
    }
}
