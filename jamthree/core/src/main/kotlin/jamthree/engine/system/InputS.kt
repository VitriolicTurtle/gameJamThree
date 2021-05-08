package jamthree.engine.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import jamthree.engine.component.Direction
import jamthree.engine.component.DirectionComponent
import jamthree.engine.component.PlayerComponent
import jamthree.engine.component.TransformComponent
import jamthree.screen.Controller
import jamthree.screen.FirstScreen
import jamthree.screen.SecondScreen
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.logger
import javax.xml.crypto.dsig.Transform

private val LOG = logger<InputSystem>()


class InputSystem(private val myViewport: Viewport, val controller: Controller) : IteratingSystem(allOf(PlayerComponent::class, TransformComponent::class, DirectionComponent::class).get()) {
    private val temp = Vector2()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val dir = entity[DirectionComponent.mapper]
        require(dir!=null) {"No direction component"}
        val trans = entity[TransformComponent.mapper]
        require(trans!=null) {"No transform component "}

        /*
        //  Look in touched direction
        if(Gdx.input.isTouched) {
            temp.x = Gdx.input.x.toFloat()
            myViewport.unproject(temp)
            val xDiff = temp.x - trans.pos.x - trans.size.x * 0.5
            dir.currentDirection = when {
                xDiff < -0.2f -> Direction.LEFT
                xDiff > 0.2f -> Direction.RIGHT
                else -> Direction.DEFAULT
            }
        }
         */

        if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
            val player = entity[PlayerComponent.mapper]
            require(player!=null) {"No player component "}
            player.gameinit.setScreen<SecondScreen>()
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W) || controller.isUpPressed){
            dir.walkDirection = Direction.UP
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.A) || controller.isLeftPressed){
            dir.walkDirection = Direction.LEFT
            dir.currentDirection = Direction.LEFT
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.D)|| controller.isRightPressed){
            dir.walkDirection = Direction.RIGHT
            dir.currentDirection = Direction.RIGHT
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.S)|| controller.isDownPressed){
            dir.walkDirection = Direction.DOWN
        }
        else{dir.walkDirection = Direction.DEFAULT}

    }
}