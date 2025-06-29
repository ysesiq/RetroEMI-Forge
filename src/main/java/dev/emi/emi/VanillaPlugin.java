package dev.emi.emi;

import com.google.common.collect.Sets;
import dev.emi.emi.api.EmiInitRegistry;
import dev.emi.emi.api.widget.GeneratedSlotWidget;
import dev.emi.emi.config.EffectLocation;
import dev.emi.emi.config.EmiConfig;
import dev.emi.emi.handler.CookingRecipeHandler;
import dev.emi.emi.handler.CraftingRecipeHandler;
import dev.emi.emi.handler.InventoryRecipeHandler;
import dev.emi.emi.platform.EmiAgnos;
import dev.emi.emi.recipe.*;
import dev.emi.emi.recipe.special.*;
import dev.emi.emi.registry.EmiStackList;
import dev.emi.emi.registry.EmiTags;
import dev.emi.emi.runtime.EmiDrawContext;
import dev.emi.emi.runtime.EmiReloadLog;
import dev.emi.emi.screen.Bounds;
import dev.emi.emi.stack.serializer.ItemEmiStackSerializer;
import dev.emi.emi.stack.serializer.TagEmiIngredientSerializer;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.*;
import net.minecraft.util.SyntheticIdentifier;
import net.xylose.emi.inject_interface.EMIGuiContainerCreative;
import net.xylose.emi.inject_interface.EMIShapelessRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import com.rewindmc.retroemi.PredicateAsSet;
import com.rewindmc.retroemi.RetroEMI;
import net.minecraft.tag.TagKey;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static dev.emi.emi.api.recipe.VanillaEmiRecipeCategories.*;

@EmiEntrypoint
public class VanillaPlugin implements EmiPlugin {
	public static EmiRecipeCategory TAG =
			new EmiRecipeCategory(new ResourceLocation("emi", "tag"), EmiStack.of(Item.getItemFromBlock(Blocks.iron_ore)), simplifiedRenderer(240, 208),
					EmiRecipeSorting.none());

	public static EmiRecipeCategory INGREDIENT =
			new EmiRecipeCategory(new ResourceLocation("emi", "ingredient"), EmiStack.of(Items.compass), simplifiedRenderer(240, 208));
	public static EmiRecipeCategory RESOLUTION =
			new EmiRecipeCategory(new ResourceLocation("emi", "resolution"), EmiStack.of(Items.compass), simplifiedRenderer(240, 208));

	static {
		CRAFTING = new EmiRecipeCategory(new ResourceLocation("minecraft", "crafting"), EmiStack.of(Blocks.crafting_table), simplifiedRenderer(240, 240),
				EmiRecipeSorting.compareOutputThenInput());
		SMELTING = new EmiRecipeCategory(new ResourceLocation("minecraft", "smelting"), EmiStack.of(Blocks.furnace), simplifiedRenderer(224, 240),
				EmiRecipeSorting.compareOutputThenInput());
		ANVIL_REPAIRING = new EmiRecipeCategory(new ResourceLocation("emi", "anvil_repairing"), EmiStack.of(Blocks.anvil), simplifiedRenderer(240, 224),
				EmiRecipeSorting.none());
		BREWING = new EmiRecipeCategory(new ResourceLocation("minecraft", "brewing"), EmiStack.of(Items.brewing_stand), simplifiedRenderer(224, 224),
				EmiRecipeSorting.none());
		WORLD_INTERACTION = new EmiRecipeCategory(new ResourceLocation("emi", "world_interaction"), EmiStack.of(Item.getItemFromBlock(Blocks.grass)),
				simplifiedRenderer(208, 224), EmiRecipeSorting.none());
		EmiRenderable flame = (matrices, x, y, delta) -> {
			EmiTexture.FULL_FLAME.render(matrices, x + 1, y + 1, delta);
		};
		FUEL = new EmiRecipeCategory(new ResourceLocation("emi", "fuel"), flame, flame, EmiRecipeSorting.compareInputThenOutput());
		INFO = new EmiRecipeCategory(new ResourceLocation("emi", "info"), EmiStack.of(Items.written_book), simplifiedRenderer(208, 224), EmiRecipeSorting.none());
	}

	@Override
	public void initialize(EmiInitRegistry registry) {
		registry.addIngredientSerializer(ItemEmiStack.class, new ItemEmiStackSerializer());
//		registry.addIngredientSerializer(FluidEmiStack.class, new FluidEmiStackSerializer());
		registry.addIngredientSerializer(TagEmiIngredient.class, new TagEmiIngredientSerializer());
	}

	@Override
	public void register(EmiRegistry registry) {
		registry.addIngredientSerializer(ItemEmiStack.class, new ItemEmiStackSerializer());
		registry.addIngredientSerializer(TagEmiIngredient.class, new TagEmiIngredientSerializer());
		registry.addCategory(CRAFTING);
		registry.addCategory(SMELTING);
		registry.addCategory(ANVIL_REPAIRING);
		registry.addCategory(BREWING);
		registry.addCategory(WORLD_INTERACTION);
		registry.addCategory(FUEL);
		registry.addCategory(INFO);
		registry.addCategory(TAG);
		registry.addCategory(INGREDIENT);
		registry.addCategory(RESOLUTION);

        registry.addWorkstation(CRAFTING, EmiStack.of(new ItemStack(Blocks.crafting_table, 1, 0)));
        registry.addWorkstation(ANVIL_REPAIRING, EmiStack.of(Blocks.anvil));
		registry.addWorkstation(SMELTING, EmiStack.of(Blocks.furnace));
		registry.addWorkstation(BREWING, EmiStack.of(Items.brewing_stand));
		registry.addWorkstation(WORLD_INTERACTION, EmiStack.of(Blocks.grass));

		registry.addRecipeHandler(ContainerPlayer.class, new InventoryRecipeHandler());
		registry.addRecipeHandler(ContainerWorkbench.class, new CraftingRecipeHandler());
		registry.addRecipeHandler(ContainerFurnace.class, new CookingRecipeHandler<>(SMELTING));

		registry.addExclusionArea(GuiContainerCreative.class, (screen, consumer) -> {
			int left = ((EMIGuiContainerCreative) screen).getGuiLeft();
			int top = ((EMIGuiContainerCreative) screen).getGuiTop();
			int width = ((EMIGuiContainerCreative) screen).getxSize();
			int bottom = top + ((EMIGuiContainerCreative) screen).getySize();
			consumer.accept(new Bounds(left, top - 28, width, 28));
			consumer.accept(new Bounds(left, bottom, width, 28));
		});

		registry.addGenericExclusionArea((screen, consumer) -> {
			if (screen instanceof GuiInventory inv) {
				Minecraft client = Minecraft.getMinecraft();
				Collection collection = client.thePlayer.getActivePotionEffects();
				if (!collection.isEmpty()) {
					int k = 33;
					if (collection.size() > 5) {
						k = 132 / (collection.size() - 1);
					}
					int right = ((EMIGuiContainerCreative) inv).getGuiLeft() + ((EMIGuiContainerCreative) inv).getxSize() + 2;
					int rightWidth = inv.width - right;
					if (rightWidth >= 32) {
						int top = ((EMIGuiContainerCreative) inv).getGuiTop();
						int height = (collection.size() - 1) * k + 32;
						int left, width;
						if (EmiConfig.effectLocation == EffectLocation.TOP) {
							int size = collection.size();
							top = ((EMIGuiContainerCreative) inv).getGuiTop() - 34;
							int xOff = 34;
							if (size == 1) {
								xOff = 122;
							} else if (size > 5) {
								xOff = (((EMIGuiContainerCreative) inv).getxSize() - 32) / (size - 1);
							}
							width = Math.max(122, (size - 1) * xOff + 32);
							left = ((EMIGuiContainerCreative) inv).getGuiLeft() + (((EMIGuiContainerCreative) inv).getxSize() - width) / 2;
							height = 32;
						} else {
							left = switch (EmiConfig.effectLocation) {
								case LEFT_COMPRESSED -> ((EMIGuiContainerCreative) inv).getGuiLeft() - 2 - 32;
								case LEFT -> ((EMIGuiContainerCreative) inv).getGuiLeft() - 2 - 120;
								default -> right;
							};
							width = switch (EmiConfig.effectLocation) {
								case LEFT, RIGHT -> 120;
								case LEFT_COMPRESSED, RIGHT_COMPRESSED -> 32;
								default -> 32;
							};
						}
						consumer.accept(new Bounds(left, top, width, height));
					}
				}
			}
		});

		Comparison potionComparison = Comparison.of((a, b) -> RetroEMI.getEffects(a).equals(RetroEMI.getEffects(b)));

		registry.setDefaultComparison(Items.potionitem, potionComparison);
		registry.setDefaultComparison(Items.enchanted_book, Comparison.compareNbt());
		var prev = EmiStack.of(Items.enchanted_book);
		for (var ench : Enchantment.enchantmentsList) {
			if (ench == null) continue;
			var book = new ItemStack(Items.enchanted_book);
			EnchantmentHelper.setEnchantments(com.rewindmc.retroemi.shim.java.Map.of(ench.effectId, ench.getMaxLevel()), book);
			registry.addEmiStackAfter(prev = EmiStack.of(book), prev);
		}

		PredicateAsSet<Item> hiddenItems = i -> {
			for (var inv : EmiStackList.invalidators) {
				if (inv.test(EmiStack.of(i))) {
					return true;
				}
			}
			return false;
		};

		for (Item i : EmiRepairItemRecipe.TOOLS) {
			if (!hiddenItems.contains(i)) {
				addRecipeSafe(registry, () -> new EmiRepairItemRecipe(i, synthetic("crafting/repairing", EmiUtil.subId(i))));
			}
		}

		for (IRecipe recipe : (List<IRecipe>) registry.getRecipeManager().getRecipeList()) {
			if (recipe instanceof RecipesMapExtending map) {
				EmiStack paper = EmiStack.of(Items.paper);
				addRecipeSafe(registry, () -> new EmiCraftingRecipe(com.rewindmc.retroemi.shim.java.List.of(
						paper, paper, paper, paper,
						EmiStack.of(Items.map),
						paper, paper, paper, paper
				),
						EmiStack.of(Items.map),
						new ResourceLocation("minecraft", "map_extending"), false, null), recipe);
			} else if (recipe instanceof ShapedRecipes shaped) {
				addRecipeSafe(registry, () -> new EmiShapedRecipe(shaped), recipe);
			} else if (recipe instanceof ShapelessRecipes shapeless) {
				addRecipeSafe(registry, () -> new EmiShapelessRecipe((EMIShapelessRecipes) shapeless, shapeless), recipe);
			} else if (recipe instanceof RecipesArmorDyes dye) {
				for (Item i : EmiArmorDyeRecipe.DYEABLE_ITEMS) {
					if (!hiddenItems.contains(i)) {
						addRecipeSafe(registry, () -> new EmiArmorDyeRecipe(i, synthetic("crafting/dying", EmiUtil.subId(i))), recipe);
					}
				}
			} else if (recipe instanceof RecipeFireworks fwork) {
				// All firework recipes are one recipe in 1.7
				addRecipeSafe(registry, () -> new EmiFireworkStarRecipe(new ResourceLocation("minecraft", "firework_star")), recipe);
				addRecipeSafe(registry, () -> new EmiFireworkStarFadeRecipe(new ResourceLocation("minecraft", "firework_star_fade")), recipe);
				addRecipeSafe(registry, () -> new EmiFireworkRocketRecipe(new ResourceLocation("minecraft", "firework_rocket")), recipe);
			} else if (recipe instanceof RecipesMapCloning map) {
				addRecipeSafe(registry, () -> new EmiMapCloningRecipe(new ResourceLocation("minecraft", "map_cloning")), recipe);
			} else {
				// No way to introspect arbitrary recipes in 1.7. :(
			}
		}

        for (var recipe : ((Map<ItemStack, ItemStack>)FurnaceRecipes.smelting().getSmeltingList()).entrySet()) {
            ItemStack in = recipe.getKey();
            ItemStack out = recipe.getValue();
            String id = in + "." + out;
            float xp = FurnaceRecipes.smelting().func_151398_b(out);
            addRecipeSafe(registry, () -> new EmiCookingRecipe(new ResourceLocation("minecraft", "furnace/" + id), in, out, xp, SMELTING, 1, false));
        }

		for (Object obj : Item.itemRegistry) {
            Item i = (Item) obj;
			if (i == null) continue;
			if (hiddenItems.contains(i)) {
				continue;
			}
			if (i.isRepairable()) {
				if (i instanceof ItemArmor ai && ai.getArmorMaterial() != null) {
					var material = ai.getArmorMaterial().func_151685_b();
					addRecipeSafe(registry, () -> new EmiAnvilRecipe(EmiStack.of(i), EmiStack.of(material),
							new ResourceLocation("minecraft", "anvil/armor/" + SyntheticIdentifier.describe(i) + "/" + SyntheticIdentifier.describe(material))));
				} else if (i instanceof ItemTool ti && ti.func_150913_i() != null) {
					var material = ti.func_150913_i().getRepairItemStack();
					addRecipeSafe(registry, () -> new EmiAnvilRecipe(EmiStack.of(i), EmiStack.of(material),
							new ResourceLocation("minecraft", "anvil/tool/" + SyntheticIdentifier.describe(i) + "/" + SyntheticIdentifier.describe(material))));
				}
			}
			if (i.isDamageable()) {
				addRecipeSafe(registry, () -> new EmiAnvilRepairToolRecipe(i, new ResourceLocation("minecraft", "anvil/repair/tool/" + SyntheticIdentifier.describe(i))));
            }
			var is = new ItemStack(i);
			if (is.isItemEnchantable() && !(is.getItem() instanceof ItemBook) && !(is.getItem() instanceof ItemCarrotOnAStick)) {
				for (Enchantment e : EmiAnvilEnchantRecipe.ENCHANTMENTS) {
					if (e.canApply(is)) {
						int max = e.getMaxLevel();
						int min = EnchantmentHelper.getEnchantmentLevel(e.effectId, is);
						while (min <= max) {
							int finalMin = min;
							if (max == min)
								addRecipeSafe(registry, () -> new EmiAnvilEnchantRecipe(i, e, finalMin,
										new ResourceLocation("minecraft", "anvil/enchant/" + SyntheticIdentifier.describe(i) + "/" + e.effectId + "/" + SyntheticIdentifier.describe(finalMin))));
							min++;
						}
					}
				}
			}
		}

		EmiAgnos.addBrewingRecipes(registry);

		for (Object obj : Item.itemRegistry) {
            Item item = (Item) obj;
			if (item instanceof ItemHoe itemHoe)
				addRecipeSafe(registry, () -> basicWorld(EmiStack.of(Blocks.dirt), EmiStack.of(itemHoe), EmiStack.of(Blocks.farmland), new ResourceLocation("minecraft", item + "/tilling")));
        }

		for (Item item : EmiArmorDyeRecipe.DYEABLE_ITEMS) {
			if (!hiddenItems.contains(item)) {
				continue;
			}
			EmiStack cauldron = EmiStack.of(Items.cauldron);
			EmiStack waterThird = EmiStack.of(Blocks.water
//					, FluidUnit.BOTTLE
			);
			int uniq = EmiUtil.RANDOM.nextInt();
			addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
					.id(synthetic("world/cauldron_washing", EmiUtil.subId(item)))
					.leftInput(EmiStack.EMPTY, s -> new GeneratedSlotWidget(r -> {
						ItemStack stack = new ItemStack(item);
						if (stack.hasTagCompound() && stack.getTagCompound().hasKey("display")) {
							stack.getTagCompound().getCompoundTag("display").removeTag("Color");
						}
						return EmiStack.of(stack);
					}, uniq, s.getBounds().x(), s.getBounds().y()))
					.rightInput(cauldron, true)
					.rightInput(waterThird, false)
					.output(EmiStack.of(item))
					.supportsRecipeTree(false)
					.build());
		}

		EmiStack water = EmiStack.of(Blocks.water
//				, FluidUnit.BUCKET
		);
		EmiStack waterBottle = EmiStack.of(Blocks.water
//				, FluidUnit.BOTTLE
		);
		EmiStack lava = EmiStack.of(Blocks.lava
//				, FluidUnit.BUCKET
		);
		EmiStack waterCatalyst = water.copy().setRemainder(water);
		EmiStack lavaCatalyst = lava.copy().setRemainder(lava);

		addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
				.id(synthetic("world/fluid_spring", "minecraft/water"))
				.leftInput(waterCatalyst)
				.rightInput(waterCatalyst, false)
				.output(EmiStack.of(Blocks.water
//						, FluidUnit.BUCKET
				))
				.build());
		addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
				.id(synthetic("world/fluid_interaction", "minecraft/cobblestone"))
				.leftInput(waterCatalyst)
				.rightInput(lavaCatalyst, false)
				.output(EmiStack.of(Blocks.cobblestone))
				.build());
		addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
				.id(synthetic("world/fluid_interaction", "minecraft/stone"))
				.leftInput(waterCatalyst)
				.rightInput(lavaCatalyst, false)
				.output(EmiStack.of(Blocks.stone))
				.build());
		addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
				.id(synthetic("world/fluid_interaction", "minecraft/obsidian"))
				.leftInput(lava)
				.rightInput(waterCatalyst, false)
				.output(EmiStack.of(Blocks.obsidian))
				.build());

		//TODO
//		for (var entry : LiquidContainerRegistry.getRegisteredLiquidContainerData()) {
//			Fluid fluid = Fluid.of(entry.stillLiquid);
//			if (entry.container.itemID == Items.BUCKET.itemID) {
//				ItemStack bucket = entry.filled;
//				addRecipeSafe(registry, () -> basicWorld(EmiStack.of(Items.BUCKET), EmiStack.of(fluid, FluidUnit.BUCKET), EmiStack.of(bucket),
//						synthetic("emi", "bucket_filling/" + EmiUtil.subId(fluid)), false));
//			}
//		}

		addRecipeSafe(registry, () -> basicWorld(EmiStack.of(Items.glass_bottle), waterBottle,
				EmiStack.of(new ItemStack(Items.potionitem)),
				synthetic("world/unique", "minecraft/water_bottle")));


		for (TagKey<?> key : EmiTags.TAGS) {
			if (new TagEmiIngredient(key, 1).getEmiStacks().size() > 1) {
				addRecipeSafe(registry, () -> new EmiTagRecipe(key));
			}
		}

		addFuel(registry, hiddenItems);
	}

	private static void addFuel(EmiRegistry registry, PredicateAsSet<Item> hiddenItems) {
		Map<Prototype, Integer> fuelMap = EmiAgnos.getFuelMap();
		compressRecipesToTags(fuelMap.keySet(), Comparator.comparingInt(fuelMap::get), tag -> {
			EmiIngredient stack = EmiIngredient.of(tag);
			Prototype item = Prototype.of(stack.getEmiStacks().get(0).getItemStack());
			int time = fuelMap.getOrDefault(item, 0);
			registry.addRecipe(new EmiFuelRecipe(stack, time, synthetic("fuel/tag", EmiUtil.subId(tag.id()))));
		}, item -> {
			if (!hiddenItems.contains(item.getItem())) {
				int time = fuelMap.get(item);
				registry.addRecipe(new EmiFuelRecipe(EmiStack.of(item), time,
						synthetic("fuel/item", EmiUtil.subId(item.getItem()) + "/" + item.toStack().getItemDamage())));
			}
		});
	}

	private static void compressRecipesToTags(Set<Prototype> stacks, Comparator<Prototype> comparator, Consumer<TagKey<Prototype>> tagConsumer,
			Consumer<Prototype> itemConsumer) {
		Set<Prototype> handled = Sets.newHashSet();
		outer:
		for (TagKey<Prototype> key : EmiTags.getTags(Prototype.class)) {
			List<Prototype> items = key.get();
			if (items.size() < 2) {
				continue;
			}
			Prototype base = items.get(0);
			if (!stacks.contains(base)) {
				continue;
			}
			for (int i = 1; i < items.size(); i++) {
				Prototype item = items.get(i);
				if (!stacks.contains(item) || comparator.compare(base, item) != 0) {
					continue outer;
				}
			}
			if (handled.containsAll(items)) {
				continue;
			}
			handled.addAll(items);
			tagConsumer.accept(key);
		}
		for (Prototype item : stacks) {
			if (handled.contains(item)) {
				continue;
			}
			itemConsumer.accept(item);
		}
	}

	private static ResourceLocation synthetic(String type, String name) {
		return new ResourceLocation("emi", "/" + type + "/" + name);
	}

	private static void addRecipeSafe(EmiRegistry registry, Supplier<EmiRecipe> supplier) {
		try {
			registry.addRecipe(supplier.get());
		}
		catch (Throwable e) {
			EmiReloadLog.warn("Exception when parsing EMI recipe (no ID available)");
			EmiReloadLog.error(e);
		}
	}

	private static void addRecipeSafe(EmiRegistry registry, Supplier<EmiRecipe> supplier, IRecipe recipe) {
		try {
			registry.addRecipe(supplier.get());
		}
		catch (Throwable e) {
			EmiReloadLog.warn("Exception when parsing vanilla recipe " + recipe);
			EmiReloadLog.error(e);
		}
	}

	private static EmiRenderable simplifiedRenderer(int u, int v) {
		return (raw, x, y, delta) -> {
			EmiDrawContext context = EmiDrawContext.wrap(raw);
			context.drawTexture(EmiRenderHelper.WIDGETS, x, y, u, v, 16, 16);
		};
	}

	private EmiRecipe basicWorld(EmiIngredient left, EmiIngredient right, EmiStack output, ResourceLocation id) {
		return basicWorld(left, right, output, id, true);
	}

	private EmiRecipe basicWorld(EmiIngredient left, EmiIngredient right, EmiStack output, ResourceLocation id, boolean catalyst) {
		return EmiWorldInteractionRecipe.builder()
				.id(id)
				.leftInput(left)
				.rightInput(right, catalyst)
				.output(output)
				.build();
	}
}
