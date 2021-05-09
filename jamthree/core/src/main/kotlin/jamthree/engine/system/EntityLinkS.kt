package jamthree.engine.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureRegion
import jamthree.engine.component.*
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.get
import javax.xml.crypto.dsig.Transform

class EntityLinkSystem(
    private val rightReg: TextureRegion,
    private val leftReg: TextureRegion
) : EntityListener, IteratingSystem(allOf(EntityLinkComponent::class, TransformComponent::class, GraphicComponent::class).get()){
    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity) = Unit

    override fun entityRemoved(otherEntity: Entity) {
        entities.forEach {entity ->
            entity[EntityLinkComponent.mapper]?.let { link ->
                if(link.parentEntity == otherEntity){
                    //entity.addComponent<RemoveComponent>(engine)
                }
            }
        }
    }


    override fun processEntity(entity: Entity, deltaTime: Float) {
        val gfx = entity[GraphicComponent.mapper]
        require(gfx!=null) {"No transform component "}
        val transform = entity[TransformComponent.mapper]
        require(transform!=null) {"No direction component"}
        val link = entity[EntityLinkComponent.mapper]
        require(link!=null) {"No direction component"}

        // Update position
        link.parentEntity[TransformComponent.mapper]?.let { linkTransform ->
            transform.pos.set(
                linkTransform.pos.x + link.offset.x,
                linkTransform.pos.y + link.offset.y,
                    linkTransform.pos.z
            )

        }
        // Update alpha
        link.parentEntity[GraphicComponent.mapper]?.let{ linkGfx ->
            gfx.sprite.setAlpha(linkGfx.sprite.color.a)

        }

        // Update direction
        link.parentEntity[DirectionComponent.mapper]?.let { linkDirection ->
            val region = when(linkDirection.currentDirection){
                Direction.LEFT -> leftReg
                Direction.RIGHT -> rightReg
                else -> leftReg
            }
            gfx.setSpriteRegion(region)
        }


    }
}
