package dev.emi.emi.platform.forge;

//@Mod.EventBusSubscriber(modid = "emi", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EmiClientForge {

//    @SubscribeEvent
//    public static void clientInit(FMLClientSetupEvent event) {
//        StackBatcher.EXTRA_RENDER_LAYERS.addAll(Arrays.stream(ForgeRenderTypes.values()).map(f -> f.get()).toList());
//        EmiClient.init();
//        EmiNetwork.initClient(packet -> EmiPacketHandler.CHANNEL.send(packet, MinecraftClient.getInstance().getNetworkHandler().getConnection()));
//        MinecraftForge.EVENT_BUS.addListener(EmiClientForge::recipesReloaded);
//        MinecraftForge.EVENT_BUS.addListener(EmiClientForge::tagsReloaded);
//        MinecraftForge.EVENT_BUS.addListener(EmiClientForge::renderScreenForeground);
//        MinecraftForge.EVENT_BUS.addListener(EmiClientForge::postRenderScreen);
//        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
//            () -> new ConfigScreenHandler.ConfigScreenFactory((client, last) -> new ConfigScreen(last)));
//    }
//
//    @SubscribeEvent
//    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
//        MinecraftClient client = MinecraftClient.getInstance();
//        EmiTags.registerTagModels(client.getResourceManager(), event::register);
//    }
//
//    @SubscribeEvent
//    public static void registerResourceReloaders(RegisterClientReloadListenersEvent event) {
//        EmiData.init(reloader -> event.registerReloadListener(reloader));
//    }
//
//    public static void recipesReloaded(RecipesUpdatedEvent event) {
//        EmiReloadManager.reloadRecipes();
//    }
//
//    public static void tagsReloaded(TagsUpdatedEvent event) {
//        EmiReloadManager.reloadTags();
//    }

//    @SubscribeEvent
//    public void renderScreenForeground(GuiScreenEvent.DrawScreenEvent.Post event) {
//        EmiDrawContext context = EmiDrawContext.instance();
////        GuiContainer screenRaw = (GuiContainer) event.gui;
//        GuiContainerAccessor screen = (GuiContainerAccessor) event.gui;
//        EmiScreenBase base = EmiScreenBase.of(event.gui);
//        if (base != null) {
//            Minecraft client = Minecraft.getMinecraft();
//            context.push();
//            context.matrices().translate(-screen.getGuiLeft(), -screen.getGuiTop(), 0.0);
//            EmiPort.setPositionTexShader();
//            EmiScreenManager.render(context, event.mouseX, event.mouseY, client.timer.renderPartialTicks);
//            EmiScreenManager.drawForeground(context, event.mouseX, event.mouseY, client.timer.renderPartialTicks);
//            context.pop();
//        }
//    }
//
//    @SubscribeEvent
//    public void postRenderScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
//        EmiDrawContext context = EmiDrawContext.instance();
//        GuiScreen screen = event.gui;
//        if (!(screen instanceof GuiContainer)) {
//            return;
//        }
//        EmiScreenBase base = EmiScreenBase.of(screen);
//        if (base != null) {
//            Minecraft client = Minecraft.getMinecraft();
//            context.push();
//            EmiPort.setPositionTexShader();
//            EmiScreenManager.drawForeground(context, event.mouseX, event.mouseY, client.timer.renderPartialTicks);
//            context.pop();
//        }
//    }
}
