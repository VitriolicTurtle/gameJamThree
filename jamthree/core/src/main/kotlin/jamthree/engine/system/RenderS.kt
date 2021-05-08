package jamthree.engine.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.Viewport
import jamthree.Jam
import jamthree.engine.component.DirectionComponent
import jamthree.engine.component.GraphicComponent
import jamthree.engine.component.PlayerComponent
import jamthree.engine.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use
import ktx.log.debug
import ktx.log.logger

private val LOG = logger<RenderSystem>()


class RenderSystem(
        private val batch: Batch,
        private val batch2: Batch,
    private val myViewport: Viewport
) : SortedIteratingSystem(
    allOf(TransformComponent::class, GraphicComponent::class).get(), compareBy{ entity -> entity[TransformComponent.mapper]}){

    override fun update(deltaTime: Float) {
        forceSort()
        myViewport.apply()
        batch.use(myViewport.camera.combined){
            super.update(deltaTime)

        }

    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform!=null){"Must have transform component"}
        val graphic = entity[GraphicComponent.mapper]
        require(graphic!=null){"Must have transform component"}


        val player = entity[PlayerComponent.mapper]
        if (player != null) {
            entity[TransformComponent.mapper]?.let { trans ->
                myViewport.camera.position.x = trans.pos.x
                myViewport.camera.position.y = trans.pos.y

            }
        }


        graphic.sprite.run{
            rotation = transform.rot
            setBounds(transform.pos.x, transform.pos.y, transform.size.x, transform.size.y)
            draw(batch)
        }
    }

}
