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
import java.io.File


private val LOG = logger<FirstScreen>()

/** First screen of the application. Displayed after the application is created.  */
class FirstScreen(game: Jam, val controller: Controller) : JamScreen(game) {
    private val viewport = FitViewport(16f, 9f)


    private val magic = Texture(Gdx.files.internal("graphics/Magic.png"))
    private val enemyOne = Texture(Gdx.files.internal("graphics/Enemy01.png"))
    private val floorOne = Texture(Gdx.files.internal("graphics/Floor01.png"))
    private val wallOne = Texture(Gdx.files.internal("graphics/Wall01.png"))
    var wildMagicLevel = 0.0f


    private val playerBody = game.engine.entity{
        with<TransformComponent> { pos.set(7f, 6f, -1f) }
        with<MovementComponent>()
        with<GraphicComponent>()
        with<PlayerComponent>()
        with<DirectionComponent>()
    }

    private val playerHead = game.engine.entity{
        with<TransformComponent>()
        with<EntityLinkComponent> {
            parentEntity = playerBody
            offset.set(0.7f * unitScale, 7.5f * unitScale)
        }
        with<GraphicComponent>()
    }



    override fun show(){
        LOG.debug{ "First screen "}

        renderMap()

        val testEnemy = game.engine.entity{
            with<TransformComponent>{ pos.set(1f, 1f, 0f) }
            with<EnemyMovementComponent>()
            with<GraphicComponent>{
                sprite.run {
                    setRegion(enemyOne)
                    setSize(texture.width * unitScale, texture.height * unitScale)
                    setOriginCenter()
                }
            }
        }
    }

    var secondCounter = 0f
    var castTimer = 0f
    var doOnce = true
    override fun render(delta: Float){
        engine.update(delta)
        secondCounter+=delta
        castTimer+=delta


        if(castTimer > 0.1f) {
            // Pressing "J" = Using magic = Wild Magic bar goes up with an inconsistant amount. (current value * random multiplier)

                if (controller.isAttackOnePressed || controller.isAttackTwoPressed) {
                    while(doOnce) {

                        val projectile = game.engine.entity {
                        with<TransformComponent>()
                        with<GraphicComponent>()
                        with<ProjectileComponent> {
                            parentEntity = playerBody
                            offset.set(1 * unitScale, 0f)
                        }
                    }

                    val random = 0.0f + Math.random() * (0.8f - 0.0f)
                    if (wildMagicLevel <= 0.0f) wildMagicLevel += 0.0015f
                    else wildMagicLevel += wildMagicLevel * random.toFloat()
                    LOG.debug { wildMagicLevel.toString() }
                    batch.begin()
                    //  Wild magic bar updated every time magic is used
                    batch.draw(magic, 0f, 0f, Gdx.graphics.width * wildMagicLevel, 0.2f)
                    batch.end()
                    doOnce = false
                }
            }else { doOnce = true }
            castTimer = 0f
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

    fun renderMap(){
        try{
            var tileArray = arrayOf<CharArray>()
            var column = 0
            var row = 0
            val fileName = "assets/magicMap.txt"
            val lines:List<String> = File(fileName).readLines().asReversed()
            lines.forEach { line ->
                column = 0
                line.forEach { char ->
                    val mapEntity = engine.entity {
                        with<TransformComponent> {
                            pos.set(column.toFloat(), row.toFloat(), -2f)
                        }
                        with<GraphicComponent> {
                            sprite.run {
                                if(char == '0'){setRegion(floorOne)}
                                if(char == '1'){setRegion(wallOne)}
                                setSize(texture.width * unitScale, texture.height * unitScale)
                                setOriginCenter()
                            }
                        }
                        if(char == '1') with<CollisionComponent>()
                    }
                    column=column+1
                }
                row=row+1
                LOG.debug { line }
            }


        }catch(e:Exception){
            e.printStackTrace()
            LOG.debug { "Reading Failed" }
        }finally{
            LOG.debug { "Done Reading" }
        }
    }

}