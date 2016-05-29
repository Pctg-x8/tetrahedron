package com.cterm2.tetra

object LocalTranslationUtils
{
	// "t" interpolator
	implicit class TranslatedStringHelper(val context: StringContext) extends AnyVal
	{
		import net.minecraft.util.StatCollector

		def buildActualString(args: Any*) =
		{
			val strings = context.parts.iterator
			val values = args.iterator
			val buf = new StringBuffer(strings.next)
			while(strings.hasNext)
			{
				buf append values.next
				buf append strings.next
			}
			buf.toString
		}
		def t(args: Any*) = buildActualString _ andThen StatCollector.translateToLocal apply args
	}
}
