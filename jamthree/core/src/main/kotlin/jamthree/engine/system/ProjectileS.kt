package jamthree.engine.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import jamthree.engine.component.*
import jamthree.screen.Controller
import ktx.ashley.allOf
import ktx.ashley.get
import org.omg.CORBA.TRANSACTION_MODE

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

    var sinOffset = 0f
    var sinTimer = 0f
    override fun processEntity(entity: Entity, deltaTime: Float) {
        sinTimer+=deltaTime
        val gfx = entity[GraphicComponent.mapper]
        require(gfx!=null) {"No transform component "}
        val transform = entity[TransformComponent.mapper]
        require(transform!=null) {"No transform component "}
        val projectile = entity[ProjectileComponent.mapper]
        require(projectile!=null) {"No transform component "}


        if(projectile.lastPos == transform.pos){
           // engine.removeEntity(entity)
        }

        if(projectile.type == ProjectileType.WATER){  // WATER MOVES LIKE A WAVE
            sinOffset = Math.sin(sinTimer.toDouble()*20).toFloat()/5
            transform.pos.x = MathUtils.clamp(transform.pos.x + projectile.velocity*deltaTime, -10f, 150f - transform.size.x)
            transform.pos.y = MathUtils.clamp((transform.pos.y + sinOffset), -10f, 150f - transform.size.y)
        }else if(projectile.type == ProjectileType.EARTH){  // EARTH CREATED A PROTECTIVE WALL
            transform.size.y = 3.0f
        }else if(projectile.type == ProjectileType.FIRE){   // FIRE SCORCHES THE AREA
            transform.size.x=3f
            transform.size.y=3f
            transform.pos.x = MathUtils.clamp(transform.pos.x + projectile.velocity*deltaTime, -10f, 150f - transform.size.x)
        }else if(projectile.type == ProjectileType.AIR){  // AIR IS SMALL BUT FAST
            transform.size.x=0.7f
            transform.size.y=0.7f
            var velocity = 8
            if(projectile.velocity<0) velocity = -8
            transform.pos.x = MathUtils.clamp(transform.pos.x + velocity*deltaTime, -10f, 150f - transform.size.x)
        }




    }


}