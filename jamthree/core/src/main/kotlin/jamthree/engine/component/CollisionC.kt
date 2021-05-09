package jamthree.engine.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class CollisionComponent() : Component, Pool.Poolable {
    var isWall = true
    var isEnemy = false
    var isBomb = true

    override fun reset(){
        isWall = true
        isEnemy = false
        isBomb = true
    }

    companion object{
        val mapper = mapperFor<CollisionComponent>()
    }
}