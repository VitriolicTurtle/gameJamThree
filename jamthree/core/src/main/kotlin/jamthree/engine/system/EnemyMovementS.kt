package jamthree.engine.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import jamthree.engine.component.*
import jamthree.screen.FirstScreen
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.debug
import ktx.log.logger
import javax.xml.bind.ValidationEvent
import kotlin.math.sqrt


const val ENEMY_SPEED = 0.03F
private val LOG = logger<EnemyMovementSystem>()

class EnemyMovementSystem() : IteratingSystem(allOf(EnemyMovementComponent::class, TransformComponent::class).get()) {
    private val pEntities by lazy { engine.getEntitiesFor(allOf(PlayerComponent::class).get()) }

    /*
    override fun entityAdded(entity: Entity){
    }
     */

    var posDiffHolder = Vector2(0f,0f)
    var tempPlayerPos = Vector3(0f,0f,0f)
    var emDir = Vector2(0f, 0f)
    var emMovement = Vector2(0f, 0f)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val tComponent = entity[TransformComponent.mapper]
        require(tComponent != null) { "tComponent" }
        val emComponent = entity[EnemyMovementComponent.mapper]
        require(emComponent != null) { "emComponent" }

        pEntities.forEach { p ->
            val pComponent = p[PlayerComponent.mapper]
            require(pComponent != null)
            p[TransformComponent.mapper]?.let { ptComponent ->



                posDiffHolder = Vector2(ptComponent.pos.x-tComponent.pos.x, ptComponent.pos.y-tComponent.pos.y)
                val emLength = sqrt(posDiffHolder.x*posDiffHolder.x + posDiffHolder.y*posDiffHolder.y)
                emDir = Vector2(posDiffHolder.x / emLength, posDiffHolder.y / emLength)
                emMovement = Vector2(emDir.x * ENEMY_SPEED, emDir.y * ENEMY_SPEED)
                tComponent.pos.x += emMovement.x
                tComponent.pos.y += emMovement.y

               // LOG.debug { emMovement.x.toString() + "     +     " + emMovement.y.toString() }
            }
        }


    }


}