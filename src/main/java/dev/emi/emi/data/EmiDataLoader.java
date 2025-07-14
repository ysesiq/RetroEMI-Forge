package dev.emi.emi.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dev.emi.emi.runtime.EmiLog;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class EmiDataLoader<T> {
    private static final Gson GSON = new Gson();
    private final String path;
    private final Supplier<T> baseSupplier;
    private final DataConsumer<T> prepare;
    private final Consumer<T> apply;

    public EmiDataLoader(String path, Supplier<T> baseSupplier,
                         DataConsumer<T> prepare, Consumer<T> apply) {
        this.path = path;
        this.baseSupplier = baseSupplier;
        this.prepare = prepare;
        this.apply = apply;
    }

    public T prepare(IResourceManager manager) {
        T t = baseSupplier.get();
        Collection<String> domains = manager.getResourceDomains();

        for (String domain : domains) {
            try {
                String resourcePath = path.endsWith(".json") ? path : path + ".json";
                ResourceLocation location = new ResourceLocation(domain, resourcePath);

                List<IResource> resources = manager.getAllResources(location);

                for (IResource resource : resources) {
                    try (InputStream stream = resource.getInputStream();
                         InputStreamReader reader = new InputStreamReader(stream)) {

                        JsonObject json = GSON.fromJson(reader, JsonObject.class);
                        ResourceLocation id = new ResourceLocation(domain, resourcePath);

                        if (!domain.equals("emi")) {
                            continue;
                        }

                        prepare.accept(t, json, id);
                    } catch (Exception e) {
                        EmiLog.error("Error loading data in " + domain + ":" + resourcePath, e);
                    }
                }
            } catch (IOException e) {
                EmiLog.error("Error accessing resources in domain: " + domain, e);
            }
        }

        return t;
    }

    public void apply(T t) {
        apply.accept(t);
    }

    public void load(IResourceManager manager) {
        T data = prepare(manager);
        apply(data);
    }

    public interface DataConsumer<T> {
        void accept(T t, JsonObject json, ResourceLocation id);
    }

    public interface Supplier<T> {
        T get();
    }

    public interface Consumer<T> {
        void accept(T t);
    }
}
