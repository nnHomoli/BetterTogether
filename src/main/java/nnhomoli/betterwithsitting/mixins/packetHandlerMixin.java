package nnhomoli.betterwithsitting.mixins;


import net.minecraft.server.net.handler.PacketHandlerServer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = PacketHandlerServer.class,remap = false)
public class packetHandlerMixin {
//	@Shadow private MinecraftServer mcServer;
//	@Shadow private PlayerServer playerEntity;
//
//	@Inject(method = "handleUseEntity",at= @At(value = "INVOKE", target = "Lnet/minecraft/server/entity/player/PlayerServer;useCurrentItemOnEntity(Lnet/minecraft/core/entity/Entity;)Z"),remap = false)
//	public void useCurrentItemOnEntity(PacketInteract interactPacket, CallbackInfo ci) {
//		WorldServer worldserver = this.mcServer.getDimensionWorld(this.playerEntity.dimension);
//		Entity targetEntity = worldserver.getEntityFromId(interactPacket.targetEntityID);
//	}
}
