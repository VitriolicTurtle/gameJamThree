package jamthree

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.viewport.FitViewport
import jamthree.engine.system.*
import jamthree.engine.system.AnimationPlayerSystem
import jamthree.engine.system.EntityLinkSystem
import jamthree.engine.system.InputSystem
import jamthree.engine.system.RenderSystem
import jamthree.screen.Controller
import jamthree.screen.FirstScreen
import jamthree.screen.SecondScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.debug
import ktx.log.logger

private val LOG = logger<Jam>()
const val unitScale = 1/16f

class Jam(var type1: String, var type2: String) : KtxGame<KtxScreen>() {
    val gameViewport = FitViewport(16f, 9f)
    val batch: Batch by lazy { SpriteBatch() }
    val controller: Controller by lazy {Controller(batch as SpriteBatch)}

    //      Get bodyparts
    private val playerTexture1 = when(type1){
        "Earth" ->  "character/earthhat.png"
        "Wind" ->  "character/airhat.png"
        "Fire" ->  "character/firehat.png"
        "Water" ->  "character/waterhat.png"
        else -> "character/airhat.png"
    }
    private val playerTexture1L = when(type1){
        "Earth" ->  "character/left/earthhatLeft.png"
        "Wind" ->  "character/left/airhatLeft.png"
        "Fire" ->  "character/left/firehatLeft.png"
        "Water" ->  "character/left/waterhatLeft.png"
        else -> "character/left/airhatL.png"
    }
    private val playerTexture2 = when(type2){
        "Earth" ->  "character/earthbody.png"
        "Wind" ->  "character/airbody.png"
        "Fire" ->  "character/firebody.png"
        "Water" ->  "character/waterbody.png"
        else -> "character/airbody.png"
    }
    private val playerTexture2L = when(type2){
        "Earth" ->  "character/left/earthbodyLeft.png"
        "Wind" ->  "character/left/airbodyLeft.png"
        "Fire" ->  "character/left/firebodyLeft.png"
        "Water" ->  "character/left/waterbodyLeft.png"
        else -> "character/left/airbodyLeft.png"
    }

    // Bullet texture
    private val bulletTexture1 = when(type1){
        "Earth" ->  "graphics/earthbullet.png"
        "Wind" ->  "graphics/airbullet.png"
        "Fire" ->  "graphics/firebullet.png"
        "Water" ->  "graphics/waterbullet.png"
        else -> "graphics/airbullet.png"
    }
    private val bulletTexture1L = when(type1){
        "Earth" ->  "graphics/left/earthbulletLeft.png"
        "Wind" ->  "graphics/left/airbulletLeft.png"
        "Fire" ->  "graphics/left/firebulletLeft.png"
        "Water" ->  "graphics/left/waterbulletLeft.png"
        else -> "graphics/left/airbulletLeft.png"
    }

    private val bulletTexture2 = when(type2){
        "Earth" ->  "graphics/earthbullet.png"
        "Wind" ->  "graphics/airbullet.png"
        "Fire" ->  "graphics/firebullet.png"
        "Water" ->  "graphics/waterbullet.png"
        else -> "graphics/airbullet.png"
    }
    private val bulletTexture2L = when(type2){
        "Earth" ->  "graphics/left/earthbulletLeft.png"
        "Wind" ->  "graphics/left/airbulletLeft.png"
        "Fire" ->  "graphics/left/firebulletLeft.png"
        "Water" ->  "graphics/left/waterbulletLeft.png"
        else -> "graphics/left/airbulletLeft.png"
    }
    //////////////////////////////////////////////////////////////
    private val playerBodyTextureRight by lazy { TextureRegion(Texture(Gdx.files.internal(playerTexture2))) }
    private val playerBodyTextureLeft by lazy { TextureRegion(Texture(Gdx.files.internal(playerTexture2L))) }
    private val playerHeadTextureRight by lazy { TextureRegion(Texture(Gdx.files.internal(playerTexture1))) }
    private val playerHeadTextureLeft by lazy { TextureRegion(Texture(Gdx.files.internal(playerTexture1L))) }
    private val bulletOneTextureRight by lazy { TextureRegion(Texture(Gdx.files.internal(bulletTexture1))) }
    private val bulletOneTextureLeft by lazy { TextureRegion(Texture(Gdx.files.internal(bulletTexture1L))) }
    private val bulletTwoTextureRight by lazy { TextureRegion(Texture(Gdx.files.internal(bulletTexture2))) }
    private val bulletTwoTextureLeft by lazy { TextureRegion(Texture(Gdx.files.internal(bulletTexture2L))) }

    val engine: Engine by lazy { PooledEngine().apply{
        addSystem(InputSystem(gameViewport, controller))
        addSystem(MovementSystem())
        addSystem(AnimationPlayerSystem(
            playerBodyTextureRight,
            playerBodyTextureLeft,
            playerBodyTextureRight
        ))
        addSystem(EntityLinkSystem(
            playerHeadTextureRight,
            playerHeadTextureLeft
        ))
        addSystem(ProjectileSystem(
            controller,
            bulletOneTextureRight,
            bulletOneTextureLeft,
            bulletTwoTextureRight,
            bulletTwoTextureLeft

        ))
        addSystem(RenderSystem(batch, gameViewport))
    }
    }


    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        LOG.debug { "Create game instance" }
        addScreen(FirstScreen(this, controller))
        addScreen(SecondScreen(this))
        setScreen<FirstScreen>()
    }


    /*
    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        controller.resize(width, height)
    }
     */

    override fun render() {
        super.render()
        controller.draw()
    }


    override fun dispose() {
        super.dispose()
        playerHeadTextureLeft.texture.dispose()
        playerHeadTextureRight.texture.dispose()
        playerBodyTextureLeft.texture.dispose()
        playerBodyTextureRight.texture.dispose()
        bulletOneTextureRight.texture.dispose()
        bulletOneTextureLeft.texture.dispose()
        bulletTwoTextureRight.texture.dispose()
        bulletTwoTextureLeft.texture.dispose()

    }

}