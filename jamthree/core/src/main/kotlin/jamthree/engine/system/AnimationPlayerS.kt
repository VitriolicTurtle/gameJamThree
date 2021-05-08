package jamthree.engine.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureRegion
import jamthree.engine.component.*
import ktx.ashley.allOf
import ktx.ashley.get

class AnimationPlayerSystem(
    private val defaultReg: TextureRegion,
    private val leftReg: TextureRegion,
    private val rightReg: TextureRegion,) : IteratingSystem(allOf(PlayerComponent::class, DirectionComponent::class, GraphicComponent::class).get()),
    EntityListener {
    private var previousDirection = Direction.DEFAULT

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity){
        entity[GraphicComponent.mapper]?.setSpriteRegion(defaultReg)
    }

    override fun entityRemoved(entity: Entity) = Unit

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val dir = entity[DirectionComponent.mapper]
        require(dir!=null) {"No direction component"}
        val gfx = entity[GraphicComponent.mapper]
        require(gfx!=null) {"No transform component "}

        if(dir.currentDirection == previousDirection && gfx.sprite.texture!=null){
            // No change
            return
        }
        previousDirection = dir.currentDirection
        val region = when(dir.currentDirection){
            Direction.LEFT -> leftReg
            Direction.RIGHT -> rightReg
            else -> defaultReg
        }
        gfx.setSpriteRegion(region)
    }
}