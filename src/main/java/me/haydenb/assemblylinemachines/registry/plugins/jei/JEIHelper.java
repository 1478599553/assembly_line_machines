package me.haydenb.assemblylinemachines.registry.plugins.jei;

import java.util.List;
import me.haydenb.assemblylinemachines.AssemblyLineMachines;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

public class JEIHelper {

	public static ResourceLocation getGUIPath(String name) {
		return new ResourceLocation(AssemblyLineMachines.MODID, "textures/gui/jei/" + name + ".png");
	}
	
	public static <C extends IInventory, T extends IRecipe<C>> List<T> getRecipes(IRecipeType<T> recipe){
		
		ClientWorld world = Minecraft.getInstance().world;
		RecipeManager rm = world.getRecipeManager();
		return rm.getRecipes(recipe, null, world);
	}
}