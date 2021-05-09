package jamthree.engine.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import jamthree.engine.component.*
import jamthree.screen.Controller
import jamthree.screen.SecondScreen
import ktx.ashley.allOf
import ktx.ashley.get

class ProjectileSystem(
    val controller: Controller,
    private val projectileRight: TextureRegion,
    private val projectileLeft: TextureRegion,
    private val projectileTwoRight: TextureRegion,
    private val projectileTwoLeft: TextureRegion,
)
    : IteratingSystem(allOf(TransformComponent::class, GraphicComponent::class, ProjectileComponent::class).get()), EntityListener {

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)

    }



    override fun entityAdded(entity: Entity){
        val gfx = entity[GraphicComponent.mapper]
        require(gfx!=null) {"No transform component "}
        val transform = entity[TransformComponent.mapper]
        require(transform!=null) {"No transform component "}
        val projectile = entity[ProjectileComponent.mapper]
        require(projectile!=null) {"No transform component "}



        projectile.parentEntity[TransformComponent.mapper]?.let { parentTransform ->
            transform.pos.set(
                parentTransform.pos.x + projectile.offset.x,
                parentTransform.pos.y + projectile.offset.y,
                transform.pos.z
            )
        }

        projectile.parentEntity[DirectionComponent.mapper]?.let { parentDirection ->

            //  If J is pressed you shoot HEAD type magic
            if(controller.isAttackOnePressed) {
                val region = when (parentDirection.currentDirection) {
                    Direction.LEFT -> projectileLeft
                    Direction.RIGHT -> projectileRight
                    else -> projectileRight
                }
                if (region == projectileLeft) projectile.velocity = projectile.velocity * -1
                gfx.setSpriteRegion(region)
            }

            // Otherwise shoot body type magic (MEANING IF K IS PRESSED, SEE RENDERER IN FIRST SCREEN)
            else {
                val region = when (parentDirection.currentDirection) {
                    Direction.LEFT -> projectileTwoLeft
                    Direction.RIGHT -> projectileTwoRight
                    else -> projectileTwoRight
                }
                if (region == projectileTwoLeft) projectile.velocity = projectile.velocity * -1
                gfx.setSpriteRegion(region)
            }
        }
    }

    override fun entityRemoved(entity: Entity) = Unit

    var acc = 0.0f
    override fun processEntity(entity: Entity, deltaTime: Float) {
        acc+=deltaTime
        val gfx = entity[GraphicComponent.mapper]
        require(gfx!=null) {"No transform component "}
        val transform = entity[TransformComponent.mapper]
        require(transform!=null) {"No transform component "}
        val projectile = entity[ProjectileComponent.mapper]
        require(projectile!=null) {"No transform component "}


        if(projectile.lastPos == transform.pos){
           // engine.removeEntity(entity)
        }
        transform.pos.x = MathUtils.clamp(transform.pos.x + projectile.velocity*deltaTime, -10f, 50f - transform.size.x)

        if(acc > 2.0f) {
            projectile.lastPos = transform.pos
            acc = 0.0f
        }
    }


}