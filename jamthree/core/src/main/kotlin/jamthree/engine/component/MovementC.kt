package jamthree.engine.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class MovementComponent : Component, Pool.Poolable {
    val velocity = Vector2(2f, 2f)


    override fun reset() {
        velocity.set(2f, 2f)
    }

    companion object {
        val mapper = mapperFor<MovementComponent>()
    }
}