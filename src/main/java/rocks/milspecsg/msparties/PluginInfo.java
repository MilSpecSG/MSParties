package rocks.milspecsg.msparties;


import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public abstract class PluginInfo {
    public static final String Id = "msparties";
    public static final String Name = "MSParties";
    public static final String Version = "0.1.0-dev";
    public static final String Description = "Parties plugin api";
    public static final String Url = "https://milspecsg.rocks";
    public static final String Authors = "Cableguy20";
    public static final Text PluginPrefix = Text.of(TextColors.GREEN, "[MSParties] ");
}
