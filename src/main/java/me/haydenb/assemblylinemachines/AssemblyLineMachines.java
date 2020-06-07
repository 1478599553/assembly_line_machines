package me.haydenb.assemblylinemachines;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.haydenb.assemblylinemachines.registry.ConfigHandler.ConfigHolder;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(AssemblyLineMachines.MODID)
public final class AssemblyLineMachines{
	public static final String MODID = "assemblylinemachines";
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	public AssemblyLineMachines() {
		
		ModLoadingContext mlc = ModLoadingContext.get();
		mlc.registerConfig(ModConfig.Type.COMMON, ConfigHolder.COMMON_SPEC);
	}
}