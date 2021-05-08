package jamthree.engine.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor

class TransformComponent : Component, Pool.Poolable, Comparable<TransformComponent> {
    val pos = Vector3()
    val size = Vector2(1f, 1f)
    var rot = 0f

    override fun reset(){
        pos.set(0f,0f,0f)
        size.set(1f, 1f)
        rot = 0f
    }

    override fun compareTo(other:TransformComponent): Int{
        val difference = pos.z - other.pos.z
        return(if(difference==0F) pos.y - other.pos.y else difference).toInt()
    }

    companion object{
        val mapper = mapperFor<TransformComponent>()
    }
}

