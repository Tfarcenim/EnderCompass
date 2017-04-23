package io.github.mribby.endercompass.client;

import io.github.mribby.endercompass.EnderCompassMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.List;
import java.util.Set;

public class EnderCompassGuiFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraftInstance) {}

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return EnderCompassConfigGui.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }

    public static class EnderCompassConfigGui extends GuiConfig {
        public EnderCompassConfigGui(GuiScreen parentScreen) {
            super(parentScreen, getConfigElements(), "endercompass", false, false, I18n.format("endercompass.configgui.title"));
        }

        private static List<IConfigElement> getConfigElements() {
            ConfigCategory category = EnderCompassMod.config.getCategory(Configuration.CATEGORY_GENERAL);
            IConfigElement element = new ConfigElement(category);
            return element.getChildElements();
        }
    }
}
