package jamthree.engine.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class EnemyMovementComponent : Component, Pool.Poolable {
    val velocity = Vector2(0f, 0f)


    override fun reset() {
        velocity.set(0f, 0f)
    }

    companion object {
        val mapper = mapperFor<EnemyMovementComponent>()
    }
}