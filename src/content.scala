package com.cterm2.tetra

// Tetrahedron Framework: Content Registration Helper
object ContentRegistry
{
	import net.minecraft.item.{Item, ItemBlock}, net.minecraft.block.Block, net.minecraft.tileentity.TileEntity
	import cpw.mods.fml.common.registry.GameRegistry

	sealed trait INameableRegister
	{
		def as(name: String): Unit
	}
	final class ItemRegister(val item: Item) extends INameableRegister
	{
		override def as(name: String) = GameRegistry.registerItem(item, name)
	}
	final class BlockRegister(val block: Block) extends INameableRegister
	{
		def in[T <: ItemBlock](ibclass: Class[T]) = new CustomContainmentBlockRegister(block, ibclass)
		override def as(name: String) = GameRegistry.registerBlock(block.setBlockName(name), name)
	}
	final class CustomContainmentBlockRegister(val block: Block, val ibclass: Class[_ <: ItemBlock]) extends INameableRegister
	{
		override def as(name: String) = GameRegistry.registerBlock(block.setBlockName(name), ibclass, name)
	}
	final class TileEntityRegister(val teClass: Class[_ <: TileEntity]) extends INameableRegister
	{
		override def as(name: String) = GameRegistry.registerTileEntity(teClass, name)
	}

	final def register(item: Item) = new ItemRegister(item)
	final def register(block: Block) = new BlockRegister(block)
	final def register[T <: TileEntity](teclass: Class[T]) = new TileEntityRegister(teclass)
}
