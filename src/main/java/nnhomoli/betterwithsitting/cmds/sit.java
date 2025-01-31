package nnhomoli.betterwithsitting.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.lang.I18n;
import com.mojang.brigadier.tree.CommandNode;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import nnhomoli.betterwithsitting.entity.seat;

import java.util.ArrayList;
import java.util.Collection;

public class sit implements CommandManager.CommandRegistry {
	private static final SimpleCommandExceptionType PlayerIsNull = new SimpleCommandExceptionType(() -> {
		return I18n.getInstance().translateKey("cmd.sit.nullplayer");
	});

	@Override
	@SuppressWarnings("unchecked")
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		CommandNode<CommandSource> node = dispatcher.register((LiteralArgumentBuilder<CommandSource>) (Object) LiteralArgumentBuilder.literal("sit")
			.executes(
				context -> {
					CommandSource src = (CommandSource) context.getSource();
					Player player = src.getSender();
					if(player == null) throw PlayerIsNull.create();
					seat ent = new seat(player.world,player);
					player.world.addLoadedEntities(new ArrayList<Entity>((Collection<? extends Entity>) ent));
					return 0;
				}
		));
		dispatcher.register((LiteralArgumentBuilder<CommandSource>) (Object) LiteralArgumentBuilder.literal("seat").redirect((CommandNode <Object>) (Object) node));
	}
}
