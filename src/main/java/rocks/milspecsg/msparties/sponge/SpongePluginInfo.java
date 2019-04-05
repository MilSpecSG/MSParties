package rocks.milspecsg.msparties.sponge;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msparties.PluginInfo;

abstract class SpongePluginInfo extends PluginInfo {
    static final Text PluginPrefix = Text.of(TextColors.GREEN, PluginInfo.PluginPrefixText);
}
