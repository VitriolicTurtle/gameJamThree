package jamthree.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.utils.viewport.FitViewport
import jamthree.Jam
import jamthree.engine.component.*
import jamthree.unitScale
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger


private val LOG = logger<FirstScreen>()

/** First screen of the application. Displayed after the application is created.  */
class FirstScreen(game: Jam, val controller: Controller) : JamScreen(game) {
    private val viewport = FitViewport(9f, 16f)



    private val magic = Texture(Gdx.files.internal("graphics/Magic.png"))
    var wildMagicLevel = 0.0f


    private val playerBody = game.engine.entity{
        with<TransformComponent>{pos.set(3f, 2f, -1f)}
        with<MovementComponent>()
        with<GraphicComponent>()
        with<PlayerComponent>{ gameinit = game}
        with<DirectionComponent>()
    }

    private val playerHead = game.engine.entity{
        with<TransformComponent>()
        with<EntityLinkComponent>{
            parentEntity = playerBody
            offset.set(0.7f * unitScale, 7.5f* unitScale)
        }
        with<GraphicComponent>()
    }


    override fun show(){
        LOG.debug{ "First screen "}
    }

    var secondCounter = 0f
    override fun render(delta: Float){
        engine.update(delta)
        secondCounter+=delta


        // Pressing "J" = Using magic = Wild Magic bar goes up with an inconsistant amount. (current value * random multiplier)
        if( Gdx.input.isKeyJustPressed(Input.Keys.J) || Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            val projectile = game.engine.entity{
                with<TransformComponent>()
                with<GraphicComponent>()
                with<ProjectileComponent> {
                    parentEntity = playerBody
                    offset.set(1*unitScale, 0f)
                }
            }

            val random = 0.0f + Math.random() * (0.8f - 0.0f)
            if(wildMagicLevel<=0.0f) wildMagicLevel += 0.0015f
            else wildMagicLevel += wildMagicLevel*random.toFloat()
            LOG.debug{ wildMagicLevel.toString()}
            batch.begin()
            //  Wild magic bar updated every time magic is used
            batch.draw(magic, 0f, 0f,  Gdx.graphics.width * wildMagicLevel, 0.2f)
            batch.end()
        }

        // Every second wild magic is reduced by 0.001 * random multiplier
        if(secondCounter >= 1.0f && wildMagicLevel > 0.0f){
            val random = 0.0f + Math.random() * (4f - 0.0f)
            secondCounter = 0.0f
            wildMagicLevel -= 0.001f*random.toFloat()

            //  Wild magic bar update every second when bar is reduced
            batch.begin()
            batch.draw(magic, 0f, 0f,  Gdx.graphics.width * wildMagicLevel, 0.2f)
            batch.end()
        }

        batch.begin()
        //  Wild magic bar
        batch.draw(magic, 0f, 0f,  Gdx.graphics.width * wildMagicLevel, 0.2f)

        batch.end()


        if(wildMagicLevel>0.02){
            game.setScreen<SecondScreen>()
        }

            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
                game.setScreen<SecondScreen>()
            }
        }



}