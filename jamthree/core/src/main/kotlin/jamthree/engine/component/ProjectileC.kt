package jamthree.engine.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

enum class ProjectileType {
    WATER, EARTH, FIRE, AIR
}

class ProjectileComponent : Component, Pool.Poolable{
    lateinit var parentEntity: Entity
    var startPos = Vector2()
    var currentPos = Vector2()
    var velocity = 4.0f
    var offset = Vector2()
    var lastPos = Vector3()
    var type = ProjectileType.WATER

    override fun reset(){
        startPos = Vector2(0f,0f)
        currentPos = Vector2(0f,0f)
        velocity = 4.0f
        offset.set(0f, 0f)
        lastPos = Vector3(0f, 0f, 0f)
        type = ProjectileType.WATER
    }

    companion object{
        val mapper = mapperFor<ProjectileComponent>()
    }
}