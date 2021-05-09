package jamthree.engine.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import jamthree.engine.component.*
import ktx.ashley.allOf
import ktx.ashley.get

class ActivatableSystem() : IteratingSystem(allOf(ActivatableComponent::class, TransformComponent::class).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val tComponent = entity[TransformComponent.mapper]
        require(tComponent != null) {}
        val aComponent = entity[ActivatableComponent.mapper]
        require(aComponent != null) {}



    }
}