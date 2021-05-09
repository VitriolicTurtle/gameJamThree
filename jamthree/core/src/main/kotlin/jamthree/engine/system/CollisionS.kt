package jamthree.engine.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import jamthree.engine.component.*
import jamthree.engine.component.CollisionComponent.Companion.mapper
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.debug

class CollisionSystem() : IteratingSystem(allOf(CollisionComponent::class, TransformComponent::class).get()) {

    private val pEntities by lazy { engine.getEntitiesFor(allOf(PlayerComponent::class).get()) }
    private val projEntities by lazy { engine.getEntitiesFor(allOf(ProjectileComponent::class).get()) }
    private var pHitbox = Rectangle()           // Player
    private var collisionHitbox = Rectangle()   // Entity player collides with
    private var projectileHitbox = Rectangle()   // Entity player collides with

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val tComponent = entity[TransformComponent.mapper]
        require(tComponent != null) {}
        val cComponent = entity[CollisionComponent.mapper]
        require(cComponent != null) {}
        collisionHitbox.set(tComponent.pos.x, tComponent.pos.y, tComponent.size.x, tComponent.size.y)

        // FOR EACH PLAYER
        pEntities.forEach { p ->
            val pComponent = p[PlayerComponent.mapper]
            require(pComponent != null) {}
            p[TransformComponent.mapper]?.let { ptComponent ->
                pHitbox.set(
                        ptComponent.pos.x,
                        ptComponent.pos.y,
                        ptComponent.size.x * 1.2f,
                        ptComponent.size.y * 1.2f
                )
                if (pHitbox.overlaps(collisionHitbox)) {
                    if(cComponent.isWall){
                        if (ptComponent.pos.x < collisionHitbox.x) ptComponent.pos.x = ptComponent.pos.x - 0.069f
                        if (ptComponent.pos.x > collisionHitbox.x) ptComponent.pos.x = ptComponent.pos.x + 0.069f
                        if (ptComponent.pos.y < collisionHitbox.y) ptComponent.pos.y = ptComponent.pos.y - 0.069f
                        if (ptComponent.pos.y > collisionHitbox.y) ptComponent.pos.y = ptComponent.pos.y + 0.069f
                    } else if(cComponent.isEnemy){
                        pComponent.mana += 0.007f
                        engine.removeEntity(entity)
                    }
                }
            }
        }

        //  FOR EACH PROJECTILE
        projEntities.forEach { proj ->
            val projComponent = proj[ProjectileComponent.mapper]
            require(projComponent != null) {}
            proj[TransformComponent.mapper]?.let { ptComponent ->
                projectileHitbox.set(
                        ptComponent.pos.x,
                        ptComponent.pos.y,
                        ptComponent.size.x * 0.4f,
                        ptComponent.size.y * 0.4f
                )
                if (projectileHitbox.overlaps(collisionHitbox)) {
                    engine.removeEntity(proj)
                    if(cComponent.isEnemy) engine.removeEntity(entity)
                }
            }
        }
    }
}