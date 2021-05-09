package jamthree.engine.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils
import jamthree.engine.component.*
import jamthree.screen.Controller
import jamthree.screen.FirstScreen
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.debug
import ktx.log.logger


private val LOG = logger<MovementSystem>()
private const val updateRate = 1f/30f

class MovementSystem() : IteratingSystem(allOf(TransformComponent::class, MovementComponent::class).get()){
    private var accumulator = 0f
    override fun update(deltaTime: Float) {         // Used to prevent unpredictable behavior of game engine, if not used wonky logic can occur during a rendered frame
        accumulator += deltaTime
        while (accumulator >= updateRate){
            accumulator -= updateRate
        }
        super.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) {"Error: 5007. entity=$entity"}
        val movement = entity[MovementComponent.mapper]
        require(movement != null) {"Error: 5008. entity=$entity"}

        val player = entity[PlayerComponent.mapper]
        if (player != null) {
            entity[DirectionComponent.mapper]?.let { direction ->
                if (direction.walkDirection != Direction.DEFAULT) {
                    when (direction.walkDirection) {
                        Direction.LEFT -> {transform.pos.x = MathUtils.clamp(transform.pos.x + movement.velocity.x* -deltaTime, -10f, 100f - transform.size.x); }
                        Direction.RIGHT -> {transform.pos.x = MathUtils.clamp(transform.pos.x + movement.velocity.x* deltaTime, -10f, 100f - transform.size.x); }
                        Direction.UP -> {transform.pos.y = MathUtils.clamp(transform.pos.y +movement.velocity.y* deltaTime, -10f, 100f - transform.size.y); }
                        Direction.DOWN -> {transform.pos.y = MathUtils.clamp(transform.pos.y + movement.velocity.y* -deltaTime, -10f, 100f - transform.size.y); }
                    }

                }
            }
        }
    }
}