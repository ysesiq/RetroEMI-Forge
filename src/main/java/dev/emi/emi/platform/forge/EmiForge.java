package dev.emi.emi.platform.forge;

import com.rewindmc.retroemi.RetroEMI;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import dev.emi.emi.EMIPostInit;
import dev.emi.emi.network.EmiNetwork;
import dev.emi.emi.network.EmiPacket;
import dev.emi.emi.network.PingS2CPacket;
import dev.emi.emi.platform.EmiMain;
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
    }

    @Mod.EventHandler
    public void postInit(FMLInitializationEvent event) {
        EMIPostInit.initEMI();
    }

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
}
