package jamthree.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.FitViewport
import jamthree.Jam
import jamthree.engine.component.*
import jamthree.unitScale
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.get
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
    private val floorOne = Texture(Gdx.files.internal("graphics/floor01.png"))
    private val spawnOne = Texture(Gdx.files.internal("graphics/Spawn01.png"))
    private val bombOne = Texture(Gdx.files.internal("graphics/Bomb01.png"))
    private val wallOne = Texture(Gdx.files.internal("graphics/wall01.png"))
    var wildMagicLevel = 0.0f
    private var enemyPosArray = Array<Vector3>()
    private var activatablePosArray = Array<Vector3>()



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


    }

    var secondCounter = 0f
    var castTimer = 0f
    var doOnce = true
    override fun render(delta: Float){

        engine.update(delta)
        secondCounter+=delta
        castTimer+=delta






        //  Handle spawnning of enemies
        spawnEnemies(delta)

        //  Handle spawning activatables
        spawnActivatables(delta)


        // Handles shooting
        val pEntities by lazy { engine.getEntitiesFor(allOf(PlayerComponent::class).get()) }
        pEntities.forEach { p ->
            val pComponent = p[PlayerComponent.mapper]
            require(pComponent != null)
            val tComponent = p[TransformComponent.mapper]
            require(tComponent != null)

            val magicBarPosition = Vector2(tComponent.pos.x-7, tComponent.pos.y+4)

        if(castTimer > 0.01f) {
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
                        if (pComponent.mana <= 0.0f) pComponent.mana += 1f
                        else pComponent.mana += pComponent.mana * random.toFloat()
                        if(pComponent.mana > 14) pComponent.mana = 14f
                        //if(pComponent.mana > 0.018) pComponent.mana = 0.018f

                        batch.begin()
                         //  Wild magic bar updated every time magic is used
                        batch.draw(magic, magicBarPosition.x, magicBarPosition.y, pComponent.mana, 0.2f)
                        batch.end()
                         doOnce = false
                  }
            }else { doOnce = true }
            castTimer = 0f
        }




            // Every second wild magic is reduced by 0.001 * random multiplier
            if (secondCounter >= 1.0f && pComponent.mana > 0.0f) {
                val random = 0.0f + Math.random() * (4f - 0.0f)
                secondCounter = 0.0f
                pComponent.mana -= 1f * random.toFloat()

                if(pComponent.mana < 0) pComponent.mana = 0f
                if(pComponent.mana > 14) pComponent.mana = 14f

                //  Wild magic bar update every second when bar is reduced
                batch.begin()
                batch.draw(magic, magicBarPosition.x, magicBarPosition.y, pComponent.mana, 0.2f)
                batch.end()
            }

        batch.begin()
        //  Wild magic bar
        batch.draw(magic, magicBarPosition.x, magicBarPosition.y,  pComponent.mana, 0.2f)
        batch.end()


        if(pComponent.mana == 14f){
            game.setScreen<SecondScreen>()
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            game.setScreen<SecondScreen>()
        }
        }

        controller.draw()
    }

    fun renderMap(){
        try{
            var tileArray = arrayOf<CharArray>()
            var column = 0
            var row = 0
            val quizMap = Gdx.files.internal("magicMap.txt")
            val lines:List<String> = (quizMap.readString()).lines().asReversed()
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
                                if(char == '2'){
                                    enemyPosArray.add(Vector3(column.toFloat(), row.toFloat(), 0f))
                                    setRegion(spawnOne)
                                }
                                if(char == '3'){
                                    activatablePosArray.add(Vector3(column.toFloat(), row.toFloat(), 0f))
                                    setRegion(floorOne)
                                }
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

    var spawnTimer = 0f
    var posIndex = 0

    fun spawnEnemies(delta: Float){
        spawnTimer+=delta

        if(spawnTimer > 1f) {
            val testEnemy = game.engine.entity{
                with<TransformComponent>{ pos.set(enemyPosArray[posIndex]) }
                with<EnemyMovementComponent>()
                with<GraphicComponent>{
                    sprite.run {
                        setRegion(enemyOne)
                        setSize(texture.width * unitScale, texture.height * unitScale)
                        setOriginCenter()
                    }
                }
                with<CollisionComponent>{
                    isWall = false
                    isEnemy = true
                }
            }
            spawnTimer = 0f
            posIndex+=1
            if(posIndex>=enemyPosArray.size)  posIndex = 0
        }

    }

    var activatableSpawnTime = 0f
    fun spawnActivatables(delta: Float){
        activatableSpawnTime+=delta
        val random = 0 + Math.random() * (activatablePosArray.size - 0)

        if(activatableSpawnTime > 0.1f) {
            val testActivatable = game.engine.entity{
                with<TransformComponent>{ pos.set(activatablePosArray[random.toInt()]) }
                with<GraphicComponent>{
                    sprite.run {
                        setRegion(bombOne)
                        setSize(texture.width * unitScale, texture.height * unitScale)
                        setOriginCenter()
                    }
                }
                with<CollisionComponent>{
                    isWall = false
                    isEnemy = false
                    isBomb = true
                }
            }
            activatableSpawnTime = 0f
        }
    }
}