package me.haydenb.assemblylinemachines.block.machines.electric;

import java.util.HashMap;
import java.util.stream.Stream;

import me.haydenb.assemblylinemachines.util.General;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.ToolType;

public abstract class BlockQuarryAddon extends Block {

	
	
	private static final HashMap<Direction, BooleanProperty> QUARRY_ADDON_PROPERTIES = new HashMap<>();
	static {
		
		for(Direction d : Direction.values()) {
			QUARRY_ADDON_PROPERTIES.put(d, BooleanProperty.create(d.toString().toLowerCase()));
		}
		
	}
	
	private static final VoxelShape SHAPE_N = Stream.of(
			Block.makeCuboidShape(3, 10, 0, 6, 13, 2),
			Block.makeCuboidShape(10, 10, 0, 13, 13, 2),
			Block.makeCuboidShape(10, 3, 0, 13, 6, 2),
			Block.makeCuboidShape(3, 3, 0, 6, 6, 2),
			Block.makeCuboidShape(7, 7, 0, 9, 9, 2)
			).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
	
	
	private static final VoxelShape SHAPE_U = Stream.of(
			Block.makeCuboidShape(3, 14, 3, 6, 16, 6),
			Block.makeCuboidShape(10, 14, 3, 13, 16, 6),
			Block.makeCuboidShape(10, 14, 10, 13, 16, 13),
			Block.makeCuboidShape(3, 14, 10, 6, 16, 13),
			Block.makeCuboidShape(7, 14, 7, 9, 16, 9)
			).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
	
	private static final VoxelShape SHAPE_D = Stream.of(
			Block.makeCuboidShape(3, 0, 3, 6, 2, 6),
			Block.makeCuboidShape(10, 0, 3, 13, 2, 6),
			Block.makeCuboidShape(10, 0, 10, 13, 2, 13),
			Block.makeCuboidShape(3, 0, 10, 6, 2, 13),
			Block.makeCuboidShape(7, 0, 7, 9, 2, 9)
			).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
	
	private static final VoxelShape SHAPE_S = General.rotateShape(Direction.NORTH, Direction.SOUTH, SHAPE_N);
	private static final VoxelShape SHAPE_W = General.rotateShape(Direction.NORTH, Direction.WEST, SHAPE_N);
	private static final VoxelShape SHAPE_E = General.rotateShape(Direction.NORTH, Direction.EAST, SHAPE_N);
	
	public BlockQuarryAddon() {
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(4f, 15f).harvestLevel(0).harvestTool(ToolType.PICKAXE).sound(SoundType.METAL));

		BlockState bs = this.stateContainer.getBaseState().with(BlockStateProperties.FACING, Direction.DOWN);
		bs = addToBlockState(bs);
		this.setDefaultState(bs);
	}
	
	public static void addToBuilder(Builder<Block, BlockState> builder){
		
		for(Direction d : Direction.values()) {
			builder.add(QUARRY_ADDON_PROPERTIES.get(d));
		}
	}
	
	public static BlockState addToBlockState(BlockState bs) {
		for(Direction d : Direction.values()) {
			bs = bs.with(QUARRY_ADDON_PROPERTIES.get(d), false);
		}
		return bs;
	}
	
	public static BooleanProperty getAddonProperty(Direction d) {
		return QUARRY_ADDON_PROPERTIES.get(d);
	}
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {

		
		addToBuilder(builder);
		builder.add(BlockStateProperties.FACING);
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos) {
		if (!worldIn.isRemote()) {
			if (facing == stateIn.get(BlockStateProperties.FACING)) {
				if (worldIn.getBlockState(currentPos.offset(facing)).getBlock() == Blocks.AIR) {
					return Blocks.AIR.getDefaultState();
				}
			}
		}

		return stateIn;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		
		for(Direction d : Direction.values()) {
			
			if(state.get(QUARRY_ADDON_PROPERTIES.get(d))) {
				return VoxelShapes.combineAndSimplify(getConnectionShape(d), getAddonBaseShape(d), IBooleanFunction.OR);
				
			}
			
		}
		return VoxelShapes.empty();
	}
	
	public static VoxelShape getConnectionShape(Direction d) {
		if(d == Direction.UP) {
			return SHAPE_U;
		}else if(d == Direction.DOWN) {
			return SHAPE_D;
		}else if(d == Direction.NORTH) {
			return SHAPE_N;
		}else if(d == Direction.SOUTH) {
			return SHAPE_S;
		}else if(d == Direction.EAST) {
			return SHAPE_E;
		}else {
			return SHAPE_W;
		}
	}
	
	public abstract VoxelShape getAddonBaseShape(Direction d);
	
	public static class BlockSpeedQuarryAddon extends BlockQuarryAddon{
		
		private static final VoxelShape SHAPE_D = Stream.of(
				Block.makeCuboidShape(2, 2, 2, 14, 4, 14),
				Block.makeCuboidShape(2, 12, 2, 4, 14, 14),
				Block.makeCuboidShape(2, 4, 2, 4, 12, 4),
				Block.makeCuboidShape(2, 4, 12, 4, 12, 14),
				Block.makeCuboidShape(12, 4, 12, 14, 12, 14),
				Block.makeCuboidShape(12, 4, 2, 14, 12, 4),
				Block.makeCuboidShape(12, 12, 2, 14, 14, 14),
				Block.makeCuboidShape(4, 12, 12, 12, 14, 14),
				Block.makeCuboidShape(4, 12, 2, 12, 14, 4),
				Block.makeCuboidShape(5, 5, 5, 6, 6, 11),
				Block.makeCuboidShape(5, 10, 5, 6, 11, 11),
				Block.makeCuboidShape(10, 5, 5, 11, 6, 11),
				Block.makeCuboidShape(10, 10, 5, 11, 11, 11),
				Block.makeCuboidShape(6, 5, 10, 10, 6, 11),
				Block.makeCuboidShape(6, 10, 10, 10, 11, 11),
				Block.makeCuboidShape(6, 5, 5, 10, 6, 6),
				Block.makeCuboidShape(6, 10, 5, 10, 11, 6),
				Block.makeCuboidShape(10, 6, 5, 11, 10, 6),
				Block.makeCuboidShape(10, 6, 10, 11, 10, 11),
				Block.makeCuboidShape(5, 6, 10, 6, 10, 11),
				Block.makeCuboidShape(5, 6, 5, 6, 10, 6),
				Block.makeCuboidShape(7, 7, 7, 9, 9, 9)
				).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
		
		private static final VoxelShape SHAPE_U = Stream.of(
				Block.makeCuboidShape(2, 12, 2, 14, 14, 14),
				Block.makeCuboidShape(2, 2, 2, 4, 4, 14),
				Block.makeCuboidShape(2, 4, 12, 4, 12, 14),
				Block.makeCuboidShape(2, 4, 2, 4, 12, 4),
				Block.makeCuboidShape(12, 4, 2, 14, 12, 4),
				Block.makeCuboidShape(12, 4, 12, 14, 12, 14),
				Block.makeCuboidShape(12, 2, 2, 14, 4, 14),
				Block.makeCuboidShape(4, 2, 2, 12, 4, 4),
				Block.makeCuboidShape(4, 2, 12, 12, 4, 14),
				Block.makeCuboidShape(5, 10, 5, 6, 11, 11),
				Block.makeCuboidShape(5, 5, 5, 6, 6, 11),
				Block.makeCuboidShape(10, 10, 5, 11, 11, 11),
				Block.makeCuboidShape(10, 5, 5, 11, 6, 11),
				Block.makeCuboidShape(6, 10, 5, 10, 11, 6),
				Block.makeCuboidShape(6, 5, 5, 10, 6, 6),
				Block.makeCuboidShape(6, 10, 10, 10, 11, 11),
				Block.makeCuboidShape(6, 5, 10, 10, 6, 11),
				Block.makeCuboidShape(10, 6, 10, 11, 10, 11),
				Block.makeCuboidShape(10, 6, 5, 11, 10, 6),
				Block.makeCuboidShape(5, 6, 5, 6, 10, 6),
				Block.makeCuboidShape(5, 6, 10, 6, 10, 11),
				Block.makeCuboidShape(7, 7, 7, 9, 9, 9)
				).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
		
		private static final VoxelShape SHAPE_N = Stream.of(
				Block.makeCuboidShape(2, 2, 2, 14, 14, 4),
				Block.makeCuboidShape(2, 2, 12, 4, 14, 14),
				Block.makeCuboidShape(2, 12, 4, 4, 14, 12),
				Block.makeCuboidShape(2, 2, 4, 4, 4, 12),
				Block.makeCuboidShape(12, 2, 4, 14, 4, 12),
				Block.makeCuboidShape(12, 12, 4, 14, 14, 12),
				Block.makeCuboidShape(12, 2, 12, 14, 14, 14),
				Block.makeCuboidShape(4, 2, 12, 12, 4, 14),
				Block.makeCuboidShape(4, 12, 12, 12, 14, 14),
				Block.makeCuboidShape(5, 5, 5, 6, 11, 6),
				Block.makeCuboidShape(5, 5, 10, 6, 11, 11),
				Block.makeCuboidShape(10, 5, 5, 11, 11, 6),
				Block.makeCuboidShape(10, 5, 10, 11, 11, 11),
				Block.makeCuboidShape(6, 5, 5, 10, 6, 6),
				Block.makeCuboidShape(6, 5, 10, 10, 6, 11),
				Block.makeCuboidShape(6, 10, 5, 10, 11, 6),
				Block.makeCuboidShape(6, 10, 10, 10, 11, 11),
				Block.makeCuboidShape(10, 10, 6, 11, 11, 10),
				Block.makeCuboidShape(10, 5, 6, 11, 6, 10),
				Block.makeCuboidShape(5, 5, 6, 6, 6, 10),
				Block.makeCuboidShape(5, 10, 6, 6, 11, 10),
				Block.makeCuboidShape(7, 7, 7, 9, 9, 9)
				).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
		
		private static final VoxelShape SHAPE_S = General.rotateShape(Direction.NORTH, Direction.SOUTH, SHAPE_N);
		private static final VoxelShape SHAPE_W = General.rotateShape(Direction.NORTH, Direction.WEST, SHAPE_N);
		private static final VoxelShape SHAPE_E = General.rotateShape(Direction.NORTH, Direction.EAST, SHAPE_N);
		
		@Override
		public VoxelShape getAddonBaseShape(Direction d) {
			if(d == Direction.UP) {
				return SHAPE_U;
			}else if(d == Direction.DOWN) {
				return SHAPE_D;
			}else if(d == Direction.NORTH) {
				return SHAPE_N;
			}else if(d == Direction.SOUTH) {
				return SHAPE_S;
			}else if(d == Direction.EAST) {
				return SHAPE_E;
			}else {
				return SHAPE_W;
			}
		}
	}
	
	public static class BlockFortuneVoidQuarryAddon extends BlockQuarryAddon{
		
		private static final VoxelShape SHAPE_D = Stream.of(
				Block.makeCuboidShape(2, 2, 2, 14, 4, 14),
				Block.makeCuboidShape(2, 12, 2, 4, 14, 14),
				Block.makeCuboidShape(2, 4, 2, 4, 12, 4),
				Block.makeCuboidShape(2, 4, 12, 4, 12, 14),
				Block.makeCuboidShape(12, 4, 12, 14, 12, 14),
				Block.makeCuboidShape(12, 4, 2, 14, 12, 4),
				Block.makeCuboidShape(12, 12, 2, 14, 14, 14),
				Block.makeCuboidShape(4, 12, 12, 12, 14, 14),
				Block.makeCuboidShape(4, 12, 2, 12, 14, 4),
				Block.makeCuboidShape(5, 5, 5, 11, 11, 11)
				).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
		
		private static final VoxelShape SHAPE_N = Stream.of(
				Block.makeCuboidShape(2, 2, 2, 14, 14, 4),
				Block.makeCuboidShape(2, 2, 12, 4, 14, 14),
				Block.makeCuboidShape(2, 12, 4, 4, 14, 12),
				Block.makeCuboidShape(2, 2, 4, 4, 4, 12),
				Block.makeCuboidShape(12, 2, 4, 14, 4, 12),
				Block.makeCuboidShape(12, 12, 4, 14, 14, 12),
				Block.makeCuboidShape(12, 2, 12, 14, 14, 14),
				Block.makeCuboidShape(4, 2, 12, 12, 4, 14),
				Block.makeCuboidShape(4, 12, 12, 12, 14, 14),
				Block.makeCuboidShape(5, 5, 5, 11, 11, 11)
				).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
		
		private static final VoxelShape SHAPE_U = Stream.of(
				Block.makeCuboidShape(2, 12, 2, 14, 14, 14),
				Block.makeCuboidShape(2, 12, 2, 14, 14, 14),
				Block.makeCuboidShape(2, 2, 2, 4, 4, 14),
				Block.makeCuboidShape(2, 4, 2, 4, 12, 4),
				Block.makeCuboidShape(2, 4, 12, 4, 12, 14),
				Block.makeCuboidShape(12, 4, 12, 14, 12, 14),
				Block.makeCuboidShape(12, 4, 2, 14, 12, 4),
				Block.makeCuboidShape(12, 2, 2, 14, 4, 14),
				Block.makeCuboidShape(4, 2, 12, 12, 4, 14),
				Block.makeCuboidShape(4, 2, 2, 12, 4, 4),
				Block.makeCuboidShape(5, 5, 5, 11, 11, 11)
				).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
		
		private static final VoxelShape SHAPE_S = General.rotateShape(Direction.NORTH, Direction.SOUTH, SHAPE_N);
		private static final VoxelShape SHAPE_W = General.rotateShape(Direction.NORTH, Direction.WEST, SHAPE_N);
		private static final VoxelShape SHAPE_E = General.rotateShape(Direction.NORTH, Direction.EAST, SHAPE_N);
		
		@Override
		public VoxelShape getAddonBaseShape(Direction d) {
			if(d == Direction.UP) {
				return SHAPE_U;
			}else if(d == Direction.DOWN) {
				return SHAPE_D;
			}else if(d == Direction.NORTH) {
				return SHAPE_N;
			}else if(d == Direction.SOUTH) {
				return SHAPE_S;
			}else if(d == Direction.EAST) {
				return SHAPE_E;
			}else {
				return SHAPE_W;
			}
		}
	}
}
