package dev.emi.emi.platform.forge;

import com.rewindmc.retroemi.PacketReader;
import com.rewindmc.retroemi.RetroEMI;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import dev.emi.emi.data.EmiData;
import dev.emi.emi.data.EmiResourceReloadListener;
import dev.emi.emi.mixin.accessor.PlayerControllerMPAccessor;
import dev.emi.emi.network.*;
import dev.emi.emi.platform.EmiClient;
import dev.emi.emi.platform.EmiMain;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraftforge.common.MinecraftForge;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

@Mod(
    modid = "emi",
    guiFactory = "dev.emi.emi.compat.EmiGuiFactory"
)
public class EmiForge {

    public EmiForge() {
        EmiMain.init();
        EmiNetwork.initServer((player, packet) -> {
            player.playerNetServerHandler.sendPacket(toVanilla(packet));
        });
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(new EmiClientForge());
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        EmiMain.init();
        EmiAgnosForge.poke();
        Client.init();
    }

    @Mod.EventHandler
    public void postInit(FMLInitializationEvent event) {
        PacketReader.registerServerPacketReader(EmiNetwork.FILL_RECIPE, FillRecipeC2SPacket::new);
        PacketReader.registerServerPacketReader(EmiNetwork.CREATE_ITEM, CreateItemC2SPacket::new);
        PacketReader.registerServerPacketReader(EmiNetwork.CHESS, EmiChessPacket.C2S::new);    }

//    public void registerCommands(RegisterCommandsEvent event) {
//        EmiCommands.registerCommands(event.getDispatcher());
//    }

    @SubscribeEvent
    public void playerConnect(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP spe) {
            EmiNetwork.sendToClient(spe, new PingS2CPacket(spe.mcServer.isDedicatedServer() || (spe.mcServer instanceof IntegratedServer integratedServer && integratedServer.getPublic())));
        }
    }

    private static S3FPacketCustomPayload toVanilla(EmiPacket packet) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        PacketByteBuf buf = PacketByteBuf.out(dos);
        packet.write(buf);
        S3FPacketCustomPayload pkt = new S3FPacketCustomPayload(RetroEMI.compactify(packet.getId()), baos.toByteArray());
        return pkt;
    }

    public static final class Client {

        public static void init() {
            EmiClient.init();
            EmiData.init(EmiResourceReloadListener::reload);

            EmiNetwork.initClient(packet -> ((PlayerControllerMPAccessor) Minecraft.getMinecraft().playerController).getNetClientHandler().addToSendQueue(toVanilla(packet)));
            PacketReader.registerClientPacketReader(EmiNetwork.PING, PingS2CPacket::new);
            //NYI
            //PacketReader.registerClientPacketReader(EmiNetwork.COMMAND, CommandS2CPacket::new);
            PacketReader.registerClientPacketReader(EmiNetwork.CHESS, EmiChessPacket.S2C::new);
        }
    }
}
