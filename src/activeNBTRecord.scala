package com.cterm2.tetra

// Tetrahedron Framework: Parametric-type based Active Record for Minecraft NBT
object ActiveNBTRecord
{
	import net.minecraft.nbt._
	import net.minecraftforge.common.util.Constants

	implicit class Record(val tag: NBTTagCompound) extends AnyVal
	{
		// Setters
		def update[T](key: String, value: T)(implicit setterImpl: SetterImpl[T]) = setterImpl.set(tag, key, value)
		def update[T](key: String, value: Option[T])(implicit setterImpl: SetterImpl[T]) = value foreach { setterImpl.set(tag, key, _) }
		// Getters
		def apply[T](key: String)(implicit getterImpl: GetterImpl[T]) = getterImpl.get(tag, key)
	}

	// Type Classes //
	sealed trait SetterImpl[@specialized T]
	{
		def set(tag: NBTTagCompound, key: String, value: T): Unit
	}
	implicit object ByteSetter extends SetterImpl[Byte]
	{
		def set(tag: NBTTagCompound, key: String, value: Byte) = tag.setByte(key, value)
	}
	implicit object ShortSetter extends SetterImpl[Short]
	{
		def set(tag: NBTTagCompound, key: String, value: Short) = tag.setShort(key, value)
	}
	implicit object IntSetter extends SetterImpl[Int]
	{
		def set(tag: NBTTagCompound, key: String, value: Int) = tag.setInteger(key, value)
	}
	implicit object StringSetter extends SetterImpl[String]
	{
		def set(tag: NBTTagCompound, key: String, value: String) = tag.setString(key, value)
	}
	implicit object TagSetter extends SetterImpl[NBTTagCompound]
	{
		def set(tag: NBTTagCompound, key: String, value: NBTTagCompound) = tag.setTag(key, value)
	}
	implicit object ListSetter extends SetterImpl[NBTTagList]
	{
		def set(tag: NBTTagCompound, key: String, value: NBTTagList) = tag.setTag(key, value)
	}

	sealed trait GetterImpl[@specialized T]
	{
		def get(tag: NBTTagCompound, key: String): Option[T]
	}
	implicit object ByteGetter extends GetterImpl[Byte]
	{
		override def get(tag: NBTTagCompound, key: String) = if(tag.hasKey(key)) Some(tag.getByte(key)) else None
	}
	implicit object ShortGetter extends GetterImpl[Short]
	{
		override def get(tag: NBTTagCompound, key: String) = if(tag.hasKey(key)) Some(tag.getShort(key)) else None
	}
	implicit object IntGetter extends GetterImpl[Int]
	{
		override def get(tag: NBTTagCompound, key: String) = if(tag.hasKey(key)) Some(tag.getInteger(key)) else None
	}
	implicit object StringGetter extends GetterImpl[String]
	{
		override def get(tag: NBTTagCompound, key: String) = if(tag.hasKey(key)) Some(tag.getString(key)) else None
	}
	implicit object TagGetter extends GetterImpl[NBTTagCompound]
	{
		override def get(tag: NBTTagCompound, key: String) = Option(tag.getCompoundTag(key))
	}
	implicit object ListGetter extends GetterImpl[NBTTagList]
	{
		override def get(tag: NBTTagCompound, key: String) = Option(tag.getTagList(key, Constants.NBT.TAG_COMPOUND))
	}
}
