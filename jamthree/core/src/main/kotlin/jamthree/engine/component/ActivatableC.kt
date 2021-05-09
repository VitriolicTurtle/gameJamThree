package jamthree.engine.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class ActivatableComponent : Component, Pool.Poolable {
    var isBomb = true

    override fun reset() {
        isBomb = true
    }

    companion object {
        val mapper = mapperFor<ActivatableComponent>()
    }

}