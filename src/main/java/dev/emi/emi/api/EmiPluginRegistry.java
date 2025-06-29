package dev.emi.emi.api;

import dev.emi.emi.VanillaPlugin;
import dev.emi.emi.registry.EmiPluginContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmiPluginRegistry {
    private static final Map<String, List<EmiPlugin>> plugins = new HashMap<>();

    /**
     * Used to register the EmiPlugin Mod in the @Mod class of the Mod
     */
    public static void registerPlugin(String modId, EmiPlugin plugin) {
        plugins.computeIfAbsent(modId, k -> new ArrayList<>()).add(plugin);
    }

    /**
     * Get all registered EMI plugin containers
     */
    public static List<EmiPluginContainer> getPlugins() {
        List<EmiPluginContainer> containers = new ArrayList<>();
        for (Map.Entry<String, List<EmiPlugin>> entry : plugins.entrySet()) {
            String modId = entry.getKey();
            for (EmiPlugin plugin : entry.getValue()) {
                containers.add(new EmiPluginContainer(plugin, modId));
            }
        }
        containers.add(new EmiPluginContainer(new VanillaPlugin(), "emi"));
        return containers;
    }
}
