package jamthree.engine.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import jamthree.engine.component.*
import jamthree.engine.component.CollisionComponent.Companion.mapper
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.debug
import ktx.log.logger


private val LOG = logger<CollisionSystem>()

class CollisionSystem(val batch: Batch) : IteratingSystem(allOf(CollisionComponent::class, TransformComponent::class).get()) {

    private val pEntities by lazy { engine.getEntitiesFor(allOf(PlayerComponent::class).get()) }
    private val projEntities by lazy { engine.getEntitiesFor(allOf(ProjectileComponent::class).get()) }
    private val enemyEntities by lazy { engine.getEntitiesFor(allOf(EnemyMovementComponent::class).get()) }
    private val magic = Texture(Gdx.files.internal("graphics/Blast.png"))

    private var pHitbox = Rectangle()           // Player
    private var collisionHitbox = Rectangle()   // Entity player collides with
    private var projectileHitbox = Rectangle()   // Entity player collides with
    private var bombHitbox = Rectangle()   // Entity player collides with




    override fun processEntity(entity: Entity, deltaTime: Float) {
        val tComponent = entity[TransformComponent.mapper]
        require(tComponent != null) {}
        val cComponent = entity[CollisionComponent.mapper]
        require(cComponent != null) {}
        val gComponent = entity[GraphicComponent.mapper]
        require(gComponent != null) {}

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
                        pComponent.mana += 2f
                        engine.removeEntity(entity)
                    } else if(cComponent.isBomb) {

                        gComponent.sprite.run {
                            setRegion(magic)
                        }
                        //var tempPos = tComponent
                        tComponent.pos = Vector3(tComponent.pos.x - (pComponent.mana / 2), tComponent.pos.y - (pComponent.mana / 2), 2f)
                        tComponent.size = Vector2(pComponent.mana, pComponent.mana)


                        // Destroy all enemies that are hit by explosion
                        enemyEntities.forEach { e ->
                            e[TransformComponent.mapper]?.let { etComponent ->
                                if (etComponent.pos.x < tComponent.pos.x + pComponent.mana &&
                                        etComponent.pos.x > tComponent.pos.x - pComponent.mana &&
                                        etComponent.pos.y < tComponent.pos.y + pComponent.mana &&
                                        etComponent.pos.y > tComponent.pos.y - pComponent.mana) {
                                    pComponent.mana = 0f

                                    engine.removeEntity(e)
                                }
                            }
                        }
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