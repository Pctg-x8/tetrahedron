package com.cterm2.tetra

// Tetrahedron Framework: Lazy-executed commands for RenderBlocks
package StaticMeshData
{
	sealed abstract class RenderCommands
	case class SetRenderBounds(minX: Float, minY: Float, minZ: Float, maxX: Float, maxY: Float, maxZ: Float) extends RenderCommands
	case class RenderFaceX(neg: Boolean) extends RenderCommands
	case class RenderFaceY(neg: Boolean) extends RenderCommands
	case class RenderFaceZ(neg: Boolean) extends RenderCommands
}
package object StaticMeshData
{
	type StaticMesh = Seq[RenderCommands]
	lazy val EmptyStaticMesh = Seq[RenderCommands]()
	implicit class StaticMeshConstructor(val mesh: StaticMesh) extends AnyVal
	{
		def setRenderBounds(minX: Float, minY: Float, minZ: Float, maxX: Float, maxY: Float, maxZ: Float) =
			mesh :+ SetRenderBounds(minX, minY, minZ, maxX, maxY, maxZ)
		def renderFaceXPos() = mesh :+ RenderFaceX(false)
		def renderFaceXNeg() = mesh :+ RenderFaceX(true)
		def renderFaceYPos() = mesh :+ RenderFaceY(false)
		def renderFaceYNeg() = mesh :+ RenderFaceY(true)
		def renderFaceZPos() = mesh :+ RenderFaceZ(false)
		def renderFaceZNeg() = mesh :+ RenderFaceZ(true)
		def renderAllFaces() = mesh.renderFaceYPos.renderFaceYNeg.renderFaceXZ
		def renderFaceXZ() = mesh.renderFaceXPos.renderFaceXNeg.renderFaceZPos.renderFaceZNeg
	}

	implicit class RenderableObjectMethods(val mesh: StaticMesh) extends AnyVal
	{
		import net.minecraft.block.Block, net.minecraft.client.renderer.RenderBlocks
		import net.minecraft.world.IBlockAccess

		def render(world: IBlockAccess, block: Block, meta: Int, x: Int, y: Int, z: Int, renderer: RenderBlocks, yLit: Float, xLit: Float, zLit: Float, r: Float, g: Float, b: Float)
		{
			val tess = net.minecraft.client.renderer.Tessellator.instance
			val lit = block.getMixedBrightnessForBlock(world, x, y, z)

			mesh foreach
			{
				case SetRenderBounds(minX, minY, minZ, maxX, maxY, maxZ) => renderer.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ)
				case RenderFaceX(neg) if neg =>
					tess.setBrightness(if(renderer.renderMinX > 0.0d) lit else block.getMixedBrightnessForBlock(world, x - 1, y, z))
					tess.setColorOpaque_F(xLit * r, xLit * g, xLit * b)
					renderer.renderFaceXNeg(block, x, y, z, block.getIcon(4, meta))
				case RenderFaceX(neg) if !neg =>
					tess.setBrightness(if(renderer.renderMaxX < 1.0d) lit else block.getMixedBrightnessForBlock(world, x + 1, y, z))
					tess.setColorOpaque_F(xLit * r, xLit * g, xLit * b)
					renderer.renderFaceXPos(block, x, y, z, block.getIcon(5, meta))
				case RenderFaceY(neg) if neg =>
					tess.setBrightness(if(renderer.renderMinY > 0.0d) lit else block.getMixedBrightnessForBlock(world, x, y - 1, z))
					tess.setColorOpaque_F(yLit * r, yLit * g, yLit * b)
					renderer.renderFaceYNeg(block, x, y, z, block.getIcon(0, meta))
				case RenderFaceY(neg) if !neg =>
					tess.setBrightness(if(renderer.renderMaxY < 1.0d) lit else block.getMixedBrightnessForBlock(world, x, y + 1, z))
					tess.setColorOpaque_F(r, g, b)
					renderer.renderFaceYPos(block, x, y, z, block.getIcon(1, meta))
				case RenderFaceZ(neg) if neg =>
					tess.setBrightness(if(renderer.renderMinZ > 0.0d) lit else block.getMixedBrightnessForBlock(world, x, y, z - 1))
					tess.setColorOpaque_F(zLit * r, zLit * g, zLit * b)
					renderer.renderFaceZNeg(block, x, y, z, block.getIcon(2, meta))
				case RenderFaceZ(neg) if !neg =>
					tess.setBrightness(if(renderer.renderMaxZ < 1.0d) lit else block.getMixedBrightnessForBlock(world, x, y, z + 1))
					tess.setColorOpaque_F(zLit * r, zLit * g, zLit * b)
					renderer.renderFaceZPos(block, x, y, z, block.getIcon(3, meta))
			}
		}
		def renderWithNormals(block: Block, meta: Int, renderer: RenderBlocks)
		{
			val tess = net.minecraft.client.renderer.Tessellator.instance

			mesh foreach
			{
				case SetRenderBounds(minX, minY, minZ, maxX, maxY, maxZ) => renderer.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ)
				case RenderFaceX(neg) =>
					tess.startDrawingQuads()
					tess.setNormal(if(neg) -1.0f else 1.0f, 0.0f, 0.0f)
					if(neg) renderer.renderFaceXNeg(block, 0, 0, 0, block.getIcon(4, meta))
					else renderer.renderFaceXPos(block, 0, 0, 0, block.getIcon(5, meta))
					tess.draw()
				case RenderFaceY(neg) =>
					tess.startDrawingQuads()
					tess.setNormal(0.0f, if(neg) -1.0f else 1.0f, 0.0f)
					if(neg) renderer.renderFaceYNeg(block, 0, 0, 0, block.getIcon(0, meta))
					else renderer.renderFaceYPos(block, 0, 0, 0, block.getIcon(1, meta))
					tess.draw()
				case RenderFaceZ(neg) =>
					tess.startDrawingQuads()
					tess.setNormal(0.0f, 0.0f, if(neg) -1.0f else 1.0f)
					if(neg) renderer.renderFaceZNeg(block, 0, 0, 0, block.getIcon(2, meta))
					else renderer.renderFaceZPos(block, 0, 0, 0, block.getIcon(3, meta))
					tess.draw()
			}
		}
	}
}
