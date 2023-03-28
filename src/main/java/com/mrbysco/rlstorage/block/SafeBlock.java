package com.mrbysco.rlstorage.block;

import com.mrbysco.rlstorage.block.entity.SafeBlockEntity;
import com.mrbysco.rlstorage.storage.SafeInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class SafeBlock extends BaseEntityBlock {
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public SafeBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, false));
	}

	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		}
		if (level.getBlockEntity(pos) instanceof SafeBlockEntity safeBlockEntity) {
			if (handIn == InteractionHand.MAIN_HAND) {
				if (player.isShiftKeyDown()) {
					BlockState newState = (BlockState) state.setValue(OPEN, !(Boolean) state.getValue(OPEN));
					level.setBlock(pos, newState, 3);
					level.playSound((Player) null, pos, SoundEvents.UI_BUTTON_CLICK, SoundSource.BLOCKS, 0.3F, 0.6F);
					return InteractionResult.CONSUME;
				} else {
					SimpleContainer safeContainer = safeBlockEntity.getInventory(player.getUUID(), level);
					SafeInventory safeInventory = safeContainer == null ? safeBlockEntity.getInventory(player.getUUID(), level) : (SafeInventory) safeContainer;
					if (safeInventory != null) {
						safeInventory.setAssociatedVault(safeBlockEntity);
						player.openMenu(new SimpleMenuProvider((id, inventory, playerIn) ->
								new ChestMenu(MenuType.GENERIC_9x1, id, inventory, safeContainer, 1), safeBlockEntity.getDisplayName()));
						return InteractionResult.CONSUME;
					}
				}
			}
		}

		return super.use(state, level, pos, player, handIn, hitResult);
	}

	@Nullable
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SafeBlockEntity(pos, state);
	}

	public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
		return this.defaultBlockState().setValue(FACING, placeContext.getHorizontalDirection().getOpposite());
	}

	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	protected void createBlockStateDefinition(StateDefinition.Builder stateBuilder) {
		stateBuilder.add(FACING, OPEN);
	}
}
