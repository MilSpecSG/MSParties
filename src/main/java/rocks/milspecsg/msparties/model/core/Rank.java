package rocks.milspecsg.msparties.model.core;

import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.generator.dummy.DummyObjectProvider;
import rocks.milspecsg.msparties.model.Dbo;

public class Rank extends Dbo {

    public int index;

    public String name;

    public String color;

    public TextColor getColor() {
        switch (color.toLowerCase()) {
            case "aqua":
                return TextColors.AQUA;
            case "black":
                return TextColors.BLACK;
            case "blue":
                return TextColors.BLUE;
            case "dark_aqua":
                return TextColors.DARK_AQUA;
            case "dark_blue":
                return TextColors.DARK_BLUE;
            case "dark_gray":
                return TextColors.DARK_GRAY;
            case "dark_green":
                return TextColors.DARK_GREEN;
            case "dark_purple":
                return TextColors.DARK_PURPLE;
            case "dark_red":
                return TextColors.DARK_RED;
            case "gold":
                return TextColors.GOLD;
            case "gray":
                return TextColors.GRAY;
            case "green":
                return TextColors.GREEN;
            case "light_purple":
                return TextColors.LIGHT_PURPLE;
            case "red":
                return TextColors.RED;
            case "white":
                return TextColors.WHITE;
            case "yellow":
                return TextColors.YELLOW;
            default:
                return TextColors.WHITE;
        }
    }
}
