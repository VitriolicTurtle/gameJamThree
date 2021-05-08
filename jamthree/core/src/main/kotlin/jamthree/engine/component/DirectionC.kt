package jamthree.engine.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

enum class Direction{
    LEFT,
    DEFAULT,
    RIGHT,
    UP,
    DOWN
}

class DirectionComponent : Component, Pool.Poolable {
    var currentDirection = Direction.DEFAULT
    var walkDirection = Direction.UP

    override fun reset() {
        currentDirection = Direction.DEFAULT
        walkDirection = Direction.UP
    }


    companion object{
        val mapper = mapperFor<DirectionComponent>()
    }
}